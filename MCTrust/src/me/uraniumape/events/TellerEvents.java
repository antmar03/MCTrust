package me.uraniumape.events;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitScheduler;

import me.uraniumape.core.AccountGUI;
import me.uraniumape.core.ConfigValues;
import me.uraniumape.core.Dollar;
import me.uraniumape.core.MCTrust;
import net.milkbowl.vault.economy.Economy;

public class TellerEvents implements Listener{
	Economy econ = MCTrust.getEconomy();
	int oldBuffer = 0;
	
	//Bank fee on player join
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
        BukkitScheduler scheduler = MCTrust.getInstance().getServer().getScheduler();
        int taskID = scheduler.scheduleSyncRepeatingTask(MCTrust.getInstance(), new Runnable() {
            @Override
            public void run() {
            	Player p = e.getPlayer();
                double pBal = econ.getBalance(p);
                double bankFee = MCTrust.bankFee;
                
                
                //Take fee
                if(pBal - bankFee >= 0) {
                	econ.withdrawPlayer(p, bankFee);
                }else if(pBal - bankFee < 0) {
                	econ.withdrawPlayer(p, pBal);
                	bankFee = pBal;
                }
                
               
                if(p.isOnline()) {
                	p.sendMessage("§2You have paid a bank fee of §a" + MCTrust.currencyType + bankFee);
                }
                
                
            }
        }, 0L, MCTrust.bankFeeInterval);
		
        PlayerBuffers.setScheduler(e.getPlayer(), taskID);
		
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		
		//Stop fee on player leave
		BukkitScheduler scheduler = MCTrust.getInstance().getServer().getScheduler();
		int taskID = PlayerBuffers.getScheduler(e.getPlayer());
		
