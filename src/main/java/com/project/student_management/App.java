package com.project.student_management;

import java.util.Scanner;

import org.hibernate.SessionFactory;

import com.project.student_management.entity.Admin;
import com.project.student_management.entity.Student;
import com.project.student_management.services.AdminDao;
import com.project.student_management.services.AttendanceDao;
import com.project.student_management.services.ConfigurationFile;
import com.project.student_management.services.CourseDao;
import com.project.student_management.services.MarksDao;
import com.project.student_management.services.StudentDao;

public class App 
{
    public static void main( String[] args )
    {
    	Scanner sc = new Scanner(System.in);
    	
    	// Configuration
    	ConfigurationFile cf = new ConfigurationFile();
    	SessionFactory factory = cf.isConfigured();
    	
    	if(factory == null) {
    		System.out.println("Problem in SessionFactory");
    		return;
    	}
    	
    	//Admin
    	Admin registeredAdmin = new Admin();
    	//AdminDao
        AdminDao adminDao = new AdminDao(factory);
        
        // Student
        Student registeredStudent = new Student();
        //StudentDao
        StudentDao studentDao = new StudentDao(factory);
        //Attendance Dao
        AttendanceDao attendanceDao = new AttendanceDao(factory);
        
        // MarksDao
        MarksDao marksDao = new MarksDao(factory);
       
        
        //CourseDao
        CourseDao courseDao = new CourseDao(factory);
       
    	
    	// Admin
        System.out.println("Enter 1 to Signup  as Admin");
        System.out.println("Enter 2 to Signin  as Admin");
        
        //Student
        System.out.println("Enter 3 to Signup  as Student");
        System.out.println("Enter 4 to Signin  as Student");
        
        //Check Login
        boolean isAdminLogin = false;       
        boolean isStudentLogin = false;
        
        System.out.println("Enter Option : ");
        int option = sc.nextInt();
              
        switch(option) {
        case 1:
        	 // AdminDao configure
          
        	System.out.println("Enter Admin Passcode");
        	String passcode = sc.next();
        	
        	if(passcode.equals("adminpasscode")) {        		
        		registeredAdmin = adminDao.singupAdmin();
        		if(registeredAdmin != null) {
        			isAdminLogin = true;
        		}
        		System.out.println("Admin not registered");
        	}
        	break;    	
        case 2:
        	 // AdminDao configure
           
        	registeredAdmin = adminDao.loginAdmin();
        	if(registeredAdmin != null) {
        		isAdminLogin = true;
        	}
        	break;
        case 3:
        	
        	registeredStudent = studentDao.singupStudent();
    		if(registeredStudent != null) {
    			isStudentLogin = true;
    		}
        	break;
        case 4:
        	// student login
        	
        	registeredStudent = studentDao.loginStudent();
        	if(registeredStudent != null) {
        		isStudentLogin = true;
        	}	
        	break;
        }
        
        //Admin functionalities
        while(isAdminLogin) {
        	System.out.println("Enter 1 to See Profile"); 
        	System.out.println("Enter 2 to View Course");       	
        	System.out.println("Enter 3 to Add Course");
        	System.out.println("Enter 4 to Update Course");      
        	System.out.println("Enter 5 to Delete Course");
        	System.out.println("Enter 6 to add Course in your profile");
        	
        	System.out.println("Enter 7 to View Students With Course");      
        	System.out.println("Enter 8 to Add Student");      	
        	System.out.println("Enter 9 to Update Student");
        	System.out.println("Enter 10 to remove Student Enrollement");
        	System.out.println("Enter 11 to give marks to Student");
        	
        	System.out.println("Enter Option");
        	int opt = sc.nextInt();
        	
        	switch(opt) {
        	case 1:
        		adminDao.profile(registeredAdmin);
        		break;
        	case 2:
        		courseDao.fetchAllCourse();
        		break;
        		
        	case 3:
        		courseDao.insertcourse();
        		break;
        		
        	case 4:
        		courseDao.updateCourse();
        		break;
        		
        	case 5:
        		courseDao.deleteCourese();
        		break;
        		
        	case 6:
        		adminDao.addCourseToProfile(registeredAdmin);
        		break;
        		
        	case 7:
        		studentDao.viewAllStudents();
        		break;
        		
        	case 8:
        		studentDao.singupStudent();
        		break;
        		
        	case 9:
        		studentDao.updateStudent();
        		break;
        		
        	case 10:
        		studentDao.removeEnrollment();
        		break;
        				
        	case 11:
        		marksDao.giveMarksToStudent();
        		break;
        	
        	default:
    			System.out.println("Enter Given Option");
        	}
        	
        	System.out.println("Do you want to Signout y/n ? ");
        	char per = sc.next().charAt(0);
        	if(per == 'y') {
        		isAdminLogin = false;
        	} 	
        	
        }
        
        //Student Functionalities
        
        while(isStudentLogin) {
        	System.out.println("Enter 1 to See Profile");
        	System.out.println("Enter 2 to See all Course");
        	System.out.println("Enter 3 to Buy Course");
        	System.out.println("Enter 4 to Give Attendance");
        	
        	System.out.println("Enter Option");
        	int opt = sc.nextInt();
        	
        	switch(opt) {
        	case 1:
        		studentDao.profile(registeredStudent);
        		break;
        	case 2:
        		courseDao.fetchAllCourse();
        		break;
        	case 3:
        		studentDao.buyCourse(registeredStudent);
        		break;
        		
        	case 4:
        		attendanceDao.giveAttendance(registeredStudent);
        		break;
        	
        	default:
        			System.out.println("Enter Given Option");
        	}
        	
        	System.out.println("Do you want to Signout y/n ? ");
        	char per = sc.next().charAt(0);
        	if(per == 'y') {
        		isStudentLogin = false;
        	} 	
        	
        }
        
        
        System.out.println("You are signOut");
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }   
}
