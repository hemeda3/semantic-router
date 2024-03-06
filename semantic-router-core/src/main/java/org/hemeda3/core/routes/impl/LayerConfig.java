package org.hemeda3.core.routes.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class LayerConfig {

	private static final Logger logger = Logger.getLogger(LayerConfig.class.getName());

	private String encoderType;

	private String encoderName;

	private List<Route> routes;

	public LayerConfig(String encoderType, String encoderName, List<Route> routes) {
		this.encoderType = encoderType;
		this.encoderName = encoderName;
		this.routes = routes;
	}

	public static LayerConfig fromFile(String path) throws IOException {
		ObjectMapper mapper;
		String extension = FilenameUtils.getExtension(path);
		if ("json".equals(extension)) {
			mapper = new ObjectMapper();
		}
		else if ("yaml".equals(extension) || "yml".equals(extension)) {
			mapper = new ObjectMapper(new YAMLFactory());
		}
		else {
			throw new IllegalArgumentException("Unsupported file type: " + extension);
		}

		String content = new String(Files.readAllBytes(Paths.get(path)));
		Map<String, Object> data = mapper.readValue(content, Map.class);

		if (!isValid(data)) {
			throw new IllegalArgumentException("Invalid layer configuration");
		}

		// Handle the 'llm' field specially if it exists
		List<Map<String, Object>> routesData = (List<Map<String, Object>>) data.get("routes");
		for (Map<String, Object> routeData : routesData) {
			if (routeData.containsKey("llm") && routeData.get("llm") != null) {
				Map<String, Object> llmData = (Map<String, Object>) routeData.remove("llm");
				// Use the module path directly from llmData without modification
				String llmModulePath = (String) llmData.get("module");
				// Dynamically import the module and then the class from that module
				// Not directly translatable to Java, need to handle differently
				// llm_module = importlib.import_module(llm_module_path)
				// llm_class = getattr(llm_module, llm_data["class"])
				// Instantiate the LLM class with the provided model name
				// llm = llm_class(name=llm_data["model"])
				// route_data["llm"] = llm // Reassign the instantiated llm object back to
				// route_data
			}
		}

		return new LayerConfig((String) data.get("encoder_type"), (String) data.get("encoder_name"), null);
	}

	public void toFile(String path) throws JsonProcessingException {
		ObjectMapper mapper;
		String extension = FilenameUtils.getExtension(path);
		if ("json".equals(extension)) {
			mapper = new ObjectMapper();
		}
		else if ("yaml".equals(extension) || "yml".equals(extension)) {
			mapper = new ObjectMapper(new YAMLFactory());
		}
		else {
			throw new IllegalArgumentException("Unsupported file type: " + extension);
		}

		String content = mapper.writeValueAsString(toMap());
		try {
			Files.write(Paths.get(path), content.getBytes());
		}
		catch (IOException e) {
			logger.severe("Failed to save route config to file: " + e.getMessage());
		}
	}

	public Map<String, Object> toMap() {
		return Map.of("encoder_type", encoderType, "encoder_name", encoderName, "routes", routes);
	}

	public static boolean isValid(Map<String, Object> layerConfig) {
		return layerConfig.containsKey("encoder_name") && layerConfig.containsKey("encoder_type")
				&& layerConfig.containsKey("routes");
	}

	// Getters and setters
	public String getEncoderType() {
		return encoderType;
	}

	public void setEncoderType(String encoderType) {
		this.encoderType = encoderType;
	}

	public String getEncoderName() {
		return encoderName;
	}

	public void setEncoderName(String encoderName) {
		this.encoderName = encoderName;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

}
