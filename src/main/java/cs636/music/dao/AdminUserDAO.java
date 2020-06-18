package cs636.music.dao;

import static cs636.music.dao.DBConstants.ADMIN_TABLE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;


@Repository
public class AdminUserDAO {
	
	public AdminUserDAO (DbUtils db) {
	}
	
	public Boolean findAdminUser(Connection connection, String uid, String pwd) throws SQLException {
		System.out.println("Entered DAO layer:-");
		Statement stmt = connection.createStatement();
		try {
			ResultSet set = stmt.executeQuery(" select * from " + ADMIN_TABLE +
					" where username = '" + uid + "'" +
					" and password = '" + pwd + "'");
			if (set.next()) {
				set.close();
				return true;
			}
		} finally {
			stmt.close();
		}
		return false;
	}
}
