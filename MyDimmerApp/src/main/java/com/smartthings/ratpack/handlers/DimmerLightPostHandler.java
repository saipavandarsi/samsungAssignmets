package com.smartthings.ratpack.handlers;

import static ratpack.jackson.Jackson.fromJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.smartthings.behaviour.beans.DimmerLight;
import com.smartthings.behaviour.constants.Constants;

import ratpack.exec.ExecResult;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.TypedData;
import ratpack.jackson.Jackson;
import ratpack.test.exec.ExecHarness;

public class DimmerLightPostHandler implements Handler {
	public static String jsonResponse = "completed";
	public String val = "";

	@Inject
	public DimmerLightPostHandler() {
		System.out.println("Post Default Constructor");
	}

	@Override
	public void handle(Context ctx) throws Exception {
		System.out.println("entered post :: handle");
		Promise<TypedData> body = ctx.getRequest().getBody();
		ExecResult<String> result1 = ExecHarness.yieldSingle(c -> body.map(p -> p.getText()));
		System.out.println("Level Json as String : "+result1.getValue());
		JsonFactory _factory = new JsonFactory();
		JsonParser parser = _factory.createJsonParser(result1.getValue().toString());
		//JsonFactory _factory = JsonFactory.createParser(result1.getValue().toString());
		//JSONWrappedObject json = (JSONWrappedObject) parser.parse(stringToParse);
		Promise<DimmerLight> payLoadValue = ctx.parse(fromJson(DimmerLight.class));
		
		//ExecResult<String> result1 = ExecHarness.yieldSingle(c -> payLoadValue.map(p -> p.getValue()));
		System.out.println("before post :: handle");
		HttpClient httpclient = HttpClients.createDefault();
		//HttpPost httppost = new HttpPost(Constants.smartAppEndpointURL + Constants.setLevlPath + result1.getValue());
		HttpPost httppost = new HttpPost(Constants.smartAppEndpointURL + Constants.setLevlPath);
		// + Jackson.json(result1.getValue())
		System.out.println(httppost.getURI());
		System.out.println(httppost.getRequestLine());
		httppost.setHeader("Authorization", Constants.authorizationKey);
		httppost.setHeader("Content-Type","application/json");
		httppost.setEntity(new StringEntity(result1.getValue()));
		
		HttpResponse response = httpclient.execute(httppost);
		//response.getStatusLine();
		HttpEntity entity = response.getEntity();
		//String jsonResponse = "";
		if (entity != null) {
			InputStream instream = entity.getContent();
			
			try {
				jsonResponse = convertInputStreamToString(instream);
			} finally {
				instream.close();
			}
		}
		System.out.println(jsonResponse);
		ctx.render("successful");
	}
	
	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line+"\n";

		inputStream.close();
		return result;

	}

}
