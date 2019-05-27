package client;

public class Proxy {
    /**
     * URL: /user/login
     * Logs the user in and sets their catan.user HTTP cookie
     *
     * @param Username
     * @param password
     * @return responseObject
     */
    private void login(String username,String password){

    }

    /**
     * URL: /user/register
     * Registers a new user and logs them in, and sets their catan.user HTTP cookie
     *
     * @param username
     * @param password
     * @return responseObject
     */
    private void register(String username,String password){

    }

    /**
     * URL: /games/list
     * Returns information about all of the current games on the server.
     *
     * @return gamesList
     */
    private void listGames(){

    }

    /**
     * URL: /games/create
     * Creates a new game on the server
     *
     * @param name string representing the name of the game being created
     * @param randomTiles ?
     * @param randomNumbers ?
     * @param randomPorts ?
     * @return gameInformation
     */
    private void createGame(String name){

    }

    /**
     * URL: /games/join
     * Adds the player to the specified game and sets their catan.game cookie
     *
     * @param gameId
     * @return responseObject
     */
    private void joinGame(int gameId){

    }

    /**
     * URL: /games/save
     * saves the state of a game with the file name specified in the server's saves/ directory
     *
     * @param gameId
     * @param fileName
     * @return responseObject
     */
    private void saveGame(int gameId, String fileName){

    }

    /**
     * URL: /games/load
     * loads the game state from the file specified by fileName param if it exists in the server's saves/ directory
     *
     * @param fileName
     * @return gameState
     */
    private void loadGame(String fileName){

    }

    /**
     * URL: /game/model?version=N
     * Returns the state of the game as JSON.  If version in included it will check to see if the version given is the
     *  Current version.  If it is it will return true.  If not it will return the new state.
     *
     * @param version
     * @param responseObject
     * @return gameState
     */
    private void getGameState(String version){

    }

    /**
     * URL: /game/reset
     * resets the current game to the state just after placement rounds have finished
     *
     * @return gameState
     */
    private void resetGame(){

    }

    /**
     * URL: /game/commands
     * gets the list of commands that have been executed in the current game
     *
     * @return
     */
    private void getGameCommands(){

    }

    /**
     * URL: /game/commands
     * sends a command to the server to be executed
     *
     * @return
     */
    private void sendGameCommand(){

    }

    /**
     * URL: /game/listAI
     * returns a list of supported AI player types.  Currently LARGEST_ARMY is the only supported type
     *
     * @return
     */
    private void getAI(){

    }

    /**
     * URL: /game/addAI
     * adds and AI player to the current game
     *
     * @return
     */
    private void addAI(){

    }

    /**
     * URL: /util/changeLogLevel
     * sets the server's logging level
     *
     * @param level
     */
    private void changeLogLevel(String level){

    }
}
