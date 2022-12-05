package GUI;

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

}
