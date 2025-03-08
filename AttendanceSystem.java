import java.sql.*;
import java.util.Scanner;


class MainMenu {
    public static void mainmenu() {
        Scanner scn = new Scanner(System.in);

        System.out.println(">(PRESS 01) FOR INSERTING STUDENT DATA:");
        System.out.println(">(PRESS 02) FOR CHECKING A SINGLE STUDENT'S DATA:");
        System.out.println(">(PRESS 03) FOR CHECKING ALL STUDENTS' DATA:");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.print("PLEASE MAKE A CHOICE: ");

        int choice = scn.nextInt();
        System.out.println("-------------------------------------------------------------------------------");

        if (choice == 1) {
            InsertStudentsData.insertstudent(scn);
        } else if (choice == 2) {
            CheckSingleStudentsData.checksinglestudentsdata();
        } else if (choice == 3) {
            CheckWholeStudentsData.checkwholestudentsdata();
        } else {
            System.out.println("PLEASE MAKE A VALID CHOICE.");
            System.out.println("THANK YOU.");
        }

        scn.close();
    }
}

class InsertStudentsData {
    public static void insertstudent(Scanner scn) {
        String url = "jdbc:mysql://localhost:3306/StudentAttendanceSystemss";
        String user = "root";
        String password = "(mehtabali)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO StudentDetail (Student_ID, Student_Name, Student_Department, Total_Classes, Attended_Classes, Attendance_Marks) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("                        'INSERT STUDENT DATA'                                  ");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.print("ENTER THE NUMBER OF STUDENTS: ");
            int total = scn.nextInt();
            scn.nextLine();

            for (int i = 0; i < total; i++) {
                System.out.println("ENTER DETAILS FOR STUDENT " + (i + 1) + ":");

                System.out.print("STUDENT ID: ");
                int studentID = scn.nextInt();
                scn.nextLine();

                System.out.print("STUDENT NAME: ");
                String studentName = scn.nextLine();

                System.out.print("STUDENT DEPARTMENT: ");
                String studentDept = scn.nextLine();

                System.out.print("TOTAL NUMBER OF CLASSES: ");
                int totalClasses = scn.nextInt();

                System.out.print("NUMBER OF CLASSES ATTENDED: ");
                int attendedClasses = scn.nextInt();
                scn.nextLine();

                // Calculate Attendance Marks (out of 10)
                double attendanceMarks = ((double) attendedClasses / totalClasses) * 10;
                if (attendanceMarks > 10) attendanceMarks = 10;

                pstmt.setInt(1, studentID);
                pstmt.setString(2, studentName);
                pstmt.setString(3, studentDept);
                pstmt.setInt(4, totalClasses);
                pstmt.setInt(5, attendedClasses);
                pstmt.setDouble(6, attendanceMarks);

                pstmt.executeUpdate();

                System.out.println("STUDENT DATA INSERTED SUCCESSFULLY. CONGRATULATIONS ");
            }

            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL JDBC DRIVER NOT FOUND: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("CONNECTION FAILED: " + e.getMessage());
        }
    }
}

class CheckSingleStudentsData {
    public static void checksinglestudentsdata() {
        String url = "jdbc:mysql://localhost:3306/StudentAttendanceSystemss";
        String user = "root";
        String password = "(mehtabali)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            Scanner scn = new Scanner(System.in);

            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("                      'CHECK SINGLE STUDENT'S DATA'                            ");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.print("ENTER STUDENT ID TO SEARCH: ");
            int studentID = scn.nextInt();

            String sql = "SELECT * FROM StudentDetail WHERE Student_ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentID);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalClasses = rs.getInt("Total_Classes");
                int attendedClasses = rs.getInt("Attended_Classes");
                int remainingClasses = totalClasses - attendedClasses;

                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("                          'STUDENT DETAILS FOUND'                             ");
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("STUDENT ID        : " + rs.getInt("Student_ID"));
                System.out.println("STUDENT NAME      : " + rs.getString("Student_Name"));
                System.out.println("STUDENT DEPARTMENT: " + rs.getString("Student_Department"));
                System.out.println("TOTAL CLASSES     : " + totalClasses);
                System.out.println("ATTENDED CLASSES  : " + attendedClasses);
                System.out.println("ABSENT IN CLASSES : " + remainingClasses);
                System.out.println("ATTENDANCE MARKS  : " + rs.getDouble("Attendance_Marks"));
                System.out.println("-------------------------------------------------------------------------------");
            } else {
                System.out.println("STUDENT NOT FOUND WITH ID: " + studentID);
            }

            rs.close();
            pstmt.close();
            conn.close();
            scn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL JDBC DRIVER NOT FOUND: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("CONNECTION FAILED: " + e.getMessage());
        }
    }
}

class CheckWholeStudentsData {
    public static void checkwholestudentsdata() {
        String url = "jdbc:mysql://localhost:3306/StudentAttendanceSystemss"; // Fixed Database Name
        String user = "root";
        String password = "(mehtabali)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);

            System.out.println("----------------------------------------------------------------------------------------");
            System.out.println("| ID   | NAME          | DEPARTMENT       | TOTAL CLASSES | ATTENDED | ABSENT | MARKS  |");
            System.out.println("----------------------------------------------------------------------------------------");

            String sql = "SELECT * FROM StudentDetail"; // Fetch all student records
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.isBeforeFirst()) {
                System.out.println("|                         NO STUDENT DATA FOUND                              |");
            } else {
                while (rs.next()) {
                    int id = rs.getInt("Student_ID");
                    String name = rs.getString("Student_Name");
                    String dept = rs.getString("Student_Department");
                    int totalClasses = rs.getInt("Total_Classes");
                    int attendedClasses = rs.getInt("Attended_Classes");
                    int remainingClasses = totalClasses - attendedClasses;
                    double attendanceMarks = rs.getDouble("Attendance_Marks");

                    // Formatting output for better readability
                    System.out.printf("| %-4d | %-12s | %-15s | %-13d | %-8d | %-6d | %-6.2f |\n",
                            id, name, dept, totalClasses, attendedClasses, remainingClasses, attendanceMarks);
                }
            }

            System.out.println("----------------------------------------------------------------------------------------");

            rs.close();
            stmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}


public class AttendanceSystem {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/StudentAttendanceSystemss"; 
        String user = "root";  
        String password = "(mehtabali)";  

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("                  'STUDENTS ATTENDANCE MANAGEMNET SYSTEM'                      ");
            System.out.println("-------------------------------------------------------------------------------");
            MainMenu.mainmenu(); 

            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL JDBC DRIVER NOT FOUND: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("CONNECTION FAILED: " + e.getMessage());
        }
    }
}
