/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ai.chat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * Unit Tests for {@link ChatClient}.
 *
 * @author John Blum
 * @since 0.2.0
 */
class ChatClientTests {

	@Test
	void generateWithStringCallsGenerateWithPromptAndReturnsResponseCorrectly() {

		String userMessage = "Zero Wing";
		String responseMessage = "All your bases are belong to us";

		ChatClient mockClient = Mockito.mock(ChatClient.class);

		AssistantMessage mockAssistantMessage = Mockito.mock(AssistantMessage.class);
		when(mockAssistantMessage.getContent()).thenReturn(responseMessage);

		// Create a mock Generation
		Generation generation = Mockito.mock(Generation.class);
		when(generation.getOutput()).thenReturn(mockAssistantMessage);

		// Create a mock ChatResponse with the mock Generation
		ChatResponse response = Mockito.mock(ChatResponse.class);
		when(response.getResult()).thenReturn(generation);

		// Generation generation = spy(new Generation(responseMessage));
		// ChatResponse response = spy(new
		// ChatResponse(Collections.singletonList(generation)));

		doCallRealMethod().when(mockClient).call(anyString());

		doAnswer(invocationOnMock -> {

			Prompt prompt = invocationOnMock.getArgument(0);

			assertThat(prompt).isNotNull();
			assertThat(prompt.getContents()).isEqualTo(userMessage);

			return response;

		}).when(mockClient).call(any(Prompt.class));

		assertThat(mockClient.call(userMessage)).isEqualTo(responseMessage);

		verify(mockClient, times(1)).call(eq(userMessage));
		verify(mockClient, times(1)).call(isA(Prompt.class));
		verify(response, times(1)).getResult();
		verify(generation, times(1)).getOutput();
		verify(mockAssistantMessage, times(1)).getContent();
		verifyNoMoreInteractions(mockClient, generation, response);
	}

}
