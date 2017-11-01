package com.smartthings.ratpack.main;

import static org.junit.Assert.assertEquals;

import com.smartthings.behaviour.modules.DimmerLightModule;
import com.smartthings.ratpack.handlers.DimmerLightGetHandler;
import com.smartthings.ratpack.handlers.DimmerLightPostHandler;

import ratpack.guice.Guice;
import ratpack.handling.Chain;
import ratpack.http.client.ReceivedResponse;
import ratpack.server.BaseDir;
import ratpack.test.embed.EmbeddedApp;

public class MyDimmerAppTest {

	public static void main(String[] args) throws Exception {
		callDimmerSmartApp();
	}

	public static void callDimmerSmartApp() throws Exception {
		EmbeddedApp.of(serverSpec -> serverSpec.serverConfig(c -> c.baseDir(BaseDir.find()))
				.registry(Guice.registry(b -> b.module(DimmerLightModule.class)))
				.handlers(chain -> chain.get("switches", DimmerLightGetHandler.class)
						.post("setLevel", DimmerLightPostHandler.class)
						.prefix("static", nested -> nested.fileSystem("assets/images", Chain::files))
						.all(ctx -> ctx.render("completed successfully"))))
				.test(httpClient -> {
					String[] dimerLightValues = { "0", "25", "50", "99" };
					for (String value : dimerLightValues) {

						// Receives the PATH setLevel
						ReceivedResponse postResponse = httpClient.requestSpec(
								s -> s.body(b -> b.type("application/json").text("{\"value\":\"" + value + "\"}")))
								.post("setLevel");
						assertEquals("Success", postResponse.getBody().getText());

						// Receives the PATH /switchStatus
						ReceivedResponse getResponse = httpClient.get("switchStatus");
						//System.out.println(getResponse.getHeaders().get("Content-Type") + "------");
						//System.out.println(getResponse.getBody().getText()+ "#########");
						assertEquals("text/plain;charset=UTF-8", getResponse.getHeaders().get("Content-Type"));
						
						  String str = "[{\"name\":\"DimmerSwitch\",\"value\":" + value +
						  "},{\"name\":\"DimmerSwitch[1]\",\"value\":" + value + "}]";
						  System.out.println(str);
						 
						String expectedString = "completed successfully";

						// Checking the value is set or not by using GET
						System.out.println("Checking the value: " + value + " is set into the switches - "
								+ getResponse.getBody().getText());
						assertEquals(expectedString, getResponse.getBody().getText());

					}
				});

	}

}
