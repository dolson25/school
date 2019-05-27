package model;

public class Port {
	
	private String _resource;
	private HexLocation _location;
	private String _direction;
	private int _ratio;
	
	public Port(){
		
	}

	public String get_resource() {
		return _resource;
	}

	public void set_resource(String _resource) {
		this._resource = _resource;
	}

	public HexLocation get_location() {
		return _location;
	}

	public void set_location(HexLocation _location) {
		this._location = _location;
	}

	public String get_direction() {
		return _direction;
	}

	public void set_direction(String _direction) {
		this._direction = _direction;
	}

	public int get_ratio() {
		return _ratio;
	}

	public void set_ratio(int _ratio) {
		this._ratio = _ratio;
	}
	
	

}
