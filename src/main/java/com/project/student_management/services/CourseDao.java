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

public class CourseDao {
	SessionFactory factory;
	Session session;
	Transaction transaction;
	boolean status = false;
	Scanner sc = new Scanner(System.in);
	
	
	
	public CourseDao(SessionFactory factory) {
		super();
		this.factory = factory;
	}
	
	//fetch all courses
	public void fetchAllCourse() {
		
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			String hql = "FROM Course";
			Query<Course> query = session.createQuery(hql,Course.class);
			
			List<Course> results = query.list();
			
			if(results.isEmpty()) {
				System.out.println("Course is Empty");
				return ;
			}
			
			for(Course course: results) {
				System.out.println("course id : " + course.getCourse_id() + ", course : " + course.getCourse_name() + ", duration : " + course.getDuration() + ", fees : " + course.getFees());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception in fetchAllCourse() : " + ex);
			
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	//insert course
	public boolean insertcourse() {
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			Course course = new Course();
			
			System.out.println("Enter course name");
			String course_name = sc.next();
			
			System.out.println("Enter course duration");
			String duration = sc.next();
			
			System.out.println("Enter course fees");
			int fees = sc.nextInt();
			
			String hql = "FROM Course WHERE course_name= :name";
			Query<Course> query = session.createQuery(hql,Course.class);
			query.setParameter("name", course_name);
			
			List<Course> results = query.list();
			if(!results.isEmpty()) {
				System.out.println("Course Already Exists");
				return false;
			}
			
			course.setCourse_name(course_name);
			course.setDuration(duration);
			course.setFees(fees);
			session.save(course);
			transaction.commit();
			System.out.println(course_name + " is inserted");
			return true;
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in insertCourse() : " + ex);
			return false;
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	// update course
	public void updateCourse() {
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Enter course_id");
			int id = sc.nextInt();
			
			Course courseInfo = session.get(Course.class, id);

			if(courseInfo == null) {
				System.out.println("course is not available");
				return;
			}
			
			courseInfo.setCourse_id(id);
			
			System.out.println("Do want to update duration?y/n.");
			char per = sc.next().charAt(0);
			if(per == 'y') {
				System.out.println("Enter course_duration");
				String duration = sc.next();
				courseInfo.setDuration(duration);
			}
			
			System.out.println("Do want to update fees?y/n.");
			char per2 = sc.next().charAt(0);
			if(per2 == 'y') {
				System.out.println("Enter course_fees");
				int fees = sc.nextInt();
				courseInfo.setFees(fees);	
			}
			
			session.update(courseInfo);
			transaction.commit();
			System.out.println(courseInfo.getCourse_name() + " is Updated");
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in updateCourse() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	// delete course
	public void deleteCourse() {
		try {

			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Enter course_id");
			int id = sc.nextInt();
			
			Course courseInfo = session.get(Course.class, id);
			
			if(courseInfo == null) {
				System.out.println("Course not found");
				return;
			}
			
			String hql = "DELETE FROM Attendance WHERE course.id = :cid";
			//DELETE queries are not SELECT queries, so you must not pass a result type (Attendance.class).
			Query<?> query = session.createQuery(hql); //  after running hql query it does not return Attendance object but return no of row effected and 
			query.setParameter("cid", id);
			query.executeUpdate();
			
			String hql2 = "DELETE FROM Marks WHERE course.id = :cid";
			Query<?> query2 = session.createQuery(hql2);
			query2.setParameter("cid", id);
			

			query2.executeUpdate();
			
			
			// shared reference deleted
				for(Student s: courseInfo.getStudents()) {
					s.getCourses().remove(courseInfo);
				}
				
				for(Admin a: courseInfo.getAdmins()) {
					a.getCourses().remove(courseInfo);
				}				
				
			
			
			
			String course_name = courseInfo.getCourse_name();
			
			
			session.delete(courseInfo);
			transaction.commit();
			
			System.out.println(course_name + " is deleted");
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in deleteCourse() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
