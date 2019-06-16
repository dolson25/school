package shared.model;

/** The container for a field */
public class Field {
	
	private int id;
	private String title;
	private int xCoordinate;
	private int width;
	private String fieldHelpPath;
	private String knownDataPath;
	private int project_id;
	private int columnNumber;
	
	public Field(){
		
	}

	public Field(int id, String title, int xCoordinate, int width, String fieldHelpPath, 
								String knownDataPath, int project_id, int columnNumber){
		setId(id);
		setTitle(title);
		setXCoordinate(xCoordinate);
		setWidth(width);
		setFieldHelpPath(fieldHelpPath);
		setKnownDataPath(knownDataPath);
		setProject_id(project_id);
		setColumnNumber(columnNumber);
	}

	/** gets a field ID
	 * 
	 * @return the fields's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets a field ID
	 * 
	 * @param id the field's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** gets the title, or name of this field
	 * 
	 * @return the fields's title
	 */
	public String getTitle() {
		return title;
	}

	/** sets the title, or name of this field
	 * 
	 * @param title the fields's title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/** gets the pixel x coordinate of the field in the image
	 * 
	 * @return the field's x coordinate
	 */
	public int getXCoordinate() {
		return xCoordinate;
	}

	/** sets the pixel x coordinate of the field in the image
	 * 
	 * @param xCoordinate the field's x coordinate
	 */
	public void setXCoordinate(int xCoordinate) {
		;
		this.xCoordinate = xCoordinate;
	}

	/** gets the pixel width of the field
	 * 
	 * @return the field's width
	 */
	public int getWidth() {
		return width;
	}

	/** sets the pixel width of the field
	 * 
	 * @param width the field's width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/** gets the file path to the help for this field
	 * 
	 * @return the fields's help file path
	 */
	public String getFieldHelpPath() {
		return fieldHelpPath;
	}

	/** sets the file path to the help for this field
	 * 
	 * @param fieldHelpPath the fields's help file path
	 */
	public void setFieldHelpPath(String fieldHelpPath) {
		this.fieldHelpPath = fieldHelpPath;
	}

	/** gets the file path to the known data for this field
	 * 
	 * @return the fields's known data file path
	 */
	public String getKnownDataPath() {
		return knownDataPath;
	}

	/** sets the file path to the known data for this field
	 * 
	 * @param knownDataPath the fields's known data file path
	 */
	public void setKnownDataPath(String knownDataPath) {
		this.knownDataPath = knownDataPath;
	}

	/** gets the ID that refers to that field's project
	 * 
	 * @return the project ID
	 */
	public int getProject_id() {
		return project_id;
	}

	/** sets the ID that refers to that field's project
	 * 
	 * @param project_id the project ID
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	/** gets the column location of the field in the image
	 * 
	 * @return the column number
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/** sets the column location of the field in the image
	 * 
	 * @param columnNumber the column number
	 */
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

}
