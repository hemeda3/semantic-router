package org.hemeda3.core.routes.impl;

import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.encoders.impl.EncoderType;
import org.hemeda3.core.routes.SemanticRouterApiOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(SemanticRouterApiProperties.CONFIG_PREFIX)
public class SemanticRouterApiProperties {

	public static final String CONFIG_PREFIX = "semantic-router-api";

	private boolean enabled = true;

	private double scoreThreshold = 0.8;

	private String defaultRoute = "fallback";

	private EncoderType encoderType = EncoderType.OPENAI;

	@NestedConfigurationProperty
	private SemanticRouterApiOptions options = new SemanticRouterApiOptions();

	// Getters and setters
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public double getScoreThreshold() {
		return scoreThreshold;
	}

	public void setScoreThreshold(double scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}

	public EncoderType getEncoderType() {
		return encoderType;
	}

	public void setEncoderType(EncoderType encoderType) {
		this.encoderType = encoderType;
	}

	public String getDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(String defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public SemanticRouterApiOptions getOptions() {
		return options;
	}

	public void setOptions(SemanticRouterApiOptions options) {
		this.options = options;
	}

}
