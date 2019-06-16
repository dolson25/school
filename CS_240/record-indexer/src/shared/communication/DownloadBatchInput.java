package shared.communication;

/** a container for Download Batch Params*/
public class DownloadBatchInput {
	
	private String username;
	private String password;
	private int project_id;
	
	public DownloadBatchInput(String username, String password, int project_id){
		
		setUsername(username);
		setPassword(password);
		setProject_id(project_id);

	}

	/**gets the user's username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**sets the user's username
	 * 
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**gets the user's password
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**sets the user's password
	 * 
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**gets the ID of the project the batch will come from 
	 * 
	 * @return the project ID
	 */
	public int getProject_id() {
		return project_id;
	}

	/**sets the ID of the project the batch will come from 
	 * 
	 * @param project_id the project ID
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

}
