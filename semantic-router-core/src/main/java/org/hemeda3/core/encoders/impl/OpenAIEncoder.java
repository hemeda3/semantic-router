package org.hemeda3.core.encoders.impl;

import org.hemeda3.core.encoders.Encoder;
import org.mapstruct.factory.Mappers;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.openai.OpenAiEmbeddingClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OpenAIEncoder implements Encoder {

	private final OpenAiEmbeddingClient openAIClient;

	public OpenAIEncoder(OpenAiEmbeddingClient openAIClient) {
		this.openAIClient = openAIClient;
	}

	@Override
	public List<List<Double>> encode(List<String> documents) {
		EmbeddingRequest request = new EmbeddingRequest(documents, EmbeddingOptions.EMPTY);
		EmbeddingResponse response = openAIClient.call(request);
		return response.getResults()
			.stream()
			.map(result -> new ArrayList<>(result.getOutput()))
			.collect(Collectors.toList());

	}

}
