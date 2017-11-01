package com.smartthings.ratpack.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.smartthings.behaviour.constants.Constants;

public class DimmerAppTest {
	// HttpClient httpclient = null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDimmerAppGetRequest() {
		HttpClient httpclient = null;
		try {
			httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(Constants.smartAppEndpointURL + Constants.getSwitchesPath);
			httpGet.setHeader("Authorization", Constants.authorizationKey);
			HttpResponse response = httpclient.execute(httpGet);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
		} catch (Exception e) {
			fail("test case failed");
		}

	}

	@Test
	public void testDimmerAppPostRequest() {
		HttpClient httpclient = null;
		try {
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(Constants.smartAppEndpointURL + Constants.setLevlPath + 75);
			httpPost.setHeader("Authorization", Constants.authorizationKey);
			HttpResponse response = httpclient.execute(httpPost);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			assertEquals(response.getStatusLine().getStatusCode(), 201);
		} catch (Exception e) {
			fail("test case failed");
		}

	}

	public void testRatPackDimmerAppPostRequest() {
		HttpClient httpclient = null;
		try {
			// EmbeddedApp.fromHandler(c->c.get("/switch"))
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost("http://localhost:5050/" + Constants.setLevlPath + 25);
			httpPost.setHeader("Authorization", Constants.authorizationKey);
			HttpResponse response = httpclient.execute(httpPost);
			assertEquals(response.getStatusLine().getStatusCode(), 201);
		} catch (Exception e) {
			fail("test case failed");
		}

	}

	@Test
	public void testRatpackDimmerAppGetRequest() {
		HttpClient httpclient = null;
		try {
			httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet("http://localhost:5050/" + Constants.getSwitchesPath);
			httpGet.setHeader("Authorization", Constants.authorizationKey);
			HttpResponse response = httpclient.execute(httpGet);
			assertEquals(response.getStatusLine().getStatusCode(), 200);
		} catch (Exception e) {
			fail("test case failed");
		}

	}

}
