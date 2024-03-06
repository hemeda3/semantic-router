package org.hemeda3.core.llms;

import java.util.List;

public abstract class BaseLLM {

	protected String name;

	public BaseLLM(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// Abstract method that subclasses must implement
	public abstract String call(List<Message> messages);

}
