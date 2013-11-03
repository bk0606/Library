package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface IDatabaseController {
	public ResultSet executeQuery(String query) throws SQLException;
	public ResultSet select(String table);
	public void insert(LinkedHashMap<String, String> tableLine, String table);
	public void delete(String table, int ID);
}
