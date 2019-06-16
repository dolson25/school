package shared.model;

/** The container for a cell */
public class Cell {
	
	private int id;
	private int record_id;
	private int field_id;
	private String value;
	
	public Cell(int id, int record_id, int field_id, String value){
		
		setId(id);
		setRecord_id(record_id);
		setField_id(field_id);
		setValue(value);
	}

	/** gets a cell ID
	 * 
	 * @return the cell's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets a cell ID
	 * 
	 * @param id the cell's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** gets the ID that refers to the record this cell is in
	 * 
	 * @return the record ID
	 */
	public int getRecord_id() {
		return record_id;
	}

	/** sets the ID that refers to the record this cell is in
	 * 
	 * @param record_id the record ID
	 */
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	
	/** gets the ID that refers to the field this cell is in
	 * 
	 * @return the field ID
	 */
	public int getField_id() {
		return field_id;
	}

	/** sets the ID that refers to the field this cell is in
	 * 
	 * @param field_id the field ID
	 */
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	/** gets the value, or contents of the cell
	 * 
	 * @return the cell value
	 */
	public String getValue() {
		return value;
	}

	/** sets the value, or contents of the cell
	 * 
	 * @param value the cell value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
