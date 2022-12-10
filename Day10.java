import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day10 {

    static int position = 0;
    static int total = 0;

    static void nextCycle(int cycle, int X) {
        for (int i = 20; i <= 220; i += 40 ) {
            if (cycle == i) {
                total += cycle * X;
            }
        }
        if (position >= X - 1 && position <= X + 1) {
            System.out.print("#");
        } else {
            System.out.print(".");
        }
        position++;
        if (cycle % 40 == 0) {
            System.out.println();
            position = 0;
        }
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int cycle = 1, X = 1, ahead = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(" ");
            X += ahead;
            ahead = 0;
            nextCycle(cycle++, X);
            if (tokens[0].equals("addx")) {
                ahead = Integer.valueOf(tokens[1]);
                nextCycle(cycle++, X);
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 10");
        readFile(new File("data/day10.txt"));
        System.out.println("Total: " + total);
    }
}
