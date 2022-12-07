package GameCore;

import Comms.Communication;
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

    Communication communication = new Communication(true);

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

    }

    public void submitRedAppleP() {

    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }

    private void announceJudge() {
        for(Player player: this.players) {
            if(player.getIsBot()) {
                return; // can do this because player list will be structured in a predetermined way
            }
            if(player.getPlayerId() != 0) {
                communication.sendData("judge;" + this.judgeId, player.getPsi());
            }
        }
        System.out.println("Player " + this.judgeId + " is the judge this turn");
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
            PlayerSocketInfo psi = communication.connectToClient();

            players.add(new Player(false, psi, i, null));

            communication.sendStartData(
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

    private void distributePlayerDataToClients() {

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
