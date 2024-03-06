// package org.hemeda3.core.mappings;
//
// import org.mapstruct.Mapper;
// import org.springframework.ai.openai.api.OpenAiApi;
//
// @Mapper
// public interface EmbeddingMapper {
//
// org.springframework.ai.embedding.EmbeddingRequest toSpringRequest(
// org.hemeda3.core.ai.embedding.EmbeddingRequest request);
//
// org.hemeda3.core.ai.embedding.EmbeddingRequest toHemedaRequest(
// org.springframework.ai.embedding.EmbeddingRequest request);
//
// org.springframework.ai.openai.api.OpenAiApi.EmbeddingRequest
// toSpringEmbeddingOpenAiRequest(
// org.hemeda3.core.ai.embedding.EmbeddingRequest request);
//
// org.hemeda3.core.ai.embedding.EmbeddingRequest toCoreEmbeddingRequest(
// org.springframework.ai.embedding.EmbeddingRequest request);
//
// org.springframework.ai.embedding.EmbeddingResponse toSpringEmbeddingResponse(
// org.hemeda3.core.ai.embedding.EmbeddingResponse response);
//
// org.hemeda3.core.ai.embedding.EmbeddingResponse toCoreEmbeddingResponse(
// org.springframework.ai.embedding.EmbeddingResponse response);
//
// org.springframework.ai.embedding.EmbeddingOptions toSpringEmbeddingOptions(
// org.hemeda3.core.ai.embedding.EmbeddingOptions options);
//
// org.hemeda3.core.ai.embedding.EmbeddingOptions toCoreEmbeddingOptions(
// org.springframework.ai.embedding.EmbeddingOptions options);
//
// // New mapping method
// org.hemeda3.core.ai.embedding.EmbeddingResponse toCoreEmbeddingResponseFromOpenAi(
// OpenAiApi.EmbeddingList<OpenAiApi.Embedding> openAiEmbeddingList);
//
// }
