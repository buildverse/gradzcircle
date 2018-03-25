package com.drishika.gradzcircle.web.rest.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomJobFilterDeserializer extends StdDeserializer<String> {
	private final Logger log = LoggerFactory.getLogger(CustomJobFilterDeserializer.class);

	public CustomJobFilterDeserializer() {
		this(null);
	}

	public CustomJobFilterDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public String deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
		TreeNode tree = jsonparser.getCodec().readTree(jsonparser);
		return tree.toString();

	}
}