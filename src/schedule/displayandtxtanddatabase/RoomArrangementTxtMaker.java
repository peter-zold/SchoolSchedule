package schedule.displayandtxtanddatabase;

import schedule.data.Lesson;

import java.io.*;

public class RoomArrangementTxtMaker {
    TimetableDataInput timetableDataInput = TimetableDataInput.getInstance();
    Lesson[][] lessonArray = timetableDataInput.getLessonArray();
    private String[] classNamesArray = timetableDataInput.getClassNamesArray();
    private String[] daysOfTheWeekArray = timetableDataInput.getDaysOfTheWeekArray();
    int lessonArraySize = timetableDataInput.getLessonArraySize();

    int dayOfTheWeekCounter = 0;

   public void txtMaker() {
       try (BufferedWriter writer = new BufferedWriter(new FileWriter("timetable.txt"))) {

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
/*
    //ne legyen hashmap
        public void txtMaker(CustomHashMapForDisplayingRoomArrangement<Integer, String, Integer) {
            // This will be replaced with a database connection in time. Placeholder.
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("timetable.txt"), StandardCharsets.UTF_8))) {
                for (Integer key : timeTableCustomDisplayMap.keySet()) {
                    String valueString = timeTableCustomDisplayMap.getString(key);
                    if (valueString != null) {
                        writer.write(key + ": " + valueString);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

 */
}