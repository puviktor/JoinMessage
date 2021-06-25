package hu.Konfigbacsi00.JoinMessage.listeners;

import hu.Konfigbacsi00.JoinMessage.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {
	
	private Util util = new Util();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		
		if(util.hasWelcomeMSG(p.getName())){
			e.setJoinMessage(util.getWelcomeMSG(p.getName()));
			return;
		}
		
		e.setJoinMessage(util.connect.replaceAll("%jatekos%", p.getName()));
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		Player p = e.getPlayer();
		
		e.setQuitMessage(util.disconnect.replaceAll("%jatekos%", p.getName()));
	}

}
