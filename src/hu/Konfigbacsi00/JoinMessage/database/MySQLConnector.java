package hu.Konfigbacsi00.JoinMessage.database;

import hu.Konfigbacsi00.JoinMessage.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnector extends DatabaseHandler {
	
	private Connection con;
    private Main main = Main.getInstance();
    
    public MySQLConnector(MySQLCredentials cred){
        try{
            synchronized (this) {
                if (con != null && !con.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                boolean autoReconnect = main.getConfig().getBoolean("adatbazis.autoReconnect") ? null : true;
                boolean useSSL = main.getConfig().getBoolean("adatbazis.useSSL") ? null : false;
                con = DriverManager.getConnection("jdbc:mysql://" + cred.getHost() + ":" + cred.getPort() + "/" + cred.getDatabase() + "?autoReconnect=" + String.valueOf(autoReconnect) + "&useSSL=" + String.valueOf(useSSL), cred.getUsername(), cred.getPassword());
                main.getLogger().info("Az adatbázis kapcsolat sikeresen létrejött!");
                main.connected = true;
            }
        }catch(SQLException | ClassNotFoundException e){
        	main.connected = false;
        	main.getLogger().severe("Nem sikerült létrehozni az adatbázis kapcsolatot: " + e.getMessage());
        }
    }
    
    public Statement getStatement(){
        try {
            return con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

	@Override
	public Connection getConnection() {
		return con;
	}

}
