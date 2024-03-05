package org.springframework.ai.vectorstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SimpleVectorStore is a simple implementation of the VectorStore interface.
 *
 * It also provides methods to save the current state of the vectors to a file, and to
 * load vectors from a file.
 *
 * For a deeper understanding of the mathematical concepts and computations involved in
 * calculating similarity scores among vectors, refer to this
 * [resource](https://docs.spring.io/spring-ai/reference/api/vectordbs.html#_understanding_vectors).
 *
 * @author Raphael Yu
 * @author Dingmeng Xue
 * @author Mark Pollack
 * @author Christian Tzolov
 */
public class SimpleVectorStore implements VectorStore {

	private static final Logger logger = LoggerFactory.getLogger(SimpleVectorStore.class);

	protected Map<String, Document> store = new ConcurrentHashMap<>();

	protected EmbeddingClient embeddingClient;

	public SimpleVectorStore(EmbeddingClient embeddingClient) {
		Objects.requireNonNull(embeddingClient, "EmbeddingClient must not be null");
		this.embeddingClient = embeddingClient;
	}

	@Override
	public void add(List<Document> documents) {
		for (Document document : documents) {
			logger.info("Calling EmbeddingClient for document id = {}", document.getId());
			List<Double> embedding = this.embeddingClient.embed(document);
			document.setEmbedding(embedding);
			this.store.put(document.getId(), document);
		}
	}

	@Override
	public Optional<Boolean> delete(List<String> idList) {
		for (String id : idList) {
			this.store.remove(id);
		}
		return Optional.of(true);
	}

	@Override
	public List<Document> similaritySearch(SearchRequest request) {
		if (request.getFilterExpression() != null) {
			throw new UnsupportedOperationException(
					"The [" + this.getClass() + "] doesn't support metadata filtering!");
		}

		List<Double> userQueryEmbedding = getUserQueryEmbedding(request.getQuery());
		return this.store.values()
			.stream()
			.map(entry -> new Similarity(entry.getId(),
					EmbeddingMath.cosineSimilarity(userQueryEmbedding, entry.getEmbedding())))
			.filter(s -> s.score >= request.getSimilarityThreshold())
			.sorted(Comparator.<Similarity>comparingDouble(s -> s.score).reversed())
			.limit(request.getTopK())
			.map(s -> this.store.get(s.key))
			.toList();
	}

	/**
	 * Serialize the vector store content into a file in JSON format.
	 * @param file the file to save the vector store content
	 */
	public void save(File file) {
		String json = getVectorDbAsJson();
		try {
			if (!file.exists()) {
				logger.info("Creating new vector store file: {}", file);
				file.createNewFile();
			}
			else {
				logger.info("Overwriting existing vector store file: {}", file);
			}
			try (OutputStream stream = new FileOutputStream(file);
					Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
				writer.write(json);
				writer.flush();
			}
		}
		catch (IOException ex) {
			logger.error("IOException occurred while saving vector store file.", ex);
			throw new RuntimeException(ex);
		}
		catch (SecurityException ex) {
			logger.error("SecurityException occurred while saving vector store file.", ex);
			throw new RuntimeException(ex);
		}
		catch (NullPointerException ex) {
			logger.error("NullPointerException occurred while saving vector store file.", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Deserialize the vector store content from a file in JSON format into memory.
	 * @param file the file to load the vector store content
	 */
	public void load(File file) {
		TypeReference<HashMap<String, Document>> typeRef = new TypeReference<>() {
		};
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String, Document> deserializedMap = objectMapper.readValue(file, typeRef);
			this.store = deserializedMap;
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Deserialize the vector store content from a resource in JSON format into memory.
	 * @param resource the resource to load the vector store content
	 */
	public void load(Resource resource) {
		TypeReference<HashMap<String, Document>> typeRef = new TypeReference<>() {
		};
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Map<String, Document> deserializedMap = objectMapper.readValue(resource.getInputStream(), typeRef);
			this.store = deserializedMap;
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private String getVectorDbAsJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
		String json;
		try {
			json = objectWriter.writeValueAsString(this.store);
		}
		catch (JsonProcessingException e) {
			throw new RuntimeException("Error serializing documentMap to JSON.", e);
		}
		return json;
	}

	private List<Double> getUserQueryEmbedding(String query) {
		return this.embeddingClient.embed(query);
	}

	public static class Similarity {

		private String key;

		private double score;

		public Similarity(String key, double score) {
			this.key = key;
			this.score = score;
		}

	}

	public class EmbeddingMath {

		private EmbeddingMath() {
			throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
		}

		public static double cosineSimilarity(List<Double> vectorX, List<Double> vectorY) {
			if (vectorX == null || vectorY == null) {
				throw new RuntimeException("Vectors must not be null");
			}
			if (vectorX.size() != vectorY.size()) {
				throw new IllegalArgumentException("Vectors lengths must be equal");
			}

			double dotProduct = dotProduct(vectorX, vectorY);
			double normX = norm(vectorX);
			double normY = norm(vectorY);

			if (normX == 0 || normY == 0) {
				throw new IllegalArgumentException("Vectors cannot have zero norm");
			}

			return dotProduct / (Math.sqrt(normX) * Math.sqrt(normY));
		}

		public static double dotProduct(List<Double> vectorX, List<Double> vectorY) {
			if (vectorX.size() != vectorY.size()) {
				throw new IllegalArgumentException("Vectors lengths must be equal");
			}

			double result = 0;
			for (int i = 0; i < vectorX.size(); ++i) {
				result += vectorX.get(i) * vectorY.get(i);
			}

			return result;
		}

		public static double norm(List<Double> vector) {
			return dotProduct(vector, vector);
		}

	}

}
