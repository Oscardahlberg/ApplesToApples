package GameCore;

import Comms.Communication;
import Player.PlayerSocketInfo;

public class GameClient extends GameBase {

    Communication communication = new Communication(false);

    public void start(int playerCount) {
        this.playerCount = playerCount;

        setUp();

        phase();
    }

    public void setUp() {

        PlayerSocketInfo psi = communication.connectToServer();
        communication.waitOnData(psi);

    }

    public void drawGreenAppleP() {

    }

    public void submitRedAppleP() {

    }

    public void judgeWinnerP() {

    }

    public void distributeRedApplesP() {

    }
}
