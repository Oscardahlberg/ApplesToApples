package GameCore;

import Comms.Communication;
import Player.Player;

import java.util.ArrayList;

public abstract class GameBase {

    ArrayList<String> greenApples;
    ArrayList<String> redApples;
    int greenApplesWinCount = 3;
    int playerCount;
    int judgeId = -1;

    public ArrayList<Player> players = new ArrayList<Player>();

    abstract void start(int playerCount, int botCount);
    abstract void setUp();

    void phase() {
        decideJudgeP();
        drawGreenAppleP();
        submitRedAppleP();
        judgeWinnerP();
        distributeRedApplesP();
    }

    abstract void decideJudgeP();
    abstract void drawGreenAppleP();
    abstract void submitRedAppleP();
    abstract void judgeWinnerP();
    abstract void distributeRedApplesP();
}
