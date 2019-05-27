package shared.model;

public class VertexObject {
	
	//!!!!!! type index not int
	private int _owner;
	private EdgeLocation _location;
	
	public VertexObject(){
		
	}

	public int get_owner() {
		return _owner;
	}

	public void set_owner(int _owner) {
		this._owner = _owner;
	}

	public EdgeLocation get_location() {
		return _location;
	}

	public void set_location(EdgeLocation _location) {
		this._location = _location;
	}
	
	
}
