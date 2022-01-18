package com.zversal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class Practice {
	public static final Type hashTypeToken = new TypeToken<HashMap<String, Object>>() {
	}.getType();

	public APIGatewayProxyResponseEvent practiceLambda(APIGatewayProxyRequestEvent input, Context context) {

		APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

		Gson gson = new Gson();
//		Map<String, String> QueryStringParameters = input.getQueryStringParameters();
//		responseEvent.setBody("{\"id\":" + QueryStringParameters.get("id") + "\"}");

//		responseEvent.setHeaders(QueryStringParameters); Don't set unnecessary headers
		Map<String, Object> responseMap = new HashMap<>();
		try {

			String body = input.getBody();
			System.out.println("Body : " + body);
  
//		1st Way - using typetoken, converting from json to map of string and object
			HashMap<String, Object> response = gson.fromJson(body, hashTypeToken);
			System.out.println("Response converted using type token : Id : " + response.get("id"));

//		//2nd Way - using Json Object 
			JsonObject responseJson = gson.fromJson(body, JsonObject.class);
			System.out.println("Response converted using Json Object class : Name : " + responseJson.get("name"));

			// Set Response
			responseMap.put(body, responseJson);
		//	responseMap.put("{\"id\":" + response.get("id") + "\"}", responseJson);
		//	responseMap.put("{\"name\":" + response.get("name") + "\"}", responseJson);
		//	responseMap.put("msg", "Success");
			String responeString = gson.toJson(responseMap);
			return responseEvent.withStatusCode(200).withBody(responeString);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			responseMap.put("msg", "Failure");
			responseMap.put("Exception", e.getMessage());
			String responeString = gson.toJson(responseMap);
			return responseEvent.withStatusCode(400).withBody(responeString);
		}

	}

}
