/*
 * Copyright 2024-2024 the original author or authors.
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

package org.springframework.ai.embedding;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.model.ResponseMetadata;

/**
 * @author Christian Tzolov
 */
public class EmbeddingResponseMetadata extends HashMap<String, Object> implements ResponseMetadata {

	private static final long serialVersionUID = 1L;

	public EmbeddingResponseMetadata() {
	}

	public EmbeddingResponseMetadata(int initialCapacity) {
		super(initialCapacity);
	}

	public EmbeddingResponseMetadata(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public EmbeddingResponseMetadata(Map<String, ?> metadata) {
		super(metadata);
	}

}
