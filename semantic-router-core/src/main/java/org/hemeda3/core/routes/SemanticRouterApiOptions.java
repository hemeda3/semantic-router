package org.hemeda3.core.routes;

import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.routes.impl.Route;

import java.util.ArrayList;
import java.util.List;

public class SemanticRouterApiOptions {

	private Encoder encoder;

	private List<Route> routes = new ArrayList<>();

	private double scoreThreshold;

	private String defaultRoute;

	public Encoder getEncoder() {
		return encoder;
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public double getScoreThreshold() {
		return scoreThreshold;
	}

	public void setScoreThreshold(double scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}

	public String getDefaultRoute() {
		return defaultRoute;
	}

	public void setDefaultRoute(String defaultRoute) {
		this.defaultRoute = defaultRoute;
	}

	public void addRoute(Route route) {
		this.routes.add(route);
	}

}
