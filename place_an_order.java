package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class place_an_order {

	static int cid;
	static int wid;
	static int sid;
	static int table_no;
	static int flag;
	static int array[] = new int[18];
	static int table[] = new int[12];

	public static void place_order(){

		get_userid();

	}

	public static void get_order(){

		int temp_tno = 0;
		Connection connection;
		PreparedStatement ps;
		Scanner scanner = new Scanner(System.in);

		for (int z=1;z<11;z++){
			table[z] = 0;
		}

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");

			System.out.println('\n');
			System.out.print("Choose one among the available table: ");
			ps = connection.prepareStatement("SELECT Table_no FROM `orders` WHERE `Flag` = 1");
			ResultSet result = ps.executeQuery();
			if(result.next()){
				int tno = result.getInt("Table_no");
				temp_tno = tno;
				//System.out.print("  " +tno);
				table[tno] = 1;
				while(result.next()){
					tno = result.getInt("Table_no");
					if (temp_tno == tno) {

					}
					else{
						table[tno] = 1;

						temp_tno = tno;
					}
				}
				for(int f=1;f<11;f++){
					if(table[f] == 1){

					}
					else{
						System.out.print("  " +f);
					}
				}
			}
			else{
				System.out.println('\n');
				System.out.println("There was some problem..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				place_order();
			}

			System.out.println('\n');
			System.out.print("Choose the table no: ");
			table_no = scanner.nextInt();
			System.out.println('\n');
			flag = 1;

			ps = connection.prepareStatement("SELECT SId FROM `orders` ORDER BY OId DESC LIMIT 1;");
			ResultSet result5 = ps.executeQuery();
			if(result5.next()){
				int sid1 = result5.getInt("SId");
				sid = sid1+1;
				choose_waiter();
			}
			else{
				System.out.println('\n');
				System.out.println("Problem in connection ,please retry..!");
				place_order();
			}			
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		scanner.close();
	}

	public static void get_userid(){
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
						cid = result.getInt("CId");
						String name = result.getString("name");
						Long phn = result.getLong("phone");

						System.out.print("Customer Id number: " +cid);
						System.out.print('\n');
						System.out.print("Customer Name: " +name);
						System.out.print('\n');
						System.out.print("Customr Mobile Number: " +phn);
						get_order();
					}
					else{
						System.out.println('\n');
						System.out.println("No Customer details found for this number.First add the user..!");
						landing_page.options();
					}

				} catch (SQLException ex) {
					System.out.println(ex);
				}

			}else {
				System.out.println('\n');
				System.out.println("Enter valid 10 digit mobile number");
				get_userid();
			}

		} else {
			System.out.println("\nInvalid..Enter valid mobile number!");
			get_userid();
		}

		scanner.close();
	}

	static void display(){
		System.out.println('\n');
		System.out.println("These are the items order:- ");
		int listno = 1;

		Connection connection;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
			ps = connection.prepareStatement("SELECT distinct(m.MId),m.Item_name,m.Price FROM orders o,menu m WHERE `sid` = ? and Flag = 1 and m.MId = o.MId");
			ps.setInt(1, sid);
			ResultSet result = ps.executeQuery();

			System.out.print("Sl.No");
			System.out.print("\t");
			System.out.print("Item number");
			System.out.print("\t");
			System.out.print("    Item name");
			System.out.print("\t");
			System.out.print("    Quantity");
			System.out.print("\t");
			System.out.println("   Price");

			if(result.next()){
				int menuid = result.getInt("MId");
				String itemname = result.getString("Item_name");
				int price = result.getInt("Price");

				ps = connection.prepareStatement("SELECT COUNT(MId) AS CountofMId FROM orders WHERE MId = ? and SId = ?");
				ps.setInt(1, menuid);
				ps.setInt(2, sid);
				ResultSet result5 = ps.executeQuery();
				if(result5.next()){
					int quantity = result5.getInt("CountofMId");

					//	SELECT COUNT(MId) AS CountofMId FROM orders WHERE MId = 3 and SId = 4
					System.out.print("  " +listno);
					System.out.print('\t');
					System.out.print("   " +menuid);
					System.out.print('\t');
					System.out.print('\t');
					System.out.print(itemname);
					System.out.print('\t');
					System.out.print('\t');	
					System.out.print(" " +quantity);
					System.out.print('\t');
					System.out.println("    " +price);
					listno++;

					while(result.next()){
						menuid = result.getInt("MId");
						itemname = result.getString("Item_name");
						price = result.getInt("Price");

						ps = connection.prepareStatement("SELECT COUNT(MId) AS CountofMId FROM orders WHERE MId = ? and SId = ?");
						ps.setInt(1, menuid);
						ps.setInt(2, sid);
						ResultSet result7 = ps.executeQuery();
						if(result7.next()){		
							quantity = result7.getInt("CountofMId");
							
							System.out.print("  " +listno);
							System.out.print('\t');
							System.out.print("   " +menuid);
							System.out.print('\t');
							System.out.print('\t');
							System.out.print(itemname);
							System.out.print('\t');
							System.out.print(" " +quantity);
							System.out.print('\t');
							System.out.print('\t');
							System.out.println("    " +price);	
							listno++;
						}
					}
				}
			}
			else{
				System.out.println('\n');
				System.out.println("write something..!");
				landing_page.options();
			}

			System.out.print('\n');
			System.out.println("These are the items ordered...");
			landing_page.options();
		}

		catch (SQLException ex) {
			System.out.println(ex);
		}
	}


	public static void choose_waiter(){

		Scanner scanner = new Scanner(System.in);
		System.out.print("Choose the name of the waiter who's going to serve: ");

		Connection connection;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
			ps = connection.prepareStatement("SELECT * FROM `waiter`");
			ResultSet result1 = ps.executeQuery();

			System.out.print("Waiter-Id");
			System.out.print('\t');
			System.out.println("Name");

			if(result1.next()){
				int wid = result1.getInt("WId");
				String name = result1.getString("Name");
				System.out.print("  " +wid);
				System.out.print('\t');
				System.out.print('\t');
				System.out.println(name);
				while(result1.next()){
					wid = result1.getInt("WId");
					name = result1.getString("Name");

					System.out.print("  " +wid);
					System.out.print('\t');
					System.out.print('\t');
					System.out.println(name);
				}
			}
			else{
				System.out.println('\n');
				System.out.println("There was some problem..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				place_order();
			}

			System.out.print("Choose the waiter Id: ");
			wid = scanner.nextInt();
			System.out.println('\n');
			if(wid<7){
				choose_order();
			}
			else{
				System.out.println("Invalid option choosen,please choose right one..!");
				choose_waiter();
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}

		scanner.close();
	}

	public static void choose_order(){

		int rs = 0;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Here is the menu: ");

		Connection connection;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
			ps = connection.prepareStatement("SELECT * FROM `menu`");
			ResultSet result2 = ps.executeQuery();

			System.out.print("Item-Id");
			System.out.print('\t');
			System.out.print("   Item Name");
			System.out.print('\t');
			System.out.print('\t');
			System.out.println("Price");

			if(result2.next()){
				int mid1 = result2.getInt("MId");
				String item = result2.getString("Item_name");
				int price = result2.getInt("Price");

				System.out.print("  " +mid1);
				System.out.print('\t');
				System.out.print(item);
				System.out.print('\t');
				System.out.println("  " +price);
				while(result2.next()){
					int mid = result2.getInt("MId");
					item = result2.getString("Item_name");
					price = result2.getInt("Price");

					System.out.print("  " +mid);
					System.out.print('\t');
					System.out.print(item);
					System.out.print('\t');
					System.out.println("  " +price);
				}
				System.out.println("Please select the item numbers and keep pressing enter..!");
				System.out.println("Press 000 to exit");

				int mid = 0;
				while (scanner.hasNext()){
					if(scanner.hasNextInt()){
						int value = scanner.nextInt();
						if(value == 000){
							System.out.println("000 value reached");
							//landing_page.options();
							display();
						}
						else if(value >= 18){
							System.out.println("Invalid..Choose valid option");
							choose_order();
						}
						else{
							mid = value;
						/*	array[value] = ++array[value];

							System.out.println("sid" +sid);
							System.out.println("table no " +table_no);
							System.out.println("wid" +wid);
							System.out.println("cid" +cid);
							System.out.println("flag" +flag);
							System.out.println("mid" +mid);
						 */
							ps = connection.prepareStatement("INSERT INTO `orders`(`SId`, `Table_no`, `WId`, `CId`, `Flag`, `MId`) VALUES (? , ? , ? , ? , ? , ?)");
							ps.setInt(1, sid);
							ps.setInt(2, table_no);
							ps.setInt(3, wid);
							ps.setInt(4, cid);
							ps.setInt(5, flag);
							ps.setInt(6, mid);
							rs = ps.executeUpdate();

							/*if(rs>0){
								System.out.println("\nOrder successfully placed..!");
								display();
							}
							else{
								System.out.println('\n');
								System.out.println("Problem in adding the customer details..!");
								System.out.println("Please try again..");
								System.out.println('\n');
								place_order();
							}*/
						}
					}		
					else{
						System.out.println("\nInvalid..Enter valid numbers...!");
						place_order();
					}
				}

				if(rs>0){
					System.out.println("\nOrder successfully placed..!");
					display();
				}
				else{
					System.out.println('\n');
					System.out.println("Problem in adding the customer details..!");
					System.out.println("Please try again..");
					System.out.println('\n');
					place_order();
				}

			}
			else{
				System.out.println('\n');
				System.out.println("There was some problem..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				place_order();
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}

		scanner.close();
	}
}
