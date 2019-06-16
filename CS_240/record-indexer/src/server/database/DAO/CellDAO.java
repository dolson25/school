package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import server.database.DatabaseException;
import server.database.Database;
import shared.communication.SearchOutputArray;
import shared.model.Cell;

/** deals with the cell table in the database*/
public class CellDAO {

	private Database db;
	
	public CellDAO(Database db) {
		this.db = db;
	}
	
	/**adds a cell to the database
	 * 
	 * @param  cell the cell object
	 * @throws DatabaseException 
	 */
	public void add(Cell cell) throws DatabaseException
	{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into cell (field_id, record_id, value) "
												+ "values (?, ?, ?)";
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getField_id());
			stmt.setInt(2, cell.getRecord_id());
			stmt.setString(3, cell.getValue());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				cell.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert cell", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	
	
	
	/**returns the output for search
	 * 
	 * @param field field_id to be searched
	 * @param value value to be searched
	 * @return Array of (batch id, URL, row#, feild id) objects
	 * @throws DatabaseException 
	 */
	public ArrayList<SearchOutputArray> search(int field, String value) throws DatabaseException
	{		
		ArrayList<SearchOutputArray> output = new ArrayList<SearchOutputArray>();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select image.id, image.filepath, record.rowNumber, cell.field_id "
						    + "from record "
						    + "join image on record.image_id=image.id "
						    + "join cell on cell.record_id=record.id " 
					        + "where cell.field_id = ? AND LOWER(cell.value) = ?";

			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field);
			stmt.setString(2, value.toLowerCase());
			rs = stmt.executeQuery();
			
			while(rs.next())
			{
				SearchOutputArray s = new SearchOutputArray(rs.getString(1), rs.getString(2),
						                      rs.getString(3), rs.getString(4));
				output.add(s);
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
		
		return output;
	}
}
