package me.uraniumape.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.uraniumape.events.PlayerBuffers;

enum Checkout_Type{
	ADMIN,
	PLAYER
}

public class SignCreator {

	private static void openSignEditor(Checkout_Type type, Player p, UUID shopUUID) {
		Inventory inv = null;
		String inventoryName = null;
		
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta infoMeta = info.getItemMeta();
		
		ItemStack finish = new ItemStack(Material.FILLED_MAP);
		ItemMeta finishMeta = info.getItemMeta();
		finishMeta.setDisplayName("§a§lFinish Shop");
		finish.setItemMeta(finishMeta);
		
		StorageClass sClass = new StorageClass();
		FileConfiguration sConfig = sClass.getStore();
		
		String shopPath = "admin.checkouts." + shopUUID.toString();
		ItemStack dropArea;
		
		//If this ID is already created
		if(sConfig.get(shopPath + ".item") == null) {
			dropArea = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
			ItemMeta dropMeta = dropArea.getItemMeta();
			dropMeta.setDisplayName("§fDrop item you would like to sell here");
			dropArea.setItemMeta(dropMeta);
		}else {
			dropArea = ((ItemStack)sConfig.get(shopPath + ".item"));
		}
		
		

		
		ItemStack setPrice = new ItemStack(Material.KELP);
		ItemMeta priceMeta = setPrice.getItemMeta();
		priceMeta.setDisplayName("§aSet Price: " + MCTrust.currencyType + + PlayerBuffers.getBuffer(p));
		
		
		//The shop ID Item to identify the shop
		ItemStack shopID = new ItemStack(Material.PAINTING);
		ItemMeta shopIDMeta = shopID.getItemMeta();
		shopIDMeta.setDisplayName(ConfigValues.shop_id);
		List<String> shopIDLore = new ArrayList<String>();
		shopIDLore.add(shopUUID.toString());
		shopIDMeta.setLore(shopIDLore);
		shopID.setItemMeta(shopIDMeta);

		
		ItemStack setQuantity = new ItemStack(Material.OAK_SIGN);
		ItemMeta quantityMeta = setQuantity.getItemMeta();
		quantityMeta.setDisplayName("§7Set Quantity");
		
		
		List<String> lore = new ArrayList<String>();
		
		infoMeta.setDisplayName("§7§lHelp");
		
		switch(type) {
		
		case ADMIN:
			lore.add("*Drag an item onto the sell slot");
			lore.add("*Click the price setter to set the price");
			lore.add("*Click finish to complete the shop");
			inventoryName = "Admin Checkout";
			inv = Bukkit.createInventory(p, 45, inventoryName);
		
			break;
			
			
		case PLAYER:
			
			
			lore.add("*Drag an item onto the sell slot");
			lore.add("*Click the price setter to set the price");
			lore.add("*Click the amount setter to set the amount you want to set");
			
			lore.add("*Click finish to complete the shop");
			inventoryName = "Player Checkout";
			inv = Bukkit.createInventory(p, 45, inventoryName);
			
			setQuantity.setItemMeta(quantityMeta);
			inv.setItem(25, setQuantity);
			
			break;
			
		}
		
		infoMeta.setLore(lore);
		info.setItemMeta(infoMeta);
		setPrice.setItemMeta(priceMeta);
		
		
		inv.setItem(31, finish);
		inv.setItem(22, dropArea);
		inv.setItem(43, info);
		inv.setItem(16, setPrice);
		inv.setItem(0, shopID);
		
		p.openInventory(inv);
	}
	
	
	
	public static void openAdminCheckout(Player p, Location loc) {
		StorageClass sClass = new StorageClass();
		FileConfiguration sConfig = sClass.getStore();
		UUID shopID = UUID.randomUUID();
		Checkout_Type type = Checkout_Type.ADMIN;
		
		openSignEditor(type, p, shopID);
		
		//Store the location for this in the config
		sConfig.set("admin.checkouts." + shopID.toString() + ".location", loc.toVector().toString());
		sClass.saveStore();
		
		
	}
	
	
	//Open already made checkout
	public static void openAdminCheckout(Player p, String shopID) {
		StorageClass sClass = new StorageClass();
		FileConfiguration sConfig = sClass.getStore();

		Checkout_Type type = Checkout_Type.ADMIN;
		
		openSignEditor(type, p, UUID.fromString(shopID));

	}
	
	
	/*public static void openPlayerCheckout(Player p) {
		Checkout_Type type = Checkout_Type.PLAYER;
		openSignEditor(type, p);
	}*/
}
