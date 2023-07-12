package schedule.database;

import schedule.displayandtxt.TimetableDataInputAndOutput;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

//this class is the one that should be instantiated not Jdbc
public class SQLCommands {
    //establishes SQL connection, checks if database and table exist, creates them if they don't, uses Singleton
    Jdbc jdbc = Jdbc.getInstance();

    String user = "root";
    String password = "Karakter1986!";
    private TimetableDataInputAndOutput timeTableDataInputAndOutput = TimetableDataInputAndOutput.getInstance();

    //start here, creates connection to database and table, starts the SQL command selection method
    public void createConnectionStart() {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_schedule", user, password);
                Scanner scanner = new Scanner(System.in);
        ) {
            selectSQLCommand(connection, scanner);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //select which command you want to run
    public void selectSQLCommand(Connection connection, Scanner scanner) {
        int choice = 0;

        choice = scanner.nextInt();

        System.out.println(
                "Choose the SQL operation you want, select by typing the number of the operation: \n" +
                        "1 - Import school schedule from TXT file. Make sure you have all the files needed(you need 3 txt files in the displayandtxt package," +
                        " usally automaticaly generated)! \n" +
                        "2 - Manually add new row. \n" +
                        "3 - Modify existing row based on id number (oyu can find the id in MySQL. \n" +
                        "4 - Delete row. \n" +
                        "5 - Generate SQL table from the latest calcualted school schedule, from Lesson array and Classes  (not yet done)."
        );

        switch (choice) {
            case 1:
                importFromTXTFile(connection);
                break;
            case 2:
                addNewRowManually(connection, scanner);
                break;
            case 3:
                modifyExistingRowManually(connection, scanner);
                break;
            case 4:
                deleteRow(connection, scanner);
                break;
            case 5:
                //createFromLessonArray
                break;
            case 6:

                break;
            default:
                System.out.println("wrong choice, only from 1 - 6");
                break;
        }
    }

    //imports values from TXT file automatically
    private void importFromTXTFile(Connection connection) {
        String lineClass = " ";
        String lineDay = " ";
        String linePeriod = " ";
        String lineSubject = " ";
        String lineTeacher = " ";
        int recessValueOfFreeness = 0;
        int counter = 0;
        int dayCounter = 0;
        int periodCounter = 0;

        try (PreparedStatement importFromTXT = connection.prepareStatement(
                "INSERT INTO timetable " +
                        "VALUES (0, ?, ?, ?, ?, ?, ?);");
             BufferedReader readerClass = new BufferedReader(new FileReader("src/schedule/data/classes.txt"));
             BufferedReader readerSubject = new BufferedReader(new FileReader("src/schedule/displayandtxt/all_the_subjects_in_the_timetable.txt"));
             BufferedReader readerTeacher = new BufferedReader(new FileReader("src/schedule/displayandtxt/all_the_teacher_names_in_the_timetable.txt"));

             //scanner is faster when reading int, bufferedreader can not directly read int, it needs to be converted back from ASCII character code numbers
             Scanner scannerRecess = new Scanner(new FileReader("src\\main\\java\\test\\recess.txt"));
        ) {
            do {
                lineSubject = readerSubject.readLine();

                //Split the lineSubject by whitespace to extract words, regex \s+ means whitespace 1 or more
                //String[] lineWords = lineSubject.split("\\s+"); -I'm not using it, but it might be need in the future, so I kept it
                //lineFixed = lineSubject.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
                if (lineSubject != null) {
                    if (!lineSubject.isEmpty()) {

                        //Class, changes class every 45 rows, the 45 is imported from TimeTableDataAndOutput, so it follows
                        if (counter % timeTableDataInputAndOutput.getLessonArraySize() == 0) {
                            lineClass = readerClass.readLine();
                        }

                        if (lineClass != null) {
                            importFromTXT.setString(1, lineClass);
                        }

                        //Day, changes day every 9 rows, from an array, no reader
                        String[] dayArray = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                        if (counter % 9 == 0) {
                            lineDay = dayArray[dayCounter];
                            dayCounter++;
                            if (dayCounter == 5) {
                                dayCounter = 0;
                            }
                        }

                        if (lineDay != null) {
                            importFromTXT.setString(2, lineDay);
                        }

                        //Period, made with an int array, no reader
                        int[] periodArray = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};

                        if (periodCounter == 9) {
                            periodCounter = 0;
                        }

                        importFromTXT.setInt(3, periodArray[periodCounter]);

                        periodCounter++;

                        //Subject
                        importFromTXT.setString(4, lineSubject);

                        //Teacher- GOTTA FINISH REMOVING EMPTY LINES FROM TEACHER TXT
                        lineTeacher = readerTeacher.readLine();

                        if (lineTeacher != null) {
                            importFromTXT.setString(5, lineTeacher);
                        }

                        //Recess, value of freeness - GOTTA FINISH REMOVING EMPTY LINES FROM TEACHER TXT
                        recessValueOfFreeness = scannerRecess.nextInt();

                        importFromTXT.setInt(6, recessValueOfFreeness);
                        counter++;
                        importFromTXT.execute();
                    }
                }
            } while (lineSubject != null);
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception, problem in database/SQLCommands");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        } catch (IOException f) {
            System.out.println("Input Output exception, problem in database/SQLCommands");
            System.out.println(f.getMessage());
            System.out.println(f.getCause());
            f.printStackTrace();
        } catch (SQLException g) {
            System.out.println("SQL exception, problem in database/SQLCommands");
            System.out.println(g.getMessage());
            System.out.println(g.getCause());
            g.printStackTrace();
        }
    }

