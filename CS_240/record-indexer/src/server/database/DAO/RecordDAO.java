package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.database.DatabaseException;
import shared.model.Record;
import server.database.Database;

/** deals with the record table in the database*/
public class RecordDAO {

	private Database db;
	
	public RecordDAO(Database db) {
		this.db = db;
	}
	
	/**adds a record to the database
	 * 
	 * @param record the record object
	 * @throws DatabaseException 
	 */
	public int add(Record record) throws DatabaseException
	{
		int id = 0;
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into record (image_id, rowNumber) values (?, ?)";
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getImage_id());
			stmt.setInt(2, record.getRowNumber());

			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				id = keyRS.getInt(1);
				record.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert record");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert record", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		return id;
	}
}
