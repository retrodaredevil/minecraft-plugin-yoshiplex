package com.yoshiplex.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.yoshiplex.Main;
import com.yoshiplex.particles.Particles;

public class ParticleCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = cmd.getName();
		if(label.equalsIgnoreCase("particle") || label.equalsIgnoreCase("particles") || label.equalsIgnoreCase("part")){
			if(!(sender instanceof Player)){
				sender.sendMessage("Only players can do this.");
				return true;
			}
			Player p = (Player) sender;
			if(args.length == 0){
				p.sendMessage(ChatColor.RED + "We do not have a particle menu yet. For now do /particle <particle-name>");
				return true;
			} else if (args.length > 0){
				if(args[0].equalsIgnoreCase("remove")){
					for(Particles particle : Particles.getAll()){
						particle.removePlayer(p);
					}
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.sendMessage(ChatColor.GREEN + "Your particle has been removed.");
					return true;
				}
				
				Particles request = null;
				for(Particles particle : Particles.getAll()){
					System.out.println("name:" + particle.getName());
					if(args[0].equalsIgnoreCase(particle.getName())){
						request = particle;
						break;
					}
				}
				System.out.println("size: " + Particles.getAll().size());
				if(request == null){
					p.sendMessage(ChatColor.RED + "That particle does not exist.");
					return true;
				}
				List<String> particles = Main.config.getStringList("players." + p.getUniqueId() + ".particles");
				if(particles != null && !particles.isEmpty() && particles.contains(request.getName())){
					for(Particles particle : Particles.getAll()){
						particle.removePlayer(p);
					}
					request.addPlayer(p);
					p.sendMessage(ChatColor.GREEN + "You are now using the " + ChatColor.RED + request.getName() + ChatColor.GREEN + " particle!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "You do not have this particle!");
					return true;
				}
			} else {
				p.sendMessage("How the heck did you put in negative arguments???");
			}
		}
		return false;
	}

}
