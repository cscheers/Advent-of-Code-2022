import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day2 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
//            System.out.println(line);
            String[] tokens = line.split(",");
            for (String token : tokens) {
                int value = Integer.valueOf(token);
            }
        }
    }

    static void play() {
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 2");
        readFile(new File("data/day2.txt"));
        play();
    }
}
