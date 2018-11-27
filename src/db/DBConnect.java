package db;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnect {

	static private final String jdbcUrl = "jdbc:mysql://localhost/passwords?user=root&password=root";
	static private DataSource ds = null;
	
	public static DataSource getDataSource() {
		if (ds == null) {
			//crea i DataSource
			try {
				ds = DataSources.pooledDataSource(
						DataSources.unpooledDataSource(jdbcUrl));
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return ds;
	}

	public static Connection getConnection() {
		getDataSource();
		
		try {

			Connection conn = ds.getConnection();
			return conn;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot get connection " + jdbcUrl, e);
		}
	}

}
