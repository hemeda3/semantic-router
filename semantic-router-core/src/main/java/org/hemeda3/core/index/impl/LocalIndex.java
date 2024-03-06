package org.hemeda3.core.index.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.hemeda3.core.index.BaseIndex;
import org.hemeda3.core.utils.IndexUtils;

import java.util.ArrayList;
import java.util.List;

public class LocalIndex implements BaseIndex {

	private double[][] index;

	private String[] routes;

	private String[] utterances;

	private String type = "local";

	public LocalIndex() {
		this.index = null;
		this.routes = null;
		this.utterances = null;
	}

	@Override
	public void add(List<List<Double>> embeddings, List<String> routes, List<String> utterances) {
		double[][] newEmbeddings = IndexUtils.convertTo2DArray(embeddings);

		if (this.index == null) {
			this.index = newEmbeddings;
			this.routes = routes.toArray(new String[0]);
			this.utterances = utterances.toArray(new String[0]);
		}
		else {
			this.index = IndexUtils.concatenate(this.index, newEmbeddings);
			this.routes = IndexUtils.concatenate(this.routes, routes.toArray(new String[0]));
			this.utterances = IndexUtils.concatenate(this.utterances, utterances.toArray(new String[0]));
		}
	}

	@Override
	public List<Pair<String, Double>> query(List<Double> vector, int topK) {
		double[] queryVector = vector.stream().mapToDouble(Double::doubleValue).toArray();
		List<Pair<Integer, Double>> topScores = IndexUtils.topScores(queryVector, this.index, topK);

		List<Pair<String, Double>> results = new ArrayList<>();
		for (Pair<Integer, Double> score : topScores) {
			results.add(Pair.of(this.routes[score.getLeft()], score.getRight()));
		}

		return results;
	}

	@Override
	public void delete(String routeName) {
		List<Integer> indicesToDelete = new ArrayList<>();
		for (int i = 0; i < this.routes.length; i++) {
			if (this.routes[i].equals(routeName)) {
				indicesToDelete.add(i);
			}
		}

		this.index = IndexUtils.deleteRows(this.index, indicesToDelete);
		this.routes = IndexUtils.deleteElements(this.routes, indicesToDelete);
		this.utterances = IndexUtils.deleteElements(this.utterances, indicesToDelete);
	}

	public String describe() {
		return String.format("Type: %s, Dimensions: %d, Vectors: %d", this.type,
				this.index != null ? this.index[0].length : 0, this.index != null ? this.index.length : 0);
	}

	public int length() {
		return this.index != null ? this.index.length : 0;
	}

}
