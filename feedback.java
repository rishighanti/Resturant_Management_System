package rms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class feedback {
	
	static int cid;
	static int wid;
	static int sid;
	static int table_no;
	static int flag;
	static int array[] = new int[18];
	static int table[] = new int[12];

	public static void text(){
	
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
				System.out.println("No tables active..!");
				System.out.println('\n');
				landing_page.options();
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
				give();
			}
			else{
				System.out.println('\n');
				System.out.println("Problem in connection ,please retry..!");
				text();
			}			
		} catch (SQLException ex) {
			System.out.println(ex);
		}

		scanner.close();
	}
	
	public static void give(){
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Write some feedback: ");
		System.out.println("Press ',,' to close..");
		String TERMINATOR_STRING = ",,";
		StringBuilder b = new StringBuilder();
        String strLine;
        while (!(strLine = scanner.nextLine()).equals(TERMINATOR_STRING)) {
            b.append(strLine);
        }
		
        String text = b.toString();
        
		Connection connection;
		PreparedStatement ps;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/restocare", "root", "");
			ps = connection.prepareStatement("Insert into feedback (Ftext, SId) Values (? , ? )");
			ps.setString(1, text);
			ps.setInt(2, sid);
			int result = ps.executeUpdate();
			if(result>0){
				System.out.println("\nFeedback was successfully added..!");
				landing_page.options();
			}
			else{
				System.out.println('\n');
				System.out.println("Problem in adding the feedback..!");
				System.out.println("Please try again..");
				System.out.println('\n');
				give();
			}
		}
		catch (SQLException ex) {
			System.out.println(ex);
		}
		
		scanner.close();
	}
}
