package server.database.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import server.database.DatabaseException;
import shared.model.Project;
import server.database.Database;

/** deals with the Project table in the database*/
public class ProjectDAO {

	private Database db;
	
	public ProjectDAO(Database db) {
		this.db = db;
	}
	
	/**reads and retrieves one project from database, if successful
	 * 
	 * @param project_id the project id
	 * @return a project object(that rhymes)
	 * @throws DatabaseException 
	 */
	public Project getProject(int project_id) throws DatabaseException
	{		
		Project project = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String query = "select* from project where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project_id);

			rs = stmt.executeQuery();
			
			if(rs.next()) {
				String title = rs.getString(2);
				int recordsPerImage = rs.getInt(3);
				int firstYCoordinate = rs.getInt(4);
				int recordHeight = rs.getInt(5);

				project = new Project(project_id, title, recordsPerImage, 
						                                 firstYCoordinate, recordHeight);
			}
			else
			{
				return null;
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
		
		return project;
	}
	
	/**reads and retrieves all the projects from database, if successful
	 * 
	 * @return all projects id/title
	 * @throws DatabaseException 
	 */
	public ArrayList<ArrayList<String>> getProjects() throws DatabaseException
	{			
			ArrayList<ArrayList<String>> projects = new ArrayList<ArrayList<String>>();
			
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				String query = "select id, title from project";
				stmt = db.getConnection().prepareStatement(query);

				rs = stmt.executeQuery();
				while (rs.next()) {
					ArrayList<String> project = new ArrayList<String>();
					
					project.add(rs.getString(1));
					project.add(rs.getString(2));

					projects.add(project);
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
			
			return projects;	
	}
	
	/**populates the Project table with a project
	 * 
	 * @param project the project to be added
	 * @throws DatabaseException 
	 */
	public int add(Project project) throws DatabaseException
	{
		int id = 0;
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into project (title, recordsPerImage, "
							+ "firstYCoordinate, recordHeight) values (?, ?, ?, ?)";
			
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getTitle());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoordinate());
			stmt.setInt(4, project.getRecordHeight());

			
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				id = keyRS.getInt(1);
				project.setId(id);
			}
			else {
				throw new DatabaseException("Could not insert project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not insert project", e);
		}
		finally {
			Database.safeClose(stmt);
			Database.safeClose(keyRS);
		}
		
		return id;
	}

}
