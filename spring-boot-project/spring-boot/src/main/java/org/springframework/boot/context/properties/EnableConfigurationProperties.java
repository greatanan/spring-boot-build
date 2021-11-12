package org.springframework.boot.context.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable support for {@link ConfigurationProperties} annotated beans.
 * {@link ConfigurationProperties} beans can be registered in the standard way (for
 * example using {@link Bean @Bean} methods) or, for convenience, can be specified
 * directly on this annotation.
 *
 * @author Dave Syer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EnableConfigurationPropertiesImportSelector.class)


// my: 这个注解EnableConfigurationProperties是什么时候生效的呢，其实1是手动设置2就是
// 自动注入啦，在类ConfigurationPropertiesAutoConfiguration中引入了这个注解，ConfigurationPropertiesAutoConfiguration是自动配置类，放在了spring.factory文件中了
/**
 * {@link org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration}
 */
public @interface EnableConfigurationProperties {

	/**
	 * Convenient way to quickly register {@link ConfigurationProperties} annotated beans
	 * with Spring. Standard Spring Beans will also be scanned regardless of this value.
	 *
	 * @return {@link ConfigurationProperties} annotated beans to register
	 */
	Class<?>[] value() default {};

}
