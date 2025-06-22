import java.sql.Connection;
import java.sql.DriverManager;

public class Databaseconnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/student_db";  
        String username = "root"; 
        String password = "dev@7727"; 
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }
}
