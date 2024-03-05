package org.springframework.ai.chat.prompt;

import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.Map;

public interface PromptTemplateChatActions {

	List<Message> createMessages();

	List<Message> createMessages(Map<String, Object> model);

}
