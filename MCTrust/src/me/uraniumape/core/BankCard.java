package me.uraniumape.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BankCard {
	Player p;
	
	public BankCard(Player p) {
		this.p = p;
	}
	
	
	public void writeCheque(int amount) {
		ItemStack cheque = new ItemStack(Material.FILLED_MAP);
		ItemMeta chequeMeta = cheque.getItemMeta();
		chequeMeta.setDisplayName("§9Cheque");
		
		List<String> lore = new ArrayList<String>();
		lore.add("§2Withdraw this at a bank");
		lore.add("§2Amount: " + MCTrust.currencyType + amount);
		
		chequeMeta.setLore(lore);
		
		cheque.setItemMeta(chequeMeta);
		
		p.getInventory().addItem(cheque);
		
		
	}
	
	
}
