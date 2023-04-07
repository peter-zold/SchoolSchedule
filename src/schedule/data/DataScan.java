package schedule.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataScan {
    public static void main(String[] args) {
        File scheduleData = new File("src\\schedule\\data\\classes_summary.txt");
        try (Scanner scanner = new Scanner(scheduleData);) {
            while (scanner.hasNext()) {
                //String data = scanner.nextLine();
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }
    }
}

