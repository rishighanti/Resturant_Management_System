package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class get_customer_details {

	public static void get(){

		int count = 0;
		Long temp;
		Scanner scanner = new Scanner(System.in);
		System.out.println('\n');
		System.out.println("Please enter the details:- ");

		System.out.print("Enter the Customer Mobile Number: ");
		if (scanner.hasNextLong()) {

			Long phone = scanner.nextLong(); 
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
					ps = connection.prepareStatement("SELECT * FROM `customer` WHERE `phone` = ?");
					ps.setLong(1, phone);
					ResultSet result = ps.executeQuery();
					if(result.next()){
						System.out.println('\n');
						System.out.println("Customer Details:- ");
						String name = result.getString("name");
						Long phn = result.getLong("phone");

						System.out.print("Customer Name: " +name);
						System.out.print('\n');
						System.out.print("Customr Mobile Number: " +phn);
						landing_page.options();
					}
					else{
						System.out.println('\n');
						System.out.println("No Customer details found for this number..!");
						landing_page.options();
					}

				} catch (SQLException ex) {
					System.out.println(ex);
				}

			}else {
				System.out.println('\n');
				System.out.println("Enter valid 10 digit mobile number");
				get();
			}

		} else {
			System.out.println("\nInvalid..Enter valid mobile number!");
			get();
		}

		scanner.close();
	}
}
