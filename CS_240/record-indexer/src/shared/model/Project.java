package shared.model;

/** The container for a project */
public class Project {
	
	private int id;
	private String title;
	private int recordsPerImage;
	private int firstYCoordinate;
	private int recordHeight;
	
	public Project(int id, String title, int recordsPerImage, 
						int firstYCoordinate, int recordHeight){
		setId(id);
		setTitle(title);
		setRecordsPerImage(recordsPerImage);
		setFirstYCoordinate(firstYCoordinate);
		setRecordHeight(recordHeight);
	}

	/** gets a project ID
	 * 
	 * @return the projects's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets a project ID
	 * 
	 * @param id the project's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** gets the title, or name of this project
	 * 
	 * @return the project's title
	 */
	public String getTitle() {
		return title;
	}

	/** sets the title, or name of this project
	 * 
	 * @param title the project's title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** gets the number of records that every image in this project has
	 * 
	 * @return records per image
	 */
	public int getRecordsPerImage() {
		return recordsPerImage;
	}

	/** sets the number of records that every image in this project has
	 * 
	 * @param recordsPerImage records per image
	 */
	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}

	/** gets the y coordinate of the top of the first record of each image
	 * 
	 * @return the first Y coordinate
	 */
	public int getFirstYCoordinate() {
		return firstYCoordinate;
	}

	/** sets the y coordinate of the top of the first record of each image
	 * 
	 * @param firstYCoordinate the first y coordinate
	 */
	public void setFirstYCoordinate(int firstYCoordinate) {
		this.firstYCoordinate = firstYCoordinate;
	}

	/** gets the pixel height of the records in this project
	 * 
	 * @return the record height
	 */
	public int getRecordHeight() {
		return recordHeight;
	}

	/** sets the pixel height of the records in this project
	 * 
	 * @param recordHeight the record height
	 */
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}

}
