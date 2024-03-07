# Semantic Router (Java Version)

Semantic Router is a superfast decision-making layer for your Large Language Models (LLMs) and agents based `spring-ai` project . Rather than waiting for slow LLM generations to make tool-use decisions, Semantic Router uses the magic of semantic vector space to make those decisions — routing requests using semantic meaning.

## Quickstart

To get started with Semantic Router in Java, you first need to define a set of `Route` objects. These are the decision paths that the semantic router can decide to use. Let's try two simple routes for now — one for talk on politics and another for chitchat:

```java
import org.hemeda3.core.routes.impl.Route;

Route politics = new Route("politics",
    List.of("isn't politics the best thing ever", "why don't you tell me about your political opinions",
            "don't you just love the president", "don't you just hate the president",
            "they're going to destroy this country!", "they will save the country!"),
    "A route to avoid or handle political conversations");

Route chitchat = new Route("chitchat",
    List.of("how's the weather today?", "how are things going?", "lovely weather today",
            "the weather is horrendous", "let's go to the chippy"),
    "A route for casual chitchat and small talk");

List<Route> routes = List.of(politics, chitchat);
```
Next, we initialize an embedding/encoder model. We currently support OpenAI's GPT-3 embeddings. To initialize it, we do:

```java
import org.hemeda3.core.encoders.Encoder;
import org.hemeda3.core.encoders.impl.OpenAIEncoder;
import org.springframework.ai.openai.OpenAiEmbeddingClient;

OpenAiEmbeddingClient openAiEmbeddingClient = new OpenAiEmbeddingClient(new OpenAiApi("<YOUR_OPENAI_API_KEY>"));
Encoder encoder = new OpenAIEncoder(openAiEmbeddingClient);

```

With our routes and encoder defined, we now create a RouteLayer. The route layer handles our semantic decision making.

```java
import org.hemeda3.core.routes.impl.RouteLayer;

RouteLayer routeLayer = new RouteLayer(encoder, routes, 0.8);

```

We can now use our route layer to make super fast decisions based on user queries. Let's try with two queries that should trigger our route decisions:


```java

RouteChoice decision = routeLayer.call("how's the weather today?");
System.out.println(decision.getName());
// Output: "chitchat"

```

If a query doesn't match any route, the route layer will return a RouteChoice with a null name.


This Java port of Semantic Router is based on the Spring AI project, leveraging the spring-ai-openai-spring-boot-starter dependency for seamless integration with Spring Boot applications. By utilizing the Spring AI framework, the Semantic Router takes advantage of its robust and scalable architecture, making it easier to build and deploy AI-powered applications with the OpenAI API.


## from Configuration

```properties
semantic-router-api.enabled=true
semantic-router-api.score-threshold=0.75
semantic-router-api.default-route=fallback

# Define routes as a list of comma-separated values
semantic-router-api.routes.politics.utterances=isn't politics the best thing ever,why don't you tell me about your political opinions,don't you just love the president,don't you just hate the president,they're going to destroy this country!,they will save the country!
semantic-router-api.routes.politics.description=A route to avoid or handle political conversations

semantic-router-api.routes.chitchat.utterances=how's the weather today?,how are things going?,lovely weather today,the weather is horrendous,let's go to the chippy
semantic-router-api.routes.chitchat.description=A route for casual chitchat and small talk

```
Semantic Router is released under the MIT License. See the LICENSE file for more details.

