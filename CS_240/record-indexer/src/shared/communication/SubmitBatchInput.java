package shared.communication;

/** a container for Submit Batch Params*/
public class SubmitBatchInput {
	
	private String username;
	private String password;
	private String batch_id;
	private String cellValues;
	
	public SubmitBatchInput(String username, String password, String batch_id,
														    String cellValues){
		setUsername(username);
		setPassword(password);
		setBatch_id(batch_id);
		setCellValues(cellValues);
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
	
	/**gets the ID of the batch
	 * 
	 * @return the batch ID
	 */
	public String getBatch_id() {
		return batch_id;
	}

	/**sets the ID of the batch 
	 * 
	 * @param batch_id the batch ID
	 */
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	
	/**gets values input by a user
	 * 
	 * @return the values
	 */
	public String getCellValues() {
		return cellValues;
	}

	/**sets values input by a user
	 * 
	 * @param cellValues the values
	 */
	public void setCellValues(String cellValues) {
		this.cellValues = cellValues;
	}

}
