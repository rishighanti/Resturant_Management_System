package rms;

import java.util.Scanner;

public class landing_page {

	public static void options(){

		int choice;
		Scanner scanner = new Scanner(System.in);

		System.out.println('\n');
		System.out.println("Please choose among the following option:");
		System.out.println("1.Place an order");
		System.out.println("2.Update an order");
		System.out.println("3.Add New Customer Details");
		System.out.println("4.Get Customer Details");
		System.out.println("5.Give Feedback");
		System.out.println("6.Print bill");
		System.out.println("7.Exit..!"); 
		System.out.println('\n');

		System.out.print("Choose option: ");
		
		if (scanner.hasNextInt()) {

			choice = scanner.nextInt() ; 
			if (choice <8 ){
				do{
					switch (choice) {
					case 1: place_an_order.place_order();
					break;

					case 2: update_an_order.update_order();
					break;

					case 3: add_customer_details.add();
					break;

					case 4: get_customer_details.get();
					break;
					
					case 5: feedback.text();
					break;

					case 6: print_bill.print();
					break;

					case 7:	System.out.println("Program Terminated...!"); 
							System.exit(0);

					}
				}while(choice == 7);

			}else {
				System.out.println("Invalid....Choose from 1 to 6 option.");
				options();
			}
			
		} else {
			System.out.println("Invalid input..Please enter the valid option!");
			options();
		}

		scanner.close();
	}

}
