package shared.model;

import java.util.ArrayList;

/**
 * Created by Home on 9/18/2016.
 */
public class TurnTracker {
    private int _whosTurn;
    private Map _map;
    private MessageList _log;
    private ResourceList _bank;
    private ArrayList<Player> _players;

    public TurnTracker() {}

    public int getWhosTurn() {
        return whosTurn;
    }

    public void setWhosTurn(int whosTurn) {
        this.whosTurn = whosTurn;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public MessageList getLog() {
        return log;
    }

    public void setLog(MessageList log) {
        this.log = log;
    }

    public ResourceList getBank() {
        return bank;
    }

    public void setBank(ResourceList bank) {
        this.bank = bank;
    }

    public void finishTurn(){}

    public void buyDevCard(){}

    public boolean canBuyDevCard(){}

    public boolean canBuildRoad(){}

    public boolean canBuildSettlement(){}

    public boolean canbuildCity(){}

    public boolean canPlayDevCard(){}

    public void playDevCard(DevCardType){}

    public void buyDevCard(){}

    public void buildRoad(){}

    public void buildCity(){}

    public void buildSettlement(){}

    public void assignLargestArmy(){}

    public void assignLongestRoad(){}

    public void moveRobber(HexLocation){}

    public void stealResource(int){}

    public void rolledSeven(){}

    public void rollDice(){}

    public void collectResources(){}

    public void makeTrade(){}

}
