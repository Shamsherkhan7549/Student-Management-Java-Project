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



public class StudentDao {

	SessionFactory factory;
	Session session;
	Transaction transaction;
	Scanner sc = new Scanner(System.in);
	
	
	public StudentDao(SessionFactory factory) {
		super();
		this.factory = factory;
	}

	// student signup
	public Student singupStudent() {
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();		
			
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
			
			
			Student student = new Student();	
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
			
			
			System.out.println("Student Registered Successfully");
			return student;
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception In signupStudent() : " + ex);
			return null;
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	//Student login
	public Student loginStudent() {
		
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
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
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
		
	}

	// student profile
	public void profile(Student registeredStudent) {
		
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Student Profile: ");
			
			Student student = session.get(Student.class, registeredStudent.getId());
			
			
			System.out.println("Username : " + student.getUsername());
			System.out.println("Email : " + student.getEmail());
			System.out.println("--> Purchased Courses : ");
			
			if(!student.getCourses().isEmpty()) {
				for(Course course: student.getCourses()) {
					System.out.println("     Course_id : " + course.getCourse_id() + ", Course : " + course.getCourse_name() + ", Duration : " + course.getDuration());
				}
				System.out.println();
			}else {
				System.out.println("--> No course Purchased ");
				System.out.println();
			}
			 
			// Printing marks
			int totalSub = 0;
			int totalMarks = 0;
			System.out.println("Your Results : ");
			if(!student.getMarks().isEmpty()) {
				for(Marks marks: student.getMarks()) {
					totalSub++;
					totalMarks += marks.getMarks();
					System.out.println("course: " + marks.getCourse().getCourse_name() + ",  marks: " + marks.getMarks());
				}
			}
			
			System.out.println("Total Marks : " + totalMarks);
			if(totalSub == 0) {
				totalSub = 1;
			}
			int avg = totalMarks/totalSub;
			System.out.println("Average : " +  avg);
			System.out.print("Grade : ");
			if(avg >= 0 && avg <30) {
				System.out.println("Fails");
			}else if(avg >= 30 && avg <=60) {
				System.out.println("C");
			}else if(avg>60 && avg <80) {
				System.out.println("B");
			}else if(avg>=80 && avg<=100 ) {
				System.out.println("A");
			}else {
				System.out.println("Invalid marks");
			}
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Exception in student profile() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
		
	}

	//buy course
	public void buyCourse(Student registeredStudent) {
		 	
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
			
			
			System.out.println("Enter Course_id to Buy : ");
			int course_id = sc.nextInt();
			
			Course selectedCourse = session.get(Course.class, course_id);
			
			if(selectedCourse == null) {
				System.out.println("Course is not available with this id");
				return;
			}
			
			Student studentFromDb = session.get(Student.class, registeredStudent.getId());
			
			if(studentFromDb.getCourses().contains(selectedCourse)) {
				System.out.println("This Course is Already Purchased");
				return;
			}
			
			
			
			System.out.println("Enter Fees");
			int fees = sc.nextInt();
			
			if(fees != selectedCourse.getFees()) {
				System.out.println("Payment Failed");
				return;
			}

			studentFromDb.getCourses().add(selectedCourse);
			
			session.update(studentFromDb);
			transaction.commit();
			
			System.out.println("Course Saved");	
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Course Not Saved");	
			System.out.println("Exception in buyCourse() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	// view students
	public void viewAllStudents() {

		try {	
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			
			String hql = "FROM Student";
			Query<Student> query = session.createQuery(hql, Student.class);
			
			List<Student> results = query.list();
			System.out.println("List of Students With Purchased Course: ");
			for(Student s: results) {
				System.out.println("Id : " + s.getId() + ", Username : " + s.getUsername() + ", Email : " + s.getEmail());
				
				if(!s.getCourses().isEmpty()) {
					System.out.println("List of Courses Purchased : ");
					for(Course c: s.getCourses()) {
						System.out.println("  --> Course_id : " + c.getCourse_id() + ", Course : " + c.getCourse_name() + ", Duration : " + c.getDuration() + ", Fees : " + c.getFees());
					}
					System.out.println();
				}else {
					System.out.println("  --> No Course Purchased");
					System.out.println();
				}
			}
			
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in viewAllStudent() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	public void updateStudent() {
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
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
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	//remove enrollment
	public void removeEnrollment() {
		try {
			
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Enter student id to remove enrollment.");
			int id = sc.nextInt();
			
			Student student = session.get(Student.class, id);
			
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
					
					transaction.commit();
					System.out.println("Student Enrollment removed");
					
				}	
			}
			
			
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exception in removeEnrollment() : " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	
	public void studentsResult() {
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			String hql = "FROM Student";
			Query<Student> query = session.createQuery(hql,Student.class);
			
			List<Student> results = query.list();
			
			if(results.isEmpty()) {
				System.out.println("Student not available");
				return;
			}
			
			int totalMarks;
			int totalSub;
			int avg;
			
			for(Student s: results) {
				System.out.println("  --> Student id: " + s.getId() + ", Username: " + s.getUsername() + ", Email: " + s.getEmail());
				totalMarks = 0;
				totalSub = 0;
				avg = 0;
				if(!s.getMarks().isEmpty()) {
					for(Marks marks: s.getMarks()) {
						totalMarks += marks.getMarks();
						totalSub++;
						System.out.println("  -> Course id: " + marks.getCourse().getCourse_id() + ", Course : " + marks.getCourse().getCourse_name() + ", marks: " + marks.getMarks());
							
					}
					
					System.out.println("Total Marks: " + totalMarks);
					avg = totalMarks/totalSub;
					System.out.println("Average Marks: " + avg);
					System.out.print("Grade : ");
					if(avg >= 0 && avg <30) {
						System.out.println("Fails");
					}else if(avg >= 30 && avg <=60) {
						System.out.println("C");
					}else if(avg>60 && avg <80) {
						System.out.println("B");
					}else if(avg>=80 && avg<=100 ) {
						System.out.println("A");
					}else {
						System.out.println("Invalid marks");
					}
					
				}else {
					System.out.println("   -> Marks not given to " + s.getUsername());
				}
				
				
			}
			
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exceptioin in studentsResult(): " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
	}

	public void singleStudentResult() {
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();			
			System.out.println("Enter student id");
			int id = sc.nextInt();
			
			Student student = session.get(Student.class, id);
			System.out.println("  --> Id: " + student.getId() +", Username: " + student.getUsername() + ", Email: " + student.getEmail());
			System.out.println("  --> Results: ");
			if(student.getMarks().isEmpty()) {
				System.out.println("   -> Marks of " + student.getUsername() + " not added");
				return;
			}
			
			int totalMarks = 0;
			int avg = 0;
			int totalSub = 0;
			
			for(Marks m: student.getMarks()) {
				totalMarks += m.getMarks();
				totalSub++;
				System.out.println("Course id : " + m.getCourse().getCourse_id() + ", Course: " + m.getCourse().getCourse_name() + "Marks: " + m.getMarks());
			}
			System.out.println("Total Marks: " + totalMarks);
			avg = totalMarks/totalSub;
			System.out.println("Average Marks: " + avg);
			System.out.print("Grade : ");
			if(avg < 0 && avg <30) {
				System.out.println("Fails");
			}else if(avg >= 30 && avg <=60) {
				System.out.println("C");
			}else if(avg>60 && avg <80) {
				System.out.println("B");
			}else if(avg>=80 && avg<=100 ) {
				System.out.println("A");
			}else {
				System.out.println("Invalid marks");
			}
				
		}catch(Exception ex) {
			if(transaction != null && transaction.getStatus() != TransactionStatus.COMMITTED) transaction.rollback();
			ex.printStackTrace();
			System.out.println("Exceptioin in singleStudentResult(): " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}

	public void studentsAttendanceReportByMonth() {
		try {
			session = factory.openSession();
			transaction = session.beginTransaction();
			
			System.out.println("Enter no of month:");
			int month = sc.nextInt();
			
			if(month<1 && month >12) {
				System.out.println("Enter a valid month value.");
				return;
			}
			
			String hql = "FROM Student";
			Query<Student>query = session.createQuery(hql, Student.class);
			
			List<Student> results = query.list();
			
			if(results.isEmpty()) {
				System.out.print("Student not available");
				return;
			}
			
			
			for(Student s: results) {
				System.out.println("  --> Id: " + s.getId() + ", Username: " + s.getUsername() + ", Email: " + s.getEmail());
				
				if(!s.getAttendance().isEmpty()) {
					
					System.out.println("  --> Attendance of " + month + "th month");
					List<Attendance> attendances = s.getAttendance();
					for(Attendance atd: attendances) {
						if(atd.getDate().getMonthValue() == month) {
							System.out.println("  --> Course: " + atd.getCourse().getCourse_name() + ", Date: " + atd.getDate() + ", Status: " + atd.getStatus());			
						}
					}
					System.out.println();
					
				}else {
					System.out.println("  --> " + s.getUsername() + " was absent whole course");
					System.out.println();
				}
				
			}
			
		}catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Exceptioin in singleStudentResult(): " + ex);
		}finally {
			if(session != null) {
				session.close();
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
