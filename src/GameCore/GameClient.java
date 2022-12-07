package GameCore;

import Comms.Communication;
import Player.Player;
import Player.PlayerSocketInfo;

import java.util.ArrayList;

public class GameClient extends GameBase {

    Communication communication = new Communication(false);

    private int clientPlayerId = -1;


    public void start(int playerCount, int botCount) {
        this.playerCount = playerCount + botCount; // doesn't matter for client

        setUp();

        phase();
    }

    public void setUp() {

        PlayerSocketInfo psi = communication.connectToServer();

        String[] setupData = communication.waitForData(psi);

        handleSetupData(setupData);
    }

    public void drawGreenAppleP() {

    }

    public void submitRedAppleP() {

    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }

    private void handleSetupData(String[] setupData) { //TODO: TESTTTTTTTTTT
        ArrayList<String> hand = new ArrayList<>();
        int playerId = -1;

        int cardCount = 8;
        for(int i = 0; i < setupData.length; i++) {
            if(cardCount > 0) {
                if(!setupData[i].equals("hand")) {
                    hand.add(setupData[i]);
                }
                cardCount--;
            } else if(setupData[i].equals("playerId")) {
                playerId = Integer.parseInt(setupData[i + 1]);

            } else if(setupData[i].equals("playerCount")) {
                for(int j = 0; j < Integer.parseInt(setupData[i + 1]); j++) {
                    if(j == playerId) {
                        players.add(new Player(false, null, j, hand));
                    }
                    players.add(new Player(false, null, j, null));
                }

            } else if (setupData[i].equals("botCount")){
                for(int j = this.players.size(); j < Integer.parseInt(setupData[i + 1]); j++) {
                    players.add(new Player(true, null, j, null));
                }
            }
        }

        this.clientPlayerId = playerId;

    }

}
