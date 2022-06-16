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
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.uraniumape.core.ConfigValues;
import me.uraniumape.core.Dollar;
import me.uraniumape.core.MCTrust;
import me.uraniumape.core.StorageClass;
import net.milkbowl.vault.economy.Economy;

public class PurchaseEvents implements Listener{

	private Economy econ = MCTrust.getEconomy();
	
	
	private void openShop(Player p, ItemStack item, int price, String shopUUID) {
		Inventory inv = Bukkit.createInventory(p, 27, "Shop");
		
		List<String> priceLore = new ArrayList<String>();
		ItemMeta itemMeta = item.getItemMeta();
		priceLore.add(ConfigValues.price + "§2" + MCTrust.currencyType + price);
		itemMeta.setLore(priceLore);
		item.setItemMeta(itemMeta);
		
		//Shop ID
		ItemStack shopID = new ItemStack(Material.PAINTING);
		ItemMeta shopIDMeta = shopID.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(shopUUID);
		shopIDMeta.setDisplayName(ConfigValues.shop_id);
		shopIDMeta.setLore(lore);
		shopID.setItemMeta(shopIDMeta);
		
		//Pay with cash item
		ItemStack payWithCash = new ItemStack(Material.KELP);
		ItemMeta payWithCashMeta = payWithCash.getItemMeta();
		payWithCashMeta.setDisplayName("§2" + ConfigValues.pay_with_cash);
		payWithCash.setItemMeta(payWithCashMeta);
		
		
		//Pay with debit item
		ItemStack payWithDebit = new ItemStack(Material.PAPER);
		ItemMeta payWithDebitMeta = payWithDebit.getItemMeta();
		payWithDebitMeta.setDisplayName("§2" + ConfigValues.pay_with_debit);
		payWithDebit.setItemMeta(payWithDebitMeta);
		

		
		//Set the item to the shop item
		inv.setItem(0, shopID);
		inv.setItem(12, payWithCash);
		inv.setItem(13, item);
		inv.setItem(14, payWithDebit);
		
		p.openInventory(inv);
		
	}
	
	//Shop Event Handler
	@EventHandler
	public void clickShop(InventoryClickEvent e) {
		if(e.getView().getTitle().equals("Shop")) {
			Player p = (Player)e.getWhoClicked();
			String clickedName = e.getCurrentItem().getItemMeta().getDisplayName();
			ItemStack saleItem = e.getInventory().getItem(13);
			
			//Long ugly string to get the price
			int price = Integer.parseInt(saleItem.getItemMeta().getLore().get(0).split(" ")[1].substring(3));
			//Long ugly string to get message
			String purchaseMsg = ConfigValues.msg_color_positive + (ConfigValues.purchase_message.replace("%item%", saleItem.getItemMeta().getDisplayName())
					.replace("%amount%", MCTrust.currencyType + String.valueOf(price)));
			
			if(clickedName.contains(ConfigValues.pay_with_debit)) {
				if(econ.getBalance(p) >= price) {
					

					
					econ.withdrawPlayer(p, price);
					p.getInventory().addItem(saleItem);
					e.setCancelled(true);
					p.sendMessage(purchaseMsg);
				}else{
					p.sendMessage(ConfigValues.msg_color_negative + ConfigValues.not_enough_bank);
				}
			
				
				
				e.setCancelled(true);
				
			}else if(clickedName.contains(ConfigValues.pay_with_cash)) {
				
				//If the player has enough cash in their inventory
				if(p.getInventory().containsAtLeast(Dollar.createDollar(1), price)) {
					p.getInventory().removeItem(Dollar.createDollar(price));
					p.getInventory().addItem(saleItem);
					String itemName = "";
					
					if(saleItem.getItemMeta().hasDisplayName()) {
						itemName = saleItem.getItemMeta().getDisplayName();
					}else {
						itemName = saleItem.getType().name();
					}
					
					
					p.sendMessage(purchaseMsg);
					
				}else{
					p.sendMessage(ConfigValues.msg_color_negative + ConfigValues.not_enough_cash);
				}
				
				e.setCancelled(true);
			}
			
			p.closeInventory();
		}
	}
	
	
	@EventHandler
	public void clickSign(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			if(b.getState() instanceof Sign) {
				Sign s = (Sign)b.getState();
				if(s.getLine(0).contains(ConfigValues.shop_name)) {
					int price = 0;
					ItemStack item = null;
					Location loc = b.getLocation();
					StorageClass sClass = new StorageClass();
					FileConfiguration sConfig = sClass.getStore();
					
					//Loop through all shops
					for(String sec : sConfig.getConfigurationSection("admin.checkouts").getKeys(false)) {
						
						//If the location of the shop is the sign the player clicked, load the info into variables
						if(sConfig.getString("admin.checkouts." + sec + ".location").equals(loc.toVector().toString())) {
							price = sConfig.getInt("admin.checkouts." + sec + ".price");
							item = sConfig.getItemStack("admin.checkouts." + sec + ".item");
							openShop(e.getPlayer(), item, price, sec);
						}
					}

				}
			}
		}
	}
	
	
}
