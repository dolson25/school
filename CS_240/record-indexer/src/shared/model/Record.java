package shared.model;

/** The container for a record */
public class Record {
	
	private int id;
	private int rowNumber;
	private int image_id;
	
	public Record(int id, int rowNumber, int image_id){
		
		setId(id);
		setRowNumber(rowNumber);
		setImage_id(image_id);
	}

	/** gets a record ID
	 * 
	 * @return the record's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets a record ID
	 * 
	 * @param id the record's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** gets the record's row number in its image
	 * 
	 * @return the record's row number
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/** sets the record's row number in its image
	 * 
	 * @param rowNumber the record's row number
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/** gets the ID that refers the the image this record is in 
	 * 
	 * @return the image ID
	 */
	public int getImage_id() {
		return image_id;
	}

	/** sets the ID that refers the the image this record is in 
	 * 
	 * @param image_id the image ID
	 */
	public void setImage_id(int image_id) {
		this.image_id = image_id;
	}

}
