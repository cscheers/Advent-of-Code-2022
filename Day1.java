import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day1 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int value = Integer.valueOf(line);
            System.out.println("Calories:", value);
        }
        System.out.println("Number of increasing measurements: " + increasing);
        System.out.println("Number of increasing sliding window measurements: " + increasingWindow);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 1");
        readFile(new File("data/day1.txt"));
    }
}
