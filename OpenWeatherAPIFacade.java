package com.servlet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class OpenWeatherAPIFacade
 */
@WebServlet("/weather")
public class OpenWeatherAPIFacade extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String zipCode = request.getParameter("zip");
		String cityName = request.getParameter("city_name");
		String tempChoice = request.getParameter("temp_choice");

		JSONObject responseObj = getOpenWeatherDetails(zipCode, cityName,
				tempChoice);
		response.getWriter().println(responseObj);
	}

	private JSONObject getOpenWeatherDetails(String zipCode, String cityName,
			String tempChoice) {
		JSONObject responseText = new JSONObject();
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			URI uri = new URI("");
			switch (tempChoice) {
			case "1":
				uri = new URIBuilder().setScheme("http")
						.setHost("api.openweathermap.org")
						.setPath("/data/2.5/weather")
						.setParameter("zip", zipCode)
						.setParameter("q", cityName).build();
				break;
			case "2":
				uri = new URIBuilder().setScheme("http")
						.setHost("api.openweathermap.org")
						.setPath("/data/2.5/weather")
						.setParameter("zip", zipCode)
						.setParameter("q", cityName)
						.setParameter("units", "metric").build();
				break;
			case "3":
				uri = new URIBuilder().setScheme("http")
						.setHost("api.openweathermap.org")
						.setPath("/data/2.5/weather")
						.setParameter("zip", zipCode)
						.setParameter("q", cityName)
						.setParameter("units", "imperial").build();
				break;
			default:
				break;
			}
			HttpGet httpget = new HttpGet(uri);
			CloseableHttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = getRequiredData(EntityUtils.toString(entity));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseText;
	}

	private JSONObject getRequiredData(String inputStr) {
		JSONObject output = new JSONObject();
		try {
			JSONObject input = new JSONObject(inputStr);
			Double lat = input.getJSONObject("coord").getDouble("lat");
			Double lon = input.getJSONObject("coord").getDouble("lon");
//			String currentCondition = input.getJSONObject("weather").getString(
//					"main");
			Double currentTemp = input.getJSONObject("main").getDouble("temp");
			Double minTemp = input.getJSONObject("main").getDouble("temp_min");
			Double maxTemp = input.getJSONObject("main").getDouble("temp_max");

			output.put("latitude", lat);
			output.put("longitude", lon);
//			output.put("current_conditions", currentCondition);
			output.put("current_temp", currentTemp);
			output.put("min_temp", minTemp);
			output.put("max_temp", maxTemp);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return output;
	}
}
