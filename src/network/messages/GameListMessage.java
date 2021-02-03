package network.messages;

import java.util.ArrayList;

public class GameListMessage extends Message{
    private ArrayList<Integer> gameList;
    public GameListMessage(char type, ArrayList<Integer> gameList){
        super(type);
        this.gameList = gameList;
    }

    public ArrayList<Integer> getGameList() {
        return gameList;
    }

    public void setGameList(ArrayList<Integer> gameList) {
        this.gameList = gameList;
    }
}
