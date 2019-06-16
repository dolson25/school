package shared.model;

/** The container for an image */
public class Image {
	
	private int id;
	private String filepath;
	private int project_id;
	private int available;

	
	public Image(int id, String filepath, int project_id, int available){
						
		setId(id);
		setFilepath(filepath);
		setProject_id(project_id);
		setAvailable(available);
	}

	/** gets an image ID
	 * 
	 * @return the images's unique ID
	 */
	public int getId() {
		return id;
	}

	/** sets an image ID
	 * 
	 * @param id the images's database ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/** gets the file location of the image
	 * 
	 * @return the the image's filepath
	 */
	public String getFilepath() {
		return filepath;
	}

	/** sets the file location of the image
	 * 
	 * @param filepath the image's filepath
	 */
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	/** gets the ID that refers to the image's project 
	 * 
	 * @return the project ID
	 */
	public int getProject_id() {
		return project_id;
	}

	/** sets the ID that refers to the image's project 
	 * 
	 * @param project_id the project ID
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}

	/** a check if this image is available for a user
	 * 
	 * @return true if the image is available
	 */
	public int getAvailable() {
		return available;
	}

	/** sets an images availability status
	 * 
	 * @param available true if the image is available
	 */
	public void setAvailable(int available) {
		this.available = available;
	}
}
