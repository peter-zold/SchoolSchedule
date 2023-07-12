package schedule.database;

import schedule.displayandtxt.TimetableDataInputAndOutput;

import java.sql.*;
import java.util.Scanner;

public class Jdbc {
    //This class creates a Jdbc and checks whether the database already exists or not, if not it automatically creates it.
    private static volatile Jdbc instance = null;
    String user ="root";
    String password = "Karakter1986!";

    public static Jdbc getInstance() {
        if (instance == null) {
            synchronized (Jdbc.class) {
                if (instance == null) {
                    instance = new Jdbc();
                }
            }
        }
        return instance;
    }

    Jdbc() {
        //constructor makes sure the database is created everytime this class is initialized, if it already exists it does not do anything.
        initialize();
    }

    public void initialize() {
        boolean connectionTestResult = false;
        boolean databaseAlreadyExist = false;
        boolean databaseCreated = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user, password)) {
            connectionTestResult = testTheConnection(connection);
            databaseAlreadyExist = connect();

            if (connectionTestResult) {
                if (!databaseAlreadyExist) {
                    databaseCreated = createTheDatabase(connection);
                }

                if (databaseAlreadyExist) {
                    System.out.println("The database already exist, therefore it was not created again. Moving on to table creation.");
                }

                if (databaseCreated | databaseAlreadyExist) {
                    if (createTheTable(connection)) {
                    } else {
                        System.out.println("Table was not created due to some error.");
                    }
                }
            }
            if (!connectionTestResult) {
                System.out.println("Not connected to SQL server!");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            System.out.println(e.getMessage());
        }
    }

    //creates database if needed
    private boolean createTheDatabase(Connection connection) {
        try (PreparedStatement createDatabaseCommand = connection.prepareStatement("CREATE DATABASE school_schedule")) {
            createDatabaseCommand.execute();
            System.out.println("Database was created.");
        } catch (SQLException e) {
            System.out.println("Error in database package Jdbc class and createDatabase() method.");
            e.getMessage();
            return false;
        }
        return true;
    }

    //connects to database, if it is already created
    public boolean connect() {
        boolean validConnectionToExistingDatabase = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/school_schedule", user, password);
             Scanner scanner = new Scanner(System.in)) {

            validConnectionToExistingDatabase = connection.isValid(0);

            if (validConnectionToExistingDatabase) {
                System.out.println("Connected to the existing database.");
                return true;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return false;
    }

    //tests connection to SQL server
    private boolean testTheConnection(Connection connection) {
        boolean isConnectedToDatabase = false;

        try {
            isConnectedToDatabase = connection.isValid(0);
            if (isConnectedToDatabase) {
                System.out.println("SQL connection works.");
            }

            if (!isConnectedToDatabase) {
                System.out.println("No SQL connection. Check username and password and the SQL server.");
            }

        } catch (SQLException e) {
            System.out.println("Error in database package Jdbc class and testTheConnection() method.");
            e.getMessage();
        }
        return isConnectedToDatabase;
    }

    //tests whether table exist, if not it creates it, the various columns of the table can be edited below
    private boolean createTheTable(Connection connection) {
        boolean tableAlreadyExist = false;

        try (PreparedStatement checkTableExists = connection.prepareStatement(
                "SELECT table_name " +
                        "FROM information_schema.tables " +
                        "WHERE table_schema = 'school_schedule' " +
                        "AND table_name = 'timetable'; ");

             PreparedStatement createTableCommand = connection.prepareStatement("CREATE TABLE school_schedule.timetable (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "class VARCHAR(50), " +
                     "day VARCHAR(10), " +
                     "period INT, " +
                     "subject VARCHAR(50), " +
                     "teacher VARCHAR(50), " +
                     "recess INT " +
                     ");")) {

            ResultSet checkTableResultSet = checkTableExists.executeQuery();
            if (checkTableResultSet.next()) {
                tableAlreadyExist = true;
            } else {
                tableAlreadyExist = false;
            }

            if (!tableAlreadyExist) {
                createTableCommand.execute();
                System.out.println("Table was created.");
            }

            if (tableAlreadyExist) {
                System.out.println("Table already exists.");
            }

        } catch (SQLException e) {
            if (!tableAlreadyExist) {
                System.out.println("Error in database package Jdbc class and createTheTable() method in the table creation section.");
                System.out.println(e.getMessage());
            }
            return false;
        }
        return true;
    }
}
