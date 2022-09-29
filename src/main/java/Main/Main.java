package Main;

import java.awt.EventQueue;

import javax.swing.JFrame;

import Main.Data.DataClass;
import database.DatabaseConnection;
import database.userDao;

public class Main extends JFrame {

	private DataClass data;
	private DatabaseConnection connection;

	public Main() {
		
		connection = DatabaseConnection.getInstance();
		data = DataClass.getInstance();
		System.out.println(connection.getConnection());
		userDao dao = new userDao();
		dao.insertUser("test");
		//initUI();
	}

	private void initUI() {

		add(new Board());

		setSize(data.getWindowWidth(), data.getWindowHeight());
		setResizable(false);

		setTitle("Test");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Main ex = new Main();
				ex.setVisible(true);
			}
		});
	}
}
