package Comms;

import Player.PlayerSocketInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientComms extends Communication {
    private final String ipAddress = "127.0.0.1";

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
}
