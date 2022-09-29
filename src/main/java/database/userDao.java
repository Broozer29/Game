package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class userDao {
	
	private String queryString;
	private DatabaseConnection connection;
	
	public userDao() {
		this.connection = DatabaseConnection.getInstance();
	}
	
	public boolean insertUser(String userName) {
		queryString = "INSERT INTO Players(ID, Name) "
                + "VALUES(?,?)";
		UUID uuid = UUID.randomUUID();
		String name = "DoesThisWork? Hopefully";
		Connection conn = connection.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(queryString);
            pstmt.setObject(1, uuid);
            pstmt.setString(2, name);
			
            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	public boolean deleteUser(String userName) {
		return true;
	}
	
	
}
