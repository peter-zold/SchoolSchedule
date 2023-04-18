package schedule.roomarrangement;

import java.util.ArrayList;
import java.util.List;

public class RoomArrangementDataInput {

    //Lists:
    List<String> classNamesStringArrayList;
    List<Integer> timeRangesIntegerArrayList;
    List<String> subjectNamesStringArrayList;
    List<String> teacherNamesStringArrayList;

    //Arrays:
    String[] daysOfTheWeekArray;
    String[] classNamesArray;
    int[] timeRangesArray;
    String[] subjectNamesArray;
    String[] teacherNamesArray;

    //Other
    private static volatile RoomArrangementDataInput instance = null;

    public RoomArrangementDataInput() {
        createDaysOfTheWeekArray();
        createClassNamesStringArrayList();
        createClassNamesArray();
        createTimeRangeIntegerArrayList();
        createTimeRangesArray();
        createSubjectNamesStringArrayList();
        createSubjectNamesArray();
        createTeacherNamesStringArrayList();
        createTeacherNamesArray();
        createDaysOfTheWeekArray();
    }

    //Singleton
    public static RoomArrangementDataInput getInstance() {
        if (instance == null) {
            synchronized (RoomArrangementDataInput.class) {
                if (instance == null) {
                    instance = new RoomArrangementDataInput();
                }
            }
        }
        return instance;
    }

    int timeRangeIPrevious = 0;
    public int ChooseDayOfTheWeek(int daysOfTheWeekArrayCounterInt, int timeRangeICurrent, int timeRange) {
        if (timeRangeIPrevious < timeRangeICurrent) {
            daysOfTheWeekArrayCounterInt = -1;
            timeRangeIPrevious = timeRangeICurrent;
        }
        if (0 == timeRange) {
            ++daysOfTheWeekArrayCounterInt;
        }
        return daysOfTheWeekArrayCounterInt;
    }

    private void createDaysOfTheWeekArray() {
        daysOfTheWeekArray = new String[7];

        daysOfTheWeekArray[0] = "Monday";
        daysOfTheWeekArray[1] = "Tuesday";
        daysOfTheWeekArray[2] = "Wednesday";
        daysOfTheWeekArray[3] = "Thursday";
        daysOfTheWeekArray[4] = "Friday";
        daysOfTheWeekArray[5] = "Saturday";
        daysOfTheWeekArray[6] = "Sunday";
    }

    public String[] getDaysOfTheWeekArray() {
        return daysOfTheWeekArray;
    }

    private void createClassNamesStringArrayList() {
        classNamesStringArrayList = new ArrayList<>();
    }

    public void fillUpClassNamesStringArrayList(String className) {
        classNamesStringArrayList.add(className);
    }

    private void createClassNamesArray() {
        classNamesArray = new String[classNamesStringArrayList.size()];

        for (int i = 0; i < classNamesStringArrayList.size(); i++) {
            classNamesArray[i] = classNamesStringArrayList.get(i);
        }

        this.classNamesArray = classNamesArray;

     /*  might need this later, don't delete
      File timeranges = new File("src\\schedule\\data\\timeranges.txt");
        try (Scanner scanner = new Scanner(timeranges);) {
            while (scanner.hasNext()) {
                timeRangeList.add(Integer.valueOf(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }      */
    }

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

     /*  might need this later, don't delete
      File timeranges = new File("src\\schedule\\data\\timeranges.txt");
        try (Scanner scanner = new Scanner(timeranges);) {
            while (scanner.hasNext()) {
                timeRangeList.add(Integer.valueOf(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }      */
    }

    private void createSubjectNamesStringArrayList() {
        subjectNamesStringArrayList = new ArrayList<>();
    }

    public void fillUpSubjectNamesStringArrayList(String subjectName) {
        subjectNamesStringArrayList.add(subjectName);
    }

    private void createSubjectNamesArray() {
        subjectNamesArray = new String[subjectNamesStringArrayList.size()];

        for (int i = 0; i < subjectNamesStringArrayList.size(); i++) {
            subjectNamesArray[i] = subjectNamesStringArrayList.get(i);
        }

        this.subjectNamesArray = subjectNamesArray;

     /* don't delete, might need it later for the database connection
        List<String> allSubjectNamesList = new ArrayList<>();

        File subjects = new File("src\\schedule\\data\\subjects.txt");
        try (Scanner scanner = new Scanner(subjects);) {
            while (scanner.hasNext()) {
                allSubjectNamesList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found!");
            e.printStackTrace();
        }

        String[] allSubjectNamesArray = new String[allSubjectNamesList.size()];

        for (int i = 0; i < allSubjectNamesList.size(); i++) {
            allSubjectNamesArray[i] = allSubjectNamesList.get(i);
        }

        this.subjectNamesArray = allSubjectNamesArray; */
    }

   private void createTeacherNamesStringArrayList() {
        teacherNamesStringArrayList = new ArrayList<>();
    }

   public void fillUpTeacherNamesStringArrayList(String teacherName) {
        teacherNamesStringArrayList.add(teacherName);
    }

    private void createTeacherNamesArray() {
        //teacher names
        teacherNamesArray = new String[teacherNamesStringArrayList.size()];

        for (int i = 0; i < teacherNamesStringArrayList.size(); i++) {
            teacherNamesArray[i] = teacherNamesStringArrayList.get(i);
        }

        this.teacherNamesArray = teacherNamesArray;
    }
}
