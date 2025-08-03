package com.project.student_management.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String email;
	private String password;
	
	@ManyToMany
	@JoinTable(
				name = "enrollment",
				joinColumns = @JoinColumn(name = "student_id"),
				inverseJoinColumns = @JoinColumn(name = "course_id")
			)
	private Set<Course> courses = new HashSet<>();
	
	@OneToMany(mappedBy = "student")
	private List<Attendance> attendance = new ArrayList<>();
	
	@OneToMany(mappedBy = "student")
	private List<Marks>marks = new ArrayList<>();
	
	

	public List<Marks> getMarks() {
		return marks;
	}

	public void setMarks(List<Marks> marks) {
		this.marks = marks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Course> getCourses() {
		return courses;
	}

	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}

	public List<Attendance> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<Attendance> attendance) {
		this.attendance = attendance;
	}
	
	
}
