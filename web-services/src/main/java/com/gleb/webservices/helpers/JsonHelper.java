package com.gleb.webservices.helpers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class JsonHelper {

	public Map<String, Object> makeAsMap(String key, Object value) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(key, value);
		return result;
	}
}
