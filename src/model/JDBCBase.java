package model; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ivannnnnn
 *
 */
public class JDBCBase 
{
	static String driverPath = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static String connectionUrl = "jdbc:sqlserver://localhost;user=sa;password=sa;";

	public static void BaseMethod() throws SQLException {
		try {
			Class.forName(driverPath);
			System.out.println("check the driverPath succesed...");

			Connection con = DriverManager.getConnection(connectionUrl);
			System.out.println("connection created...");
			Statement st = con.createStatement();

			String dataBaseName = "Library";
			String tableName = "Users";

			System.out.println("try to drop database");

			try {
				String dropDataBaseQuery = "DROP DATABASE " + dataBaseName;
				st.executeUpdate(dropDataBaseQuery);
				System.out.println("drop succesed"); 
			} 
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("some wrong with dropping or database is not exist");
			}

			try {
				String createDatabase = "CREATE DATABASE " + dataBaseName;
				st.executeUpdate(createDatabase);// only for update insert delete createTable dropTable
				System.out.println("database created...");

			} 
			catch (Exception e) {
				System.out.println("error or database exist");
			}
			try {

				String createTableTest = "CREATE TABLE [" + dataBaseName + "].[dbo].[" + tableName + "] (id int PRIMARY KEY IDENTITY(1,1) NOT NULL, name varchar(50) NOT NULL, age int NOT NULL, Date date, phone varchar(50))";
				st.executeUpdate(createTableTest);
				System.out.println("table " +tableName + " created...");

				tableName = "Book";
				createTableTest = "CREATE TABLE [" + dataBaseName + "].[dbo].[" + tableName + "] (id int PRIMARY KEY IDENTITY(1,1) NOT NULL, name varchar(50) NOT NULL,  Date date, isPublic bit NOT NULL)";
				st.executeUpdate(createTableTest);
				System.out.println("table " +tableName + " created...");

				tableName = "USBooks";
				createTableTest = "CREATE TABLE [" + dataBaseName + "].[dbo].[" + tableName + "] (id int PRIMARY KEY IDENTITY(1,1) NOT NULL, id_user int NOT NULL FOREIGN KEY REFERENCES Users(id), id_book int NOT NULL FOREIGN KEY REFERENCES Book(id))";
				st.executeUpdate(createTableTest);
				System.out.println("table " +tableName + " created...");


			} 
			catch (Exception e) {
				System.out.println("error or tables exists");
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}