		scheduler.cancelTask(taskID);
	}
	
	
	@EventHandler
	public void onClickVillager(PlayerInteractEntityEvent e) {
		Entity clicked = e.getRightClicked();
		Player p = e.getPlayer();
		
		if(clicked.getCustomName().equals("§9" + ConfigValues.bank_teller) || clicked.getCustomName().equals("§9" + ConfigValues.atm)) {
			AccountGUI account = new AccountGUI(p);
			account.openAccount();
		}
	}
	
	
	private Inventory populateInventory(Player p) {
		Inventory inv = Bukkit.createInventory(p, 54, "Transfer Menu");
		ItemStack head;
		ItemMeta headMeta;
		SkullMeta skullMeta;
		
		int prevIndex = 0;
		
		//Loop through all players and add their head to the inventory
		for(Player currentPlayer : Bukkit.getOnlinePlayers()) {
			head = new ItemStack(Material.PLAYER_HEAD);
			headMeta = head.getItemMeta();
			
			skullMeta = (SkullMeta)headMeta;
			skullMeta.setOwningPlayer(currentPlayer);
			skullMeta.setDisplayName("§f§l" + currentPlayer.getName());
			
			head.setItemMeta(skullMeta);
			
			inv.setItem(prevIndex, head);
			
			prevIndex++;
		}
		
		return inv;
		
	}
	
	private void openTransferInventory(Player p) {
		Inventory inv = populateInventory(p);
		
		//Info item
		ItemStack info = new ItemStack(Material.PAPER);
		ItemMeta infoMeta = info.getItemMeta();
		List<String> lore = new ArrayList<String>();
		infoMeta.setDisplayName("§f§lHelp");
		lore.add("*Click a player to transfer to");
		infoMeta.setLore(lore);
		info.setItemMeta(infoMeta);
		
		
		inv.setItem(44, info);
		
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClickTransferInventory(InventoryClickEvent e) {
		if(e.getView().getTitle().contains("Transfer Menu")) {
			ItemStack clickedItem = e.getCurrentItem();
			if(clickedItem.getType() == Material.PLAYER_HEAD) {
				SkullMeta skullMeta = (SkullMeta)clickedItem.getItemMeta();
				Player target = (Player)skullMeta.getOwningPlayer();
				Player p = (Player)e.getWhoClicked();
				AccountGUI account = new AccountGUI(p);
				e.setCancelled(true);
				account.openTransfer(target);

			}
			
			
		}else if (e.getView().getTitle().contains("Transfer to")) {
			Player p = (Player)e.getWhoClicked();
			AccountGUI account = new AccountGUI(p);
			String clickedName = e.getCurrentItem().getItemMeta().getDisplayName();
			String targetName = e.getInventory().getItem(4).getItemMeta().getLore().get(0);
			Player target = Bukkit.getPlayer(targetName);
			
			
			if(econ.getBalance(p) - account.getBuffer() >= 1.0 && clickedName.contains(ConfigValues.add_one)) {
				Bukkit.getLogger().info("work please");
				PlayerBuffers.addToBuffer(p, 1);
			}else if(econ.getBalance(p) - account.getBuffer() >= 10.0  && clickedName.contains(ConfigValues.add_ten)) {
				PlayerBuffers.addToBuffer(p, 10);
			}else if(econ.getBalance(p) - account.getBuffer() >= 64.0 && clickedName.contains(ConfigValues.add_max)) {
				PlayerBuffers.addToBuffer(p, 64);
			}else if(account.getBuffer() - 1 >= 0 && clickedName.contains(ConfigValues.take_one)) {
				PlayerBuffers.takeFromBuffer(p, 1);
			}else if(account.getBuffer() - 10 >= 0 && clickedName.contains(ConfigValues.take_ten)) {
				PlayerBuffers.takeFromBuffer(p, 10);
			}else if(account.getBuffer() - 64 >= 0 && clickedName.contains(ConfigValues.take_max)) {
				PlayerBuffers.takeFromBuffer(p, 64);
			}else if(clickedName.contains("Finish Transaction")) {
				//TODO Fix ugliness of this
				
				String transferTo = ConfigValues.transfer_message.replace("%player%", targetName).replace("%amount%", 
						String.valueOf(PlayerBuffers.getBuffer(p)));
				String transferFrom = ConfigValues.transfer_message_from.replace("%player%", p.getName()).replace("%amount%", 
						String.valueOf(PlayerBuffers.getBuffer(p)));;
						
				//Do the transaction
				econ.withdrawPlayer(p, PlayerBuffers.getBuffer(p));
				econ.depositPlayer(target, PlayerBuffers.getBuffer(p));
				
				//Send confirmation messages to player
				p.sendMessage(ConfigValues.msg_color_positive + transferTo);
				target.sendMessage(ConfigValues.msg_color_positive + transferFrom);
				
				PlayerBuffers.initializeBuffer(p);
				
				p.closeInventory();

				return;
			}
			
			account.openTransfer(target);
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onClickTellerInventory(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		if(e.getView().getTitle().contains("'s Account")) {
			AccountGUI account = new AccountGUI((Player)e.getWhoClicked());
			ItemStack clickedItem = e.getCurrentItem();
			String clickedName = clickedItem.getItemMeta().getDisplayName();
			
			
			//Disable taking items from teller
			if(clickedName.contains("Withdraw Money")) {
				account.openWithdraw();
				e.setCancelled(true);
			}else if(clickedName.contains("Deposit Money")) {
				account.openDeposit();
				e.setCancelled(true);			
			}else if(clickedName.contains("Transfer to Other Player")) {
				openTransferInventory(p);
			}else if(clickedName.contains("Withdraw a Cheque")) {
				if(e.getCursor().getItemMeta() != null && e.getCursor().getItemMeta().getDisplayName().contains("Cheque")) {
					
					//Fraud cheque check
					if(e.getCursor().getItemMeta().hasLore()) {
						
						String amountString = e.getWhoClicked().getItemOnCursor().getItemMeta().getLore().get(1).split(" ")[1].substring(1);
						
						int amount = Integer.parseInt(amountString);
						
						e.getWhoClicked().setItemOnCursor(new ItemStack(Material.AIR));
						
						econ.depositPlayer(p, amount);
						account.openAccount();
					}
				}
				
				e.setCancelled(true);
			}
			
		
		}
	}
	
	@EventHandler
	public void onClickWithdrawInventory(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		if(e.getView().getTitle().contains("Withdraw")) {
			AccountGUI account = new AccountGUI((Player)e.getWhoClicked(), oldBuffer);

			
			ItemStack clickedItem = e.getCurrentItem();
			String clickedName = clickedItem.getItemMeta().getDisplayName();
			
			//Disable taking items from menu
			e.setCancelled(true);
			
			
			if(econ.getBalance(p) - account.getBuffer() >= 1.0 && clickedName.contains(ConfigValues.add_one)) {
				account.addToBuffer(1);
			}else if(econ.getBalance(p) - account.getBuffer() >= 10.0  && clickedName.contains(ConfigValues.add_ten)) {
				account.addToBuffer(10);
			}else if(econ.getBalance(p) - account.getBuffer() >= 64.0 && clickedName.contains(ConfigValues.add_max)) {
				account.addToBuffer(64);
			}else if(account.getBuffer() - 1 >= 0 && clickedName.contains(ConfigValues.take_one)) {
				account.takeFromBuffer(1);
			}else if(account.getBuffer() - 10 >= 0 && clickedName.contains(ConfigValues.take_ten)) {
				account.takeFromBuffer(10);
			}else if(account.getBuffer() - 64 >= 0 && clickedName.contains(ConfigValues.take_max)) {
				account.takeFromBuffer(64);
			}else if(clickedName.contains("Finish Transaction")) {
				econ.withdrawPlayer(p, account.getBuffer());
				p.getInventory().addItem(Dollar.createDollar(account.getBuffer()));
				oldBuffer = 0;
				p.closeInventory();
				return;
			}
			
			
			account.openWithdraw();
			oldBuffer = account.getBuffer();
			
			
		}
	}
	
	@EventHandler
	public void onClickDepositInventory(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		if(e.getView().getTitle().contains("Deposit")) {
			AccountGUI account = new AccountGUI((Player)e.getWhoClicked(), oldBuffer);

			ItemStack dollar = Dollar.createDollar(1);
			ItemStack clickedItem = e.getCurrentItem();
			String clickedName = clickedItem.getItemMeta().getDisplayName();
			
			//Disable taking items from menu
			e.setCancelled(true);
			
			
			if(p.getInventory().containsAtLeast(dollar, account.getBuffer() + 1) && clickedName.contains(ConfigValues.add_one)) {
				account.addToBuffer(1);
			}else if(p.getInventory().containsAtLeast(dollar, account.getBuffer() + 10)  && clickedName.contains(ConfigValues.add_ten)) {
				account.addToBuffer(10);
			}else if(p.getInventory().containsAtLeast(dollar, account.getBuffer() + 64) && clickedName.contains(ConfigValues.add_max)) {
				account.addToBuffer(64);
			}else if(account.getBuffer() - 1 >= 0 && clickedName.contains(ConfigValues.take_one)) {
				account.takeFromBuffer(1);
			}else if(account.getBuffer() - 10 >= 0 && clickedName.contains(ConfigValues.take_ten)) {
				account.takeFromBuffer(10);
			}else if(account.getBuffer() - 64 >= 0 && clickedName.contains(ConfigValues.take_max)) {
				account.takeFromBuffer(64);
			}else if(clickedName.contains("Finish Transaction")) {
				econ.depositPlayer(p, account.getBuffer());
				p.getInventory().removeItem(Dollar.createDollar(account.getBuffer()));
				oldBuffer = 0;
				p.closeInventory();
				return;
			}
			
			
			account.openDeposit();
			oldBuffer = account.getBuffer();
		}
	}

}
