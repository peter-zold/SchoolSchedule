package schedule.displayandtxtanddatabase;

import schedule.Individual;
import schedule.data.Classes;
import schedule.data.Lesson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimetableDataInput {
    //Variables:
    private int valueOfFreeness = -1;
    private int timeRangePrevious = 0;
    private String subjectNameTemp = null;
    private String teacherNameTemp = null;
    //change the size of the lessonArrayOneDimensional here, it will change it everywhere, this makes it dynamic, and you don't have to type in 45 everywhere
    int lessonArraySize = 45;

    //Lists:
    List<String> classNamesStringArrayList;
    List<String> subjectNamesStringArrayList;
    List<String> teacherNamesStringArrayList;
    // List<Integer> timeRangesIntegerArrayList; - not currently used

    //Arrays:
    private String[] daysOfTheWeekArray = new String[] {
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };
    String[] classNamesArray;
    String[] fullTeacherNamesArray;
    String[][] finalTeacherNamesArray;
    String[] fullSubjectNamesArray;
    String[][] finalSubjectNamesArray;
    Lesson[][] lessonArray;


    //not currently used:
    //int[] timeRangesArray;

    //Others:
    private static volatile TimetableDataInput instance = null;

    public TimetableDataInput() {
        createClassNamesStringArrayList();
        createClassNamesArray();
        createSubjectNamesStringArrayList();
        createFullSubjectNamesArray();
        createTeacherNamesStringArrayList();
        createFullTeacherNamesArray();

        //not used currently:
        //createTimeRangeIntegerArrayList();
        //reateTimeRangesArray();
    }

    //Singleton:
    public static TimetableDataInput getInstance() {
        if (instance == null) {
            synchronized (TimetableDataInput.class) {
                if (instance == null) {
                    instance = new TimetableDataInput();
                }
            }
        }
        return instance;
    }

    //creates the 2 dimensional Lesson[][] array that was requested by Roland - Simon
    private void createLessonArray() {
        lessonArray = new Lesson[classNamesArray.length][lessonArraySize];

        for (int i = 0; i < classNamesArray.length; i++) {
            for (int j = 0; j < lessonArraySize; j++) {
                //if it is a "Free period" then it assigns it a valueOfFreenness value, if it isn't it is -1 - Simon
                if(finalSubjectNamesArray[i][j] == "Free Period") {
                    valueOfFreeness = createValueOfFreeness(j);
                } else {
                    valueOfFreeness = -1;
                }
                lessonArray[i][j] = new Lesson(finalSubjectNamesArray[i][j], finalTeacherNamesArray[i][j], valueOfFreeness);
            }
        }
    }

    //Gets the necessary data for timetable, lesson array and txt files generation.
    // This is the method you need to call to make this work if you call this class - Simon
    public void getTimetableData(List<Classes> allClasses, Individual individual) {
        for (int i = 0; i < allClasses.size(); i++) {
            for (int j = 0; j < lessonArraySize; j++) {
                //get names of subjects from the fittest timetable, creates array - Simon
                createFinalSubjectNamesArray(i, j, individual.getClassTimetable(i)[j].getNameOfLesson());
                //get names of teachers from the fittest timetable, creates array - Simon
                createFinalTeacherNamesArray(i, j, individual.getClassTimetable(i)[j].getTeacher().getName());
            }
        }
        createLessonArray();
    }

    public String[] getDaysOfTheWeekArray() {
        return daysOfTheWeekArray;
    }

/*
- time ranges are not currently used - Simon

    private void createTimeRangeIntegerArrayList() {
        timeRangesIntegerArrayList = new ArrayList<>();
    }

    public void fillUpTimeRangeIntegerArrayList(int timeRange) {
        timeRangesIntegerArrayList.add(timeRange);
    }

    private void createTimeRangesArray() {
        timeRangesArray = new int[timeRangesIntegerArrayList.size()];

        for (int i = 0; i < timeRangesIntegerArrayList.size(); i++) {
            timeRangesArray[i] = timeRangesIntegerArrayList.get(i);
        }

        this.timeRangesArray = timeRangesArray;

      File timeranges = new File("src\\schedule\\data\\timeranges.txt");
        try (Scanner scanner = new Scanner(timeranges);) {
            while (scanner.hasNext()) {
                timeRangeList.add(Integer.valueOf(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }
    }
*/

    //Class Names arrays and Lists

    //1., This creates a class name array list, based on the classes.txt, dynamic
    //if you change the size, composition of teachers.txt it changes as well, automatically
    private void createClassNamesStringArrayList() {
        //String datatype

        classNamesStringArrayList = new ArrayList<>();

        try (BufferedReader createClassNamesStringArrayListReader = new BufferedReader(new FileReader("src\\schedule\\data\\classes.txt"));) {
            String nameOfClass;
            while ((nameOfClass = createClassNamesStringArrayListReader.readLine()) != null) {
                classNamesStringArrayList.add(nameOfClass);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createClassNamesArray() {
        classNamesArray = new String[classNamesStringArrayList.size()];
        classNamesArray = classNamesStringArrayList.toArray(classNamesArray);
    }

    public String[] getClassNamesArray() {
        return classNamesArray;
    }

    //Subject Names arrays and Lists

    //2., This creates a subject name array list, based on the subjects.txt, dynamic
    //if you change the size, composition of subjects.txt it changes as well, automatically

    private void createSubjectNamesStringArrayList() {
        //String datatype
        subjectNamesStringArrayList = new ArrayList<>();

        try (BufferedReader createSubjectNamesStringArrayListReader = new BufferedReader(new FileReader("src\\schedule\\data\\subjects.txt"));) {
            String nameOfSubject;
            while ((nameOfSubject = createSubjectNamesStringArrayListReader.readLine()) != null) {
                subjectNamesStringArrayList.add(nameOfSubject);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //contains all subject names in an array, not used currently, but we might need it in the future for the database
    private void createFullSubjectNamesArray() {
        fullSubjectNamesArray = new String[subjectNamesStringArrayList.size()];
        fullSubjectNamesArray = subjectNamesStringArrayList.toArray(fullSubjectNamesArray);
    }

    //the final 2 dimensional array consisting of all the subjects of the fittest timetable
    private void createFinalSubjectNamesArray(int i, int j, String subjectName) {
        if (i == 0 && j == 0) {
            finalSubjectNamesArray = new String[subjectNamesStringArrayList.size()] [lessonArraySize];
        }

        finalSubjectNamesArray[i][j] = subjectName;
    }


    public String[][] getFinalSubjectNamesArray() {
        return finalSubjectNamesArray;
    }

    public String[] getFullSubjectNamesArray() {
        return fullSubjectNamesArray;
    }


    //Teacher Names arrays and Lists

    //3., This creates a teacher name array list, based on the teachers.txt, dynamic
    //if you change the size, composition of teachers.txt it changes as well automatically
    private void createTeacherNamesStringArrayList() {
        teacherNamesStringArrayList = new ArrayList<>();

        try (BufferedReader createTeacherNamesStringArrayReader = new BufferedReader(new FileReader("src\\schedule\\data\\teachers.txt"));) {

            String nameOfTeacher;
            while ((nameOfTeacher = createTeacherNamesStringArrayReader.readLine()) != null) {
                teacherNamesStringArrayList.add(nameOfTeacher);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //contains all teacher names in an array, not used currently, but we might need it in the future for the database
    private void createFullTeacherNamesArray() {
        //String datatype
        fullTeacherNamesArray = new String[teacherNamesStringArrayList.size()];
        fullTeacherNamesArray = teacherNamesStringArrayList.toArray(fullTeacherNamesArray);
    }

    //the final 2 dimensional array consisting of all the teachers of the fittest timetable
    private void createFinalTeacherNamesArray(int i, int j, String teacherName) {
        if (i == 0 && j == 0) {
            finalTeacherNamesArray = new String[teacherNamesStringArrayList.size()] [lessonArraySize];
        }
        finalTeacherNamesArray[i][j] = teacherName;
    }

    public String[][] getFinalTeacherNamesArray() {
        return finalTeacherNamesArray;
    }

    public String[] getFullTeacherNamesArray() {
        return fullTeacherNamesArray;
    }

    //the new value of Freenes method I created to make it work - Simon
    public int createValueOfFreeness(int numberOfTheLesson) {
        //int valueOfFreeness (0 - 8): makes the identification of cancelled classes, free periods possible, easier, by assigning them a value.
        //It was originally calculated by the calculateValueOfFreeness method in the Timetable class, however that one does not function properly so I created my own method.
        //if it's nota free period it gets a value of -1, otherwise it isa self-explanatory numebr from 1-8 depending on which time period it is
        // Example: so if a free period takes the place of the 0th lesson of the day it gets a 0, if it takes the place of the 8th lesson it gets an 8 - Simon

        int howManyTimesItIsDivideableByNine = numberOfTheLesson/9;
            valueOfFreeness = numberOfTheLesson - (9 * howManyTimesItIsDivideableByNine);

        return valueOfFreeness;
    }

    public Lesson[][] getLessonArray() {
        return lessonArray;
    }

    public int getLessonArraySize() {
        return lessonArraySize;
    }
}