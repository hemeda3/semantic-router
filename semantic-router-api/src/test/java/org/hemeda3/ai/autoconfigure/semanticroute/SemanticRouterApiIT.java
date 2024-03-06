package org.hemeda3.ai.autoconfigure.semanticroute;

import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.encoders.impl.OpenAIEncoder;
import org.hemeda3.core.routes.SemanticRouterApiOptions;
import org.hemeda3.core.routes.impl.Route;
import org.hemeda3.core.routes.impl.RouteChoice;
import org.hemeda3.core.routes.impl.RouteLayer;
import org.hemeda3.core.routes.impl.SemanticRouterApiProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SemanticRouterApiIT.TestConfiguration.class)
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class SemanticRouterApiIT {

	@Autowired
	private SemanticRouterApi semanticRouterApi;

	record ActorsFilms(String actor, List<String> movies) {
	}

	@Test
	void testPoliticsRoute() {

		RouteChoice resp = semanticRouterApi.getRoute("don't you love politics?");
		System.out.println(resp.getSimilarityScore());
		System.out.println(resp.getName());

		assertThat(resp.getName()).isEqualTo("politics");
	}

	@Test
	void testChitchatRoute() {

		RouteChoice resp = semanticRouterApi.getRoute("how's the weather today?");
		System.out.println(resp.getSimilarityScore());
		System.out.println(resp.getName());

		assertThat(resp.getName()).isEqualTo("chitchat");
	}

	@Test
	void testUnRelatedQuery() {

		RouteChoice resp = semanticRouterApi.getRoute("I'm interested in learning about llama 2");
		System.out.println(resp.getSimilarityScore());
		System.out.println(resp.getName());

		assertThat(resp.getName()).isNotEqualTo("chitchat");
		assertThat(resp.getName()).isNotEqualTo("politics");
	}

	@SpringBootConfiguration
	public static class TestConfiguration {

		@Bean
		public OpenAiApi openAiApi() {
			String apiKey = System.getenv("OPENAI_API_KEY");
			if (!StringUtils.hasText(apiKey)) {
				throw new IllegalArgumentException(
						"You must provide an API key.  Put it in an environment variable under the name OPENAI_API_KEY");
			}
			return new OpenAiApi(apiKey);
		}

		@Bean
		public OpenAiEmbeddingClient openAiEmbeddingClient(OpenAiApi openAiApi) {
			return new OpenAiEmbeddingClient(openAiApi);
		}

		@Bean
		public Encoder encoder(OpenAiEmbeddingClient openAiEmbeddingClient) {
			return new OpenAIEncoder(openAiEmbeddingClient); // Or any other encoder
																// implementation
		}

		@Bean
		public List<Route> routes() {

			Route politics = new Route("politics",
					List.of("isn't politics the best thing ever", "why don't you tell me about your political opinions",
							"don't you just love the president", "don't you just hate the president",
							"they're going to destroy this country!", "they will save the country!"),
					"A route to avoid or handle political conversations");

			Route chitchat = new Route("chitchat",
					List.of("how's the weather today?", "how are things going?", "lovely weather today",
							"the weather is horrendous", "let's go to the chippy"),
					"A route for casual chitchat and small talk");

			return List.of(politics, chitchat);
		}

		@Bean
		public double scoreThreshold() {
			// Define your score threshold here
			return 0.82;
		}

		@Bean
		public RouteLayer routeLayer(Encoder encoder, List<Route> routes, double scoreThreshold) {
			return new RouteLayer(encoder, routes, scoreThreshold);
		}

		@Bean
		public SemanticRouterApiProperties semanticRouterApiProperties() {
			SemanticRouterApiProperties properties = new SemanticRouterApiProperties();
			properties.setEnabled(true);
			properties.setScoreThreshold(0.82);
			properties.setDefaultRoute("fallback");
			return properties;
		}

		@Bean
		public SemanticRouterApiOptions semanticRouterApiOptions(SemanticRouterApiProperties properties,
				Encoder encoder, List<Route> routes) {
			SemanticRouterApiOptions options = new SemanticRouterApiOptions();
			options.setEncoder(encoder);
			options.setRoutes(routes);
			options.setScoreThreshold(properties.getScoreThreshold());
			options.setDefaultRoute(properties.getDefaultRoute());
			return options;
		}

		@Bean
		public SemanticRouterApi semanticRouterApi(SemanticRouterApiOptions options) {
			return new SemanticRouterApi(options);
		}

	}

}
