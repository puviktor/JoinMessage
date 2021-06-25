package hu.Konfigbacsi00.JoinMessage;

import hu.Konfigbacsi00.JoinMessage.commands.CoreCommand;
import hu.Konfigbacsi00.JoinMessage.database.DatabaseHandler;
import hu.Konfigbacsi00.JoinMessage.database.DatabaseType;
import hu.Konfigbacsi00.JoinMessage.database.FlatFileConnector;
import hu.Konfigbacsi00.JoinMessage.database.MySQLConnector;
import hu.Konfigbacsi00.JoinMessage.database.MySQLCredentials;
import hu.Konfigbacsi00.JoinMessage.listeners.PlayerListeners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	/**
	 * Private változók részlege.
	 */
	
	private File config = new File(this.getDataFolder(), "config.yml");
	private MySQLCredentials cred;
	private DatabaseHandler sql;
	private DatabaseType type;
	private static Main instance;
	
	/**
	 * Public változók részlege.
	 */
	
	public boolean connected = false;
	
	public Main(){
		instance = this;
	}
	
	public static Main getInstance(){
		return instance;
	}
	
	public MySQLCredentials getCredentials(){
		return cred;
	}
	
	public DatabaseHandler getDatabase(){
		return sql;
	}
	
	/**
	 * Adatbázis típus kiválasztása és inicializálása.
	 * 
	 * @param MySQLCredentials, DatabaseHandler
	 */
	
	public void handleDatabase(){
		type = DatabaseType.valueOf(getConfig().getString("adatbazis.tipus").toUpperCase());
		getLogger().info("Adatbázis típusa: " + type);
		
		cred = new MySQLCredentials(getConfig().getString("adatbazis.hoszt"), getConfig().getInt("adatbazis.port"), getConfig().getString("adatbazis.felhasznalo"), getConfig().getString("adatbazis.jelszo"), getConfig().getString("adatbazis.adatbazis"), getConfig().getString("adatbazis.tabla"));
		
		if(type == DatabaseType.MYSQL){
			if(!getConfig().getString("adatbazis.jelszo").equalsIgnoreCase("")){
				getLogger().info("Csatlakozás az adatbázishoz...");
				
	        	sql = new MySQLConnector(cred);
	        }else{
	        	getLogger().severe("A plugin MySQL üzemmódban van és nincsenek beállítva az adatok!");
	        	getLogger().severe("Kérlek, fejezd be az adatok beállítását, vagy válts SQLite üzemmódra!");
	        	
	        	return;
	        }
		}else{
			sql = new FlatFileConnector(this.getDataFolder() + File.separator + "adatbazis.db");
		}
		generateDatabase();
	}
	
	public void onEnable(){
		getLogger().info("A plugin betöltése megkezdõdött...");
		
		setupConfig();
		handleDatabase();
		Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
		
		getCommand("joinmsg").setExecutor(new CoreCommand());
		
		getLogger().info("A plugin sikeresen betöltött!");
	}
	
	public void onDisable(){
		getLogger().info("A sikeresen kitöltött!");
	}
	
	public void setupConfig(){
		if(!config.exists()){
			getDataFolder().mkdirs();
			getLogger().info("Alap konfiguráció másolása...");
			
			File to = config;
			InputStream from = getResource("config.yml");
			
			try {
				OutputStream out = new FileOutputStream(to);
				byte[] buffer = new byte[1024];
				int size = 0;
				
				while((size = from.read(buffer)) != -1) {
					out.write(buffer, 0, size);
				}
				
				out.close();
				from.close();
			} catch (Exception e) {
				getLogger().severe("Nem sikerült az alap konfiguráció másolása: " + e.getMessage());
			}
			
			getLogger().info("Alap konfiguráció sikeresen átmásolva!");
		}
		
		try {
			getConfig().load(config);
		} catch (FileNotFoundException e) {
			getLogger().severe("Nem sikerült a konfiguráció betöltése: " + e.getMessage());
		} catch (IOException e) {
			getLogger().severe("Nem sikerült a konfiguráció betöltése: " + e.getMessage());
		} catch (InvalidConfigurationException e) {
			getLogger().severe("Nem sikerült a konfiguráció betöltése: " + e.getMessage());
		}
		
		getLogger().info("Konfiguráció sikeresen betöltve!");
	}
	
	public void saveConfig(){
		try {
			getConfig().save(config);
		} catch (IOException e) {
			getLogger().severe("Nem sikerült a konfiguráció elmentése: " + e.getMessage());
		}
	}
	
	public void generateDatabase(){
		try {
			sql.getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS `" + cred.getTable() + "` (`joinmsg` varchar(256), `username` varchar(16), PRIMARY KEY (username))");
		} catch (SQLException e) {
			getLogger().severe("Nem sikerült az adatbázis legenerálása: " + e.getMessage());
		}
	}

}
