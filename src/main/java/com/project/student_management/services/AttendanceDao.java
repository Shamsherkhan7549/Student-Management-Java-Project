package com.project.student_management.services;

import java.time.LocalDate;
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
import com.project.student_management.entity.Marks;
import com.project.student_management.entity.Student;

public class AttendanceDao {
	Configuration cfg;
	SessionFactory factory;
	Session session;
	Transaction transaction;
	boolean status = false;
	Scanner sc = new Scanner(System.in);
	
	public boolean isConfigure() {
		
		try {
			cfg = new Configuration().configure("hibernate.cfg.xml");
			cfg.addAnnotatedClass(Student.class);
			cfg.addAnnotatedClass(Course.class);
			cfg.addAnnotatedClass(Admin.class);
			cfg.addAnnotatedClass(Attendance.class);
			cfg.addAnnotatedClass(Marks.class);
			factory = cfg.buildSessionFactory();
			
			status = true;
		}catch(Exception ex) {
			System.out.println("Exception in Configuration : " + ex);
			status = false;
		}
		
		return status;
	}

	public void giveAttendance(Student registeredStudent) {

		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			Attendance attendance = new Attendance();
			Course course = new Course();
			Student student = new Student();
			int student_id = registeredStudent.getId();
			student.setId(student_id);
			
			System.out.println("Enter Course id : ");
			int course_id = sc.nextInt();
			course.setCourse_id(course_id);
			
			System.out.println("Enter Status : ");
			char isPresent = sc.next().charAt(0);
			
			// Todays Date
			 LocalDate date = LocalDate.now();
			 
			 // Checking if exist 
			 String hql = "FROM Attendance WHERE date= :udate AND course.id= :ucourse_id AND student.id= :ustudent_id";
			 Query<Attendance> query = session.createQuery(hql,Attendance.class);
			 query.setParameter("udate", date);
			 query.setParameter("ucourse_id", course_id);
			 query.setParameter("ustudent_id", student_id);
			 
			 List<Attendance> results = query.list();
			 
			 if(results != null) {
				 System.out.println("Your attendance already saved");
				 return;
			 }
			 
			 attendance.setDate(date);
			attendance.setStatus(isPresent);
			attendance.setStudent(student);
			attendance.setCourse(course);
			
			session.save(attendance);
			transaction.commit();
			System.out.print("You attendance saved");
			
		}catch(Exception ex) {
			if(transaction != null)transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in giveAttendance() : " + ex);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
