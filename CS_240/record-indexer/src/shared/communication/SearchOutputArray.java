package shared.communication;

/** a container the parts of a search result*/
public class SearchOutputArray {
	
	private String batch_id;
	private String image_url;
	private String record_number;
	private String field_id;
	
	public SearchOutputArray(String batch_id, String image_url, String record_number,
																  String field_id) {
		
		setBatch_id(batch_id);
		setImage_url(image_url);
		setRecord_number(record_number);
		setField_id(field_id);

	}
	
	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getRecord_number() {
		return record_number;
	}

	public void setRecord_number(String record_number) {
		this.record_number = record_number;
	}

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}
}
