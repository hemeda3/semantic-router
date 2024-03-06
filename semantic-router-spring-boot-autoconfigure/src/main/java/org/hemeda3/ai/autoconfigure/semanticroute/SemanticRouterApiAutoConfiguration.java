package org.hemeda3.ai.autoconfigure.semanticroute;

import org.hemeda3.core.routes.SemanticRouterApiOptions;
import org.hemeda3.core.routes.impl.SemanticRouterApiProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(SemanticRouterApi.class)
@EnableConfigurationProperties({ SemanticRouterApiProperties.class })
public class SemanticRouterApiAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SemanticRouterApiOptions semanticRouterApiOptions(SemanticRouterApiProperties properties) {
		SemanticRouterApiOptions options = new SemanticRouterApiOptions();
		// Configure options based on properties
		options.setScoreThreshold(properties.getScoreThreshold());
		options.setDefaultRoute(properties.getDefaultRoute());
		options.setEncoder(properties.getOptions().getEncoder());
		options.setRoutes(properties.getOptions().getRoutes());
		// Set other options as needed
		return options;
	}

	@Bean
	@ConditionalOnMissingBean
	public SemanticRouterApi semanticRouterApi(SemanticRouterApiOptions options) {
		return new SemanticRouterApi(options);
	}

}
