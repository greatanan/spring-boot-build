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

package org.springframework.boot.actuate.autoconfigure.elasticsearch;

import java.time.Duration;
import java.util.Map;

import io.searchbox.client.JestClient;
import org.elasticsearch.client.Client;

import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchHealthIndicator;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchJestHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for
 * {@link ElasticsearchHealthIndicator}.
 *
 * @author Binwei Yang
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@Configuration
@ConditionalOnEnabledHealthIndicator("elasticsearch")
@AutoConfigureBefore(HealthIndicatorAutoConfiguration.class)
@AutoConfigureAfter({ElasticsearchAutoConfiguration.class, JestAutoConfiguration.class})
public class ElasticsearchHealthIndicatorAutoConfiguration {

	@Configuration
	@ConditionalOnClass(Client.class)
	@ConditionalOnBean(Client.class)
	@EnableConfigurationProperties(ElasticsearchHealthIndicatorProperties.class)
	static class ElasticsearchClientHealthIndicatorConfiguration extends
			CompositeHealthIndicatorConfiguration<ElasticsearchHealthIndicator, Client> {

		private final Map<String, Client> clients;

		private final ElasticsearchHealthIndicatorProperties properties;

		ElasticsearchClientHealthIndicatorConfiguration(Map<String, Client> clients,
														ElasticsearchHealthIndicatorProperties properties) {
			this.clients = clients;
			this.properties = properties;
		}

		@Bean
		@ConditionalOnMissingBean(name = "elasticsearchHealthIndicator")
		public HealthIndicator elasticsearchHealthIndicator() {
			return createHealthIndicator(this.clients);
		}

		@Override
		protected ElasticsearchHealthIndicator createHealthIndicator(Client client) {
			Duration responseTimeout = this.properties.getResponseTimeout();
			return new ElasticsearchHealthIndicator(client,
					(responseTimeout != null) ? responseTimeout.toMillis() : 100,
					this.properties.getIndices());
		}

	}

	@Configuration
	@ConditionalOnClass(JestClient.class)
	@ConditionalOnBean(JestClient.class)
	static class ElasticsearchJestHealthIndicatorConfiguration extends
			CompositeHealthIndicatorConfiguration<ElasticsearchJestHealthIndicator, JestClient> {

		private final Map<String, JestClient> clients;

		ElasticsearchJestHealthIndicatorConfiguration(Map<String, JestClient> clients) {
			this.clients = clients;
		}

		@Bean
		@ConditionalOnMissingBean(name = "elasticsearchHealthIndicator")
		public HealthIndicator elasticsearchHealthIndicator() {
			return createHealthIndicator(this.clients);
		}

		@Override
		protected ElasticsearchJestHealthIndicator createHealthIndicator(
				JestClient client) {
			return new ElasticsearchJestHealthIndicator(client);
		}

	}

}
