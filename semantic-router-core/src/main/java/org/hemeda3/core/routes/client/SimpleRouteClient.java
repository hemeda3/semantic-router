package org.hemeda3.core.routes.client;

import org.hemeda3.core.routes.impl.RouteChoice;
import org.hemeda3.core.routes.impl.RouteClient;
import org.hemeda3.core.routes.impl.RouteLayer;

public class SimpleRouteClient implements RouteClient {

	private final RouteLayer routeLayer;

	public SimpleRouteClient(RouteLayer routeLayer) {
		this.routeLayer = routeLayer;
	}

	@Override
	public RouteChoice call(String text) {
		return routeLayer.call(text);
	}

}
