package com.smartthings.ratpack.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.smartthings.behaviour.constants.Constants;

import ratpack.handling.Context;
import ratpack.handling.Handler;

public class DimmerLightGetHandler implements Handler{

	@Override
	public void handle(Context ctx) throws Exception {
		HttpClient httpclient = null;
		String responseString = "Did not work!";
		try{
		 httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(Constants.smartAppEndpointURL+Constants.getSwitchesPath);
		httpGet.setHeader("Authorization", Constants.authorizationKey);
		HttpResponse response = httpclient.execute(httpGet);
		InputStream inputStream = response.getEntity().getContent();
		if (inputStream != null)
			responseString = convertInputStreamToString(inputStream);
		
		}catch(Exception e){
			e.getLocalizedMessage();
		}finally{
		}
		ctx.render(responseString);
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
