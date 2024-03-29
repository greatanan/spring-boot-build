package org.springframework.boot.actuate.autoconfigure.cloudfoundry.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.AccessLevel;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.SecurityResponse;
import org.springframework.boot.actuate.endpoint.web.EndpointLinksResolver;
import org.springframework.boot.actuate.endpoint.web.EndpointMapping;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.actuate.endpoint.web.ExposableWebEndpoint;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.boot.actuate.endpoint.web.WebOperation;
import org.springframework.boot.actuate.endpoint.web.servlet.AbstractWebMvcEndpointHandlerMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

/**
 * A custom {@link RequestMappingInfoHandlerMapping} that makes web endpoints available on
 * Cloud Foundry specific URLs over HTTP using Spring MVC.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
class CloudFoundryWebEndpointServletHandlerMapping
		extends AbstractWebMvcEndpointHandlerMapping {

	private final CloudFoundrySecurityInterceptor securityInterceptor;

	private final EndpointLinksResolver linksResolver;

	CloudFoundryWebEndpointServletHandlerMapping(EndpointMapping endpointMapping,
												 Collection<ExposableWebEndpoint> endpoints,
												 EndpointMediaTypes endpointMediaTypes, CorsConfiguration corsConfiguration,
												 CloudFoundrySecurityInterceptor securityInterceptor,
												 EndpointLinksResolver linksResolver) {
		super(endpointMapping, endpoints, endpointMediaTypes, corsConfiguration);
		this.securityInterceptor = securityInterceptor;
		this.linksResolver = linksResolver;
	}

	@Override
	protected ServletWebOperation wrapServletWebOperation(ExposableWebEndpoint endpoint,
														  WebOperation operation, ServletWebOperation servletWebOperation) {
		return new SecureServletWebOperation(servletWebOperation,
				this.securityInterceptor, endpoint.getId());
	}

	@Override
	@ResponseBody
	protected Map<String, Map<String, Link>> links(HttpServletRequest request,
												   HttpServletResponse response) {
		SecurityResponse securityResponse = this.securityInterceptor.preHandle(request,
				"");
		if (!securityResponse.getStatus().equals(HttpStatus.OK)) {
			sendFailureResponse(response, securityResponse);
		}
		AccessLevel accessLevel = (AccessLevel) request
				.getAttribute(AccessLevel.REQUEST_ATTRIBUTE);
		Map<String, Link> links = this.linksResolver
				.resolveLinks(request.getRequestURL().toString());
		Map<String, Link> filteredLinks = new LinkedHashMap<>();
		if (accessLevel == null) {
			return Collections.singletonMap("_links", filteredLinks);
		}
		filteredLinks = links.entrySet().stream()
				.filter((e) -> e.getKey().equals("self")
						|| accessLevel.isAccessAllowed(e.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		return Collections.singletonMap("_links", filteredLinks);
	}

	private void sendFailureResponse(HttpServletResponse response,
									 SecurityResponse securityResponse) {
		try {
			response.sendError(securityResponse.getStatus().value(),
					securityResponse.getMessage());
		} catch (Exception ex) {
			this.logger.debug("Failed to send error response", ex);
		}
	}

	/**
	 * {@link ServletWebOperation} wrapper to add security.
	 */
	private static class SecureServletWebOperation implements ServletWebOperation {

		private final ServletWebOperation delegate;

		private final CloudFoundrySecurityInterceptor securityInterceptor;

		private final String endpointId;

		SecureServletWebOperation(ServletWebOperation delegate,
								  CloudFoundrySecurityInterceptor securityInterceptor, String endpointId) {
			this.delegate = delegate;
			this.securityInterceptor = securityInterceptor;
			this.endpointId = endpointId;
		}

		@Override
		public Object handle(HttpServletRequest request, Map<String, String> body) {
			SecurityResponse securityResponse = this.securityInterceptor
					.preHandle(request, this.endpointId);
			if (!securityResponse.getStatus().equals(HttpStatus.OK)) {
				return new ResponseEntity<Object>(securityResponse.getMessage(),
						securityResponse.getStatus());
			}
			return this.delegate.handle(request, body);
		}

	}

}
