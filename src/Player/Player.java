package Player;

import java.util.ArrayList;

public class Player {

    private final PlayerSocketInfo psi;
    private final int playerId;
    private int greenApplesCount = 0;
    private String[] hand = new String[7];

    public Player(PlayerSocketInfo psi, int playerId, String[] hand) {
        this.psi = psi;
        this.playerId = playerId;
        this.setHand(hand);
    }

    public int getPlayerId() {
        return playerId;
    }

    public PlayerSocketInfo getPsi() {
        return this.psi;
    }

    public int getGreenApplesCount() {
        return this.greenApplesCount;
    }

    public void setGreenApplesCount(int greenApplesCount) {
        this.greenApplesCount = greenApplesCount;
    }

    public String[] getHand() {
        return this.hand;
    }

    public void setHand(String[] hand) {
        this.hand = hand;
    }

    public void clearHand() {
        this.hand = new String[7];
    }

}
