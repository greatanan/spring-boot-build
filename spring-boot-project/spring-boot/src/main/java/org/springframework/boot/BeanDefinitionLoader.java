package org.springframework.boot;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import groovy.lang.Closure;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Loads bean definitions from underlying sources, including XML and JavaConfig. Acts as a
 * simple facade over {@link AnnotatedBeanDefinitionReader},
 * {@link XmlBeanDefinitionReader} and {@link ClassPathBeanDefinitionScanner}. See
 * {@link SpringApplication} for the types of sources that are supported.
 *
 * @author Phillip Webb
 * @see #setBeanNameGenerator(BeanNameGenerator)
 *
 *
 * BeanDefinition 加载器（Loader），负责 Spring Boot 中，读取 BeanDefinition
 *
 */
class BeanDefinitionLoader {

	// 来源的数组
	private final Object[] sources;

	// 注解的 BeanDefinition 读取器
	private final AnnotatedBeanDefinitionReader annotatedReader;

	// XML 的 BeanDefinition 读取器
	private final XmlBeanDefinitionReader xmlReader;

	// Groovy 的 BeanDefinition 读取器
	private BeanDefinitionReader groovyReader;

	// Classpath 的 BeanDefinition 扫描器
	private final ClassPathBeanDefinitionScanner scanner;

	// 资源加载器
	private ResourceLoader resourceLoader;

