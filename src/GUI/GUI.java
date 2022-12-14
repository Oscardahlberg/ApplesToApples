package GUI;

import Player.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class GUI {

    public static int validUserInput(int startRange, int endRange) {
        Scanner scanConsole = new Scanner(System.in);
        while (true) {
            String input = scanConsole.nextLine();

            int i = startRange;
            while (i <= endRange) {
                if(input.equals("" + i)) {
                    return i;
                }
                i++;
            }

            System.out.println("Incorrect input");
            System.out.println("Valid input is between and including: " + startRange + ", " + endRange);
        }
    }

    public static String chooseRedApple(ArrayList<String> hand) {
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("[" + i + "] : " + hand.get(i));
        }

        int i = GUI.validUserInput(0, 7);
        return hand.get(i);
    }

}
