package Comms;

import Player.PlayerSocketInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Communication {

    private final int socket = 2048;
    private final String ipAddress = "127.0.0.1";

    private ServerSocket serverSocket;

    public Communication(boolean isServer) {
        if(isServer) {
            try {
                this.serverSocket = new ServerSocket(this.socket);
            } catch (IOException e) {
                System.out.println("couldnt create server");
                System.exit(0);
            }
        }
    }

    public PlayerSocketInfo connectToClient() {
        try {
            Socket socket = serverSocket.accept();
            ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());

            return new PlayerSocketInfo(socket, inFromClient, outToClient);
        } catch (IOException e) {
            System.out.println("couldnt connect or smt idk1");
            System.exit(0);
        }
        return null;
    }

    public PlayerSocketInfo connectToServer() {
        try {
            Socket aSocket = new Socket(this.ipAddress, this.socket);
            ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
            ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());

            return new PlayerSocketInfo(aSocket, inFromServer, outToServer);
        } catch (IOException e) {
            System.out.println("couldnt connect or smt idk2");
            System.exit(0);
        }
        return null;
    }

    // TODO: ADD MSG HANDLER
    public String[] waitForData(PlayerSocketInfo psi) {
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

    public void sendData(String msg, PlayerSocketInfo psi) {
        try {
            psi.getObjectOutputStream().writeObject("data:" + msg);
        } catch (IOException e) {
            System.out.println("couldnt connect or smt idk3");
            // notify disconnect and handle it
            System.exit(0);
        }
    }

    public void sendStartData(ArrayList<String> hand,
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

}
