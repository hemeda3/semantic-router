package org.hemeda3.core.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IndexUtils {

	public static double[][] convertTo2DArray(List<List<Double>> embeddings) {
		double[][] newEmbeddings = new double[embeddings.size()][];
		for (int i = 0; i < embeddings.size(); i++) {
			List<Double> embedding = embeddings.get(i);
			newEmbeddings[i] = embedding.stream().mapToDouble(Double::doubleValue).toArray();
		}
		return newEmbeddings;
	}

	public static double[][] concatenate(double[][] a, double[][] b) {
		double[][] result = new double[a.length + b.length][];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static String[] concatenate(String[] a, String[] b) {
		String[] result = new String[a.length + b.length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}

	public static double[][] deleteRows(double[][] array, List<Integer> indicesToDelete) {
		double[][] result = new double[array.length - indicesToDelete.size()][];
		int resultIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (!indicesToDelete.contains(i)) {
				result[resultIndex++] = array[i];
			}
		}
		return result;
	}

	public static String[] deleteElements(String[] array, List<Integer> indicesToDelete) {
		String[] result = new String[array.length - indicesToDelete.size()];
		int resultIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (!indicesToDelete.contains(i)) {
				result[resultIndex++] = array[i];
			}
		}
		return result;
	}

	public static List<Pair<Integer, Double>> topScores(double[] queryVector, double[][] index, int topK) {
		double[] scores = new double[index.length];
		for (int i = 0; i < index.length; i++) {
			scores[i] = cosineSimilarity(queryVector, index[i]);
		}

		return IntStream.range(0, scores.length)
			.mapToObj(i -> Pair.of(i, scores[i]))
			.sorted(Comparator.comparing(Pair::getRight, Comparator.reverseOrder()))
			.limit(topK)
			.collect(Collectors.toList());
	}

	private static double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
