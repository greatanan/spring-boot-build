package org.springframework.boot.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * Indicates that the package containing the annotated class should be registered with
 * {@link AutoConfigurationPackages}.
 *
 * @author Phillip Webb
 * @see AutoConfigurationPackages
 * @since 1.3.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// AutoConfigurationPackages的作用是用于存储自动配置包以供以后使用（例如，由JPA实体*扫描仪提供）。
// 关于自动配置其实这个@AutoConfigurationPackage并没有什么作用  我的理解是这个注解的作用就是通过AutoConfigurationPackages保存一个我们的自动配置包 供以后可能使用
// 可以参考文档 https://docs.spring.io/spring-boot/docs/current/api/
@Import(AutoConfigurationPackages.Registrar.class) // 注入AutoConfigurationPackages.Registrar.class
public @interface AutoConfigurationPackage {

}
