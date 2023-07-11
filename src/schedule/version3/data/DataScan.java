package schedule.version3.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataScan {

    private List<Classes> allClasses = new ArrayList<>();
    private int[] allClassesGrades;

    public List<Classes> getAllClasses() {
        return allClasses;
    }
    public int[] getAllClassesGrades() {
        return allClassesGrades;
    }

    public void scanData() {

        // Osztályok példányosítása
        File classesName = new File("src\\schedule\\version3\\data\\classes.txt");
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
            File scheduleData = new File("src\\schedule\\version3\\data\\classes_summary_wGroups.txt");
            try (Scanner scanner = new Scanner(scheduleData);) {
                while (scanner.hasNext()) {
                    String dataLine = scanner.nextLine();
                    String[] dataOfSubject = dataLine.split(",");

                    if (dataOfSubject[0].contains(allClasses.get(i).getClassName())) {
                        String groupID = dataOfSubject[0].substring(allClasses.get(i).getClassName().length());
                        if (groupID.length() == 4 && groupID.charAt(1) != '0'){
                            Relationships.putClassesOfGradeLessons(groupID.substring(1,3),i);
                            Relationships.gradeLessonPerWeek.put(groupID.substring(1,3), Integer.parseInt(dataOfSubject[2]));
                            allClasses.get(i).addGradeLessons(new Lesson(groupID, dataOfSubject[1], dataOfSubject[3], 0));
                        } else {

                            for (int j = 0; j < Integer.parseInt(dataOfSubject[2]); j++) {

                                allClasses.get(i).addLessons((new Lesson(groupID, dataOfSubject[1], dataOfSubject[3], 0)));
                            }
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

            // Fill an array with classes grades
            this.allClassesGrades = new int[allClasses.size()];
            for (int j = 0; j < allClasses.size(); j++){
                allClassesGrades[j] = allClasses.get(j).getGrade();
            }
        }

        // Az adatok tesztelése
        //testData();


    }
    private void testData(){
        for (int i = 0; i < allClasses.size(); i++) {
            System.out.println(allClasses.get(i).getClassName() + " osztálynak " + allClasses.get(i).getLessonsPerWeek() + "db órája van hetente.");
        }

        for (int i = 0;i<allClasses.size();i++) {
            for (int j =0; j < allClasses.get(i).getAllLessons().size();j++){
                System.out.println(allClasses.get(i).getAllLessons().get(j));
            }
            System.out.println();
            //TimeTable.createRandomTimeTable(allClasses.get(i));
        }
    }
}


