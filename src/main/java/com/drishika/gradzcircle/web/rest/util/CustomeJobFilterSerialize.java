package com.drishika.gradzcircle.web.rest.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomeJobFilterSerialize extends StdSerializer<String> {

	private final Logger log = LoggerFactory.getLogger(CustomeJobFilterSerialize.class);

	public CustomeJobFilterSerialize() {
		this(null);
	}

	public CustomeJobFilterSerialize(Class<String> vc) {
		super(vc);
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		jsonGenerator.writeRawValue(value);
	}

}
