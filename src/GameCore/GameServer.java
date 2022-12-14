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

        System.out.println("Game has begun!");

        setupPlayers();

        distributeHands();

    }

    public void decideJudgeP() {
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
        announceRedApples(); //TODO: BUG TEST
    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }

    private void chooseRedApple() {
        if(this.judgeId == 0) { // if server is the judge
            return;
        }

        System.out.println("Which red apple describes the green apple best");

        this.drawnRedApples.add(0, GUI.chooseRedApple(this.players.get(0).getHand()));
    }

    private void announceRedApples() {


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
            if(player.getIsBot()) {
                int pick = rand.nextInt(7);
                this.drawnRedApples.add(player.getHand().remove(pick));
                int i = 5;
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

        for (int i = this.playerCount - 1; i < this.botCount + this.playerCount - 1; i++) {

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
            if(player.getIsBot() || player.getPlayerId() == 0) {
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
            redApples = new ArrayList<String>(Files.readAllLines(Paths.get(
                    "src/Apples/RedApples", "redApples.txt"), StandardCharsets.ISO_8859_1));
            greenApples = new ArrayList<String>(Files.readAllLines(Paths.get(
                    "src/Apples/GreenApples", "greenApples.txt"), StandardCharsets.ISO_8859_1));
        } catch (IOException e) {
            System.out.println("Couldn't read cards");
            // notifyServerCrash();
            System.exit(0);
        }
    }

}
