package model;

public class StudentBean {

	private String name;
	private String major;
	private int courses;
	private double gpa;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getCourses() {
		return courses;
	}

	public void setCourses(int courses) {
		this.courses = courses;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	@Override
	public String toString() {
		return "StudentBean [name=" + name + ", major=" + major + ", courses=" + courses + ", gpa=" + gpa + "]";
	}

}