// package org.hemeda3.ai.llms.impl;
//
// import org.hemeda3.ai.llms.OpenAIClient;
// import org.hemeda3.core.ai.embedding.EmbeddingRequest;
// import org.hemeda3.core.ai.embedding.EmbeddingResponse;
// import org.hemeda3.mappings.EmbeddingMapper;
// import org.mapstruct.factory.Mappers;
// import org.springframework.ai.embedding.Embedding;
// import org.springframework.ai.openai.api.OpenAiApi;
// import org.springframework.http.ResponseEntity;
//
// import java.util.List;
// import java.util.Objects;
// import java.util.stream.Collectors;
//
// public class MyOpenAiEmbeddingClient implements OpenAIClient {
//
// private final OpenAiApi openAiApi;
//
// EmbeddingMapper INSTANCE = Mappers.getMapper(EmbeddingMapper.class);
//
// public MyOpenAiEmbeddingClient(OpenAiApi openAiApi) {
// this.openAiApi = openAiApi;
// }
//
// @Override
// public EmbeddingResponse createEmbeddings(EmbeddingRequest embeddingRequest) {
// // Implement the logic to call the OpenAI API and return the response
// ResponseEntity<OpenAiApi.EmbeddingList<OpenAiApi.Embedding>> response = openAiApi
// .embeddings(INSTANCE.toSpringEmbeddingOpenAiRequest(embeddingRequest));
//
// // Convert the OpenAiApi.EmbeddingList<OpenAiApi.Embedding> to EmbeddingResponse
// List<Embedding> embeddings = Objects.requireNonNull(response.getBody())
// .data()
// .stream()
// .map(openAiEmbedding -> new Embedding(openAiEmbedding.embedding(),
// openAiEmbedding.index()))
// .collect(Collectors.toList());
//
// return new EmbeddingResponse(embeddings);
// }
//
// }
