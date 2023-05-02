package schedule.displayandtxtanddatabase;

import schedule.data.Lesson;

//Moved the timetable display here, feel free to edit it - Simon

public class TimetableDisplay {
    //TimetableDataInput implements the Singleton design pattern - Simon

    private TimetableDataInput timeTableDataInput = TimetableDataInput.getInstance();

    //variables:
    private int valueOfFreeness = 0;
    //String subjectName = null;
    private int timeRange = 0;
    private int timeRangeCurrent = 0;
    private String subjectName = null;
    private String teacherName = null;
    private int daysOfTheWeekArrayCounter = -1;
    private int valueOfFreenessCounter = -1;
    private int timeTableCustomDisplayMapCounter = -1;
    int lessonArraySize = timeTableDataInput.getLessonArraySize();

    //Arrays:
    //All of the arrays below get their values directly from the txt files, their size is dynamic,
    // if you change the txt file their sizes will automatically adjust - Simon

    private String[] daysOfTheWeekArray = timeTableDataInput.getDaysOfTheWeekArray();
    private String[] classNamesArray = timeTableDataInput.getClassNamesArray();
    private String[][] subjectNamesArray = timeTableDataInput.getFinalSubjectNamesArray();
    private String[][] teacherNamesArray = timeTableDataInput.getFinalTeacherNamesArray();

    //The main component the lessonarray will consist of Lessons, one lesson contains
    // all 3 of the following: name of lesson, teacher, value of freeness
    //For now the Lesson array is manually set to contain 45 elements, there are unresolved issues with dynamic size
    // (I don't yet know how to do it, the way I see it some changes in Roland's and Peter's code would be needed) - Simon

    Lesson[][] lessonArray = timeTableDataInput.getLessonArray();
    String[][] finalSubjectNamesArray = timeTableDataInput.getFinalSubjectNamesArray();

    //Lesson[][][] lessonArrayExtendedWithClasses = new Lesson[classNamesArray.length][daysOfTheWeekArray.length][9];

    public void createAndDisplayTimeTable() {
        //1., first we have to create a for loop and call the array containing all the names of the classes (see above, eg.: 11a),
        //Constructed from a txt file (classes.txt), iterate using a for loop. - Simon
int dayOfTheWeekCounter = -1;

        for (int i = 0; i < classNamesArray.length; i++) {
            System.out.println("\n\nA " + classNamesArray[i] + " osztály órarendje:");
            for (int j = 0; j < lessonArraySize; j++) {
                if(j % 9 == 0) {
                    System.out.println();
                    System.out.println(daysOfTheWeekArray[++dayOfTheWeekCounter]);
                    System.out.println();
                }

                System.out.print(j % 9 + ". óra: ");
                System.out.println(lessonArray[i][j]);
                //if (i % 9 == 8) {
                //    System.out.println();
                //}
                if (j == lessonArraySize - 1 ) {
                    dayOfTheWeekCounter = -1;
                }
            }
        }



     /*
        for (int i = 0; i < classNamesArray.length; i++) {
            System.out.println("\n\nA " + classNamesArray[i] + " osztály órarendje:");

            for (int j = 0; j < 45; j++) {
                //creates a new row, 9 classes per row

                if (j % 9 == 0) {
                    System.out.println();
                }


                /*

                Ez kell a Lessonba:

                    private String nameOfLesson;
                    private Teacher teacher;
                    private int valueOfFreeness;
                    private Lesson[] lessonArrayOneDimensional;


                timeRange = j % 9; //the number of the class


                //print the name of the day only once every 9 classes (there are 8 + 1 classes per week)
                if (j % 9 == 0) {
                    daysOfTheWeekArrayCounter++;
                    System.out.println(daysOfTheWeekArray[daysOfTheWeekArrayCounter] + ":  ");
                }

                //daysOfTheWeekArrayCounter = timeTableDataInput.ChooseDayOfTheWeek(daysOfTheWeekArrayCounter, timeRangeCurrent, timeRange);





                System.out.print("[" + j % 9 + ". óra:");
                if (j == 44) {
                    daysOfTheWeekArrayCounter = -1;
                }

                    //    individual.getClassTimetable(i)[j].getNameOfLesson() + " -" + individual.getClassTimetable(i)[j].getTeacher().getName() + " ,   ");

                if (i % 9 == 8) {
                    System.out.println();
                }

                String[] teacherNamesArray = timeTableDataInput.getTeacherNamesArray();
                teacherName = individual.getClassTimetable(i)[j].getTeacher().getName();


                subjectName = individual.getClassTimetable(i)[j].getNameOfLesson();
                timeTableDataInput.createSubjectNamesStringArrayList(individual.getClassTimetable(i)[j].getNameOfLesson());

                teacherName = individual.getClassTimetable(i)[j].getTeacher().getName();

                valueOfFreenessCounter++;
                if (j % 9 == 0) {
                    valueOfFreenessCounter = 0;
                }
                if (j == 44) {
                    valueOfFreenessCounter = 0;
                }

                //valueOfFreeness = timeTableDataInput.createValueOfFreeness(valueOfFreenessCounter);

                lessonArray[i][j] = new Lesson(subjectName, teacherName, 0);

                //lessonArrayExtendedWithClasses[i][j][valueOfFreenessCounter] = new Lesson(subjectName, teacherName, valueOfFreeness);
                //System.out.print(lessonArrayExtendedWithClasses[i][j][valueOfFreenessCounter]);
                System.out.println(lessonArray[i][j] + " ");
                */
            }
        }

/*
   timeRangeCurrent = i;

        for (int i = 0; i < classNamesArray.length; i++) {
            System.out.println("\n\nA " + classNamesArray[i] + " osztály órarendje:");

            //classLessons.length in the Timetable class is 45 long, I can't extract it because of the way in which the Timetable class is written
            //Lesson[] classLessons does not have a get method, and I don't know how to work around it
            // (Roland if you could put a get method there that would be awesome)


            for (int j = 0; j < 45; j++) {

                // j % 9 == 9 because 8 classes + one extra 0-th class, and this is how it is divided into rows
                if (j % 9 == 9) {
                    System.out.println();
                }


                //2., Putting together the Lessonarray[][][], which will consist of the following: name of lesson, teacher, valueOFreeness
                //the name of the lesson (11b), teacher (Péter), valueOfFreeness (1)

                //subjectname



                //I have made an array from the teachers.txt. However, I do not yet know how to pair the subjects with the right teachers
                //, apart from the individual.getClassTimetable(i)[j].getTeacher().getName(), whcih I tried to avodi using (it's jsuta copy of your code that is why)


                9a
                        9b
                    Lesson
                        Lesson
                                Lesson

                //lessonArrayOneDimensional[osztály][óra]


                System.out.println("LESSON:" + String.valueOf(lessonArrayOneDimensional[j]));
                //System.out.print(timeRange + ". óra: " + subjectName + " -" + teacherName + " ,   ");

                //make a row
                if (i % 9 == 8) {
                    System.out.println();
                }

                //progress day of the week


                timeTableCustomDisplayMap.put(++timeTableCustomDisplayMapCounter, daysOfTheWeekArray[daysOfTheWeekArrayCounter], classNamesArray[i], timeRange, subjectName, teacherName);

            }
        }
    }
}
 */