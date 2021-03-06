package model;

import java.math.BigInteger;
import java.net.MalformedURLException;
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
	public static final double DEGREES_TO_RADIANS = Math.PI / 180.0;
	public static final double DRONE_SPEED_KMH = 150.0;
	public static final double SECONDS_IN_MIN = 60.0;
	public static final double COST_PER_MINUTE = 0.5;
	public static final double TWO = 2.0;

	private StudentDAO dao;

	private Engine() {
		dao = new StudentDAO();
	}

	public synchronized static Engine getEngine() {

		if (singleEngine == null)
			singleEngine = new Engine();

		return singleEngine;
	}

	public String doPrime(String lessThan, String greaterThan) throws Exception {

		long lessLong = 0l;
		long greaterLong = 0l;

		try {
			lessLong = Long.parseLong(lessThan);
			greaterLong = Long.parseLong(greaterThan);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid entires");
		}

		if (greaterLong < 0)
			throw new IllegalArgumentException("end < 0: " + greaterLong);
		else if (lessLong < 0)
			throw new IllegalArgumentException("start < 0: " + lessLong);
		else if (lessLong > greaterLong)
			throw new IllegalArgumentException("No more primes in range");

		BigInteger primeInt;

		primeInt = new BigInteger(lessThan);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(greaterThan))
			throw new IllegalArgumentException("No more primes in range.");

		return primeInt.toString();
	}

	public double doGps(String startLat, String startLong, String destLat, String destLong)
			throws NumberFormatException {

		double lat1 = Double.parseDouble(startLat) * DEGREES_TO_RADIANS;
		double long1 = Double.parseDouble(startLong) * DEGREES_TO_RADIANS;
		double lat2 = Double.parseDouble(destLat) * DEGREES_TO_RADIANS;
		double long2 = Double.parseDouble(destLong) * DEGREES_TO_RADIANS;

		double Y = Math.cos(lat1) * Math.cos(lat2);
		double X = Math.pow(Math.sin((lat2 - lat1) / TWO), TWO) + Y * Math.pow(Math.sin((long2 - long1) / TWO), TWO);

		double distance = EARTH_DIAMETER * Math.atan2(Math.sqrt(X), Math.sqrt(1 - X));
		return distance;
	}

	public double doDrone(String startAddr, String destAddr) throws Exception {

		String[] startLatLng, destLatLng;

		URL startURL = formatDroneURL(startAddr, "drone");
		URL destURL = formatDroneURL(destAddr, "drone");

		String startJson = accessJson(startURL);
		String destJson = accessJson(destURL);

		startLatLng = parseLatLng(startJson);
		destLatLng = parseLatLng(destJson);

		double distance = doGps(startLatLng[0], startLatLng[1], destLatLng[0], destLatLng[1]);

		double droneTime = (distance / DRONE_SPEED_KMH) * SECONDS_IN_MIN;
		return droneTime;
	}

	// Checks if the URL is empty, then replaces spaces with '+', and trims any
	// leading or trailing white space. Returns a URL.
	private URL formatDroneURL(String addr, String serviceType) throws MalformedURLException {

		if (addr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");

		addr = addr.replaceAll(" ", "+").trim();

		URL geoURL = new URL(GEO_URL + "address=" + addr + "&key=" + API_KEY);
		return geoURL;
	}

	// Gets the JSON from the URL as a string.
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

	// Parses the latitude and longitude from the google Json and stores it in an
	// Array. The first value of the array is the latitude and the second is
	// longitude.
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

		URL rideURL = formatRideURL(startAddr, destAddr);
		String rideJson = accessJson(rideURL);
		int time = parseTime(rideJson);

		double cost = ((double) time / SECONDS_IN_MIN) * COST_PER_MINUTE;
		return cost;
	}

	// Checks that the addresses aren't empty, replaces spaces with '+' and removes
	// leading and trailing whitespace. Returns a valid URL.
	private URL formatRideURL(String startAddr, String destAddr) throws MalformedURLException {

		if (startAddr.isEmpty() || destAddr.isEmpty())
			throw new IllegalArgumentException("One or more addresses are empty");

		String formatStart = startAddr.replaceAll(" ", "+").trim();
		String formatDest = destAddr.replaceAll(" ", "+").trim();

		URL rideURL = new URL(DIST_URL + "origins=" + formatStart + "&destinations=" + formatDest
				+ "&departure_time=now&key=" + API_KEY);
		return rideURL;
	}

	// Parses the time in traffic from the returned Json.
	private int parseTime(String rideJson) {

		JsonParser parser = new JsonParser();
		JsonObject rootObj = parser.parse(rideJson).getAsJsonObject();

		JsonObject rowObj = rootObj.getAsJsonArray("rows").get(0).getAsJsonObject();

		JsonObject durationObj = rowObj.getAsJsonArray("elements").get(0).getAsJsonObject()
				.getAsJsonObject("duration_in_traffic");

		int trafficSeconds = durationObj.get("value").getAsInt();
		return trafficSeconds;
	}

	public List<StudentBean> doSis(String name, String gpa, String order) throws Exception {

		List<StudentBean> students = dao.retrieve(name, gpa, order);
		return students;
	}
}
