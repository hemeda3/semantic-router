// package org.hemeda3.core.utils;
//
// import org.hemeda3.core.routes.impl.Route;
// import org.hemeda3.core.routes.impl.RouteLayer;
//
// import java.util.HashMap;
// import java.util.Map;
//
// public class RouteLayerUtils {
//
// public static Map<String, Double> thresholdRandomSearch(RouteLayer routeLayer, double
// searchRange) {
// Map<String, Double> bestThresholds = new HashMap<>();
// double bestAccuracy = 0.0;
//
// for (int i = 0; i < 100; i++) { // Perform 100 iterations
// Map<String, Double> thresholds = new HashMap<>();
// for (Route route : routeLayer.getRoutes()) {
// double currentThreshold = route.getScoreThreshold();
// double newThreshold = currentThreshold + (Math.random() * 2 * searchRange) -
// searchRange;
// newThreshold = Math.max(0.0, Math.min(newThreshold, 1.0)); // Ensure
// // threshold
// // is between
// // 0 and 1
// thresholds.put(route.getName(), newThreshold);
// }
//
// routeLayer.updateThresholds(thresholds);
// double accuracy = routeLayer.evaluate(null, null); // Implement an evaluate
// // method to check the
// // accuracy
//
// if (accuracy > bestAccuracy) {
// bestAccuracy = accuracy;
// bestThresholds = new HashMap<>(thresholds);
// }
// }
//
// return bestThresholds;
// }
//
// }
