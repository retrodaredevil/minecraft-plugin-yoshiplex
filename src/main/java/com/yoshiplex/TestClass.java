package com.yoshiplex;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.yoshiplex.rainbow.RainbowText;


public class TestClass implements CommandExecutor{
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player) sender;
			if(p.getName().equalsIgnoreCase("retrodaredevil")){
				RainbowText text = new RainbowText("This is some rainbow text. You can make it as long as you want.");
				p.sendMessage(text.getText());
				text.moveRainbow();
				p.sendMessage(text.getText());
				text.moveRainbow();
				p.sendMessage(text.getText());
				RainbowText text2 = new RainbowText("As you see above, you can even change where the rainbow starts.");
				text2.setPlace(9);
				p.sendMessage(text2.getText());
				p.sendMessage("pitch: " + p.getLocation().getPitch() + " direction" +  p.getLocation().getDirection());
			} else {
				p.sendMessage("Sorry... This command does nothing when you use it.");
			}
			
		} else {
			sender.sendMessage("... players can only use this...");
		}
		return true;
	}
	
}
