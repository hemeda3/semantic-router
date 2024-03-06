package org.hemeda3.core.routes.impl;

import java.util.Map;

public class RouteChoice {

	private String name;

	private Map<String, Object> functionCall;

	private Double similarityScore;

	private Boolean trigger;

	public RouteChoice() {
	}

	public RouteChoice(String name, Map<String, Object> functionCall, Double similarityScore, Boolean trigger) {
		this.name = name;
		this.functionCall = functionCall;
		this.similarityScore = similarityScore;
		this.trigger = trigger;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getFunctionCall() {
		return functionCall;
	}

	public void setFunctionCall(Map<String, Object> functionCall) {
		this.functionCall = functionCall;
	}

	public Double getSimilarityScore() {
		return similarityScore;
	}

	public void setSimilarityScore(Double similarityScore) {
		this.similarityScore = similarityScore;
	}

	public Boolean getTrigger() {
		return trigger;
	}

	public void setTrigger(Boolean trigger) {
		this.trigger = trigger;
	}

}
