package com.project.student_management.services;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.project.student_management.entity.Admin;
import com.project.student_management.entity.Attendance;
import com.project.student_management.entity.Course;
import com.project.student_management.entity.Student;

public class AdminDao {
	Configuration cfg;
	SessionFactory factory;
	Session session;
	Transaction transaction;
	boolean status = false;
	Scanner sc = new Scanner(System.in);
	
	public boolean isConfigureAdmin() {
		
		try {
			cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			cfg.addAnnotatedClass(Student.class);
			cfg.addAnnotatedClass(Course.class);
			cfg.addAnnotatedClass(Admin.class);
			cfg.addAnnotatedClass(Attendance.class);
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();
			status = true;
			
		}catch(Exception ex) {
			status = false;
			ex.printStackTrace();
			System.out.println("Exception in Configuration : " + ex);
		}
		
		return status;
	}
	
	//Admin signUp
	public Admin singupAdmin() {
		try {
			Admin admin = new Admin();			
			
			System.out.println("Enter Admin Username");
			String username = sc.next();
			
			System.out.println("Enter Admin Email");
			String email = sc.next();
			
			System.out.println("Enter Admin Password");
			String password = sc.next();
		
			
			//Admin exist or not			
			String hql = "FROM Admin WHERE username= :uname AND email= :umail";
			Query<Admin> query = session.createQuery(hql,Admin.class);
			query.setParameter("uname", username);
			query.setParameter("umail", email);
			List<Admin> result = query.list();
			if(!result.isEmpty()) {
				System.out.println("Admin Alread Register With This Username or Email");
				return null;
			}
			
			admin.setUsername(username);
			admin.setEmail(email);
			admin.setPassword(password);
			
			session.save(admin);
			transaction.commit();
			
			System.out.println("You are Registered Successfully");
			return admin;
		}catch(Exception ex) {
			if(transaction != null) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception In signupAdmin() : " + ex);
			return null;
		}
	}
	
	//Admin Login
	public Admin loginAdmin() {
		try {
			System.out.println("Enter Username");
        	String username = sc.next();
        	
        	System.out.println("Enter Password");
        	String password = sc.next();
        	
			String hql = "FROM Admin WHERE username = :uname AND password = :pass";
			Query<Admin> query = session.createQuery(hql, Admin.class);
			query.setParameter("uname", username);
			query.setParameter("pass", password);
			
			List<Admin> result = query.list();
			
			if(!result.isEmpty()) {
				return result.get(0);
			}
			System.out.println("Admin Not Registered");
			return null;
			
		}catch(Exception ex) {
			System.out.println("Admin Not Registered");
			System.out.println("Exception in loginAdmin() : " + ex);
			return null;
		}
		
		
	}

	// Admin Profile
	public void profile(Admin registeredAdmin) {
		try {
			System.out.println("Admin Profile: ");
			
			System.out.println("Username : " + registeredAdmin.getUsername());
			System.out.println("Email : " + registeredAdmin.getEmail());
			
		}catch(Exception ex){
			System.out.println("Exception in admin profile() : " + ex);
		}
		
	}

	public void addCourseToProfile(Admin registeredAdmin) {
		try {
				CourseDao courseDao = new CourseDao();
				if(!courseDao.isConfigureCourse()) {
					System.out.println("Problem in course configuration");
					return;
				}
				courseDao.fetchAllCourse();
				
				
				
				System.out.println("Enter Course_id to Add Your Profile : ");
				int course_id = sc.nextInt();
				
				Course selectedCourse = session.get(Course.class, course_id);
				
				if(selectedCourse == null) {
					System.out.println("Course is not available with this id");
					return;
				}
				
				if(registeredAdmin.getCourses().contains(selectedCourse)) {
					System.out.println("This Course is Already Added");
					return;
				}
				
				
				Course course = new Course();
				course.setCourse_id(course_id);
				registeredAdmin.getCourses().add(course);
				
				session.update(registeredAdmin);
				transaction.commit();
				System.out.println("Course Saved");	
			
		}catch(Exception ex) {
			if(transaction != null) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in addCourseToProfile() : " + ex);
		}
		
	}


	
	
	
	
	
	
	
	
}











	
	
