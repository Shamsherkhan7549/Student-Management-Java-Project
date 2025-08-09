package com.project.student_management.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.project.student_management.entity.Attendance;
import com.project.student_management.entity.Course;
import com.project.student_management.entity.Student;

public class AttendanceDao {
	Session session;
	SessionFactory factory;
	Transaction transaction;
	Scanner sc = new Scanner(System.in);
	
	
	
	public AttendanceDao(SessionFactory factory) {
		super();
		this.factory = factory;
	}

	
	public void giveAttendance(Student registeredStudent) {

		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();

			int student_id = registeredStudent.getId();

			System.out.println("Enter Course id : ");
			int course_id = sc.nextInt();

			System.out.println("Enter Status : ");
			char isPresent = sc.next().charAt(0);
			
			Student student = session.get(Student.class, student_id);
			Course course = session.get(Course.class, course_id);
			
			if(course == null) {
				System.out.println("Course is not available with this id.");
				return;
			}
			
			boolean isPurchased = student.getCourses().contains(course);
			
			if( !isPurchased) {
				System.out.println("Course is not purchased with this id.");
				return ;
			}
			
			// Todays Date
			 LocalDate date = LocalDate.now();
			 
			 // Checking if exist 
			 String hql = "FROM Attendance WHERE date= :udate AND course.id= :ucourse_id AND student.id= :ustudent_id";
			 Query<Attendance> query = session.createQuery(hql,Attendance.class);
			 query.setParameter("udate", date);
			 query.setParameter("ucourse_id", course_id);
			 query.setParameter("ustudent_id", student_id);
			 
			 List<Attendance> results = query.list();
			 
			 if(!results.isEmpty()) {
				 System.out.println("Your attendance already saved");
				 return;
			 }
			 
			 Attendance attendance = new Attendance();
			 attendance.setDate(date);
			attendance.setStatus(isPresent);
			attendance.setStudent(student);
			attendance.setCourse(course);
			
			session.save(attendance);
			transaction.commit();
			System.out.println("You attendance saved");
			
		}catch(Exception ex) {
			if(transaction != null)transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in giveAttendance() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
