package model;

import shared.model.TurnTracker;

public class ClientModel {
	
	private ResourceList _bank;
	private MessageList _chat;
	private MessageList _log;
	private Map _map;
	private array<Players> _players;
	private TradeManager _tradeManager;
	private TurnTracker _turnTracker;
	private int _version;
	private int _winner;
	private int _longestRoad;
	private int _largestArmy;

	public ClientModel(){}

	/**
	 * This function is in charge of setting up the board correctly and prompting each player
	 * in setting up two settlements and two roads
	 */
	public void initializeGame(){
	}

	public array<Players> getPlayers() {
		return players;
	}

	public void setPlayers(array<Players> players) {
		this.players = players;
	}

	public TradeManager getTradeManager() {
		return tradeManager;
	}

	public void setTradeManager(TradeManager tradeManager) {
		this.tradeManager = tradeManager;
	}

	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public int getLongestRoad() {
		return longestRoad;
	}

	public void setLongestRoad(int longestRoad) {
		this.longestRoad = longestRoad;
	}

	public int getLargestArmy() {
		return largestArmy;
	}

	public void setLargestArmy(int largestArmy) {
		this.largestArmy = largestArmy;
	}

	public ResourceList get_bank() {
		return _bank;
	}

	public void set_bank(ResourceList _bank) {
		this._bank = _bank;
	}

	public MessageList get_chat() {
		return _chat;
	}

	public void set_chat(MessageList _chat) {
		this._chat = _chat;
	}

	public MessageList get_log() {
		return _log;
	}

	public void set_log(MessageList _log) {
		this._log = _log;
	}

	public Map get_map() {
		return _map;
	}

	public void set_map(Map _map) {
		this._map = _map;
	}

}
