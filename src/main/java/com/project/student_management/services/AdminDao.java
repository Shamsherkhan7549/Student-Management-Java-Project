package com.project.student_management.services;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import com.project.student_management.entity.Admin;
import com.project.student_management.entity.Attendance;
import com.project.student_management.entity.Course;
import com.project.student_management.entity.Marks;
import com.project.student_management.entity.Student;

public class AdminDao {
	SessionFactory factory;
	Session session;
	Transaction transaction;
	Scanner sc = new Scanner(System.in);
	
	
	
	public AdminDao(SessionFactory factory) {
		super();
		this.factory = factory;
	}

	
	//Admin signUp
	public Admin singupAdmin() {
		session = null;
		transaction = null;
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();		
			
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
				System.out.println("Admin Already Register With This Username or Email");
				return null;
			}
			
			Admin admin = new Admin();
			admin.setUsername(username);
			admin.setEmail(email);
			admin.setPassword(password);
			
			session.save(admin);
			transaction.commit();
			
			System.out.println("You are Registered Successfully");
			return admin;
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception In signupAdmin() : " + ex);
			return null;
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}
	
	//Admin Login
	public Admin loginAdmin() {
		session = null;
		transaction = null;
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			
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
			ex.printStackTrace();
			System.out.println("Exception in loginAdmin() : " + ex);
			return null;
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
		
	}

	// Admin Profile
	public void profile(Admin registeredAdmin) {
		session = null;
		transaction = null;
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Admin Profile: ");
			
			System.out.println("Username : " + registeredAdmin.getUsername());
			System.out.println("Email : " + registeredAdmin.getEmail());
			
			System.out.println("Courses : ");
			if(!registeredAdmin.getCourses().isEmpty()) {
				for(Course course: registeredAdmin.getCourses()) {
					System.out.println("Course_id : " + course.getCourse_id() + ", Course : " + course.getCourse_name() + ", Duration : " + course.getDuration());
				}
			}else {
				System.out.println("No course Added to Profile");
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Exception in admin profile() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	public void addCourseToProfile(Admin registeredAdmin) {
		session = null;
		transaction = null;
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();

			String hql = "FROM Course";
			Query<Course> query = session.createQuery(hql, Course.class);
			
			System.out.println("Course list : ");
			List<Course> results = query.list();
			
			if(results.isEmpty()) {
				System.out.println("Course not available");
				return;
			}
			
			for(Course c: results) {
				System.out.println("Course_id : " + c.getCourse_id() + ", Course : " + c.getCourse_name() + ", Duration : " + c.getDuration() + ", Fees : " + c.getFees());
			}
				
				
				
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
				
				selectedCourse.setCourse_id(course_id);
				registeredAdmin.getCourses().add(selectedCourse);
				
				session.update(registeredAdmin);
				transaction.commit();
				System.out.println("Course Saved");	
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in addCourseToProfile() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	

	
	
	
	
	
	
	
	
}











	
	
