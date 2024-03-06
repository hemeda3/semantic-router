package org.hemeda3.core.routes.impl;

@FunctionalInterface
public interface RouteClient {

	RouteChoice call(String text);

}
