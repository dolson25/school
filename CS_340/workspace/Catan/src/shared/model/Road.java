package model;

import shared.model.DirectionEnum;

public class Road {
	private int _owner;
	private HexLocation _location;
	private DirectionEnum.Direction direction;
	
	public Road(){
	}

	public int get_owner() {
		return _owner;
	}

	public void set_owner(int _owner) {
		this._owner = _owner;
	}

	public HexLocation get_location() {
		return _location;
	}

	public void set_location(HexLocation _location) {
		this._location = _location;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
