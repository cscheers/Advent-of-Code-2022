import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.PriorityQueue;

public class Day1 {

    static void readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int sum = 0;
        Queue<Integer> queue = new PriorityQueue<Integer>();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.length() == 0) {
                queue.offer(sum);
                if (queue.size() > 3) {
                    queue.poll();
                }
                sum = 0;
            } else {
                sum += Integer.valueOf(line);
            }
        }
        // Assume last line is an empty line
        int topThree = queue.poll() + queue.poll() + queue.poll();
        System.out.println("Top 3: " + topThree);
    }

    public static void go() throws FileNotFoundException {
        System.out.println("Day 1");
        readFile(new File("data/day1.txt"));
    }
}
