package schedule.displayandtxtanddatabase;

import schedule.data.Lesson;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RoomArrangementTxtMaker {
    TimetableDataInput timetableDataInput = TimetableDataInput.getInstance();

    //Variables:
    private final int lessonArraySize = timetableDataInput.getLessonArraySize();
    private int dayOfTheWeekCounter = 0;

    //Arrays:
    private Lesson[][] lessonArray = timetableDataInput.getLessonArray();
    private final String[] classNamesArray = timetableDataInput.getClassNamesArray();
    private final String[] daysOfTheWeekArray = timetableDataInput.getDaysOfTheWeekArray();


//This creates the txt file from the fittest timetable based on the values generated in the TimeTableDataInput class - Simon
   public void txtMaker() {
       try (BufferedWriter writer = new BufferedWriter(new FileWriter("src\\schedule\\displayandtxtanddatabase\\timetable.txt"))) {

           for (int i = 0; i < classNamesArray.length; i++) {
               writer.write("\nA " + classNamesArray[i] + " osztály órarendje:\n");
               for (int j = 0; j < lessonArraySize; j++) {
                   if(j % 9 == 0) {
                       writer.write("\n" + daysOfTheWeekArray[dayOfTheWeekCounter++] + ": \n");
                   }
                   writer.write(j % 9 + ". óra: " + lessonArray[i][j] + "\n");
                   if (j == lessonArraySize - 1 ) {
                       dayOfTheWeekCounter = 0;
                   }
               }
               writer.newLine();
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}