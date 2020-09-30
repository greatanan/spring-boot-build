/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;

/**
 * {@link Conditional} that checks if the specified properties have a specific value. By
 * default the properties must be present in the {@link Environment} and
 * <strong>not</strong> equal to {@code false}. The {@link #havingValue()} and
 * {@link #matchIfMissing()} attributes allow further customizations.
 *
 * <p>
 * The {@link #havingValue} attribute can be used to specify the value that the property
 * should have. The table below shows when a condition matches according to the property
 * value and the {@link #havingValue()} attribute:
 *
 * <table summary="having values" border="1">
 * <tr>
 * <th>Property Value</th>
 * <th>{@code havingValue=""}</th>
 * <th>{@code havingValue="true"}</th>
 * <th>{@code havingValue="false"}</th>
 * <th>{@code havingValue="foo"}</th>
 * </tr>
 * <tr>
 * <td>{@code "true"}</td>
 * <td>yes</td>
 * <td>yes</td>
 * <td>no</td>
 * <td>no</td>
 * </tr>
 * <tr>
 * <td>{@code "false"}</td>
 * <td>no</td>
 * <td>no</td>
 * <td>yes</td>
 * <td>no</td>
 * </tr>
 * <tr>
 * <td>{@code "foo"}</td>
 * <td>yes</td>
 * <td>no</td>
 * <td>no</td>
 * <td>yes</td>
 * </tr>
 * </table>
 *
 * <p>
 * If the property is not contained in the {@link Environment} at all, the
 * {@link #matchIfMissing()} attribute is consulted. By default missing attributes do not
 * match.
 *
 * <p>
 * This condition cannot be reliably used for matching collection properties. For example,
 * in the following configuration, the condition matches if {@code spring.example.values}
 * is present in the {@link Environment} but does not match if
 * {@code spring.example.values[0]} is present.
 *
 * <pre class="code">
 * &#064;ConditionalOnProperty(prefix = "spring", name = "example.values")
 * class ExampleAutoConfiguration {
 * }
 * </pre>
 * <p>
 * It is better to use a custom condition for such cases.
 *
 * @author Maciej Walkowiak
 * @author Stephane Nicoll
 * @author Phillip Webb
 * @since 1.1.0
 */

/**
 * 通过其两个属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。
 * 如果该值为空，则返回false;
 * 如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。
 * 如果返回值为false，则该configuration不生效；为true则生效。
 *
 * 作者：wyatt_plus
 * 链接：https://www.jianshu.com/p/68a75c093023    具体使用可以参考这篇文章
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {

	/**
	 * Alias for {@link #name()}.
	 *
	 * @return the names
	 */
	String[] value() default {};  // 数组，获取对应property名称的值，与name不可同时使用

	/**
	 * A prefix that should be applied to each property. The prefix automatically ends
	 * with a dot if not specified.
	 *
	 * @return the prefix
	 */
	String prefix() default ""; // property名称的前缀，可有可无

	/**
	 * The name of the properties to test. If a prefix has been defined, it is applied to
	 * compute the full key of each property. For instance if the prefix is
	 * {@code app.config} and one value is {@code my-value}, the full key would be
	 * {@code app.config.my-value}
	 * <p>
	 * Use the dashed notation to specify each property, that is all lower case with a "-"
	 * to separate words (e.g. {@code my-long-property}).
	 *
	 * @return the names
	 */
	String[] name() default {};  // 数组，property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用

	/**
	 * The string representation of the expected value for the properties. If not
	 * specified, the property must <strong>not</strong> be equal to {@code false}.
	 *
	 * @return the expected value
	 */
	String havingValue() default ""; // 可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置

	/**
	 * Specify if the condition should match if the property is not set. Defaults to
	 * {@code false}.
	 *
	 * @return if should match if the property is missing
	 */
	boolean matchIfMissing() default false; // 缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错

}
