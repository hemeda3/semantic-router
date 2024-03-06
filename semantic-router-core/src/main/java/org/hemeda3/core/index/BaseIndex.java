package org.hemeda3.core.index;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface BaseIndex {

	void add(List<List<Double>> embeddings, List<String> routes, List<String> utterances);

	List<Pair<String, Double>> query(List<Double> vector, int topK);

	void delete(String routeName);
	// Additional methods as needed

}