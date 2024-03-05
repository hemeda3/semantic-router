package org.springframework.ai.aot;

import org.springframework.ai.chat.messages.*;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Set;

public class SpringAiCoreRuntimeHints implements RuntimeHintsRegistrar {

	@Override
	public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {

		var chatTypes = Set.of(AbstractMessage.class, AssistantMessage.class, ChatMessage.class, FunctionMessage.class,
				Message.class, MessageType.class, UserMessage.class, SystemMessage.class, FunctionCallbackContext.class,
				FunctionCallback.class, FunctionCallbackWrapper.class);
		for (var c : chatTypes) {
			hints.reflection().registerType(c);
		}

		Method getDescription = ReflectionUtils.findMethod(FunctionCallback.class, "getDescription");
		hints.reflection().registerMethod(getDescription, ExecutableMode.INVOKE);
		Method getInputTypeSchema = ReflectionUtils.findMethod(FunctionCallback.class, "getInputTypeSchema");
		hints.reflection().registerMethod(getInputTypeSchema, ExecutableMode.INVOKE);
		Method getName = ReflectionUtils.findMethod(FunctionCallback.class, "getName");
		hints.reflection().registerMethod(getName, ExecutableMode.INVOKE);

		for (var r : Set.of("antlr4/org/springframework/ai/vectorstore/filter/antlr4/Filters.g4",
				"embedding/embedding-model-dimensions.properties"))
			hints.resources().registerResource(new ClassPathResource(r));

	}

}