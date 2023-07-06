package schedule.displayandtxt;

import schedule.data.Lesson;

//Moved the timetable display here, feel free to edit it - Simon

public class TimetableDisplay {
    //TimetableDataInputAndOutput implements the Singleton design pattern - Simon
    private TimetableDataInputAndOutput timeTableDataInputAndOutput = TimetableDataInputAndOutput.getInstance();

    //variables:
    int lessonArraySize = timeTableDataInputAndOutput.getLessonArraySize();

    //arrays:
    //All of the arrays below get their values directly from the txt files, their size is dynamic,
    // if you change the txt file their sizes will automatically adjust - Simon
    private String[] daysOfTheWeekArray = timeTableDataInputAndOutput.getDaysOfTheWeekArray();
    private String[] classNamesArray = timeTableDataInputAndOutput.getClassNamesArray();
    private Lesson[][] lessonArray = timeTableDataInputAndOutput.getLessonArray();

    public void createAndDisplayTimeTable() {
        //1., first we have to create a for loop and call the array containing all the names of the classes (see above, eg.: 11a),
        //Constructed from a txt file (classes.txt), iterate using a for loop. - Simon
        int dayOfTheWeekCounter = -1;

        for (int i = 0; i < classNamesArray.length; i++) {
            System.out.println("\nA " + classNamesArray[i] + " osztály órarendje:");
            for (int j = 0; j < lessonArraySize; j++) {
                //display the day and creates rows, gives it structure
                if (j % 9 == 0) {
                    System.out.println();
                    System.out.println(daysOfTheWeekArray[++dayOfTheWeekCounter]);
                    System.out.println();
                }
                //display the hour
                System.out.print(j % 9 + ". óra: ");
                //display the most important part the Lesson arrray we created in the TimetableDataInputAndOutput class
                System.out.println(lessonArray[i][j]);
                if (j == lessonArraySize - 1) {
                    //resets the day of the week counter for the new week
                    dayOfTheWeekCounter = -1;
                }
            }
        }
    }
}
