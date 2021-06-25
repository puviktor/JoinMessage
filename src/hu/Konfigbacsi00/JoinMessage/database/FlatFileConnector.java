package hu.Konfigbacsi00.JoinMessage.database;

import hu.Konfigbacsi00.JoinMessage.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FlatFileConnector extends DatabaseHandler {
	
	private Connection con;
    private Main main = Main.getInstance();
    
    public FlatFileConnector(String path){
        try{
            synchronized (this) {
                if (con != null && !con.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:sqlite:" + path);
                main.getLogger().info("Az SQLite adatb�zis sikeresen csatlakoztatva!");
                main.connected = true;
            }
        }catch(SQLException | ClassNotFoundException e){
        	main.connected = false;
        	main.getLogger().severe("Nem siker�lt l�trehozni az adatb�zis kapcsolatot: " + e.getMessage());
        }
    }

	@Override
	public Connection getConnection() {
		return con;
	}

	@Override
	public Statement getStatement() {
		try {
			return con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
