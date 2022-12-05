package GameCore;

import Comms.Communication;
import Player.Player;
import Player.PlayerSocketInfo;

public class GameServer extends GameBase {

    Communication communication = new Communication(true);

    public void start(int playerCount) {
        this.playerCount = playerCount;

        setUp();

        phase();
    }

    public void setUp() {

        if(this.playerCount > 1) { // TODO: CHANGE
            for (int i = 0; i < this.playerCount; i++) {
                String msg = "";
                PlayerSocketInfo psi = communication.connectToClient();

                Player player = new Player(psi, i);

                communication.sendData("", psi);
            }
        }

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
