package me.uraniumape.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import me.uraniumape.core.ConfigValues;
import me.uraniumape.core.Dollar;
import me.uraniumape.core.MCTrust;

public class bankadmin implements CommandExecutor{
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
			//Player p = (Player) sender;
		switch(args[0]) {
			
			//Give physical currency
			case "give":
				if(sender.hasPermission(MCTrust.permissionPrefix + ".admin.give")) {
					Player target = Bukkit.getPlayer(args[1]);
					int amount = Integer.parseInt(args[2]);
					ItemStack dollar = Dollar.createDollar(amount);
					
					target.getInventory().addItem(dollar);
				}else {
					sender.sendMessage(ConfigValues.msg_color_negative + ConfigValues.permission_message);
				}

				
				
				break;
				
			case "spawn-teller":
				if(sender instanceof Player && sender.hasPermission(MCTrust.permissionPrefix + ".admin.spawn")) {
					Player p = (Player)sender;
					Villager villager = (Villager)p.getWorld().spawnEntity(p.getLocation(),EntityType.VILLAGER);
					villager.setCustomName("§9" + ConfigValues.bank_teller);
					villager.setAI(false);
				}else {
					sender.sendMessage(ConfigValues.msg_color_negative + ConfigValues.permission_message);
				}
	
				
				break;
			case "spawn-atm":
				if(sender instanceof Player && sender.hasPermission(MCTrust.permissionPrefix + ".admin.spawn")) {
					Player p = (Player)sender;
					Pillager pillager = (Pillager)p.getWorld().spawnEntity(p.getLocation(),EntityType.PILLAGER);
					pillager.setCustomName("§9" + ConfigValues.atm);
					pillager.getEquipment().clear();
					pillager.setAI(false);
				}else {
					sender.sendMessage(ConfigValues.msg_color_negative + ConfigValues.permission_message);
				}

				
				break;
		}
		

		
		return true;
	}

}
