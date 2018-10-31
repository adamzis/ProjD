package model;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jdk.nashorn.internal.parser.JSONParser;

public class Engine {

	private static Engine singleEngine = null;
	public static final int EARTH_DIAMETER = 12742;
	public static final double CONVERT_TO_RADIANS = Math.PI / 180.0;
	public static final String API_KEY = "AIzaSyDigYKqPiu7A-9lydcl2SPBAfT6mrMQ7mY";

	public static final String DISTANCE_URL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	public static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

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

	public String doGps(String fromLat, String fromLong, String toLat, String toLong) throws NumberFormatException {

		double distance = gpsCalc(fromLat, fromLong, toLat, toLong);

		long longDistance = Math.round(distance);
		String resultString = NUMBER_FORMAT.format(longDistance) + " km";

		return resultString;
	}

	private double gpsCalc(String fromLat, String fromLong, String toLat, String toLong) {

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
	public String doDrone(String startAddr, String destAddr) throws Exception {

		Map<String, String> startCoor, destCoor;
		final double DRONE_SPEED_KMH = 150.0;
		final double SECONDS_IN_MIN = 60.0;

		String fixedStart = startAddr.replaceAll(" ", "+").trim();
		String fixedDest = destAddr.replaceAll(" ", "+").trim();

		String startJson = geoApi(fixedStart);
		String destJson = geoApi(fixedDest);

		startCoor = parseCoor(startJson);
		destCoor = parseCoor(destJson);

		double gpsResult = gpsCalc(startCoor.get("lat"), startCoor.get("lng"), destCoor.get("lat"),
				destCoor.get("lng"));

		gpsResult = (gpsResult / DRONE_SPEED_KMH) * SECONDS_IN_MIN;

		long resultNum = Math.round(gpsResult);

		String resultString = NUMBER_FORMAT.format(resultNum) + " min";

		return resultString;
	}

	private String geoApi(String addr) throws Exception {

		StringBuilder queryURLBuild = new StringBuilder(DISTANCE_URL);
		queryURLBuild.append(addr);
		queryURLBuild.append("&key=");
		queryURLBuild.append(API_KEY);

		String queryURL = queryURLBuild.toString();

		URL httpURL = new URL(queryURL);
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
