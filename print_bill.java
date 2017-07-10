package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class print_bill {

	static int cid;
	static int wid;
	static int sid;
	static int table_no;
	static int flag;
	static int array[] = new int[18];
	static int table[] = new int[12];

	public static void print(){

		get_order();
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
			System.out.print("Choose the active table: ");
			ps = connection.prepareStatement("SELECT Table_no FROM `orders` WHERE `Flag` = 1");
			ResultSet result = ps.executeQuery();
			if(result.next()){
				int tno = result.getInt("Table_no");
				temp_tno = tno;
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
						System.out.print("  " +f);
					}
					else{

					}
				}
			}
			else{
				System.out.println('\n');
				System.out.println("There was some problem..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				print();
			}

			System.out.println('\n');
			System.out.print("Choose the table no: ");
			table_no = scanner.nextInt();
			flag = 1;

			ps = connection.prepareStatement("SELECT SId FROM `orders` WHERE `Table_no`=? and `Flag` = 1;");
			ps.setInt(1, table_no);
			ResultSet result5 = ps.executeQuery();
			if(result5.next()){
				sid = result5.getInt("SId");
				choose_waiter();
			}
			else{
				System.out.println('\n');
				System.out.println("Problem in connection ,please retry..!");
				print();
			}			
		} catch (SQLException ex) {
			System.out.println(ex);
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
			
			ps = connection.prepareStatement("select sum(price) as Total from menu m,orders o where o.MId = m.MId and o.SID = ? and flag = 1");
			ps.setInt(1, sid);
			ResultSet result10 = ps.executeQuery();
			if(result10.next()){
				int total = result10.getInt("Total");
				System.out.println("Total amount of the bill is " +total);
				
				ps = connection.prepareStatement("Insert into bill (SId , Amount) Values ( ? , ? )");
				ps.setInt(1, sid);
				ps.setLong(2, total);
				ps.executeUpdate();
			}
			flag = 0;
			ps = connection.prepareStatement("UPDATE orders SET Flag = 0 WHERE Table_no = ? and SId = ?");
			ps.setInt(1, table_no);
			ps.setInt(2, sid);
			ps.execute();
			landing_page.options();
		}

		catch (SQLException ex) {
			System.out.println(ex);
		}
	}


	public static void choose_waiter(){

		System.out.println("Waiter who served this table: ");

		Connection connection;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
			ps = connection.prepareStatement("select Name from waiter w,orders o where Table_no = ? and o.WId=w.WId and Flag = 1");
			ps.setInt(1, table_no);
			ResultSet result1 = ps.executeQuery();

			if(result1.next()){
				String name = result1.getString("Name");
				System.out.print("Waiter Name:  " +name);
				display();
			}
			else{
				System.out.println('\n');
				System.out.println("There was some problem..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				print();
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}
	}
}

