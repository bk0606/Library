package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

/**
 * Wrapper class for DatabaseOperations
 */
public class DatabaseController implements IDatabaseController {
	private DatabaseOperations dbo = null;
	private String databaseName    = "";
	private Connection connection  = null;
	private final String DRIVER    = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private Statement statement    = null;
	
	public ResultSet select(String table) {
		ResultSet rs = dbo.select(table);
		return rs;
	}
	public void insert(LinkedHashMap<String, String> tableLine, String table) {
		dbo.insert(tableLine, table);
	}
	public void delete(String table, int ID) {
		dbo.delete(table, ID);
	}
	
	private void createDatabaseIfNotExist() {
		String[] createDBQueryes = getCreateDBQueryes();
		try {
    		ResultSet dbList = connection.getMetaData().getCatalogs();
    		boolean isExists = false;
    		while (dbList.next()) {
    			if (dbList.getString(1).equals(databaseName))
    				isExists = true;
    		}
    		if (!isExists) {
    			for (int i = 0; i < createDBQueryes.length; i++) {
    				statement.executeUpdate(createDBQueryes[i]);
    			}
    		}
			connection.setCatalog(databaseName);
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }
	private String[] getCreateDBQueryes() {
		String[] createDBQueryes = new String[]{
			"CREATE DATABASE ["+databaseName+"]",
			"CREATE TABLE ["+databaseName+"].[dbo].[Books]("
    			+ "[ID] [smallint] IDENTITY(1,1) NOT NULL,"
    			+ "[name] [nvarchar](50) NULL,"
    			+ "[author] [nvarchar](50) NOT NULL,"
    			+ "[DOP] [date] NULL)", 
			"CREATE TABLE ["+databaseName+"].[dbo].[UserBooks]("
				+ "[ID] [smallint] IDENTITY(1,1) NOT NULL,"
				+ "[ID_user] [smallint] NOT NULL,"
				+ "[ID_book] [smallint] NOT NULL)", 
			"CREATE TABLE ["+databaseName+"].[dbo].[Users]("
				+ "[ID] [smallint] IDENTITY(1,1) NOT NULL,"
				+ "[name] [nvarchar](50) NOT NULL,"
				+ "[DOB] [date] NULL,"
				+ "[pnumber] [nvarchar](50) NULL)"
		};
		return createDBQueryes;
	}
	    
	public DatabaseController(String connectUrl, String databaseName) {
		this.databaseName = databaseName;
	    try {
            Class.forName(this.DRIVER);
            this.connection = DriverManager.getConnection(connectUrl);
            statement = connection.createStatement();
            this.createDatabaseIfNotExist();
            this.dbo = new DatabaseOperations(statement);
        } 
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
}
