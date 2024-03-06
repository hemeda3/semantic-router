package org.hemeda3.core.routes.impl;

import java.util.List;

public class Route {

	private String name;

	private List<String> utterances;

	private String description;

	public Route(String name, List<String> utterances, String description) {
		this.name = name;
		this.utterances = utterances;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getUtterances() {
		return utterances;
	}

	public void setUtterances(List<String> utterances) {
		this.utterances = utterances;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private double scoreThreshold;

	public double getScoreThreshold() {
		return scoreThreshold;
	}

	public void setScoreThreshold(double scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}

}
