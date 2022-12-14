package GameCore;

import Player.Player;

import java.util.ArrayList;

public abstract class GameBase {

    ArrayList<String> greenApples;
    ArrayList<String> redApples;

    // Tracks current turn
    ArrayList<String> drawnRedApples = new ArrayList<>();
    String drawnGreenApple;

    int greenApplesWinCount = 3;
    int playerCount;
    int judgeId = -1;

    public ArrayList<Player> players = new ArrayList<Player>();
    abstract void start(int playerCount, int botCount);
    abstract void setUp();

    void phase() {
        while(true) {
            decideJudgeP();
            drawGreenAppleP();
            submitRedAppleP();
            judgeWinnerP();
            distributeRedApplesP();
        }
    }

    abstract void decideJudgeP();
    abstract void drawGreenAppleP();
    abstract void submitRedAppleP();
    abstract void judgeWinnerP();
    abstract void distributeRedApplesP();
}
