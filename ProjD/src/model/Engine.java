package model;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Engine {

	private static Engine singleEngine = null;
	private static final String API_KEY = "AIzaSyDigYKqPiu7A-9lydcl2SPBAfT6mrMQ7mY";

	public static final int EARTH_DIAMETER = 12742;
	public static final double CONVERT_TO_RADIANS = Math.PI / 180.0;

	public static final String DISTANCE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

	public static final double DRONE_SPEED_KMH = 150.0;
	public static final double SECONDS_IN_MIN = 60.0;

	private Engine() {

	}

	public synchronized static Engine getEngine() {
		if (singleEngine == null)
			singleEngine = new Engine();

		return singleEngine;
	}

	public String doPrime(String lowerBound, String upperBound) throws Exception {

		long lowerLong = 0l;
		long upperLong = 0l;

		try {
			lowerLong = Long.parseLong(lowerBound);
			upperLong = Long.parseLong(upperBound);
		} catch (NumberFormatException e) {
			throw new Exception("Invalid entires.");
		}

		if (upperLong < 0)
			throw new Exception("end < 0: " + upperLong);
		else if (lowerLong < 0)
			throw new Exception("start < 0: " + lowerLong);
		else if (lowerLong > upperLong)
			throw new Exception("No more primes in range");

		BigInteger primeInt;

		primeInt = new BigInteger(lowerBound);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(upperBound))
			throw new Exception("No more primes in range.");

		return primeInt.toString();

	}

	public double doGps(String fromLat, String fromLong, String toLat, String toLong) throws NumberFormatException {

		double lat1 = Double.parseDouble(fromLat) * CONVERT_TO_RADIANS;
		double long1 = Double.parseDouble(fromLong) * CONVERT_TO_RADIANS;
		double lat2 = Double.parseDouble(toLat) * CONVERT_TO_RADIANS;
		double long2 = Double.parseDouble(toLong) * CONVERT_TO_RADIANS;

		double Y = Math.cos(lat1) * Math.cos(lat2);
		double X = Math.pow(Math.sin((lat2 - lat1) / 2), 2.0) + Y * Math.pow(Math.sin((long2 - long1) / 2), 2.0);

		double distance = EARTH_DIAMETER * Math.atan2(Math.sqrt(X), Math.sqrt(1 - X));

		return distance;
	}

	// TODO Refactor this piece of shit
	public double doDrone(String startAddr, String destAddr) throws Exception {

		Map<String, String> startCoor, destCoor;

		String formatStart = startAddr.replaceAll(" ", "+").trim();
		String formatDest = destAddr.replaceAll(" ", "+").trim();

		String startURL = new StringBuilder(DISTANCE_URL).append(formatStart).append("&key=").append(API_KEY)
				.toString();
		String destURL = new StringBuilder(DISTANCE_URL).append(formatDest).append("&key=").append(API_KEY).toString();

		String startJson = geoApi(startURL);
		String destJson = geoApi(destURL);

		startCoor = parseCoor(startJson);
		destCoor = parseCoor(destJson);

		double distance = doGps(startCoor.get("lat"), startCoor.get("lng"), destCoor.get("lat"), destCoor.get("lng"));

		double droneTime = (distance / DRONE_SPEED_KMH) * SECONDS_IN_MIN;

		return droneTime;
	}

	private String geoApi(String addr) throws Exception {

		URL httpURL = new URL(addr);
		URLConnection httpConnection = httpURL.openConnection();

		Scanner httpInput = new Scanner(httpConnection.getInputStream());
		String result = "";

		while (httpInput.hasNextLine()) {
			result += httpInput.nextLine();
		}

		httpInput.close();

		return result.toString();
	}

	private Map<String, String> parseCoor(String json) {

		Map<String, String> latLng = new HashMap<>();
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(json).getAsJsonObject();

		JsonObject resultObj = rootObj.getAsJsonArray("results").get(0).getAsJsonObject();
		JsonObject locObj = resultObj.getAsJsonObject("geometry").getAsJsonObject("location");

		String lat = locObj.get("lat").getAsString();
		String lng = locObj.get("lng").getAsString();

		latLng.put("lat", lat);
		latLng.put("lng", lng);

		return latLng;

	}

}
