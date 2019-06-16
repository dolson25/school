package shared.communication;

/** a container for Submit Batch Result*/
public class SubmitBatchOutput {
	
	private boolean validated;

	public SubmitBatchOutput(boolean validated) {
		
		setValidated(validated);
	}

	/**a check if the batch was submitted
	 * 
	 * @return true if successful
	 */
	public boolean isValidated() {
		return validated;
	}

	/**set the batch submitted status
	 * 
	 * @param validated true if successful
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	/**a string representation of the output
	 */
	@Override
	public String toString() {
		return "TRUE\n";
	}

}
