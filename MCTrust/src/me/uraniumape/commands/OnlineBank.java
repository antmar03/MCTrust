package me.uraniumape.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.uraniumape.core.BankCard;
import me.uraniumape.core.Dollar;
import me.uraniumape.core.MCTrust;
import net.milkbowl.vault.economy.Economy;

public class OnlineBank implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			Economy econ = MCTrust.getEconomy(); 
			
			switch(args[0]) {
				case "balance":
					if(p.hasPermission(MCTrust.permissionPrefix + ".onlinebank.balance")) {
						p.sendMessage("§2Your balance is: " + MCTrust.currencyType + String.valueOf(econ.getBalance(p)));
					}else {
						p.sendMessage("§cYou do not have permission to perform this command");
					}
					
				break;
				

				case "writecheque":
					
					if(p.hasPermission(MCTrust.permissionPrefix + ".onlinebank.writecheque")) {
						int amount;
						
						try {
							amount = Integer.parseInt(args[1]);
						}catch(NumberFormatException e){
							p.sendMessage("§2Amount must be a number");
							return true;
						}
						
						if(econ.getBalance(p) >= amount) {
							BankCard cheque = new BankCard(p);
							cheque.writeCheque(amount);
							econ.withdrawPlayer(p, amount);
						}
					}
					
				 break;
			}
			
			
		}

		return true;
	}

}
