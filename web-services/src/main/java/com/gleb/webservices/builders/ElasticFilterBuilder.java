package com.gleb.webservices.builders;

import java.util.HashMap;
import java.util.Map;

public class ElasticFilterBuilder {

	private Map<String, Object> filters = new HashMap<String, Object>();

	public static ElasticFilterBuilder filter() {
		return new ElasticFilterBuilder();
	}

	private ElasticFilterBuilder() {

	}

	public ElasticFilterBuilder with(String filterName, Object value) {
		filters.put(filterName, value);
		return this;
	}

	public Map<String, Object> build() {
		return filters;
	}
}