    private void addNewRowManually(Connection connection, Scanner scanner) {
        String classInput;
        String dayInput;
        int periodInput;
        String subjectInput;
        String teacherInput;
        int recessValueOfFreenessInput;

        System.out.println("Manually add a new row.");

        try (
                //id is auto increment that is hy it is 0
                PreparedStatement addNewRow = connection.prepareStatement(
                        "INSERT INTO timetable " +
                                "VALUES (0, ?, ?, ?, ?, ?, ?);")) {

            //class
            System.out.println("Class:");
            classInput = scanner.nextLine();
            addNewRow.setString(1, classInput);

            //day
            System.out.println("Day:");
            dayInput = scanner.nextLine();
            addNewRow.setString(2, dayInput);

            //period
            System.out.println("Period:");
            periodInput = scanner.nextInt();
            addNewRow.setInt(3, periodInput);

            //clearing buffer
            scanner.nextLine();

            //subject
            System.out.println("Subject:");
            subjectInput = scanner.nextLine();
            addNewRow.setString(4, subjectInput);

            //teacher
            System.out.println("Teacher:");
            teacherInput = scanner.nextLine();
            addNewRow.setString(5, teacherInput);

            //recess
            System.out.println("Recess:");
            recessValueOfFreenessInput = scanner.nextInt();
            addNewRow.setInt(6, recessValueOfFreenessInput);

            //clearing buffer
            scanner.nextLine();
            addNewRow.execute();

        } catch (SQLException e) {
            System.out.println("SQL exception, problem in database/SQLCommands");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        System.out.println("Row has been added successfully.");
    }

    private void modifyExistingRowManually(Connection connection, Scanner scanner) {
        String classInput;
        String dayInput;
        int periodInput;
        String subjectInput;
        String teacherInput;
        int recessValueOfFreenessInput;
        int idInt;

        System.out.println("Modify a row, based on id. All the values must be inputted again for technical reasons.");

        try (PreparedStatement modifyRowStatement = connection.prepareStatement(
                "UPDATE timetable " +
                        "SET " +
                        "class = ?," +
                        "day = ?," +
                        "period = ?," +
                        "subject= ?," +
                        "teacher = ?," +
                        "recess = ?" +
                        "WHERE `id` = ?;")) {

            System.out.println("Id of the row you wish to change:");
            idInt = scanner.nextInt();
            modifyRowStatement.setInt(7, idInt);

            //clearing buffer
            scanner.nextLine();

            System.out.println("Class:");
            classInput = scanner.nextLine();
            modifyRowStatement.setString(1, classInput);

            //day
            System.out.println("Day:");
            dayInput = scanner.nextLine();
            modifyRowStatement.setString(2, dayInput);

            //period
            System.out.println("Period (whole positive numbers only, int):");
            periodInput = scanner.nextInt();
            modifyRowStatement.setInt(3, periodInput);

            //clearing buffer
            scanner.nextLine();

            //subject
            System.out.println("Subject:");
            subjectInput = scanner.nextLine();
            modifyRowStatement.setString(4, subjectInput);

            //teacher
            System.out.println("Teacher:");
            teacherInput = scanner.nextLine();
            modifyRowStatement.setString(5, teacherInput);

            //recess
            System.out.println("Recess (whole positive numbers only, int):");
            recessValueOfFreenessInput = scanner.nextInt();
            modifyRowStatement.setInt(6, recessValueOfFreenessInput);

            modifyRowStatement.execute();
        } catch (SQLException e) {
            System.out.println("SQL exception, problem in database/SQLCommands");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        System.out.println("You modified the row.");
    }

    private void deleteRow(Connection connection, Scanner scanner) {
        int idInt;

        System.out.println("Delete a row, based on id. Deletes entire row, it will be lost forever, make sure to create a backup first!");

        try (PreparedStatement deleteRowStatement = connection.prepareStatement(
                " DELETE FROM timetable " +
                        "WHERE `id` = ?;")) {


            System.out.println("Id:");
            idInt = scanner.nextInt();
            deleteRowStatement.setInt(1, idInt);

            deleteRowStatement.execute();
        } catch (SQLException e) {
            System.out.println("SQL exception, problem in database/SQLCommands");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        System.out.println("You deleted the row.");
    }

    private void createFromLessonArray(Connection connection, Scanner scanner) {

        //can't do it without pom.xml

    }

}


