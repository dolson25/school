package shared.model;

import shared.model.ResourceEnum;

import HexLocation;

public class Hex {
	
	private HexLocation _location;
	private ResourceEnum.Resource _resource;
	private int _number;
	
	public Hex(){
	}

	public HexLocation get_location() {
		return _location;
	}

	public void set_location(HexLocation _location) {
		this._location = _location;
	}
	
	public ResourceEnum.Resource get_resource() {
		return _resource;
	}

	public void set_resource(ResourceEnum.Resource _resource) {
		this._resource = _resource;
	}

	public int get_number() {
		return _number;
	}

	public void set_number(int _number) {
		this._number = _number;
	}

}
