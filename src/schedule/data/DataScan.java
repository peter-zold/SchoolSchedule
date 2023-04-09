package schedule.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataScan {
    public static void main(String[] args) {


;


        List <String> allClassesNames = new ArrayList<>();
        List <Subject> allSubjects = new ArrayList<>();
        List<Classes> allClasses = new ArrayList<>();

        File classesName = new File("src\\schedule\\data\\classes.txt");
        try (Scanner scanner = new Scanner(classesName);) {
            while (scanner.hasNext()) {
                allClassesNames.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }



        File scheduleData = new File("src\\schedule\\data\\classes_summary.txt");
        try (Scanner scanner = new Scanner(scheduleData);) {
            while (scanner.hasNext()) {
                String dataLine = scanner.nextLine();
                String[] dataOfSubject = dataLine.split(",");
                allSubjects.add(new Subject(dataOfSubject[0], dataOfSubject[1], Integer.parseInt(dataOfSubject[2]), dataOfSubject[3], dataOfSubject[4]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }

        List<Subject> temporary = new ArrayList<>();
        int lessonsPerWeek = 0;
        for (int i = 0; i < allClassesNames.size();i++) {
            for (int j = 0; j < allSubjects.size(); j++) {
                if (allClassesNames.get(i).equals(allSubjects.get(j).className)) {
                    temporary.add(allSubjects.get(j));
                    lessonsPerWeek += allSubjects.get(j).hoursPerWeek;
                }
            }
            allClasses.add(new Classes(temporary, allClassesNames.get(i), lessonsPerWeek));
            temporary.clear();
            lessonsPerWeek = 0;
        }

        for (int i = 0; i < allClasses.size();i++){
            System.out.println(allClasses.get(i).className + " osztálynak " + allClasses.get(i).subjects.size() + " db tantárgya van.");
        }







    }
}

