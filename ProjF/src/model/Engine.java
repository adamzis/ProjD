package model;

import java.math.BigInteger;
import java.util.List;

public class Engine {

	private static Engine instance = null;
	private StudentDAO dao;

	private Engine() {
		dao = new StudentDAO();
	}

	public static Engine getInstance() {
		if (instance == null)
			instance = new Engine();

		return instance;
	}

	public String doPrime(String min, String max) throws Exception {
		long lowerLong = 0l;
		long upperLong = 0l;

		try {
			lowerLong = Long.parseLong(min);
			upperLong = Long.parseLong(max);
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

		primeInt = new BigInteger(min);
		primeInt = primeInt.nextProbablePrime();

		if (primeInt.longValue() > Long.parseLong(max))
			throw new Exception("No more primes in range.");

		return primeInt.toString();
	}

	public List<StudentBean> doSis(String prefix, String minGpa, String sortBy) throws Exception {
		if (!prefix.matches("[A-Za-z]*")) {
			throw new IllegalArgumentException("Prefix can only contain letters or be blank.");
		}
		
		if (!minGpa.matches("\\d?")) {
			throw new IllegalArgumentException("GPA can only be a number from 0 to 9.");
		}

		return dao.retrieve(prefix, minGpa, sortBy);
	}
}
