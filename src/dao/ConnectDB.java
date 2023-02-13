package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
	protected Connection con = null;

	public ConnectDB() {
		// 1 -> MySQL Local
		// 2 -> Heroku
		// 3 -> PostgreSQL
		int type = 2;
		switch (type) {
		case 1:
			// MySQL Local
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String connectionurl = "jdbc:mysql://localhost:3306/flowiee";
				String username = "root";
				String password = "123qwe!@#";
				con = DriverManager.getConnection(connectionurl, username, password);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case 2:
			// MySQL cloud heroku
			try {
				Class.forName("com.mysql.jdbc.Driver");
				String connectionurl = "jdbc:mysql://b25659d95db315:08113b7f@us-cdbr-east-06.cleardb.net/heroku_9ecb8f9e3a1120d?reconnect=true";
				String username = "b25659d95db315";
				String password = "08113b7f";
				con = DriverManager.getConnection(connectionurl, username, password);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		}
	}
}
