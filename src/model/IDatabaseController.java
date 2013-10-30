package model;

import java.sql.ResultSet;
import java.util.LinkedHashMap;

public interface IDatabaseController {
	public ResultSet performQuery(String table);
	public void performQuery(LinkedHashMap<String, String> tableLine, String table);
	public void performQuery(String table, int ID);
}
