package hu.Konfigbacsi00.JoinMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Util {
	
	private Main main = Main.getInstance();
	
	public String prefix = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.prefix"));
	public String successfully_set = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.beallitva"));
	public String successfully_cleared = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.torolve"));
	public String successfully_reloaded = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.konfig_ujratoltve"));
	public String no_perm = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.nincs_jog"));
	public String only_player = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.csak_jatekos"));
	public String connect = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.csatlakozas"));
	public String disconnect = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("uzenetek.lecsatlakozas"));
	
	public boolean hasWelcomeMSG(String p){
		ResultSet result = null;
		try {
			result = main.getDatabase().getStatement().executeQuery("SELECT * FROM " + main.getCredentials().getTable() + " WHERE username = '" + p + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getWelcomeMSG(String p){
		ResultSet result = null;
		try {
			result = main.getDatabase().getStatement().executeQuery("SELECT * FROM " + main.getCredentials().getTable() + " WHERE username = '" + p + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			while (result.next()) {
				return result.getString("joinmsg");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setWelcomeMSG(String p, String msg){
		Player player = Bukkit.getPlayer(p);
		
		try {
			if(hasWelcomeMSG(p)){
				main.getDatabase().getStatement().executeUpdate("UPDATE " + main.getCredentials().getTable() + " SET joinmsg = '" + msg + "' WHERE username = '" + p + "';");
			}else{
				main.getDatabase().getStatement().executeUpdate("INSERT INTO " + main.getCredentials().getTable() + " (username, joinmsg) VALUES ('" + p + "', '" + msg + "');");
			}
			
		} catch(SQLException e){
			
		}
	}
	
	public void removeWelcomeMSG(String p){
		try {
			main.getDatabase().getStatement().executeUpdate("DELETE FROM " + main.getCredentials().getTable() + " WHERE username='" + p + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHelp(Player p){
		List<String> help = main.getConfig().getStringList("uzenetek.segitseg");
		
		if(p.hasPermission("joinmsg.admin")){
			help = main.getConfig().getStringList("uzenetek.segitseg_admin");
		}
		
		for(String s : help){ 
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replaceAll("%prefix%", prefix));
		}
		
		help.clear();
	}

}
