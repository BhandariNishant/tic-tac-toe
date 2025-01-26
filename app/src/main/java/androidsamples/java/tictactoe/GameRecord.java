package androidsamples.java.tictactoe;

public class GameRecord {
    private String id;
    private String GameState;
    private int turn;
    private String FirstPlayerId;
    private String SecondPlayerId;


    public GameRecord(String firstPlayerId) {
        this.id = "";
        this.GameState = "000000000";
        this.turn = 1;
        this.FirstPlayerId = firstPlayerId;
        this.SecondPlayerId = "";
    }
    public GameRecord() {
        this.id = "";
        this.GameState = "000000000";
        this.turn = 1;
        this.FirstPlayerId = "";
        this.SecondPlayerId = "";
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getGameState() {
        return GameState;
    }

    public void setGameState(String gameState) {
        GameState = gameState;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getFirstPlayerId() {
        return FirstPlayerId;
    }

    public void setFirstPlayerId(String firstPlayerId) {
        FirstPlayerId = firstPlayerId;
    }

    public String getSecondPlayerId() {
        return SecondPlayerId;
    }

    public void setSecondPlayerId(String secondPlayerId) {
        SecondPlayerId = secondPlayerId;
    }


}
