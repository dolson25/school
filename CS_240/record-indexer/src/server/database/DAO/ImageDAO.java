package server.database.DAO;

import server.database.DatabaseException;
import shared.model.Image;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.database.Database;

/** deals with the Image table in the database*/
public class ImageDAO {

	private Database db;
	
	public ImageDAO(Database db) {
		this.db = db;
	}
	
	/**reads and retrieves a sample image from database
	 * 
	 * @param project_id the project_id
	 * @return an image object
	 * @throws DatabaseException 
	 */
	public Image getSampleImage(int project_id) throws DatabaseException
	{	
		Image image = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select filepath from image where project_id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);

			rs = stmt.executeQuery();
			String filepath;
			if(rs.next())
				filepath = rs.getString(1);
			else
				return null;

			image = new Image(1, filepath, project_id, 1);

		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return image;
	}
	
	/**reads and retrieves one image from database, if successful
	 * 
	 * @param project_id the project_id
	 * @return an image object
	 * @throws DatabaseException 
	 */
	public Image getAvailableImage(int project_id) throws DatabaseException
	{		
		Image image = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select * from image where project_id = ? AND available = 1";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);

			rs = stmt.executeQuery();
			
			if(rs.next())
			{
				int id = rs.getInt(1);
				String filepath = rs.getString(2);

				image = new Image(id, filepath, project_id, 1);
			}	
			else
				image = null;
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return image;
	}
	
	/**populates the Image table with an image
	 * 
	 * @param image the image to be added
	 * @throws DatabaseException 
	 */
	public int add(Image image) throws DatabaseException
	{
		int id = 0;
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into image (filepath, project_id, "
												+ "available) values (?, ?, ?)";
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, image.getFilepath());
			stmt.setInt(2, image.getProject_id());
			stmt.setInt(3, image.getAvailable());
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				id = keyRS.getInt(1);
				image.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert image");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert image", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		return id;
	}
	
	/**
	 * changes the value to true when an image is available for indexing, false if not
	 * @throws DatabaseException 
	 */
	public void updateAvailable(int available, int image_id) throws DatabaseException
	{
		PreparedStatement stmt = null;
		try {
			String query = "update image set available = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, available);
			stmt.setInt(2, image_id);
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update available");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update available", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
	/**gets the project id given a batch id
	 * 
	 * @param batch_id the batch_id
	 * @return a project id
	 * @throws DatabaseException 
	 */
	public int getProjectID(int batch_id) throws DatabaseException
	{
		int project_id;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select project_id from image where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch_id);

			rs = stmt.executeQuery();
			
			if(rs.next())
				project_id = rs.getInt(1);	
			else
				project_id = 0;
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return project_id;
	}
}
