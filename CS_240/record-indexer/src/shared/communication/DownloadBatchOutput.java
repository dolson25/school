package shared.communication;

import java.util.ArrayList;

import shared.model.Field;

/** a container for the download batch result*/
public class DownloadBatchOutput {
	
	private int batch_id;
	private int project_id;
	private ArrayList<shared.model.Field> fields;
	private String imageURL;
	private int firstYCoordinate;
	private int recordHeight;
	private int numRecords;
	private int numFields;
	private boolean validated;
	
	public DownloadBatchOutput(){
		
	}
	
	public DownloadBatchOutput(int batch_id, int project_id,
			ArrayList<Field> fields, String imageURL, int firstYCoordinate,
			int recordHeight, int numRecords, int numFields, boolean validated) {

		setBatch_id(batch_id);
		setProject_id(project_id);
		setFields(fields);
		setImageURL(imageURL);
		setFirstYCoordinate(firstYCoordinate);
		setRecordHeight(recordHeight);
		setNumRecords(numRecords);
		setNumFields(numFields);
		setValidated(validated);
	}

	/**gets the batch id
	 * 
	 * @return batch id
	 */
	public int getBatch_id() {
		return batch_id;
	}
	
	/**sets the batch id
	 * 
	 * @param batch_ id the batch id
	 */
	public void setBatch_id(int batch_id) {
		this.batch_id = batch_id;
	}
	
	/**gets the project id
	 * 
	 * @return project id
	 */
	public int getProject_id() {
		return project_id;
	}
	
	/**sets the project id
	 * 
	 * @param project_id project id
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
	/**gets the ArrayList that holds all the fields
	 * 
	 * @return the fields
	 */
	public ArrayList<shared.model.Field> getFields() {
		return fields;
	}
	
	/**sets the ArrayList that holds all the fields
	 * 
	 * @param fields the fields
	 */
	public void setFields(ArrayList<shared.model.Field> fields) {
		this.fields = fields;
	}
	
	/**gets the image URL
	 * 
	 * @return the image URL
	 */
	public String getImageURL() {
		return imageURL;
	}
	
	/**sets the image URL
	 * 
	 * @param imageURL the image URL
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	/**gets the first y coordinate
	 * 
	 * @return the first y coordinate
	 */
	public int getFirstYCoordinate() {
		return firstYCoordinate;
	}
	
	/**sets the first y coordinate
	 * 
	 * @param firstYCoordinate the first y coordinate
	 */
	public void setFirstYCoordinate(int firstYCoordinate) {
		this.firstYCoordinate = firstYCoordinate;
	}
	
	/**gets the record height
	 * 
	 * @return record height
	 */
	public int getRecordHeight() {
		return recordHeight;
	}
	
	/**sets the record height
	 * 
	 * @param recordHeight record height
	 */
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	
	/**gets the number of records total
	 * 
	 * @return total number of records
	 */
	public int getNumRecords() {
		return numRecords;
	}
	
	/**sets the number of records total
	 * 
	 * @param numRecords total number of records
	 */
	public void setNumRecords(int numRecords) {
		this.numRecords = numRecords;
	}
	
	/**gets the number of fields
	 * 
	 * @return number of fields
	 */
	public int getNumFields() {
		return numFields;
	}
	
	/**sets the number of fields
	 * 
	 * @param number of fields
	 */
	public void setNumFields(int numFields) {
		this.numFields = numFields;
	}
	
	/**a check if the batch was downloaded
	 * 
	 * @return true if successful
	 */
	public boolean isValidated() {
		return validated;
	}

	/**set the batch downloaded status
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
		
		String s = "";
		
		s += batch_id + "\n" + project_id + "\n" + imageURL + "\n"
					+ firstYCoordinate + "\n" + recordHeight + "\n"
					+ numRecords + "\n" + numFields + "\n";
			
		for(int i = 0; i < fields.size(); i++)
		{
			Field f = fields.get(i);
			s += f.getId()  + "\n" + f.getColumnNumber() + "\n" 
					+ f.getTitle() + "\n" + f.getFieldHelpPath() + 
					"\n" + f.getXCoordinate() + "\n" + f.getWidth() + "\n";
			if(!f.getKnownDataPath().equals(""))
			{
				s += f.getKnownDataPath() + "\n";
			}
		}
		
		return s;
	}
	
	

}
