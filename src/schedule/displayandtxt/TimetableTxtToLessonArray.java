package schedule.displayandtxt;

import schedule.data.Lesson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TimetableTxtToLessonArray {
    private TimetableDataInputAndOutput timeTableDataInputAndOutput = TimetableDataInputAndOutput.getInstance();

    //Variables:
    private String line = "test";

    //Arrays:
    //change the size of the Lesson array here, it is currently the default size
    private Lesson[][] lessonFromTxtFile = new Lesson[timeTableDataInputAndOutput.getClassNamesArray().length][timeTableDataInputAndOutput.getLessonArraySize()];

    //Constructor initilizes the new Lesson aray
    public TimetableTxtToLessonArray() {
        readDataFromFile();
    }

    public void readDataFromFile() {
        int row1 = 0;
        int row2 = 0;

        //You can change which timetable to turn into a Lesson array here.
        String filePath = "src\\schedule\\displayandtxtanddatabase\\timetable.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while (line != null) {
                String[] partsOfARow = line.split("\\|");
                if (partsOfARow.length > 1) {
                    if (partsOfARow.length > 1) {
                        //fill up the Lesson array
                        String subject = partsOfARow[1].trim();
                        String teacher = partsOfARow[2].trim();
                        int valueOfFreeness = Integer.parseInt(partsOfARow[3].trim());
                        lessonFromTxtFile[row1][row2] = new Lesson(subject, teacher, valueOfFreeness);

                        //iterate the Lesson array, creates a row that is 'null' if one class is finished and it switched toa new one
                        row2++;
                        if (44 <= row2) {
                            row2 = 0;
                            row1++;
                        }
                        if (7 <= row1) {
                            break;
                        }
                    }
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //use this to get teh Lesson array created here
    public Lesson[][] getLessonFromTxtFile() {
        return lessonFromTxtFile;
    }
}