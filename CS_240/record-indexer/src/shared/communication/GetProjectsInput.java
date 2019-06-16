package shared.communication;

/** a container for Get Projects Params*/
public class GetProjectsInput {
	
	private String username;
	private String password;
	
	public GetProjectsInput(String username, String password){
		
		setUsername(username);
		setPassword(password);
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

}
