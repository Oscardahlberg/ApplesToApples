package GameCore;

import Comms.Communication;
import Player.Player;
import Player.PlayerSocketInfo;

import java.util.ArrayList;

public class GameServer extends GameBase {

    private int botCount;

    Communication communication = new Communication(true);

    private ArrayList<Player> players = new ArrayList<Player>();

    public void start(int playerCount, int botCount) {
        this.playerCount = playerCount;
        this.botCount = botCount;

        setUp();

        phase();
    }

    public void setUp() {

        connectPlayers();

        initApples();

        shuffleDeck();

        distributeHands();

    }

    public void drawGreenAppleP() {

    }

    public void submitRedAppleP() {

    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }

    private void connectPlayers() {
        if(this.playerCount > 0) {
            System.out.println("Waiting on players to connect...");
        }

        for (int i = 0; i < this.playerCount; i++) {
            String msg = "";
            PlayerSocketInfo psi = communication.connectToClient();

            players.add(new Player(false, psi, i, null));

            communication.sendData("", psi);
        }

        for (int i = this.playerCount - 1; i < this.botCount + this.playerCount - 1; i++) {

            players.add(new Player(true, null, i, null));
        }

    }

    private void distributeHands() {
        for (Player player: this.players) {
            ArrayList<String> hand = generateHand();

            if(!player.getIsBot()) {
                player.setHand(hand);
            } else {
                // Communication.sendCards(player.getPsi(), hand);
            }
        }
    }


    private ArrayList<String> generateHand() {
        ArrayList<String> hand = new ArrayList<>();
        hand.add("card1");
        return hand;
    }
    /*
    private void shuffleDeck() {

    }

    private void initApples() {

    }*/

}
