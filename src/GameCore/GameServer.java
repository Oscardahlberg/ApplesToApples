package GameCore;

import Comms.Communication;
import Comms.ServerComms;
import GUI.GUI;
import Player.Player;
import Player.PlayerSocketInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameServer extends GameBase {

    private int botCount;

    public void start(int playerCount, int botCount) {
        this.playerCount = playerCount;
        this.botCount = botCount;

        setUp();

        phase();
    }

    public void setUp() {

        initApples();

        shuffleDeck();

        setupPlayers();

        System.out.println("Game has begun!");

        distributeHands();
    }

    public void decideJudgeP() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.exit(0);
        }
        if(this.judgeId == (this.players.size() - 1)) {
            this.judgeId = 0;
        } else {
            this.judgeId++;
        }

        announceJudge();
    }

    public void drawGreenAppleP() {
        this.drawnGreenApple = greenApples.remove(0);

        announceGreenApple();
    }

    public void submitRedAppleP() {
        botChooseCard();

        chooseRedApple();

        getPlayedRedApplesClients();
        announceRedApples();
    }

    public void judgeWinnerP() {
        int chosenRedApple;
        if(this.judgeId == 0) {
            System.out.println("Which of the red apples describe the green apple the best");
            chosenRedApple = judge();
        } else {
            System.out.println("The judge, Player " + this.judgeId + ", is choosing the best red apple...");

            if(this.players.get(this.judgeId).isBot()) {
                Random rand = new Random();
                // Here one would implement a bot to choose which red apple best
                // represents the green apple
                chosenRedApple = rand.nextInt(this.players.size() - 1);
            } else {
                chosenRedApple = receiveChosenApple();
            }
        }
        distributeChosenApple(chosenRedApple);
        int winningPlayerId = giveGreenApple(chosenRedApple);
        checkIfPlayerWon(winningPlayerId);
    }

    public void distributeRedApplesP() {
        for (Player player : this.players) {
            String redApple = redApples.remove(0);
            if(player.getPlayerId() == this.judgeId) {
                continue;
            }

            if(player.isBot() || player.getPlayerId() == 0) {
                player.receiveRedApple(redApple);
            } else {
                Communication.sendData(redApple, player.getPsi());
            }
        }
    }

    public void newTurnSetup() {
        System.out.println("--- New Turn ---");

        this.drawnRedApples.clear();
    }

    private void checkIfPlayerWon(int winningPlayerId) {
        if(this.players.get(winningPlayerId).getGreenApplesCount() == this.greenApplesWinCount) {
            if(winningPlayerId == 0) {
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
            if(player.equals(this.players.get(0))) {
                System.out.println("You have " + player.getGreenApplesCount());
            } else {
                System.out.println("Player " + player.getPlayerId() + " has " + player.getGreenApplesCount() + " green apples");
            }
        }
    }

    private int giveGreenApple(int chosenRedApple) {

        // This has to be done this way because the drawnRedApples contains 1 fewer cards than there are player
        // So I couldn't just do .get(chosenRedApple)
        for(int i = 0; i < this.players.size(); i++) {
            if(i == this.judgeId) {
                chosenRedApple += 1;
                continue;
            }
            if(chosenRedApple == i) {
                this.players.get(chosenRedApple).incrementGreenApplesCount();

                if(chosenRedApple == 0) {
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

    private void distributeChosenApple(int chosenApple) {
        String msg = ";" + chosenApple;
        for(int i = 1; i < this.drawnRedApples.size(); i++) {
            if(this.judgeId == i || this.players.get(i).isBot()) {
                continue;
            }
            Communication.sendData(msg, this.players.get(i).getPsi());
        }
    }

    private int receiveChosenApple() {
        int chosenRedApple = Integer.parseInt(Communication.waitForData(this.players.get(this.judgeId).getPsi())[0]);
        System.out.println("The judge chose: " + this.drawnRedApples.get(chosenRedApple));
        return chosenRedApple;
    }

    private int judge() {
        int chosenRedApple = GUI.validUserInput(0, this.players.size());
        System.out.println("You chose: " + this.drawnRedApples.get(chosenRedApple));
        return chosenRedApple;
    }

    private void chooseRedApple() {
        if(this.judgeId == 0) { // if server is the judge
            return;
        }
        System.out.println("Which red apple describes the green apple best");
        this.drawnRedApples.add(0, chooseApple());
    }

    private String chooseApple() {
        ArrayList<String> hand = this.players.get(0).getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("[" + i + "] : " + hand.get(i));
        }

        int i = GUI.validUserInput(0, 7);
        String card = hand.remove(i);
        this.players.get(0).setHand(hand);
        return card;
    }

    private void announceRedApples() {
        System.out.println("---- Played Red Apples ----");

        StringBuilder msg = new StringBuilder();

        for(int i = 0; i < this.drawnRedApples.size(); i++) {
            msg.append(this.drawnRedApples.get(i));
            // to fix msg format correctly for .split()
            if(0 != this.drawnRedApples.size() - 1) {
                msg.append(";");
            }
            System.out.println("[" + i + "] : " + this.drawnRedApples.get(i));
        }

        Communication.announceToClients(msg.toString(), this.players);
    }

    private void getPlayedRedApplesClients() {
        for(int i = 1; i < (this.playerCount); i++) {
            if(this.judgeId == i) {
                continue;
            }
            String redApple = Communication.waitForData(this.players.get(i).getPsi())[1];
            if(this.judgeId == 0) {
                this.drawnRedApples.add(i - 1, redApple);
            } else {
                this.drawnRedApples.add(i, redApple);
            }
        }
    }

    private void botChooseCard() {
        Random rand = new Random();
        for (Player player : this.players) {

            // Here you would add some bot function to choose one of their cards
            if(player.isBot() && (player.getPlayerId() != this.judgeId)) {
                int pick = rand.nextInt(7);
                this.drawnRedApples.add(player.getHand().remove(pick));
            }
        }
    }

    private void announceGreenApple() {
        System.out.println("Green Apple: " + this.drawnGreenApple);

        String msg = "greenApple;" + this.drawnGreenApple;

        Communication.announceToClients(msg, this.players);
    }

    private void announceJudge() {
        System.out.println("Player " + this.judgeId + " is the judge this turn");

        String msg = "judge;" + this.judgeId;

        Communication.announceToClients(msg, this.players);
    }

    private void setupPlayers() {
        players.add(new Player(false, null, 0, null)); // server player

        connectPlayersAndSendSetupData();

        for (int i = this.playerCount; i < this.botCount + this.playerCount; i++) {

            players.add(new Player(true, null, i, null));
        }
    }

    private void connectPlayersAndSendSetupData() {
        if(this.playerCount > 1) {
            System.out.println("Waiting on players to connect...");
        }

        for (int i = 1; i < this.playerCount; i++) {
            ServerComms communication = new ServerComms();
            PlayerSocketInfo psi = communication.connectToClient();

            players.add(new Player(false, psi, i, null));

            Communication.sendStartData(
                    generateHand(),
                    i,
                    this.playerCount,
                    this.botCount,
                    psi);
        }
    }

    private void distributeHands() {
        for (Player player: this.players) {
            if(player.isBot() || player.getPlayerId() == 0) {
                player.setHand(generateHand());
            }
        }
    }

    private ArrayList<String> generateHand() {
        ArrayList<String> hand = new ArrayList<>();
        for(int i = 0; i < 7; i++) {
            hand.add(redApples.remove(0));
        }
        return hand;
    }

    private void shuffleDeck() {
        Collections.shuffle(redApples);
        Collections.shuffle(greenApples);
    }

    private void initApples() {
        try {
            redApples = new ArrayList<>(Files.readAllLines(Paths.get(
                    "src/Apples/RedApples", "redApples.txt"), StandardCharsets.ISO_8859_1));
            greenApples = new ArrayList<>(Files.readAllLines(Paths.get(
                    "src/Apples/GreenApples", "greenApples.txt"), StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            System.out.println("Couldn't read cards");
            // notifyServerCrash();
            System.exit(0);
        }
    }

}
