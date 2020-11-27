package com.greatanan.demo.configuration.ConfigureOrder;

/**
 *   该包下内容是为了测试springboot自动配置类的顺序以及源码调试用的
 *   参考文章: https://www.cnblogs.com/yourbatman/p/13264743.html#top
 *   @AutoConfigureBefore、@AutoConfigureAfter、@AutoConfigureOrder
 *
 *
 *   总结就是:
 *
 *      这三个注解是调节自动配置类的顺序的（注意只能是调节自动配置类的顺序）
 *      什么是自动配置类呢? 我的理解就是在spring.factories文件中配置的配置类
 *
 *      我们如果需要调节两个配置类的顺序如果使用上面三个注解.则必须保证则两个配置类都是在spring.factories文件中配置了
 *
 *      如果我们写的配置类即被springboot扫描了又在spring.factories文件中配置了，这个时候其实springboot在扫描的时候是不会把配置类扫描进去的(也就是不会注册该配置类的bd)
 *                至于为什么不会被扫描进bd map是因为在下面链接方法中会调用isCandidateComponent(metadataReader)方法判断是不是候选bean:
 *                {@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#scanCandidateComponents}
 *                在isCandidateComponent(metadataReader)方法中会调用{@link org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter#match}中进行判断是不是自动配置类
 *                如果是自动配置类就不注册bd了
 *      所以我们自己写的配置类如果要让其是自动配置类其实它也是可以被扫描，我们不用单独的写一个不被扫描的包存放我们自己写的自动配置类
 *
 *
 *    另外说明一下spring.factories文件中配置的自动配置类是在最后进行加载的 我们被扫描进来的配置类顺序是优先自动配置类的
 *
 *
 *
 *
 *
 *
 */
public class Description {

}
