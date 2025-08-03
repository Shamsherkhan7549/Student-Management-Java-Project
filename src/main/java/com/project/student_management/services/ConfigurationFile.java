package com.project.student_management.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.project.student_management.entity.Admin;
import com.project.student_management.entity.Attendance;
import com.project.student_management.entity.Course;
import com.project.student_management.entity.Marks;
import com.project.student_management.entity.Student;

public class ConfigurationFile {
	Configuration cfg;
	SessionFactory factory;
	Session session;
	Transaction transaction;
	
	public SessionFactory isConfigured() {
		
		try {
			cfg = new Configuration();
			cfg.configure("hibernate.cfg.xml");
			cfg.addAnnotatedClass(Student.class);
			cfg.addAnnotatedClass(Course.class);
			cfg.addAnnotatedClass(Admin.class);
			cfg.addAnnotatedClass(Attendance.class);
			cfg.addAnnotatedClass(Marks.class);
			factory = cfg.buildSessionFactory();
				
			return factory;
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception in Configuration : " + ex);
			return null;
		}
		
		
	}
}
