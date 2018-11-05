package model;

import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Engine {
	private static Engine singleEngine = null;

	public static final String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
	public static final String DIST_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
	private static final String API_KEY = "AIzaSyDigYKqPiu7A-9lydcl2SPBAfT6mrMQ7mY";

	public static final int EARTH_DIAMETER = 12742;
	public static final double CONVERT_TO_RADIANS = Math.PI / 180.0;
	public static final double DRONE_SPEED_KMH = 150.0;
	public static final double SECONDS_IN_MIN = 60.0;
	public static final double COST_PER_MINUTE = 0.5;
	private StudentDAO dao;

	private Engine() {
		dao = new StudentDAO();
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

	public double doGps(String startLat, String startLong, String destLat, String destLong)
			throws NumberFormatException {

		double lat1 = Double.parseDouble(startLat) * CONVERT_TO_RADIANS;
		double long1 = Double.parseDouble(startLong) * CONVERT_TO_RADIANS;
		double lat2 = Double.parseDouble(destLat) * CONVERT_TO_RADIANS;
		double long2 = Double.parseDouble(destLong) * CONVERT_TO_RADIANS;

		double Y = Math.cos(lat1) * Math.cos(lat2);
		double X = Math.pow(Math.sin((lat2 - lat1) / 2), 2.0) + Y * Math.pow(Math.sin((long2 - long1) / 2), 2.0);

		double distance = EARTH_DIAMETER * Math.atan2(Math.sqrt(X), Math.sqrt(1 - X));

		return distance;
	}

	// TODO Refactor this piece of shit
	public double doDrone(String startAddr, String destAddr) throws Exception {

		String[] startLatLng, destLatLng;

		if (startAddr.isEmpty() || destAddr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");

		String formatStart = startAddr.replaceAll(" ", "+").trim();
		String formatDest = destAddr.replaceAll(" ", "+").trim();

		URL startURL = new URL(GEO_URL + "address=" + formatStart + "&key=" + API_KEY);
		URL destURL = new URL(GEO_URL + "address=" + formatDest + "&key=" + API_KEY);

		String startJson = accessJson(startURL);
		String destJson = accessJson(destURL);

		startLatLng = parseLatLng(startJson);
		destLatLng = parseLatLng(destJson);

		double distance = doGps(startLatLng[0], startLatLng[1], destLatLng[0], destLatLng[1]);

		double droneTime = (distance / DRONE_SPEED_KMH) * SECONDS_IN_MIN;

		return droneTime;
	}

	private String accessJson(URL httpAddr) throws Exception {

		URLConnection httpConnection = httpAddr.openConnection();

		Scanner httpInput = new Scanner(httpConnection.getInputStream());
		String result = "";

		while (httpInput.hasNextLine()) {
			result += httpInput.nextLine();
		}

		httpInput.close();

		return result.toString();
	}

	private String[] parseLatLng(String json) {

		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(json).getAsJsonObject();

		JsonObject resultObj = rootObj.getAsJsonArray("results").get(0).getAsJsonObject();
		JsonObject locObj = resultObj.getAsJsonObject("geometry").getAsJsonObject("location");

		String lat = locObj.get("lat").getAsString();
		String lng = locObj.get("lng").getAsString();

		String[] latLng = { lat, lng };

		return latLng;

	}

	public double doRide(String startAddr, String destAddr) throws Exception {
		if (startAddr.isEmpty() || destAddr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");

		String formatStart = startAddr.replaceAll(" ", "+").trim();
		String formatDest = destAddr.replaceAll(" ", "+").trim();

		URL rideURL = new URL(DIST_URL + "origins=" + formatStart + "&destinations=" + formatDest
				+ "&departure_time=now&key=" + API_KEY);

		String rideJson = accessJson(rideURL);
		int time = parseTime(rideJson);

		double cost = ((double) time / SECONDS_IN_MIN) * COST_PER_MINUTE;

		return cost;
	}

	private int parseTime(String rideJson) {
		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(rideJson).getAsJsonObject();

		JsonObject rowObj = rootObj.getAsJsonArray("rows").get(0).getAsJsonObject();

		JsonObject durationObj = rowObj.getAsJsonArray("elements").get(0).getAsJsonObject()
				.getAsJsonObject("duration_in_traffic");

		int trafficSeconds = durationObj.get("value").getAsInt();

		return trafficSeconds;
	}

	public List<StudentBean> doSis(String name, String gpa) throws Exception {
		List<StudentBean> students = dao.retrieve(name, gpa);

		return students;

	}

}
