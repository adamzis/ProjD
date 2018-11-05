package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

	public static final String DERBY_DRIVER = "org.apache.derby.jdbc.ClientDriver";
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	public static final String QUERY = "SELECT * FROM SIS WHERE SURNAME LIKE ? AND GPA >= ?";

	public List<StudentBean> retrieve(String name, String gpa) throws Exception {

		Class.forName(DERBY_DRIVER).newInstance();
		Connection con = DriverManager.getConnection(DB_URL);

		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");

		PreparedStatement preS = con.prepareStatement(QUERY);

		if (!name.isEmpty())
			preS.setString(1, name);
		else
			preS.setString(1, "%");

		if (!gpa.isEmpty())
			preS.setString(2, gpa);
		else
			preS.setString(2, "0");

		ResultSet r = preS.executeQuery();

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
}
