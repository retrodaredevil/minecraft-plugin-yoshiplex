package com.yoshiplex.reload;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.yoshiplex.Main;

public class ReloadManager implements CommandExecutor{

	private static ReloadManager instance = null;
	
	private List<Runnable> r = new ArrayList<>();
	private Main main;
	
	
	public ReloadManager(Main main){
		instance = this;
		main.getCommand("ypreload").setExecutor(this);
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("ypreload")){
			if(sender.hasPermission("yp.reload")){
				main.reloadConfig();
				try{
					reload();
				} catch(Exception e){
					sender.sendMessage("There was an error: "  + e.getMessage());
					e.printStackTrace();
					return true;
				}
				sender.sendMessage("Success!");
				return true;
			}
		}
		return false;
	}
	public static void reload(){
		for(Runnable run : instance.r){
			run.run();
		}
	}
	
	public static void registerReload(Runnable run){
		instance.r.add(run);
	}

}
