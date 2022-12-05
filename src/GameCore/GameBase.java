package GameCore;

import Comms.Communication;

import java.util.ArrayList;

public abstract class GameBase {

    ArrayList<String> greenApples;
    ArrayList<String> redApples;
    int greenApplesWinCount;
    int playerCount;

    abstract void start(int playerCount);
    abstract void setUp();

    void phase() {
        drawGreenAppleP();
        submitRedAppleP();
        judgeWinnerP();
        distributeRedApplesP();
    }

    // Phase methods has a suffix "P"
    abstract void drawGreenAppleP();
    abstract void submitRedAppleP();
    abstract void judgeWinnerP();
    abstract void distributeRedApplesP();
}
