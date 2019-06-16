package shared.communication;

/** a container for Get Sample Image Result*/
public class GetSampleImageOutput {

	private String filepath;
	private boolean validated;

	public GetSampleImageOutput(String filepath, boolean validated){
	
		setFilepath(filepath);
		setValidated(validated);
	}

	/**gets the URL of the sample image
	 * 
	 * @return the URL
	 */
	public String getFilepath() {
		return filepath;
	}

	/**sets the URL of the sample image
	 * 
	 * @param filepath the URL
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
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
			s += filepath + "\n";
		}
		else
		{
			s += "FAILED\n";
		}
		
		return s;
	}

}
