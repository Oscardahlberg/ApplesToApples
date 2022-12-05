package GameCore;

import GUI.GUI;

public class GameStart {

    public GameStart() {
        // ask if client or server
        boolean isServer = askIsServer();

        if(isServer) {
            System.out.println("Server");
            GameServer gameServer = new GameServer();
            int totalPlayerCount = askPlayerCount();
            int botCount = askBotCount(totalPlayerCount);
            gameServer.start(totalPlayerCount - botCount, botCount);
        } else {
            System.out.println("Client");
            GameClient gameClient = new GameClient();
            // 0 is placeholder for when playerCount is received from server
            gameClient.start(0, 0);
        }

    }

    private boolean askIsServer() {

        System.out.println("Welcome to Apples To Apples!");
        System.out.println("Do you want to [1]host a server or [2]join one?");

        return GUI.validUserInput(1,2) == 1;
    }

    private int askPlayerCount() {

        System.out.println("What is the total amount of players for this game? " +
                "Minimum 3");

        return GUI.validUserInput(3,100);
    }

    private int askBotCount(int totalPlayerCount) {
        System.out.println("How many of those are bots? Maximum " + totalPlayerCount);

        return GUI.validUserInput(0, totalPlayerCount);
    }

}