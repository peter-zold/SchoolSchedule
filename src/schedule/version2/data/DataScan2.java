package schedule.version2.data;

import schedule.version1.data.Classes;
import schedule.version1.data.Lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataScan2 {

    private List<Classes> allClasses = new ArrayList<>();

    public List<Classes> getAllClasses() {
        return allClasses;
    }

    public void scanData() {

        // Osztályok példányosítása
        File classesName = new File("src\\schedule\\version2\\data\\classes.txt");
        try (Scanner scanner = new Scanner(classesName);) {
            while (scanner.hasNext()) {
                allClasses.add(new Classes(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }
        Set<String> nameOfLessons = new HashSet<>();
        int count = 0;
        // Lessonok beolvasása és a megfelelő osztályhoz hozzáadása annyiszor ahány óra van egy héten az adott tantárgyból
        for (int i = 0; i < allClasses.size(); i++) {
            File scheduleData = new File("src\\schedule\\version2\\data\\classes_summary_wGroups.txt");
            try (Scanner scanner = new Scanner(scheduleData);) {
                while (scanner.hasNext()) {
                    String dataLine = scanner.nextLine();
                    String[] dataOfSubject = dataLine.split(",");

                    if (dataOfSubject[0].contains(allClasses.get(i).getClassName())) {

                        for (int j = 0; j < Integer.parseInt(dataOfSubject[2]); j++) {
                            allClasses.get(i).addLessons((new Lesson(dataOfSubject[0].substring(allClasses.get(i).getClassName().length()), dataOfSubject[1], dataOfSubject[3], 0)));
                        }
                        if (nameOfLessons.add(dataOfSubject[1])) {
                            count = count + Integer.parseInt(dataOfSubject[2]);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not Found!");
                e.printStackTrace();
            }
            allClasses.get(i).setLessonsPerWeek(count);
            count = 0;
            nameOfLessons.removeAll(nameOfLessons);
        }

        // Az adatok tesztelése
        for (int i = 0; i < allClasses.size(); i++) {
            System.out.println(allClasses.get(i).getClassName() + " osztálynak " + allClasses.get(i).getLessonsPerWeek() + "db órája van hetente.");
        }


        for (int i = 0;i<allClasses.size();i++) {
            TimeTable2.createRandomTimeTable(allClasses.get(i));
        }

    }
}


