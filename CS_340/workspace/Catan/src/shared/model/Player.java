package shared.model;

/**
 * Created by Home on 9/18/2016.
 */
public class Player {
    private int _cities;
    private String _color;
    private boolean _discarded;
    private int _monuments;
    private String _name;
    private DevCardList _newDevCards;
    private DevCardList _oldDevCards;
    private boolean _playedDevCard;
    private int _playerId;
    private ResourceList _resources;
    private int _roads;
    private int _settlements;
    private int _soldiers;
    private int _victory;

    public Player(){}

    public boolean canBuyDevCard(){}

    public boolean canBuildRoad(){}

    public boolean canBuildSettlement(){}

    public boolean canBuildCity(){}

    public boolean canPlayDevCard(){}

    public void buyDevCard(){}

    public void buildRoad(){}

    public void buildCity(){}

    public void buildSettlement(){}

    public void incrementVictoryPoints(int num){}

    public ResourceType loseResource(){}

    public void discardResource(ResourceType type){}

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DevCardList getNewDevCards() {
        return newDevCards;
    }

    public void setNewDevCards(DevCardList newDevCards) {
        this.newDevCards = newDevCards;
    }

    public DevCardList getOldDevCards() {
        return oldDevCards;
    }

    public void setOldDevCards(DevCardList oldDevCards) {
        this.oldDevCards = oldDevCards;
    }

    public boolean isPlayedDevCard() {
        return playedDevCard;
    }

    public void setPlayedDevCard(boolean playedDevCard) {
        this.playedDevCard = playedDevCard;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public ResourceList getResources() {
        return resources;
    }

    public void setResources(ResourceList resources) {
        this.resources = resources;
    }

    public int getRoads() {
        return roads;
    }

    public void setRoads(int roads) {
        this.roads = roads;
    }

    public int getSettlements() {
        return settlements;
    }

    public void setSettlements(int settlements) {
        this.settlements = settlements;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }

    public int getVictory() {
        return victory;
    }

    public void setVictory(int victory) {
        this.victory = victory;
    }
}
