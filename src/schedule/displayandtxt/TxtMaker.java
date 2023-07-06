package schedule.displayandtxt;

import schedule.data.Lesson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TxtMaker {
    TimetableDataInputAndOutput timetableDataInputAndOutput = TimetableDataInputAndOutput.getInstance();

    //Variables:
    private final int lessonArraySize = timetableDataInputAndOutput.getLessonArraySize();
    private int dayOfTheWeekCounter = 0;

    //Arrays:
    private Lesson[][] lessonArray = timetableDataInputAndOutput.getLessonArray();
    private final String[] classNamesArray = timetableDataInputAndOutput.getClassNamesArray();
    private final String[] daysOfTheWeekArray = timetableDataInputAndOutput.getDaysOfTheWeekArray();
    private final String[][] finalSubjectNamesArray = timetableDataInputAndOutput.getFinalSubjectNamesArray();
    private final String[][] finalTeacherNamesArray = timetableDataInputAndOutput.getFinalTeacherNamesArray();
    private final int[][] valueOfFreenessArray = timetableDataInputAndOutput.getValueOfFreenessArray();

    //This creates a txt file from a Lesson[][] built from the fittest timetable based on the values
    // generated in the TimeTableDataInput class - Simon
    public void timetableTxtMaker() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\schedule\\displayandtxtanddatabase\\timetable.txt"))) {

            for (int i = 0; i < classNamesArray.length; i++) {
                writer.write("\nA " + classNamesArray[i] + " osztály órarendje:\n");
                for (int j = 0; j < lessonArraySize; j++) {
                    if (j % 9 == 0) {
                        writer.write("\n" + daysOfTheWeekArray[dayOfTheWeekCounter++] + ": \n");
                    }
                    writer.write(j % 9 + ". óra: " + lessonArray[i][j] + "\n");
                    if (j == lessonArraySize - 1) {
                        dayOfTheWeekCounter = 0;
                    }
                    if ( i == classNamesArray.length - 1 & j == lessonArraySize - 1 ) {
                        writer.write("--- END OF TIMETABLE ----");
                        break;
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this creates a txt file from all the subjects in the Lesson[][] array created from the fittest timetable,
    //the subjects are listed as many times as they are occurring, in the order of occurrence - Simon
    public void subjectNamesTxtMaker() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\schedule\\displayandtxtanddatabase\\all_the_subjects_in_the_timetable.txt"))) {

            for (int i = 0; i < classNamesArray.length; i++) {
                for (int j = 0; j < lessonArraySize; j++) {
                    writer.write(finalSubjectNamesArray[i][j]  + "\n");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this creates a txt file from all the names of the teachers in the Lesson[][] array created from the fittest timetable,
    //the teachers are listed as many times as they are occurring, in the order of occurrence - Simon
    public void teacherNamesTxtMaker() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\schedule\\displayandtxtanddatabase\\all_the_teacher_names_in_the_timetable.txt"))) {

            //only displays the name when a teacher is assigned, filters for the keyword "none"
            for (int i = 0; i < classNamesArray.length; i++) {
                for (int j = 0; j < lessonArraySize; j++) {
                    if (finalTeacherNamesArray[i][j] != "none") {
                        writer.write(finalTeacherNamesArray[i][j] + "\n");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this creates a txt file from all the valueOfFreeness values within the Lesson[][] array created from the fittest timetable,
    //the values are listed as many times as they are occurring, in the order of occurrence - Simon
    public void valueOfFreenessTxtMaker() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\schedule\\displayandtxtanddatabase\\all_the_valueoffreeness_values_in_the_timetable.txt"))) {

            for (int i = 0; i < classNamesArray.length; i++) {
                for (int j = 0; j < lessonArraySize; j++) {
                    writer.write(valueOfFreenessArray[i][j]  + "\n");
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}