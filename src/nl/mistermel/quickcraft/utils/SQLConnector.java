package nl.mistermel.quickcraft.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

import nl.mistermel.quickcraft.QuickCraft;

public class SQLConnector {

	private Connection connection;
	private String host, database, username, password;
	private int port;
	
	private FileConfiguration conf;
	
	public SQLConnector() {
		conf = QuickCraft.getConfigManager().getConfigFile();
		
		host = conf.getString("sql.host");
		database = conf.getString("sql.database");
		username = conf.getString("sql.username");
		password = conf.getString("sql.password");
		port = conf.getInt("sql.port");
		
		if(!conf.getBoolean("sql.enabled")) return;
		
		try {
			openConnection();
			createTables();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void openConnection() throws SQLException, ClassNotFoundException {
		if(connection != null && !connection.isClosed())
			return;
		
		synchronized(this) {
			if(connection != null && !connection.isClosed())
				return;
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
		}
	}
	
	private void createTables() throws SQLException {
		if(connection == null || connection.isClosed())
			return;
		
		Statement statement = connection.createStatement();
		statement.executeQuery("CREATE TABLE players (uuid text(40), score int);");
	}
	
	public boolean isEnabled() {
		return conf.getBoolean("sql.enabled");
	}
}
