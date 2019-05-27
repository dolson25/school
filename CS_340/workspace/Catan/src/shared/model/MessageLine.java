package shared.model;

public class MessageLine {
	
	private String _message;
	private String _source;
	
	public MessageLine(){
		
	}

	public String get_message() {
		return _message;
	}

	public void set_message(String _message) {
		this._message = _message;
	}

	public String get_source() {
		return _source;
	}

	public void set_source(String _source) {
		this._source = _source;
	}
}
