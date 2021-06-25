package hu.Konfigbacsi00.JoinMessage.database;

import java.sql.Connection;
import java.sql.Statement;

public abstract class DatabaseHandler {
	
	public abstract Connection getConnection();
	
	public abstract Statement getStatement();

}
