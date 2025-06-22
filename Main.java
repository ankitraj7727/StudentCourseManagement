import java.sql.*;
import java.util.Scanner;

public class Main {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService service = new StudentService();

        while (true) {
            System.out.println(CYAN + "\nSTUDENT COURSE MANAGEMENT SYSTEM" + RESET);
            System.out.println(YELLOW + "1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student by ID");
            System.out.println("6. Exit" + RESET);
            System.out.print(CYAN + "Enter your choice: " + RESET);
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Course: ");
                    String course = sc.nextLine();
                    System.out.print("Enter Mobile: ");
                    String mobile = sc.nextLine();
                    Student s = new Student(id, name, course, mobile);
                    service.addStudent(s);
                    break;

                case 2:
                    service.viewStudents();
                    break;

                case 3:
                    System.out.print("Enter ID of student to update: ");
                    int updateId = sc.nextInt(); sc.nextLine();

                    try {
                        Connection conn = Databaseconnection.getConnection();
                        PreparedStatement ps = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
                        ps.setInt(1, updateId);
                        ResultSet rs = ps.executeQuery();

                        if (!rs.next()) {
                            System.out.println(RED + " Student not found." + RESET);
                            break;
                        }

                        System.out.println(" Current Details:");
                        System.out.println("ID: " + rs.getInt("id"));
                        System.out.println("Name: " + rs.getString("name"));
                        System.out.println("Course: " + rs.getString("course"));
                        System.out.println("Mobile: " + rs.getString("mobile"));

                        boolean continueUpdate = true;
                        int currentId = updateId;

                        while (continueUpdate) {
                            System.out.println(" What do you want to update?");
                            System.out.println("1. ID");
                            System.out.println("2. Name");
                            System.out.println("3. Course");
                            System.out.println("4. Mobile");
                            System.out.print("Enter your choice: ");
                            int fieldChoice = sc.nextInt(); sc.nextLine();

                            String field = "", newValue;
                            switch (fieldChoice) {
                                case 1: field = "id"; break;
                                case 2: field = "name"; break;
                                case 3: field = "course"; break;
                                case 4: field = "mobile"; break;
                                default:
                                    System.out.println(RED + " Invalid choice." + RESET);
                                    continue;
                            }

                            System.out.print("Enter new value for " + field + ": ");
                            newValue = sc.nextLine();
                            service.updateField(currentId, field, newValue);

                            if (field.equals("id")) {
                                currentId = Integer.parseInt(newValue);
                            }

                            System.out.print("Do you want to update more fields for this student? (yes/no): ");
                            String more = sc.nextLine();
                            if (!more.equalsIgnoreCase("yes")) {
                                continueUpdate = false;
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(RED + " Error: " + e.getMessage() + RESET);
                    }
                    break;

                case 4:
                    System.out.print("Enter ID of student to delete: ");
                    int deleteId = sc.nextInt();
                    service.deleteStudent(deleteId);
                    break;

                case 5:
                    System.out.print("Enter student ID to search: ");
                    int searchId = sc.nextInt(); sc.nextLine();
                    service.searchById(searchId);
                    break;

                case 6:
                    System.out.println(GREEN + " Exiting the program. Goodbye!" + RESET);
                    System.exit(0);
                    break;

                default:
                    System.out.println(RED + " Invalid choice. Please try again." + RESET);
            }
        }
    }
}
