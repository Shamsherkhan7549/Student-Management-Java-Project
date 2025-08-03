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
import com.project.student_management.entity.Student;



public class StudentDao {
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

	// student signup
	public Student singupStudent() {
		
		try {
			Student student = new Student();			
			
			System.out.println("Enter Student Username");
			String username = sc.next();
			
			System.out.println("Enter Student Email");
			String email = sc.next();
			
			System.out.println("Enter Student Password");
			String password = sc.next();
		
			
			//Student exist or not			
			String hql = "FROM Student WHERE username= :uname AND email= :umail";
			Query<Student> query = session.createQuery(hql,Student.class);
			query.setParameter("uname", username);
			query.setParameter("umail", email);
			List<Student> result = query.list();
			if(!result.isEmpty()) {
				System.out.println("Student Already Registered With This Username or Email");
				return null;
			}
			
			student.setUsername(username);
			student.setEmail(email);
			student.setPassword(password);
			
			session.save(student);
			transaction.commit();
			
//			System.out.println("Do You Want to Buy Course, Now?");
//			char per = sc.next().charAt(0);
//			
//			if(per == 'y') {
//				buyCourse(student);
//				System.out.println("Course Added");
//			};
			
			
			System.out.println("You are Registered Successfully");
			return student;
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception In signupStudent() : " + ex);
			return null;
		}
	}

	//Student login
	public Student loginStudent() {
		try {
			System.out.println("Enter Username");
        	String username = sc.next();
        	
        	System.out.println("Enter Password");
        	String password = sc.next();
        	
			String hql = "FROM Student WHERE username = :uname AND password = :pass";
			Query<Student> query = session.createQuery(hql, Student.class);
			query.setParameter("uname", username);
			query.setParameter("pass", password);
			
			List<Student> result = query.list();
			
			if(!result.isEmpty()) {
				return result.get(0);
			}
			System.out.println("Student Not Registered");
			return null;
			
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Student Not Registered");
			System.out.println("Exception in loginAdmin() : " + ex);
			return null;
		}
	}

	// student profile
	public void profile(Student registeredStudent) {
		try {
			System.out.println("Student Profile: ");
			
			System.out.println("Username : " + registeredStudent.getUsername());
			System.out.println("Email : " + registeredStudent.getEmail());
			System.out.println("Purchased Courses : ");
			
			if(!registeredStudent.getCourses().isEmpty()) {
				for(Course course: registeredStudent.getCourses()) {
					System.out.println("Course_id : " + course.getCourse_id() + ", Course : " + course.getCourse_name() + ", Duration : " + course.getDuration());
				}
			}else {
				System.out.println("No course Purchased ");
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Exception in student profile() : " + ex);
		}
		
	}

	//buy course
	public void buyCourse(Student registeredStudent) {
		try {
			CourseDao courseDao = new CourseDao();
			if(!courseDao.isConfigureCourse()) {
				System.out.println("Problem in course configuration");
				return;
			}
			courseDao.fetchAllCourse();
			
			
			
			System.out.println("Enter Course_id to Buy : ");
			int course_id = sc.nextInt();
			
			Course selectedCourse = session.get(Course.class, course_id);
			
			if(selectedCourse == null) {
				System.out.println("Course is not available with this id");
				return;
			}
			
			if(registeredStudent.getCourses().contains(selectedCourse)) {
				System.out.println("This Course is Already Purchased");
				return;
			}
			
			
			
			System.out.println("Enter Fees");
			int fees = sc.nextInt();
			
			if(fees != selectedCourse.getFees()) {
				System.out.println("Payment Failed");
				return;
			}
			
			Course course = new Course();
			course.setCourse_id(course_id);
			registeredStudent.getCourses().add(course);
			
			session.update(registeredStudent);
			transaction.commit();
			
			System.out.println("Course Saved");	
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Course Not Saved");	
			System.out.println("Exception in buyCourse() : " + ex);
		}
		
	}

	public void viewAllStudents() {

		try {
			Student student = new Student();
			
			String hql = "FROM Student";
			Query<Student> query = session.createQuery(hql, Student.class);
			
			List<Student> results = query.list();
			System.out.println("List of Students : ");
			for(Student s: results) {
				System.out.println("Id : " + s.getId() + ", Username : " + s.getUsername() + ", Email : " + s.getEmail());
				System.out.println("s.getCourses() : " + s.getCourses());
				if(!s.getCourses().isEmpty()) {
					System.out.println("List of Courses bought : ");
					for(Course c: s.getCourses()) {
						System.out.println("Course_id : " + c.getCourse_id() + ", Course : " + c.getCourse_name() + ", Duration : " + c.getDuration() + ", Fees : " + c.getFees());
					}
				}else {
					System.out.println("No Course Purchased");
				}
			}
			
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in viewAllStudent() : " + ex);
		}
		
	}

	public void updateStudent() {
		try {
			
			System.out.println("Enter Student_id");
			int id = sc.nextInt();
			
			Student studentInfo = session.get(Student.class, id);

			if(studentInfo == null) {
				System.out.println("Student is not available");
				return;
			}
			
			studentInfo.setId(id);
			
			System.out.println("Do want to update email?y/n.");
			char per = sc.next().charAt(0);
			if(per == 'y') {
				System.out.println("Enter Student Email/Gmail");
				String email = sc.next();
				studentInfo.setEmail(email);
			}
			
			System.out.println("Do want to update password? y/n.");
			char per2 = sc.next().charAt(0);
			if(per2 == 'y') {
				System.out.println("Enter new password");
				String newPassword = sc.next();
				studentInfo.setPassword(newPassword);	
			}
			
			session.update(studentInfo);
			transaction.commit();
			System.out.println(studentInfo.getUsername() + " is Updated");
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			System.out.println("Exception in updateCourse() : " + ex);
		}
		
	}

	//remove enrollment
	public void removeEnrollment() {
		try {
			Student student = new Student();
			
			System.out.println("Enter student id to remove enrollment.");
			int id = sc.nextInt();
			student.setId(id);
			
			student = session.get(Student.class, id);
			if(student == null) {
				System.out.println("Student is not available with this id");
				return;
			}
			
			System.out.println("Enter course id to remove enrollment.");
			int course_id = sc.nextInt();
			
			
			if(student.getCourses().isEmpty()) {
				System.out.println("This student is not Enrolled");
				return;
			}
			
			for(Course c : student.getCourses()) {
				System.out.println("course_id : " + c.getCourse_id() + ", course : " + c.getCourse_name());
				if(c.getCourse_id() == course_id) {
					  student.getCourses().remove(c);
					
					session.update(student);
					
					System.out.println("Do You Want to Undo? y/n");
					char per = sc.next().charAt(0);
					if(per == 'y') {
						transaction.rollback();
						System.out.println("Student Enrollment not removed");
						return;
					}			
				}	
			}
			
			transaction.commit();
			System.out.println("Student Enrollment removed");
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in removeEnrollment() : " + ex);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
