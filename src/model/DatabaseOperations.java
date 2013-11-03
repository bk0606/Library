package model;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

/**
 * Class realizing basic methods of work with the database
 */
public class DatabaseOperations {
    private Statement stm = null;
    
    public ResultSet select (String table) {
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM "+table;
            rs = stm.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }
    
    public void insert (Map<String, String> tableLine, String table) {
        try {
            String sqlKeys = "";
            String sqlVals = "";
            Iterator<String> itKey = tableLine.keySet().iterator();
            Iterator<String> itVal = tableLine.values().iterator();
            // Go trough Map collection and get keys & vals
            while (itKey.hasNext()) {
            	sqlKeys += itKey.next().toString();
            	sqlVals += "'"+itVal.next().toString()+"'";
            	if (itKey.hasNext()) {
            		sqlKeys += ", ";
            		sqlVals += ", ";
            	}
            }            
            String query = "INSERT INTO "+table+
                    " ("+sqlKeys+") VALUES ("+sqlVals+")";
            stm.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void delete (String table, int id) {
        try {
            String query = "DELETE FROM "+table+" WHERE ID="+id;
            stm.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public DatabaseOperations (Statement statement) {
    	this.stm = statement;
    }
}
