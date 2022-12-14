package GameCore;

import Comms.ClientComms;
import Comms.Communication;
import GUI.GUI;
import Player.Player;
import Player.PlayerSocketInfo;

import java.util.ArrayList;

public class GameClient extends GameBase {

    private int clientPlayerId = -1;

    public void start(int playerCount, int botCount) {
        this.playerCount = playerCount + botCount; // doesn't matter for client

        setUp();

        phase();
    }

    public void setUp() {

        ClientComms communication = new ClientComms();
        PlayerSocketInfo psi = communication.connectToServer();

        String[] setupData = Communication.waitForData(psi);

        System.out.println("Game has begun!");

        handleSetupData(setupData, psi);
    }

    public void decideJudgeP() {
        this.judgeId = Integer.parseInt(Communication.waitForData(players.get(this.clientPlayerId).getPsi())[1]);
        System.out.println("Player " + this.judgeId + " is the judge this turn");
    }

    public void drawGreenAppleP() {
        getApple();
    }

    public void submitRedAppleP() {
        chooseRedApple();
        submitRedApple();
        //getPlayedRedApples(); //TODO: CREATE
        int i = 5;
    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }

    private void submitRedApple() {
        if(this.judgeId == this.clientPlayerId) { // if server is the judge
            return;
        }

        String msg = "RedApple;" + this.drawnRedApples.get(0);

        Communication.sendData(msg, this.players.get(this.clientPlayerId).getPsi());
    }

    private void chooseRedApple() {
        if(this.judgeId == this.clientPlayerId) { // if client is the judge
            return;
        }

        System.out.println("Which red apple describes the green apple best");
        this.drawnRedApples.add(GUI.chooseRedApple(this.players.get(this.clientPlayerId).getHand()));

    }

    private void getApple() {
        String[] msg = Communication.waitForData(players.get(this.clientPlayerId).getPsi());

        this.drawnGreenApple = msg[1];

        System.out.println("Green Apple: " + this.drawnGreenApple);
    }

    private void handleSetupData(String[] setupData, PlayerSocketInfo psi) {
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
                        players.add(new Player(false, psi, j, hand));
                        continue;
                    }
                    players.add(new Player(false, null, j, null));
                }

            } else if (setupData[i].equals("botCount")){
                int realPlayerCount = this.players.size();
                for(int j = realPlayerCount; j < (Integer.parseInt(setupData[i + 1]) + realPlayerCount); j++) {
                    players.add(new Player(true, null, j, null));
                }
            }
        }

        this.clientPlayerId = playerId;

    }

}
