package org.hemeda3.core.routes.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.index.BaseIndex;
import org.hemeda3.core.index.impl.LocalIndex;

import java.util.*;
import java.util.stream.IntStream;

public class RouteLayer {

	private Encoder encoder;

	private BaseIndex index;

	private List<Route> routes;

	private double scoreThreshold;

	public RouteLayer(Encoder encoder, List<Route> routes, double scoreThreshold) {
		this(encoder, new LocalIndex(), routes, scoreThreshold);
	}

	public RouteLayer(Encoder encoder, BaseIndex index, List<Route> routes, double scoreThreshold) {
		this.encoder = Objects.requireNonNull(encoder, "Encoder must not be null");
		this.index = Objects.requireNonNull(index, "Index must not be null");
		this.routes = new ArrayList<>(Objects.requireNonNull(routes, "Routes must not be null"));
		this.scoreThreshold = scoreThreshold;
		initializeIndexWithRoutes();
	}

	private void initializeIndexWithRoutes() {
		if (routes != null && !routes.isEmpty()) {
			List<List<Double>> embeddings = new ArrayList<>();
			List<String> routeNames = new ArrayList<>();
			List<String> utterancesList = new ArrayList<>();

			for (Route route : routes) {
				List<List<Double>> routeEmbeddings = encoder.encode(route.getUtterances());
				embeddings.addAll(routeEmbeddings);
				routeNames.addAll(Collections.nCopies(routeEmbeddings.size(), route.getName()));
				utterancesList.addAll(route.getUtterances());
			}

			index.add(embeddings, routeNames, utterancesList);
		}
	}

	// public Optional<RouteChoice> findBestMatchingRoute(String text) {
	// List<Double> encodedText = encoder.encode(Collections.singletonList(text)).get(0);
	// List<Pair<String, Double>> queryResults = index.query(encodedText, 1);
	//
	// return queryResults.stream()
	// .filter(result -> result.getRight() > scoreThreshold)
	// .findFirst()
	// .map(result -> new RouteChoice(result.getLeft(), null, result.getRight(), true));
	// }

	public RouteChoice call(String text) {
		List<Double> encodedText = encoder.encode(Collections.singletonList(text)).get(0);
		List<Pair<String, Double>> queryResults = index.query(encodedText, 1);
		if (!queryResults.isEmpty()) {
			Pair<String, Double> bestMatch = queryResults.get(0);
			String bestRouteName = bestMatch.getLeft();
			double bestScore = bestMatch.getRight();
			if (bestScore > scoreThreshold) {
				return new RouteChoice(bestRouteName, null, bestScore, true);
			}
		}
		// Return a RouteChoice with null name and 0.0 similarityScore if no matching
		// route is found
		return new RouteChoice(null, null, 0.0, false);
	}

	public List<Route> getRoutes() {
		return Collections.unmodifiableList(routes);
	}

	public double getScoreThreshold() {
		return scoreThreshold;
	}

	public void updateThresholds(Map<String, Double> newThresholds) {
		routes.forEach(route -> {
			Double newThreshold = newThresholds.get(route.getName());
			if (newThreshold != null) {
				route.setScoreThreshold(newThreshold);
			}
		});
	}

	public void setEncoder(Encoder encoder) {
		this.encoder = encoder;
		// Re-initialize the index with the new encoder
		initializeIndexWithRoutes();
	}

	public void setScoreThreshold(double scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}

	// public double evaluate(List<String> texts, List<String> expectedRoutes) {
	// long correct = IntStream.range(0, texts.size())
	// .filter(i -> findBestMatchingRoute(texts.get(i)).map(RouteChoice::getName)
	// .equals(Optional.ofNullable(expectedRoutes.get(i))))
	// .count();
	// return (double) correct / texts.size();
	// }

	public void deleteRoute(String routeName) {
		index.delete(routeName);
		routes.removeIf(route -> route.getName().equals(routeName));
	}

	public void addRoute(Route route) {
		List<List<Double>> embeddings = encoder.encode(route.getUtterances());
		index.add(embeddings, Collections.singletonList(route.getName()), route.getUtterances());
		routes.add(route);
	}

}
