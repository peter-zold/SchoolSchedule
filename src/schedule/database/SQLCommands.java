package schedule.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SQLCommands {

    //establishes SQL connection, checks if database and table exist, creates them if they don'1't
    Jdbc jdbc = new Jdbc();

    Scanner scanner = new Scanner(System.in);
    String user ="root";
    String password = "Karakter1986!";

    //start here, creates connection to database and table, starts the SQL command selection method
    public void createConnectionStart() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_schedule", user, password)) {
            selectSQLCommand (connection);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }




    public void selectSQLCommand (Connection connection) {

        int choice = 0;

        choice = scanner.nextInt();


        switch (choice) {
            case 1:
                importFromTXTFile(connection);
                break;
            case 2:

                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;

            default:
                System.out.println("wrong choice, only from 1 - 6");
                break;
        }

    }

    //imports values from TXT file automatically
        private void importFromTXTFile (Connection connection) {

            //


            try (PreparedStatement importFromTXT = connection.prepareStatement(
                    "INSERT INTO school_schedule " +
                            "VALUES (0, ?, ?, ?, ?, ?, ?);")) {


                List<String> words = new ArrayList<>();

//        try (PreparedStatement importFromTXT = connection.prepareStatement(
//                "INSERT INTO school_schedule " +
//                        "VALUES (0, ?, ?, ?, ?, ?, ?);")) {

                try (BufferedReader reader = new BufferedReader(new FileReader("src\\schedule\\displayandtxt\\all_the_subjects_in_the_timetable.txt"))) {
                    String line;
                    String lineFixed;
                    while (reader.readLine() != null) {

                        line = reader.readLine();


                        // Split the line by whitespace to extract words, regex \s+ means whitespace 1 or more
                        //String[] lineWords = line.split("\\s+");
                        lineFixed = line.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");

                        if (!line.isEmpty()) {
                            words.add(line);
                            importFromTXT.setString(1, line);
                        }
                    }

                    for (String s : words) {
                        System.out.println(s);
                    }

//                        if (line.equals("Monday:")) {
//                            reader.close();
//                        }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();

//class
                //importFromTXT.setString(1, "test");


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
//
//
//                //class
//                importFromTXT.setString(1,"test");
//
//
//                //day
//                importFromTXT.setString(2,"test");
//
//
//                //period
//                importFromTXT.setString(3,"test");
//
//                //subject
//                importFromTXT.setString(4,"test");
//
//                //teacher
//                importFromTXT.setString(5,"test");
//
//
//
//                //kiszedni string, konveertálni int
//                //recess
//                importFromTXT.setInt(6,12);



                private void importFromTXTFile2(Connection connection) {

                    List<String> words = new ArrayList<>();

//        try (PreparedStatement importFromTXT = connection.prepareStatement(
//                "INSERT INTO school_schedule " +
//                        "VALUES (0, ?, ?, ?, ?, ?, ?);")) {

                    try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\test\\ght.txt"))) {
                        String line = " ";
                        boolean endOfThetext;

                        while (line != null) {

                            line = reader.readLine();
                            // Split the line by whitespace to extract words, regex \s+ means whitespace 1 or more
                            String[] lineWords = line.split("\\s+");

                            for (String word : lineWords) {
                                // Remove leading and trailing punctuation
                                word = word.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");

                                // Ignore empty words
                                if (!word.isEmpty()) {
                                    words.add(word);
                                }
                            }
                            if (line.equals("Monday:")) {
                                reader.close();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();

//class
                        //importFromTXT.setString(1, "test");


                    }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


                    for (String s : words) {
                        System.out.print(s + " ");
                    }


                }


                private void importFromTXTFile3 (Connection connection) {

                    //


                    try (PreparedStatement importFromTXT = connection.prepareStatement(
                            "INSERT INTO timetable " +
                                    "VALUES (0, ?, ?, ?, ?, ?, ?);")) {


                        List<String> words = new ArrayList<>();

//        try (PreparedStatement importFromTXT = connection.prepareStatement(
//                "INSERT INTO school_schedule " +
//                        "VALUES (0, ?, ?, ?, ?, ?, ?);")) {

                        try (

                                BufferedReader readerClass = new BufferedReader(new FileReader("src\\main\\java\\test\\classes.txt"));
                                BufferedReader readerDay = new BufferedReader(new FileReader("src\\main\\java\\test\\day.txt"));
                                BufferedReader readerPeriod = new BufferedReader(new FileReader("src\\main\\java\\test\\period.txt"));
                                BufferedReader readerSubject = new BufferedReader(new FileReader("src\\main\\java\\test\\ght.txt"));
                                BufferedReader readerTeacher = new BufferedReader(new FileReader("src\\main\\java\\test\\teacher.txt"));
                                BufferedReader readerRecess = new BufferedReader(new FileReader("src\\main\\java\\test\\recess.txt"));

                        ) {

                            String lineClass = " ";
                            String lineDay = " ";
                            String linePeriod = " ";
                            String lineSubject = " ";
                            String lineTeacher = " ";
                            int recessValueOfFreeness = 0;
                            int counter = 0;
                            int dayCounter = 0;
                            int periodCounter = 0;

                            do {
                                lineSubject = readerSubject.readLine();

                                // Split the lineSubject by whitespace to extract words, regex \s+ means whitespace 1 or more
                                //String[] lineWords = lineSubject.split("\\s+");
                                //lineFixed = lineSubject.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
                                if (lineSubject != null) {
                                    if (!lineSubject.isEmpty()) {

                                        //Class, changes class every 45 rows
                                        if (counter % 45 == 0) {
                                            lineClass = readerClass.readLine();
                                        }
                                        if (lineClass != null) {
                                            importFromTXT.setString(1, lineClass);
                                        }

                                        //Day, changes day every 9 rows
                                        if (counter % 9 == 0) {
                                            lineDay = readerDay.readLine();
                                            dayCounter++;
                                            if (dayCounter == 5) {
                                                dayCounter = 0;
                                            }
                                        }
                                        if (lineDay != null) {
                                            importFromTXT.setString(2, lineDay);
                                        }


                                        //Period, made with an int array
                                        int[] periodArray = new int[] {0,1,2,3,4,5,6,7,8};

                                        periodCounter++;
                                        if (periodCounter == 9) {
                                            periodCounter = 0;
                                        }
                                        linePeriod = readerPeriod.readLine();

                                        if (linePeriod != null) {
                                            importFromTXT.setInt(3, periodArray[periodCounter]);
                                        }

                                        //Subject
                                        words.add(lineSubject);
                                        importFromTXT.setString(4, lineSubject);

                                        //Teacher- GOTTA FINISH REMOVING EMPTY LINES FROM TEACHER TXT
                                        lineTeacher = readerTeacher.readLine();

                                        if (lineTeacher != null) {
                                            importFromTXT.setString(5, lineTeacher);
                                        }

                                        //Recess, value of freeness - GOTTA FINISH REMOVING EMPTY LINES FROM TEACHER TXT
                                        recessValueOfFreeness = readerRecess.read();

                                        importFromTXT.setInt(6, recessValueOfFreeness);


                                        counter++;

                                        importFromTXT.execute();
                                    }
                                }
                            } while (lineSubject != null);

                            for (String s: words) {
                                System.out.println(s);
                            }

//                        if (lineSubject.equals("Monday:")) {
//                            readerSubject.close();
//                        }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();

//class
                        //importFromTXT.setString(1, "test");


                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
//
//
//                //class
//                importFromTXT.setString(1,"test");
//
//
//                //day
//                importFromTXT.setString(2,"test");
//
//
//                //period
//                importFromTXT.setString(3,"test");
//
//                //subject
//                importFromTXT.setString(4,"test");
//
//                //teacher
//                importFromTXT.setString(5,"test");
//
//
//
//                //kiszedni string, konveertálni int
//                //recess
//                importFromTXT.setInt(6,12);





                }



            }





////class
//            importFromTXT.setString(1, "test");
//
//
//                    //day
//                    importFromTXT.setString(2, "test");
//
//
//                    //period
//                    importFromTXT.setString(3, "test");
//
//                    //subject
//                    importFromTXT.setString(4, "test");
//
//                    //teacher
//                    importFromTXT.setString(5, "test");
//
//
//                    //kiszedni string, konveertálni int
//                    //recess
//                    importFromTXT.setInt(6, 12);

