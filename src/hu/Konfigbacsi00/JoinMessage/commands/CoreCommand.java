package hu.Konfigbacsi00.JoinMessage.commands;

import hu.Konfigbacsi00.JoinMessage.Main;
import hu.Konfigbacsi00.JoinMessage.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoreCommand implements CommandExecutor {
	
	private Main main = Main.getInstance();
	private Util util = new Util();
	
	public boolean onCommand(CommandSender s, Command c, String cmd, String[] args){
		if(cmd.equalsIgnoreCase("joinmsg")){
			if(!(s instanceof Player)){
				s.sendMessage(util.prefix + " " + util.only_player);
				return false;
			}
			
			Player p = (Player) s;
			
			if(!p.hasPermission("joinmsg.set")){
				s.sendMessage(util.prefix + " " + util.no_perm);
				return false;
			}
			
			if(args.length == 0){
				util.sendHelp(p);
				return false;
			}
			
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("clear")){
					util.removeWelcomeMSG(p.getName());
					p.sendMessage(util.prefix + " " + util.successfully_cleared);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("reload")){
					if(!p.hasPermission("joinmsg.admin")){
						s.sendMessage(util.prefix + " " + util.no_perm);
						return false;
					}
					
					main.reloadConfig();
					p.sendMessage(util.prefix + " " + util.successfully_reloaded);
					return true;
				}else{
					util.sendHelp(p);
					return false;
				}
			}
			
			if(args.length > 1){
				if(args[0].equalsIgnoreCase("set")){
					StringBuilder b = new StringBuilder("");
					
					for(int i = 1; i < args.length; i++){
						b.append(args[i]).append(" ");
					}
					
					util.setWelcomeMSG(p.getName(), b.toString());
					p.sendMessage(util.prefix + " " + util.successfully_set);
					return true;
				}else{
					util.sendHelp(p);
					return false;
				}
			}
		}
		return false;
	}

}
