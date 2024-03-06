package org.hemeda3.ai.autoconfigure.semanticroute;

import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.index.impl.LocalIndex;
import org.hemeda3.core.routes.SemanticRouterApiOptions;
import org.hemeda3.core.routes.client.SimpleRouteClient;
import org.hemeda3.core.routes.impl.Route;
import org.hemeda3.core.routes.impl.RouteChoice;
import org.hemeda3.core.routes.impl.RouteLayer;

import java.util.List;

public class SemanticRouterApi {

	private final SimpleRouteClient routeClient;

	public SemanticRouterApi(SemanticRouterApiOptions options) {
		RouteLayer routeLayer = new RouteLayer(options.getEncoder(), options.getRoutes(), options.getScoreThreshold());
		this.routeClient = new SimpleRouteClient(routeLayer);
	}

	public RouteChoice getRoute(String text) {
		return routeClient.call(text);
	}

}
