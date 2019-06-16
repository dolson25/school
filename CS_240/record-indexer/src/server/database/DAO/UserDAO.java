package server.database.DAO;

import server.database.DatabaseException;
import shared.model.User;
import java.sql.*;
import server.database.Database;

/** deals with the User table in the database*/
public class UserDAO {

	private Database db;
	
	public UserDAO(Database db) {
		this.db = db;
	}
	
	/**reads the database to see if the username and password match
	 * 
	 * @param username username
	 * @param password matching password
	 * @return true if a match
	 * @throws DatabaseException 
	 */
	public boolean validateUser(String username, String password) throws DatabaseException
	{
		boolean match = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select id from user where username = ? AND password = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);

			rs = stmt.executeQuery();
			
			if(rs.next())
				match = true;
		}
		catch (SQLException e) {
			DatabaseException serverEx = new DatabaseException(e.getMessage(), e);
			
			throw serverEx;
		}		
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return match;	
	}
	
	/** gets a user
	 * 
	 * @param username username
	 * @return a user
	 * @throws DatabaseException 
	 */
	public User getUser(String username) throws DatabaseException
	{	
		User user = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select* from user where username = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, username);

			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String password = rs.getString(3);
				String firstname = rs.getString(4);
				String lastname = rs.getString(5);
				int numRecordsIndexed = rs.getInt(7);
				int checkedOut = rs.getInt(8);
				int batch_id = rs.getInt(9);

				user = new User(id, username, password, firstname, 
					lastname, "email", numRecordsIndexed, checkedOut, batch_id);
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
		
		return user;
	}
	
	/**populates the User table with a user
	 * 
	 * @param user the user to be added
	 */
	public void add(User user) throws DatabaseException
	{
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into user (username, password, firstname, "
								     + "lastname, email, numberRecordsIndexed, "
								     + "batchCheckedOut, batchNumberAssigned) "
								     + "values (?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstname());
			stmt.setString(4, user.getLastname());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getNumberRecordsIndexed());
			stmt.setInt(7, 0);
			stmt.setInt(8, 0);
			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert user", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
	}
	 
	/**
	 * updates the number of records a user has indexed
	 */
	public void updateNumRecordsIndexed(String username, int numRecords) throws DatabaseException
	{	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				String query = "update user set  NumberRecordsIndexed = ? where username = ?";
				stmt = db.getConnection().prepareStatement(query);
				stmt.setInt(1, numRecords);
				stmt.setString(2, username);

				if (stmt.executeUpdate() != 1) {
					throw new DatabaseException("Could not update user");
				}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update user", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**changes value to true when a user downloads, false when they are finished
	 * 
	 * @param checked out true or false (1 or 0)
	 * @throws DatabaseException 
	 */
	public void updateBatchCheckedOut(String username, int checkedOut) throws DatabaseException
	{	
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				String query = "update user set  batchCheckedOut = ? where username = ?";
				stmt = db.getConnection().prepareStatement(query);
				stmt.setInt(1, checkedOut);
				stmt.setString(2, username);

				if (stmt.executeUpdate() != 1) {
					throw new DatabaseException("Could not update user");
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
	}
	
	/**keeps track of which batch a user has checked out
	 * 
	 * @param batch_id
	 * @param username
	 * @throws DatabaseException 
	 */
	public void updateBatchNumberAssigned(String username, int batch_id)
														throws DatabaseException{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
				String query = "update user set  batchNumberAssigned = ? where username = ?";
				stmt = db.getConnection().prepareStatement(query);
				stmt.setInt(1, batch_id);
				stmt.setString(2, username);

				if (stmt.executeUpdate() != 1) {
					throw new DatabaseException("Could not update user");
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
	}

}
