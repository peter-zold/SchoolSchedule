package schedule.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataScan {
    public static void main(String[] args) {


        List<Classes> allClasses = new ArrayList<>();

        // Osztályok példányosítása
        File classesName = new File("src\\schedule\\data\\classes.txt");
        try (Scanner scanner = new Scanner(classesName);) {
            while (scanner.hasNext()) {
                allClasses.add(new Classes(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }


        // Lessonok beolvasása és a megfelelő osztályhoz hozzáadása annyiszor ahány óra van egy héten az adott tantárgyból
        for (int i = 0; i < allClasses.size();i++) {
            File scheduleData = new File("src\\schedule\\data\\classes_summary.txt");
            try (Scanner scanner = new Scanner(scheduleData);) {
                while (scanner.hasNext()) {
                    String dataLine = scanner.nextLine();
                    String[] dataOfSubject = dataLine.split(",");

                    if (allClasses.get(i).getClassName().equals(dataOfSubject[0])) {

                        for (int j = 0; j < Integer.parseInt(dataOfSubject[2]); j++) {
                            allClasses.get(i).addLessons((new Lesson(dataOfSubject[1], dataOfSubject[3], 0)));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not Found!");
                e.printStackTrace();
            }
            allClasses.get(i).setLessonsPerWeek(allClasses.get(i).getAllLessons().size());
        }


        // Az adatok tesztelése
        for (int i = 0; i < allClasses.size(); i++) {
            System.out.println(allClasses.get(i).getClassName() + " osztálynak " + allClasses.get(i).getLessonsPerWeek() + "db órája van hetente.");
        }

        TimeTable.createRandomTimeTable(allClasses.get(6));
    }
}

