package Comms;

import Player.Player;
import Player.PlayerSocketInfo;

import java.io.IOException;
import java.util.ArrayList;

public class Communication {
    public final int socket = 2048;

    public static String[] waitForData(PlayerSocketInfo psi) {
        String data = "";

        try {
            data = (String) psi.getObjectInputStream().readObject();
        } catch(Exception e) {
            System.out.println("Could not get msg");
            // handle
            System.exit(0);
        }
        return data.split(":")[1].split(";");
    }

    public static void sendData(String msg, PlayerSocketInfo psi) {
        try {
            psi.getObjectOutputStream().writeObject("data:" + msg);
        } catch (IOException e) {
            System.out.println("couldnt connect or smt idk3");
            // notify disconnect and handle it
            System.exit(0);
        }
    }

    public static void sendStartData(ArrayList<String> hand,
                              int playerId,
                              int players,
                              int bots,
                              PlayerSocketInfo psi) {
        String msg = "hand;";
        for (String card : hand) {
            msg += card + ";";
        }
        msg += "playerId;" + playerId;
        msg += ";playerCount;" + players;
        msg += ";botCount;" + bots;
        sendData(msg, psi);
    }

    public static void announceToClients(String msg, ArrayList<Player> players) {
        for(Player player: players) {
            if(player.getIsBot()) {
                return; // can do this because player list will be structured in a predetermined way
            }
            if(player.getPlayerId() != 0) {
                sendData(msg, player.getPsi());
            }
        }
    }
}
