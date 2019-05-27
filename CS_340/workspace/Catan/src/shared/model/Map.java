package model;

import shared.model.DirectionEnum;
import shared.model.Robber;

import java.util.ArrayList;

public class Map {

	private ArrayList<Hex> _hexes;
	private ArrayList<Port> _ports;
	private ArrayList<Road> _roads;
	private ArrayList<VertexObject> _settlements;
	private ArrayList<VertexObject> _cities;
	private int _radius;
	private Robber _robber;

	public Map(){
	}
	/*
	Places a road at a specified direction on a specified Hex for a specified player
	@param dir The cardinal direction the road starts on (continues clockwise)
	@param player The player that owns the road
	@param location The location of the Hex that the road is on
	 */
	public void placeRoad(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
    Places a settlement at a specified direction on a specified Hex for a specified player
    @param dir The cardinal direction the settlement starts on
    @param player The player that owns the settlement
    @param location The location of the Hex that the settlement is on
    */
	public void placeSettlement(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
	Places a city at a specified direction on a specified Hex for a specified player
	@param dir The cardinal direction the city starts on
	@param player The player that owns the city
	@param location The location of the Hex that the city is on
	 */
	public void placeCity(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
	@param dir The cardinal direction the city starts on
	@param player The player that owns the city
	@param location The location of the Hex that the city is on
	@return whether or not a city can be placed at the specified location by the specified player
	 */
	public boolean canPlaceCity(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
	@param dir The cardinal direction the road starts on
	@param player The player that owns the road
	@param location The location of the Hex that the road is on
	@return whether or not a road can be placed at the specified location by the specified player
	 */
	public boolean canPlaceRoad(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
	@param dir The cardinal direction the settlement starts on
	@param player The player that owns the settlement
	@param location The location of the Hex that the settlement is on
	@return whether or not a settlement can be placed at the specified location by the specified player
	 */
	public boolean canPlaceSettlement(DirectionEnum.Direction dir, int player, HexLocation location){}

	/*
	Moves the robber
	@param location The location of the Hex the robber is to be moved to
	 */
	public void moveRobber(HexLocation location){}

	/*
	@param player The index of the player we are checking for
	@return whether or not this player has a settlement or city built on a port
	 */
	public boolean hasMarinetimeTrade(int player){}

	public ArrayList<Hex> get_hexes() {
		return _hexes;
	}

	public void set_hexes(ArrayList<Hex> _hexes) {
		this._hexes = _hexes;
	}

	public ArrayList<Port> get_ports() {
		return _ports;
	}

	public void set_ports(ArrayList<Port> _ports) {
		this._ports = _ports;
	}

	public ArrayList<Road> get_roads() {
		return _roads;
	}

	public void set_roads(ArrayList<Road> _roads) {
		this._roads = _roads;
	}

	public ArrayList<VertexObject> get_settlements() {
		return _settlements;
	}

	public void set_settlements(ArrayList<VertexObject> _settlements) {
		this._settlements = _settlements;
	}

	public ArrayList<VertexObject> getB_cities() {
		return b_cities;
	}

	public void setB_cities(ArrayList<VertexObject> b_cities) {
		this.b_cities = b_cities;
	}

	public int get_radius() {
		return _radius;
	}

	public void set_radius(int _radius) {
		this._radius = _radius;
	}

	public HexLocation get_robber() {
		return _robber;
	}

	public void set_robber(HexLocation _robber) {
		this._robber = _robber;
	}
}
