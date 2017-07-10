package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import rms.landing_page;

public class login {
	public static void main (String args[]){
		System.out.println("Welcome to rms..!");
		signin();
	}
	
	public static void signin(){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the username and password");
		System.out.print("Username: ");
		String username = scanner.next();
		System.out.print("Enter the password: ");
		String password = scanner.next();
		
		Connection connection;
        PreparedStatement ps;
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
            ps = connection.prepareStatement("SELECT `username`, `password` FROM `login` WHERE `username` = ? AND `password` = ?");
            ps.setString(1, username);
            ps.setString(2, String.valueOf(password));
            ResultSet result = ps.executeQuery();
            if(result.next()){
            	System.out.println('\n');
            	System.out.println("Login Successful..!");
                landing_page.options();
            }
            else{
            	System.out.println('\n');
            	System.out.println("Username/Password incorrect..!");
                System.out.println("Please try again..");
                System.out.println('\n');
                signin();
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
		scanner.close();
	}
}
