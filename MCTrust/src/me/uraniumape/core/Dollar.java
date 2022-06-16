package me.uraniumape.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Dollar {

	
	public static ItemStack createDollar(int amount) {
		ItemStack dollar = new ItemStack(Material.KELP, amount);
		ItemMeta dollarMeta = dollar.getItemMeta();
		
		dollarMeta.setDisplayName("§2Paper Note");
		
		dollar.setItemMeta(dollarMeta);
		
		return dollar;
	}
	
	
}
