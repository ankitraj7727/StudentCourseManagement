import java.sql.*;

public class StudentService {
    private final String GREEN = "\u001B[32m";
    private final String RED = "\u001B[31m";
    private final String RESET = "\u001B[0m";

    public void addStudent(Student s) {
        if (s.name.isEmpty() || s.course.isEmpty() || s.mobile.isEmpty()) {
            System.out.println(RED + " All fields must be filled." + RESET);
            return;
        }

        try (Connection conn = Databaseconnection.getConnection()) {
            String query = "INSERT INTO students (id, name, course, mobile) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, s.id);
            ps.setString(2, s.name);
            ps.setString(3, s.course);
            ps.setString(4, s.mobile);
            ps.executeUpdate();
            System.out.println(GREEN + " Student added successfully!" + RESET);
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(RED + " Student ID already exists." + RESET);
        } catch (Exception e) {
            System.out.println(RED + " Error: " + e.getMessage() + RESET);
        }
    }

    public void viewStudents() {
        try (Connection conn = Databaseconnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            System.out.println("\n Student Records:");
            System.out.printf("%-5s %-20s %-15s %-15s\n", "ID", "Name", "Course", "Mobile");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-15s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("mobile"));
            }
        } catch (Exception e) {
            System.out.println(RED + " Error: " + e.getMessage() + RESET);
        }
    }

    public void updateField(int id, String field, String newValue) {
        try (Connection conn = Databaseconnection.getConnection()) {
            String query;
            PreparedStatement ps;

            if (field.equalsIgnoreCase("id")) {
                query = "UPDATE students SET id = ? WHERE id = ?";
                ps = conn.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(newValue));
                ps.setInt(2, id);
            } else {
                query = "UPDATE students SET " + field + " = ? WHERE id = ?";
                ps = conn.prepareStatement(query);
                ps.setString(1, newValue);
                ps.setInt(2, id);
            }

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(GREEN + "" + field + " updated successfully!" + RESET);
            } else {
                System.out.println(RED + " Student ID not found." + RESET);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println(RED + " That ID already exists. Use a unique ID." + RESET);
        } catch (NumberFormatException e) {
            System.out.println(RED + " Invalid ID format. ID must be a number." + RESET);
        } catch (Exception e) {
            System.out.println(RED + " Error while updating " + field + ": " + e.getMessage() + RESET);
        }
    }

    public void deleteStudent(int id) {
        try (Connection conn = Databaseconnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM students WHERE id = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(GREEN + " Student deleted successfully!" + RESET);
            } else {
                System.out.println(RED + " Student not found!" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + " Error: " + e.getMessage() + RESET);
        }
    }

    public void searchById(int id) {
        try (Connection conn = Databaseconnection.getConnection()) {
            String query = "SELECT * FROM students WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("\nStudent Found:");
                System.out.println("ID     : " + rs.getInt("id"));
                System.out.println("Name   : " + rs.getString("name"));
                System.out.println("Course : " + rs.getString("course"));
                System.out.println("Mobile : " + rs.getString("mobile"));
            } else {
                System.out.println(RED + "No student found with ID:" + id + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "Error while searching:" + e.getMessage() + RESET);
        }
    }
}