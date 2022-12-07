import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Day06 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner fs = new Scanner(file);
        while(fs.hasNextLine()) {
            String line = fs.nextLine();
            findFirstMarker(line, 4);
            findFirstMarker(line, 14);
        }
    }

    static int findFirstMarker(String stream, int window) {
        int[] charCounts = new int[26];
        int left = 0, right = 0;
        while (true) {
            if (charCounts[stream.charAt(right) - 'a'] == 0) {
                charCounts[stream.charAt(right++) - 'a']++;
                if (right - left == window) {
                    System.out.println("First marker for window size: " + window + " at: " + right);
                    return right;
                }
            } else {
                charCounts[stream.charAt(left++) - 'a']--;
            }
        }
    }

    public static void go() throws FileNotFoundException {
        System.out.println("=> Day 6");
        readFile(new File("data/day6.txt"));
    }
}
