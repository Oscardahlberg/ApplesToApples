package Comms;

import Player.PlayerSocketInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerComms extends Communication{

    private ServerSocket serverSocket;

    public ServerComms() {
        try {
            this.serverSocket = new ServerSocket(this.socket);
        } catch (IOException e){
            System.out.println("Couldn't create server");
            System.exit(0);
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
}
