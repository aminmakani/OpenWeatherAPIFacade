import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OpenWeatherApiCall {

	public static void main(String[] args) {
		int cityChoice = 0;
		int tempChoice = 0;
		int zip = 0;
		String cityName = "";
		OpenWeatherApiCall obj = new OpenWeatherApiCall();
		cityChoice = obj.getCityChoice();

		if (cityChoice == 1) {
			zip = obj.getZipCode();
		} else {
			cityName = obj.getCityName();
		}
		tempChoice = obj.getTempChoice();

		System.out.println("The weather details for the given input is:");
		System.out.println(obj.getWeatherDetails(zip, cityName, tempChoice));
	}

	private String getWeatherDetails(int zip, String cityName, int tempChoice) {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			URI uri = new URIBuilder().setScheme("http").setPort(8080)
					.setHost("localhost")
					.setPath("/OpenWeatherAPIFacade/weather")
					.setParameter("zip", Integer.toString(zip))
					.setParameter("city_name", cityName)
					.setParameter("temp_choice", Integer.toString(tempChoice))
					.build();
			HttpGet httpget = new HttpGet(uri);
			CloseableHttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getCityName() {
		String choice = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Please provide the city name: ");
		while (choice.equals("")) {
			try {
				choice = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (choice.equals("")) {
				System.out.println("Please enter a city name.");
			}
		}
		return choice;
	}

	private int getZipCode() {
		int choice = 0;
		boolean isValidInput = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Please provide the zip code: ");
		while (!isValidInput) {
			try {
				choice = Integer.parseInt(br.readLine());
				isValidInput = true;
			} catch (NumberFormatException e1) {
				isValidInput = false;
				System.out.println("Please enter valid zip code.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return choice;
	}

	private int getTempChoice() {
		int choice = 0;
		boolean isValidInput = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out
				.println("Please choose the unit in which the temperature will be formatted:\n1.Kelvin\n2.Celsius\n3.Fahrenheit");
		while (!isValidInput) {
			try {
				choice = Integer.parseInt(br.readLine());
				if (choice == 1 || choice == 2 || choice == 3) {
					isValidInput = true;
				} else {
					System.out.println("Please choose value 1, 2 or 3.");
				}
			} catch (NumberFormatException e1) {
				isValidInput = false;
				System.out.println("Please enter valid choice value.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return choice;
	}

	private int getCityChoice() {
		int choice = 0;
		boolean isValidInput = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out
				.println("Please choose if you want to get the weather details based on zip code or city name:\n1. Zip Code\n2. City Name");
		while (!isValidInput) {
			try {
				choice = Integer.parseInt(br.readLine());
				if (choice == 1 || choice == 2) {
					isValidInput = true;
				} else {
					System.out.println("Please choose value 1 or 2.");
				}
			} catch (NumberFormatException e) {
				isValidInput = false;
				System.out.println("Please enter valid choice value.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return choice;
	}
}