	/**
	 * Create a new {@link BeanDefinitionLoader} that will load beans into the specified
	 * {@link BeanDefinitionRegistry}.
	 *
	 * @param registry the bean definition registry that will contain the loaded beans
	 * @param sources  the bean sources
	 */
	BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources) {

		Assert.notNull(registry, "Registry must not be null");
		Assert.notEmpty(sources, "Sources must not be empty");

		this.sources = sources;

		// 注解形式的Bean定义读取器 比如：@Configuration @Bean @Component @Controller @Service等等
		this.annotatedReader = new AnnotatedBeanDefinitionReader(registry);

		// XML形式的Bean定义读取器
		this.xmlReader = new XmlBeanDefinitionReader(registry);

		if (isGroovyPresent()) {
			this.groovyReader = new GroovyBeanDefinitionReader(registry);
		}
		// 类路径扫描器
		this.scanner = new ClassPathBeanDefinitionScanner(registry);

		// 扫描器添加排除过滤器
		this.scanner.addExcludeFilter(new ClassExcludeFilter(sources));

	}

	/**
	 * Set the bean name generator to be used by the underlying readers and scanner.
	 *
	 * @param beanNameGenerator the bean name generator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.annotatedReader.setBeanNameGenerator(beanNameGenerator);
		this.xmlReader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
	}

	/**
	 * Set the resource loader to be used by the underlying readers and scanner.
	 *
	 * @param resourceLoader the resource loader
	 */
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		this.xmlReader.setResourceLoader(resourceLoader);
		this.scanner.setResourceLoader(resourceLoader);
	}

	/**
	 * Set the environment to be used by the underlying readers and scanner.
	 *
	 * @param environment the environment
	 */
	public void setEnvironment(ConfigurableEnvironment environment) {
		this.annotatedReader.setEnvironment(environment);
		this.xmlReader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Load the sources into the reader.
	 *
	 * @return the number of loaded beans
	 */
	public int load() {
		int count = 0;
		//  遍历 sources 数组，逐个加载
		for (Object source : this.sources) {
			count += load(source);
		}
		return count;
	}

	private int load(Object source) {

		Assert.notNull(source, "Source must not be null");
		// 如果是 Class 类型，则使用 AnnotatedBeanDefinitionReader 执行加载
		if (source instanceof Class<?>) {
			return load((Class<?>) source);
		}
		// 如果是 Resource 类型，则使用 XmlBeanDefinitionReader 执行加载
		if (source instanceof Resource) {
			return load((Resource) source);
		}
		// 如果是 Package 类型，则使用 ClassPathBeanDefinitionScanner 执行加载
		if (source instanceof Package) {
			return load((Package) source);
		}
		// 如果是 CharSequence 类型，则各种尝试去加载
		if (source instanceof CharSequence) {
			return load((CharSequence) source);
		}
		// 无法处理的类型，抛出 IllegalArgumentException 异常
		throw new IllegalArgumentException("Invalid source type " + source.getClass());
	}

	private int load(Class<?> source) {
		// Groovy 相关，暂时忽略
		if (isGroovyPresent() && GroovyBeanDefinitionSource.class.isAssignableFrom(source)) {
			// Any GroovyLoaders added in beans{} DSL can contribute beans here
			GroovyBeanDefinitionSource loader = BeanUtils.instantiateClass(source, GroovyBeanDefinitionSource.class);
			load(loader);
		}
		// 如果是 Component ，则执行注册
		if (isComponent(source)) { // 判断是否为 Component
			//将 启动类的 BeanDefinition注册进 beanDefinitionMap
			// 调用AnnotatedBeanDefinitionReader#register方法 执行注册
			this.annotatedReader.register(source);
			return 1;
		}
		return 0;
	}

	private int load(GroovyBeanDefinitionSource source) {
		int before = this.xmlReader.getRegistry().getBeanDefinitionCount();
		((GroovyBeanDefinitionReader) this.groovyReader).beans(source.getBeans());
		int after = this.xmlReader.getRegistry().getBeanDefinitionCount();
		return after - before;
	}

	private int load(Resource source) {
		if (source.getFilename().endsWith(".groovy")) {
			if (this.groovyReader == null) {
				throw new BeanDefinitionStoreException("Cannot load Groovy beans without Groovy on classpath");
			}
			return this.groovyReader.loadBeanDefinitions(source);
		}
		// 使用 XmlBeanDefinitionReader 加载 BeanDefinition
		return this.xmlReader.loadBeanDefinitions(source);
	}

	private int load(Package source) {
		// ClassPathBeanDefinitionScanner#scan
		return this.scanner.scan(source.getName());
	}

	private int load(CharSequence source) {
		String resolvedSource = this.xmlReader.getEnvironment()
				.resolvePlaceholders(source.toString());
		// Attempt as a Class
		try {
			return load(ClassUtils.forName(resolvedSource, null));
		} catch (IllegalArgumentException | ClassNotFoundException ex) {
			// swallow exception and continue
		}
		// Attempt as resources
		Resource[] resources = findResources(resolvedSource);
		int loadCount = 0;
		boolean atLeastOneResourceExists = false;
		for (Resource resource : resources) {
			if (isLoadCandidate(resource)) {
				atLeastOneResourceExists = true;
				loadCount += load(resource);
			}
		}
		if (atLeastOneResourceExists) {
			return loadCount;
		}
		// Attempt as package
		Package packageResource = findPackage(resolvedSource);
		if (packageResource != null) {
			return load(packageResource);
		}
		throw new IllegalArgumentException("Invalid source '" + resolvedSource + "'");
	}

	private boolean isGroovyPresent() {
		return ClassUtils.isPresent("groovy.lang.MetaClass", null);
	}

	private Resource[] findResources(String source) {
		ResourceLoader loader = (this.resourceLoader != null) ? this.resourceLoader
				: new PathMatchingResourcePatternResolver();
		try {
			if (loader instanceof ResourcePatternResolver) {
				return ((ResourcePatternResolver) loader).getResources(source);
			}
			return new Resource[]{loader.getResource(source)};
		} catch (IOException ex) {
			throw new IllegalStateException("Error reading source '" + source + "'");
		}
	}

	private boolean isLoadCandidate(Resource resource) {
		if (resource == null || !resource.exists()) {
			return false;
		}
		if (resource instanceof ClassPathResource) {
			// A simple package without a '.' may accidentally get loaded as an XML
			// document if we're not careful. The result of getInputStream() will be
			// a file list of the package content. We double check here that it's not
			// actually a package.
			String path = ((ClassPathResource) resource).getPath();
			if (path.indexOf('.') == -1) {
				try {
					return Package.getPackage(path) == null;
				} catch (Exception ex) {
					// Ignore
				}
			}
		}
		return true;
	}

	private Package findPackage(CharSequence source) {
		Package pkg = Package.getPackage(source.toString());
		if (pkg != null) {
			return pkg;
		}
		try {
			// Attempt to find a class in this package
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
					getClass().getClassLoader());
			Resource[] resources = resolver.getResources(
					ClassUtils.convertClassNameToResourcePath(source.toString())
							+ "/*.class");
			for (Resource resource : resources) {
				String className = StringUtils
						.stripFilenameExtension(resource.getFilename());
				load(Class.forName(source.toString() + "." + className));
				break;
			}
		} catch (Exception ex) {
			// swallow exception and continue
		}
		return Package.getPackage(source.toString());
	}

	private boolean isComponent(Class<?> type) {
		// This has to be a bit of a guess. The only way to be sure that this type is
		// eligible is to make a bean definition out of it and try to instantiate it.
		// 如果有 @Component 注解，则返回 true
		if (AnnotationUtils.findAnnotation(type, Component.class) != null) {
			return true;
		}
		// Nested anonymous classes are not eligible for registration, nor are groovy
		// closures
		if (type.getName().matches(".*\\$_.*closure.*") || type.isAnonymousClass()
				|| type.getConstructors() == null || type.getConstructors().length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Simple {@link TypeFilter} used to ensure that specified {@link Class} sources are
	 * not accidentally re-added during scanning.
	 */
	private static class ClassExcludeFilter
			extends AbstractTypeHierarchyTraversingFilter {

		private final Set<String> classNames = new HashSet<>();

		ClassExcludeFilter(Object... sources) {
			super(false, false);
			for (Object source : sources) {
				if (source instanceof Class<?>) {
					this.classNames.add(((Class<?>) source).getName());
				}
			}
		}

		@Override
		protected boolean matchClassName(String className) {
			return this.classNames.contains(className);
		}

	}

	/**
	 * Source for Bean definitions defined in Groovy.
	 */
	@FunctionalInterface
	protected interface GroovyBeanDefinitionSource {

		Closure<?> getBeans();

	}

}
