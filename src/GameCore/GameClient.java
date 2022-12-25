package GameCore;

import Comms.ClientComms;
import Comms.Communication;
import GUI.GUI;
import Player.Player;
import Player.PlayerSocketInfo;

import java.util.ArrayList;
import java.util.Arrays;

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
        String[] msg = Communication.waitForData(players.get(this.clientPlayerId).getPsi());
        this.judgeId = Integer.parseInt(msg[1]);
        System.out.println("Player " + this.judgeId + " is the judge this turn");
    }

    public void drawGreenAppleP() {
        getApple();
    }

    public void submitRedAppleP() {
        chooseRedApple();
        submitRedApple();
        getPlayedRedApples();
    }

    public void judgeWinnerP() {
        int chosenRedApple;
        if(this.judgeId == this.clientPlayerId) {
            System.out.println("Which of the red apples describe the green apple the best");
            chosenRedApple = judge();
            distributeChosenApple(chosenRedApple);
        } else {
            System.out.println("The judge, Player " + this.judgeId + ", is choosing the best red apple...");
            chosenRedApple = receiveChosenApple();
        }
        int winningPlayerId = giveGreenApple(chosenRedApple);
        checkIfPlayerWon(winningPlayerId);
    }

    public void distributeRedApplesP() {
        if(this.judgeId != this.clientPlayerId) {
            String[] redApple = Communication.waitForData(this.players.get(this.clientPlayerId).getPsi());
            this.players.get(this.clientPlayerId).receiveRedApple(redApple[0]);
        }
    }

    public void newTurnSetup() {
        System.out.println("--- New Turn ---");

        this.drawnRedApples.clear();
    }

    private void checkIfPlayerWon(int winningPlayerId) {
        if(this.players.get(winningPlayerId).getGreenApplesCount() == this.greenApplesWinCount) {
            if(winningPlayerId == this.clientPlayerId) {
                System.out.println("You have won!");
            } else {
                System.out.println("Player " + winningPlayerId + " has won!");
            }
            System.exit(0);
        } else {
            printStats();
        }
    }

    private void printStats() {
        System.out.println("--- Current Green Apples ---");
        for(Player player : this.players) {
            if(player.equals(this.players.get(this.clientPlayerId))) {
                System.out.println("You have " + player.getGreenApplesCount());
            } else {
                System.out.println("Player " + player.getPlayerId() + " has " + player.getGreenApplesCount() + " green apples");
            }
        }
    }

    private int giveGreenApple(int chosenRedApple) {

        // This has to be done this way because the drawnRedApples contains 1 less cards than there are player
        // So I couldn't just do .get(chosenRedApple)
        for(int i = 0; i < this.players.size(); i++) {
            if(i == this.judgeId) {
                chosenRedApple += 1;
                continue;
            }
            if(chosenRedApple == i) {
                this.players.get(chosenRedApple).incrementGreenApplesCount();

                if(chosenRedApple == this.clientPlayerId) {
                    System.out.println("You have received a green apple!");
                    System.out.println("You now have "
                            + this.players.get(chosenRedApple).getGreenApplesCount() + " green apples");
                } else {
                    System.out.println("Player " + chosenRedApple + " has received a green apple!");
                    System.out.println("Player " + chosenRedApple + " now has "
                            + this.players.get(chosenRedApple).getGreenApplesCount() + " green apples");
                }
                break;
            }
        }
        return chosenRedApple;
    }

    private void distributeChosenApple(int chosenRedApple) {
        String msg = "" + chosenRedApple;
        Communication.sendData(msg, this.players.get(this.clientPlayerId).getPsi());
    }

    private int receiveChosenApple() {
        int chosenRedApple = Integer.parseInt(Communication.waitForData(this.players.get(this.clientPlayerId).getPsi())[1]);
        System.out.println("The judge chose: " + this.drawnRedApples.get(chosenRedApple));
        return chosenRedApple;
    }

    private int judge() {
        int chosenRedApple = GUI.validUserInput(0, this.players.size() - 1);
        System.out.println("You chose: " + this.drawnRedApples.get(chosenRedApple));
        return chosenRedApple;
    }

    private void getPlayedRedApples() {
        this.drawnRedApples.clear();
        String[] data = Communication.waitForData(this.players.get(this.clientPlayerId).getPsi());
        this.drawnRedApples.addAll(Arrays.asList(data));

        System.out.println("---- Played Red Apples ----");

        for(int i = 0; i < this.drawnRedApples.size(); i++) {
            System.out.println("[" + i + "] : " + this.drawnRedApples.get(i));
        }
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
        this.drawnRedApples.add(chooseApple());
    }

    private String chooseApple() {
        ArrayList<String> hand = this.players.get(this.clientPlayerId).getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("[" + i + "] : " + hand.get(i));
        }

        int i = GUI.validUserInput(0, 7);
        String card = hand.remove(i);
        this.players.get(this.clientPlayerId).setHand(hand);
        return card;
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
