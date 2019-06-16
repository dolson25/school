package shared.communication;

/** a container for Validate User Result*/
public class ValidateUserOutput {
	
	private String firstname;
	private String lastname;
	private int numberRecordsIndexed;
	private boolean validated;

	public ValidateUserOutput(String firstname, String lastname, 
									int numberRecordsIndexed, boolean validated){	
		setFirstname(firstname);
		setLastname(lastname);
		setNumberRecordsIndexed(numberRecordsIndexed);
		setValidated(validated);
	}

	/**gets the user's firstname
	 * 
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**sets the user's firstname
	 * 
	 * @param firstname the firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**gets the user's lastname
	 * 
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**sets the user's lastname
	 * 
	 * @param lastname the lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**gets the the number of records the user has indexed
	 * 
	 * @return the number indexed
	 */
	public int getNumberRecordsIndexed() {
		return numberRecordsIndexed;
	}

	/**sets the the number of records the user has indexed
	 * 
	 * @param numberRecordsIndexed the number indexed
	 */
	public void setNumberRecordsIndexed(int numberRecordsIndexed) {
		this.numberRecordsIndexed = numberRecordsIndexed;
	}
	
	/** a check if the user was validated
	 * 
	 * @return true if validated
	 */
	public boolean isValidated() {
		return validated;
	}

	/** set the validation state
	 * 
	 * @param validated true if validated
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	/**a string representation of the output
	 * 
	 */
	@Override
	public String toString() {
		String s = "";
		if(this.validated)
		{
			s += "TRUE\n" + this.firstname + "\n" + this.lastname + "\n"
								+ this.numberRecordsIndexed + "\n";
		}
		else
		{
			s += "FALSE\n";
		}
		
		return s;
	}

}
