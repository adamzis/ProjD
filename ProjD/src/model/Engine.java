package model;

import java.math.BigInteger;

public class Engine {

	private static Engine singleEngine = null;
	public static final int EARTH_DIAMETER = 12742;
	public static final double CONVERT_TO_RADIANS = Math.PI / 180.0;

	private Engine() {

	}

	public synchronized static Engine getEngine() {
		if (singleEngine == null)
			singleEngine = new Engine();

		return singleEngine;
	}

	public String doPrime(String lowerBound, String upperBound) throws Exception {

		if (Long.parseLong(lowerBound) > Long.parseLong(upperBound))
			throw new Exception();

		BigInteger primeInt;

		primeInt = new BigInteger(lowerBound);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(upperBound))
			throw new Exception();

		return primeInt.toString();

	}

	public String doGps(String fromLat, String fromLong, String toLat, String toLong) throws NumberFormatException {

		double lat1 = Double.parseDouble(fromLat) * CONVERT_TO_RADIANS;
		double long1 = Double.parseDouble(fromLong) * CONVERT_TO_RADIANS;
		double lat2 = Double.parseDouble(toLat) * CONVERT_TO_RADIANS;
		double long2 = Double.parseDouble(toLong) * CONVERT_TO_RADIANS;

		double Y = Math.cos(lat1) * Math.cos(lat2);
		double X = Math.pow(Math.sin((lat2 - lat1) / 2), 2.0) + Y * Math.pow(Math.sin((long2 - long1) / 2), 2.0);

		double distance = EARTH_DIAMETER * Math.atan2(Math.sqrt(X), Math.sqrt(1 - X));

		long longDistance = Math.round(distance);
		return Long.toString(longDistance);
	}

}
