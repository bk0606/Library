package model;

import java.sql.ResultSet;
import java.util.LinkedHashMap;

/**
 * Wrapper class for DatabaseOperations
 */
public class DatabaseController implements IDatabaseController {
	private DatabaseOperations dbo = null;
	
	public ResultSet performQuery(String table) {
		ResultSet rs = dbo.select(table);		
		return rs;
	}
	public void performQuery(LinkedHashMap<String, String> tableLine, String table) {
		dbo.insert(tableLine, table);
	}
	public void performQuery(String table, int ID) {
		dbo.delete(table, ID);
	}
    
	public DatabaseController(String connectUrl) {
	    this.dbo = new DatabaseOperations(connectUrl);
	}
}
