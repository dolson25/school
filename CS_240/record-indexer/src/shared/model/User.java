package shared.model;

/** The container for a user */
public class User {
	
	private int id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String email;
	private int numberRecordsIndexed;
	private int batchCheckedOut;
	private int batchNumberAssigned;
	
	public User(){
		
	}
	
	public User(int id, String username, String password, String firstname, 
						String lastname, String email, int numberRecordsIndexed, 
						          int batchCheckedOut, int batchNumberAssigned){
		setId(id);
		setUsername(username);
		setPassword(password);
		setFirstname(firstname);
		setLastname(lastname);
		setEmail(email);
		setNumberRecordsIndexed(numberRecordsIndexed);
		setBatchCheckedOut(batchCheckedOut);
		setBatchNumberAssigned(batchNumberAssigned);
	}

	public User(int id2, Object username2, Object password2, Object firstname2,
			Object lastname2, Object email2, int numberRecordsIndexed2, int i) {
	}

	/** gets a user ID
	 * 
	 * @return the user's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets a user ID
	 * 
	 * @param id the user's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/** gets a user's username
	 * 
	 * @return the user's username
	 */
	public String getUsername(){
		return username;
	}
	
	/** sets a user's username
	 * 
	 * @param username the user's username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** gets a user password
	 * 
	 * @return the user's password
	 */
	public String getPassword() {
		return password;
	}

	/** sets a user password
	 * 
	 * @param password the user's password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** gets a user first name
	 * 
	 * @return the user's first name
	 */
	public String getFirstname() {
		return firstname;
	}

	/** sets a user first name
	 * 
	 * @param firstname the user's first name
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/** gets a user last name
	 * 
	 * @return the user's last name
	 */
	public String getLastname() {
		return lastname;
	}

	/** sets a user last name
	 * 
	 * @param lastname the user's last name
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/** gets a user email
	 * 
	 * @return the user's email
	 */
	public String getEmail() {
		return email;
	}

	/** sets a user email
	 * 
	 * @param email the user's email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** gets the number of records a user has indexed
	 * 
	 * @return the user's number of records indexed
	 */
	public int getNumberRecordsIndexed() {
		return numberRecordsIndexed;
	}

	/** sets the number of records this user has indexed
	 * 
	 * @param numberRecordsIndexed the user's number of records indexed
	 */
	public void setNumberRecordsIndexed(int numberRecordsIndexed) {
		this.numberRecordsIndexed = numberRecordsIndexed;
	}

	/** a check if the user already is working on a batch
	 * 
	 * @return true if the user has a batch checked out
	 */
	public int getBatchCheckedOut() {
		return batchCheckedOut;
	}

	/** sets a user's batch-checked-out status
	 * 
	 * @param batchCheckedOut true if a batch is checked out
	 */
	public void setBatchCheckedOut(int batchCheckedOut) {
		this.batchCheckedOut = batchCheckedOut;
	}
	
	/** gets the batch number a user has checked out
	 * 
	 * @return the user's assigned batch number
	 */
	public int getBatchNumberAssigned() {
		return batchNumberAssigned;
	}

	/** sets the batch number a user has checked out
	 * 
	 * @param batchNumberAssigned the user's assigned batch number
	 */
	public void setBatchNumberAssigned(int batchNumberAssigned) {
		this.batchNumberAssigned = batchNumberAssigned;
	}
	

}
