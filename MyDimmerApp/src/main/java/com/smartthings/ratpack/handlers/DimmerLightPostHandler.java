package com.smartthings.ratpack.handlers;


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
import com.smartthings.behaviour.constants.Constants;

import ratpack.exec.ExecResult;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.TypedData;
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
		Promise<TypedData> payLoad = ctx.getRequest().getBody();
		ExecResult<String> body = ExecHarness.yieldSingle(c -> payLoad.map(p -> p.getText()));
		System.out.println("Level Json as String : "+body.getValue());
		System.out.println("before post :: handle");
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(Constants.smartAppEndpointURL + Constants.setLevlPath);
		System.out.println(httppost.getURI());
		//System.out.println(httppost.getRequestLine());
		httppost.setHeader("Authorization", Constants.authorizationKey);
		httppost.setHeader("Content-Type","application/json");
		httppost.setEntity(new StringEntity(body.getValue()));
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
