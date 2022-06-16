package me.uraniumape.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.uraniumape.core.MCTrust;
import me.uraniumape.core.SignCreator;
import me.uraniumape.core.StorageClass;

public class RegisterEvents implements Listener{

	//Initialize the player buffer on join
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		PlayerBuffers.initializeBuffer(e.getPlayer());
	}
	
	//Create an increment GUI for the store
	public Inventory createIncrementGUI (String name, Player p, String StoreUUID) {
		Inventory inv = Bukkit.createInventory(p, 18, name);
		
		ItemStack storeID = new ItemStack(Material.PAINTING);
		ItemMeta storeIDMeta = storeID.getItemMeta();
		storeIDMeta.setDisplayName("Store ID");
		List<String> storeIDLore = new ArrayList<String>();
		storeIDLore.add(StoreUUID);
		storeIDMeta.setLore(storeIDLore);
		storeID.setItemMeta(storeIDMeta);
		
		
		ItemStack withdraw = new ItemStack(Material.GREEN_CANDLE);
		ItemMeta withdrawwMeta = withdraw.getItemMeta();
		withdrawwMeta.setDisplayName("§aFinish");
		withdraw.setItemMeta(withdrawwMeta);
		
		ItemStack withdrawlAmount = new ItemStack(Material.PAPER);
		ItemMeta withdrawMeta = withdrawlAmount.getItemMeta();
		withdrawMeta.setDisplayName("§2Amount" + " " + MCTrust.currencyType + PlayerBuffers.getBuffer(p));
		withdrawlAmount.setItemMeta(withdrawMeta);
		
		ItemStack addOne = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta addOneMeta = addOne.getItemMeta();
		addOneMeta.setDisplayName("§a Add One");
		addOne.setItemMeta(addOneMeta);
		
		ItemStack addTen = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 10);
		ItemMeta addTenMeta = addTen.getItemMeta();
		addTenMeta.setDisplayName("§a Add Ten");
		addTen.setItemMeta(addTenMeta);
		
		ItemStack addMax = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 64);
		ItemMeta addMaxMeta = addMax.getItemMeta();
		addMaxMeta.setDisplayName("§a Add Max");
		addMax.setItemMeta(addMaxMeta);
		
		
		ItemStack takeOne = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta takeOneMeta = takeOne.getItemMeta();
		takeOneMeta.setDisplayName("§c Take One");
		takeOne.setItemMeta(takeOneMeta);
		
		ItemStack takeTen = new ItemStack(Material.RED_STAINED_GLASS_PANE, 10);
		ItemMeta takeTenMeta = takeTen.getItemMeta();
		takeTenMeta.setDisplayName("§c Take Ten");
		takeTen.setItemMeta(takeTenMeta);
		
		ItemStack takeMax = new ItemStack(Material.RED_STAINED_GLASS_PANE, 64);
		ItemMeta takeMaxMeta = takeMax.getItemMeta();
		takeMaxMeta.setDisplayName("§c Take Max");
		takeMax.setItemMeta(takeMaxMeta);
		
		
		
		inv.setItem(13, withdraw);
		
		inv.setItem(0, storeID);
		inv.setItem(1, takeMax);
		inv.setItem(2, takeTen);
		inv.setItem(3, takeOne);
		inv.setItem(4, withdrawlAmount);
		inv.setItem(5, addOne);
		inv.setItem(6, addTen);
		inv.setItem(7, addMax);
		
		
		return inv;
		
	}
	
	
	@EventHandler
	public void onIncrementChange(InventoryClickEvent e) {
		if(e.getView().getTitle().contains("Set Price")) {
			String StoreID = e.getInventory().getItem(0).getItemMeta().getLore().get(0);
			Player p = (Player)e.getWhoClicked();
			ItemStack clickedItem = e.getCurrentItem();
			
			String clickedName = clickedItem.getItemMeta().getDisplayName();
			
			//Disable taking items from menu
			e.setCancelled(true);
			
			
			if(clickedName.contains("Add One")) {
				PlayerBuffers.addToBuffer(p, 1);
			}else if(clickedName.contains("Add Ten")) {
				PlayerBuffers.addToBuffer(p, 10);
			}else if(clickedName.contains("Add Max")) {
				PlayerBuffers.addToBuffer(p, 64);
			}else if(clickedName.contains("Take One")) {
				PlayerBuffers.takeFromBuffer(p, 1);
			}else if(clickedName.contains("Take Ten")) {
				PlayerBuffers.takeFromBuffer(p, 10);
			}else if(clickedName.contains("Take Max")) {
				PlayerBuffers.takeFromBuffer(p, 64);
			}else if(clickedName.contains("Finish")) {
				StorageClass sClass = new StorageClass();
				FileConfiguration sConfig = sClass.getStore();
				
				String shopPath = "admin.checkouts." + StoreID;
				
				//Save price to store
				sConfig.set(shopPath + ".price", PlayerBuffers.getBuffer(p));
				sClass.saveStore();

				
				
				SignCreator.openAdminCheckout(p, StoreID);
				return;
			}
			
			p.openInventory(createIncrementGUI("Set Price", p, StoreID));
			
		}
	}
	
	@EventHandler
	public void onSignEditorClick(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		ItemStack clickedItem = e.getCurrentItem();

		//TODO Player Shop
		
		/*if(e.getView().getTitle().contains("Player Checkout")) {
			
			if(clickedItem.getItemMeta().getDisplayName().contains("Set Price")) {
				
				openPriceMenu();
				e.setCancelled(true);
			}*/
			
			
		//If the player is inside of an admin checkout creator
		if(e.getView().getTitle().contains("Admin Checkout")) {
			String StoreID = e.getInventory().getItem(0).getItemMeta().getLore().get(0);
			
			if(clickedItem.getItemMeta().getDisplayName().contains("Set Price")) {
				p.openInventory(createIncrementGUI("Set Price", p, StoreID));
			}else
			
				
				
			if(clickedItem.getItemMeta().getDisplayName().contains("§fDrop item you would like to sell here")) {
				
				//If the player is holding an item, place it in the shop info and then switch the pane for the item
				if(p.getItemOnCursor() != null) {
					ItemStack sellItem = p.getItemOnCursor();
					e.getInventory().setItem(e.getSlot(), sellItem);
					StorageClass sClass = new StorageClass();
					FileConfiguration sConfig = sClass.getStore();
					String shopPath = "admin.checkouts." + StoreID;
					sConfig.set(shopPath + ".item", sellItem);
					sClass.saveStore();
				}
				e.setCancelled(true);
				
			}else if(clickedItem.getItemMeta().getDisplayName().contains("Finish Shop")) {
				int sellSlot = 22;
				int priceSlot = 16;
				String priceString = e.getInventory().getItem(priceSlot).getItemMeta().getDisplayName().split(" ")[2];
				
				
				ItemStack sellItem = e.getInventory().getItem(sellSlot);
				int price = Integer.parseInt(priceString.substring(1));
				StorageClass sClass = new StorageClass();
				FileConfiguration sConfig = sClass.getStore();
				String shopPath = "admin.checkouts." + StoreID;
				
				//Store the info
				sConfig.set(shopPath + ".item", sellItem);
				sConfig.set(shopPath + ".price", price);
				sClass.saveStore();
				
				//Create the xyz from the config
				String[] xyz = sConfig.getString(shopPath + ".location").split(",");
				double x,y,z;
				x = Double.parseDouble(xyz[0]);
				y = Double.parseDouble(xyz[1]);
				z = Double.parseDouble(xyz[2]);
				
				Location shopLoc = new Location(p.getWorld(),x,y,z);
				Block shopBlock = p.getWorld().getBlockAt(shopLoc);
				Sign sign = (Sign)shopBlock.getState();
				String itemName = "";
				
				//Item name checker
				if(sellItem.getItemMeta().hasDisplayName()) {
					itemName = sellItem.getItemMeta().getDisplayName();
				}else {
					itemName = sellItem.getType().name();
				}
				
				//Change the sign info
				sign.setLine(0, "§f[Vendor]");
				sign.setLine(1, itemName);
				sign.setLine(2, "§a" + MCTrust.currencyType + String.valueOf(price));
				sign.update();
				
				
				//Clear the players buffer
				PlayerBuffers.initializeBuffer(p);
				
				e.setCancelled(true);
				p.closeInventory();
				
				
			}
			
			
			
			
		}
	}
	
	
	//Open the sign creator
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		String signConstructor = e.getLine(0);
		Player p = e.getPlayer();
		
		if(signConstructor.equals("[Admin-Register]")) {
			if(p.hasPermission(MCTrust.permissionPrefix + ".admin.createshop")) {
				SignCreator.openAdminCheckout(p, e.getBlock().getLocation());
			}else {
				p.sendMessage("§2You do not have permission to make this sign");
			}
		}//else if(signConstructor.equals("[Register]")){
			//SignCreator.openPlayerCheckout(p, e.getBlock().getLocation());
		//}
	}
}
