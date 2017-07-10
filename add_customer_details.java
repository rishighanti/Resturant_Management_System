package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class add_customer_details {

	public static void add(){

		Long temp,phone;
		int count = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println('\n');
		System.out.println("Please enter the name and mobile no of the customer:");

		System.out.print("Customer Name: ");
		String name = scanner.next();

		System.out.print("Customer Mobile Number: ");
		if (scanner.hasNextLong()) {
			phone = scanner.nextLong(); 
			temp = phone;

			while(temp!=0)
			{
				temp/=10;           
				++count;
			}

			if (count == 10){

				Connection connection;
				PreparedStatement ps;

				try {
					connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
					ps = connection.prepareStatement("Insert into customer (name , phone) Values ( ? , ? )");
					ps.setString(1, name);
					ps.setLong(2, phone);
					int result = ps.executeUpdate();
					if(result>0){
						System.out.println("\nCustomer Details successfully added..!");
						landing_page.options();
					}
					else{
						System.out.println('\n');
						System.out.println("Problem in adding the customer details..!");
						System.out.println("Please try again..");
						System.out.println('\n');
						add();
					}
				} catch (SQLException ex) {
					System.out.println(ex);
				}
				scanner.close();
			}else {
				System.out.println("\nEnter valid 10 digit mobile number");
				add();
			}
		}else {
			System.out.println("\nInvalid..Enter valid mobile number!");
			add();
		}
	}
}