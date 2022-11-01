//package database;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class DatabaseConnection {
//	
//    private final String url = "jdbc:postgresql://localhost:5433/GameDatabase";
//    private final String user = "postgres";
//    private final String password = "Spetter6";
//	private static DatabaseConnection instance = new DatabaseConnection();
//	private Connection connection;
//	
//	private DatabaseConnection() {
//        connection = null;
//        try {
//        	connection = DriverManager.getConnection(url, user, password);
//            System.out.println("Connected to the PostgreSQL server successfully.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//	}
//
//	public static DatabaseConnection getInstance() {
//		return instance;
//	}
//	
//	public Connection getConnection() {
//		return this.connection;
//	}
//
//}
