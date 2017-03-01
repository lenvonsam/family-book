package org.family.book.service;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	public void xx () {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = "{\"name\":\"mkyong\", \"age\":29}";
			Map<String, Object> map = new HashMap<String, Object>();

			// convert JSON string to Map
			map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});

			System.out.println(map);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String[] args) {
		new Test().xx();
	}
}
