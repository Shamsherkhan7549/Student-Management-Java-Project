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

public class MarksDao {
	Configuration cfg;
	SessionFactory factory;
	Session session;
	Transaction transaction;
	boolean status = false;
	Scanner sc = new Scanner(System.in);
	
	// configuration setup
	public boolean isConfigureMarks() {
		
		try {
			cfg = new Configuration().configure("hibernate.cfg.xml");
			cfg.addAnnotatedClass(Student.class);
			cfg.addAnnotatedClass(Course.class);
			cfg.addAnnotatedClass(Admin.class);
			cfg.addAnnotatedClass(Attendance.class);
			cfg.addAnnotatedClass(Admin.class);
			cfg.addAnnotatedClass(Marks.class);
			
			factory = cfg.buildSessionFactory();
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			status = true;
			
		}catch(Exception ex) {
			status = false;
			ex.printStackTrace();
			System.out.println("Exception in Course Configuration : " + ex);
		}
		
		return status;
	}
	
	
	public void giveMarksToStudent() {
		try {
			
			Marks marks = new Marks();
			Course course = new Course();
			Student student = new Student();
					
			System.out.println("Enter Student Id : ");
			int student_id = sc.nextInt();
			student.setId(student_id);
			
			System.out.println("Enter course Id : ");
			int course_id = sc.nextInt();
			course.setCourse_id(course_id);
			
			System.out.println("Enter Marks");
			int course_marks = sc.nextInt();
			
			Student studentInfo = session.get(Student.class, student_id);
			Course courseInfo = session.get(Course.class, course_id);
			
			if(studentInfo == null && courseInfo == null) {
				System.out.println("student-id or course-id is not available");
				return;
			}
			
			
			String hql = "FROM Marks WHERE course.id= :cid AND student.id= :sid";
			Query<Marks> query = session.createQuery(hql, Marks.class);
			query.setParameter("cid", course_id);
			query.setParameter("sid", student_id);
			
			List<Marks> results = query.list();
			if(!results.isEmpty()) {
				System.out.println("Marks Already Exists");
				return;
			}
			
			marks.setStudent(student);
			marks.setCourse(course);
			marks.setMarks(course_marks);
			
			session.save(marks);
			transaction.commit();
			System.out.println("Marks is saved");
			return ;
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in insertCourse() : " + ex);
			return ;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
