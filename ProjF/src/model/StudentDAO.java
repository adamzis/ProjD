package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAO {

	public static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	public static final String QUERY = "SELECT * FROM SIS WHERE LOWER(SURNAME) LIKE LOWER(?) AND GPA >= ?";
	public static final String[] COLUMNS = { "NONE", "SURNAME", "GPA", "COURSES", "MAJOR" };

	private Connection con;

	// This helps prevent SQL injection attacks on the ORDER BY statement.
	private Map<String, String> orderMap;

	public StudentDAO() {
		orderMap = new HashMap<>();

		for (String order : COLUMNS) {
			orderMap.put(order, order);
		}

		try {
			Class.forName(DERBY_DRIVER).newInstance();
			con = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public List<StudentBean> retrieve(String prefix, String minGpa, String sortBy) throws Exception {

		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS = null;

		if (sortBy.equals("NONE"))
			preS = con.prepareStatement(QUERY);
		else
			preS = con.prepareStatement(QUERY + "ORDER BY " + getSort(sortBy));

		if (prefix.isEmpty())
			preS.setString(1, "%");
		else
			preS.setString(1, prefix + "%");

		if (minGpa.isEmpty())
			preS.setString(2, "0");
		else
			preS.setString(2, minGpa);

		ResultSet r = preS.executeQuery();

		List<StudentBean> studentList = makeStudentList(r);
		return studentList;
	}

	private List<StudentBean> makeStudentList(ResultSet r) throws SQLException {

		List<StudentBean> studentList = new ArrayList<>();

		while (r.next()) {
			StudentBean student = new StudentBean();
			student.setCourses(r.getInt("COURSES"));
			student.setGpa(r.getDouble("GPA"));
			student.setMajor(r.getString("MAJOR"));
			student.setName(r.getString("SURNAME") + ", " + r.getString("GIVENNAME"));

			studentList.add(student);
		}
		return studentList;
	}

	// Checks the input string against its value in a map and returns the mapped
	// value. If there is no matching mapped value, an exception is thrown. This
	// could indicate the user replaced the value of the options.
	private String getSort(String order) {

		String sanitizedOrder = orderMap.get(order);

		if (sanitizedOrder != null)
			return orderMap.get(order);
		else
			throw new IllegalArgumentException("Attempt to inject SQL Detected.");

	}
}
