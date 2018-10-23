package model;

import java.math.BigInteger;

public class Engine {

	private static Engine singleEngine = null;

	private Engine() {

	}

	public synchronized static Engine getEngine() {
		if (singleEngine == null)
			singleEngine = new Engine();

		return singleEngine;
	}

	public String doPrime(String lowerBound, String upperBound) {

		if (Long.parseLong(lowerBound) > Long.parseLong(upperBound))
			return null;

		BigInteger primeInt;

		primeInt = new BigInteger(lowerBound);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(upperBound))
			return null;
		else
			return primeInt.toString();

	}

}
