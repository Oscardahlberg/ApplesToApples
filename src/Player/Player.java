package Player;

import java.util.ArrayList;

public class Player {

    private final PlayerSocketInfo psi;
    private final int playerId;
    private final boolean isBot;
    private int greenApplesCount = 0;
    private ArrayList<String> hand = new ArrayList<>();

    public Player(boolean isBot, PlayerSocketInfo psi, int playerId, ArrayList<String> hand) {
        this.isBot = isBot;
        this.psi = psi;
        this.playerId = playerId;
        this.setHand(hand);
    }

    public boolean isBot() {
        return this.isBot;
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

    public void incrementGreenApplesCount() {
        this.greenApplesCount += 1;
    }

    public ArrayList<String> getHand() {
        return this.hand;
    }

    public void setHand(ArrayList<String> hand) {
        this.hand = hand;
    }

    public void receiveRedApple(String redApple) {
        this.hand.add(redApple);
    }

    public void clearHand() {
        this.hand = new ArrayList<>();
    }

}
