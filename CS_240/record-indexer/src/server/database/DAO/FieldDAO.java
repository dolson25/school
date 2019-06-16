package server.database.DAO;

import java.sql.PreparedStatement;
import server.database.Database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import server.database.DatabaseException;
import shared.model.Field;

/** deals with the Field table in the database*/
public class FieldDAO {

	private Database db;
	
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	/**populates the Field table with a field
	 * 
	 * @param field the field to be added
	 * @throws DatabaseException 
	 */
	public int add(Field field) throws DatabaseException
	{
		int id;
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into field (title, xCoordinate, width, "
								     + "fieldHelpPath, knownDataPath, project_id, "
								     + "columnNumber) values (?, ?, ?, ?, ?, ?, ?)";
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, field.getTitle());
			stmt.setInt(2, field.getXCoordinate());
			stmt.setInt(3, field.getWidth());
			stmt.setString(4, field.getFieldHelpPath());
			stmt.setString(5, field.getKnownDataPath());
			stmt.setInt(6, field.getProject_id());
			stmt.setInt(7, field.getColumnNumber());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				id = keyRS.getInt(1);
				field.setId(id);
			}
			else {
				throw new DatabaseException("Could not field");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert field", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		return id;
	}
	
	/**reads and retrieves all the fields from a project from database, if successful
	 * 
	 * @param project_id the project id
	 * @return List of field objects
	 * @throws DatabaseException 
	 */
	public ArrayList<Field> getFieldInfo(int project_id) throws DatabaseException
	{		
		ArrayList<Field> fields = new ArrayList<Field>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query;
			if(project_id != 0)
			{
				query = "select * from field where project_id = ?";
				stmt = db.getConnection().prepareStatement(query);
				stmt.setInt(1, project_id);
			}
			else
			{
				query = "select * from field";
				stmt = db.getConnection().prepareStatement(query);
			}
				
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				int xCoordinate = rs.getInt(3);
				int width = rs.getInt(4);
				String fieldHelpPath = rs.getString(5);
				String knownDataPath = rs.getString(6);
				int proj_id = rs.getInt(7);
				int columnNumber = rs.getInt(8);

				fields.add(new Field(id, title, xCoordinate, width, 
						fieldHelpPath, knownDataPath, proj_id, columnNumber));
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return fields;	
	}
	
	/**returns the number of fields belonging to a project
	 * 
	 * @param project_id the project id
	 * @return numFields
	 * @throws DatabaseException 
	 */
	public int getNumFields(int project_id) throws DatabaseException
	{	
		int numFields = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			String query = "select id from field where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				numFields++;
			}
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return numFields;	
	}
	
	/**checks the validity of this field id
	 * 
	 * @param field_id the field id
	 * @return true if found
	 * @throws DatabaseException 
	 */
	public boolean isField(int field_id) throws DatabaseException
	{	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			String query = "select id from field where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field_id);

			rs = stmt.executeQuery();
			if(rs.next())
				return true;
			return false;

		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}

}
