import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day02 {

    static int getScore(char opponent, char me) {
        int[] scores = {0, 6, 3, 0, 6};
        int index = opponent - 'A' - me + 'X' + 2;
        int score = scores[index] + me - 'X' + 1;
        return score;
    }

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        int totalPartOne = 0, totalPartTwo = 0;
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            char opponent = line.charAt(0);
            char me = line.charAt(2);
            totalPartOne += getScore(opponent, me);
            char[] play = {'Z', 'X', 'Y', 'Z', 'X'};
            int index = me - 'X' + opponent - 'A';
            totalPartTwo += getScore(opponent, play[index]);
        }
        System.out.println("Total part one: " + totalPartOne);
        System.out.println("Total part two: " + totalPartTwo);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 2");
        readFile(new File("data/day2.txt"));
    }
}
