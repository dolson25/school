package shared.communication;

/** a container for Search Params*/
public class SearchInput {
	
	private String username;
	private String password;
	private String searchFields;
	private String searchValues;
	
	public SearchInput(String username, String password, String searchFields,
															String searchValues){
		setUsername(username);
		setPassword(password);
		setSearchFields(searchFields);
		setSearchValues(searchValues);
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
	
	/**gets the id's of the fields to be searched
	 * 
	 * @return search fields
	 */
	public String getSearchFields() {
		return searchFields;
	}

	/**sets the id's of the fields to be searched
	 * 
	 * @param searchFields search fields
	 */
	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields;
	}

	/**gets the values of the fields to be searched
	 * 
	 * @return search values
	 */
	public String getSearchValues() {
		return searchValues;
	}

	/**sets the values of the fields to be searched
	 * 
	 * @param searchValues search values
	 */
	public void setSearchValues(String searchValues) {
		this.searchValues = searchValues;
	}

}
