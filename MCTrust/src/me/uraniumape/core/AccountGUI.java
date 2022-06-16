package me.uraniumape.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.uraniumape.events.PlayerBuffers;
import net.milkbowl.vault.economy.Economy;

public class AccountGUI {
	
	private int withdrawlBuffer;
	private Economy econ = MCTrust.getEconomy();
	private Player p;
	
	public AccountGUI(Player p) {
		this.p = p;
		withdrawlBuffer = 0;
	}
	
	public AccountGUI(Player p, int oldBuffer) {
		this.p = p;
		withdrawlBuffer = oldBuffer;
	}
	
	
	//Secondary Transfer GUI, theres probably a better way to do this but oh well
	public Inventory createTransferGUI(String playerName) {
		Inventory inv = Bukkit.createInventory(p, 18, "Transfer to " + playerName);
		ItemStack balance = new ItemStack(Material.KELP);
		ItemMeta balanceMeta = balance.getItemMeta();
		balanceMeta.setDisplayName("§2Balance: " + MCTrust.currencyType + econ.getBalance(p));
		balance.setItemMeta(balanceMeta);
		
		ItemStack withdraw = new ItemStack(Material.GREEN_CANDLE);
		ItemMeta withdrawwMeta = balance.getItemMeta();
		withdrawwMeta.setDisplayName("§aFinish Transaction");
		withdraw.setItemMeta(withdrawwMeta);
		
		ItemStack withdrawlAmount = new ItemStack(Material.PAPER);
		ItemMeta withdrawMeta = withdrawlAmount.getItemMeta();
		List<String> lore = new ArrayList<String>();
		withdrawMeta.setDisplayName("§2Amount to Transfer" + " " + MCTrust.currencyType + PlayerBuffers.getBuffer(p));
		lore.add(playerName);
		withdrawMeta.setLore(lore);
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
		
		
		
		inv.setItem(12, balance);
		inv.setItem(14, withdraw);
		
		inv.setItem(1, takeMax);
		inv.setItem(2, takeTen);
		inv.setItem(3, takeOne);
		inv.setItem(4, withdrawlAmount);
		inv.setItem(5, addOne);
		inv.setItem(6, addTen);
		inv.setItem(7, addMax);
		
		
		return inv;
	}
	
	public Inventory createWithdrawGUI(String name) {
		Inventory inv = Bukkit.createInventory(p, 18, name);
		ItemStack balance = new ItemStack(Material.KELP);
		ItemMeta balanceMeta = balance.getItemMeta();
		balanceMeta.setDisplayName("§2Balance: " + MCTrust.currencyType + econ.getBalance(p));
		balance.setItemMeta(balanceMeta);
		
		ItemStack withdraw = new ItemStack(Material.GREEN_CANDLE);
		ItemMeta withdrawwMeta = balance.getItemMeta();
		withdrawwMeta.setDisplayName("§aFinish Transaction");
		withdraw.setItemMeta(withdrawwMeta);
		
		ItemStack withdrawlAmount = new ItemStack(Material.PAPER);
		ItemMeta withdrawMeta = withdrawlAmount.getItemMeta();
		withdrawMeta.setDisplayName("§2Amount To " + name +" " + MCTrust.currencyType + withdrawlBuffer);
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
		
		//If transfer menu, add transfer player
		if(name.equals("Transfer")) {
			List<String> lore = new ArrayList<String>();
			lore.add(name);
		}
		
		inv.setItem(12, balance);
		inv.setItem(14, withdraw);
		
		inv.setItem(1, takeMax);
		inv.setItem(2, takeTen);
		inv.setItem(3, takeOne);
		inv.setItem(4, withdrawlAmount);
		inv.setItem(5, addOne);
		inv.setItem(6, addTen);
		inv.setItem(7, addMax);
		
		
		return inv;

		
	}
	
	
	public Inventory createAccountGUI() {
		List<String> loreHolder = new ArrayList<String>();
		
		
		ItemStack balance = new ItemStack(Material.KELP);
		ItemMeta balanceMeta = balance.getItemMeta();
		balanceMeta.setDisplayName("§2Balance: " + MCTrust.currencyType + econ.getBalance(p));
		balance.setItemMeta(balanceMeta);
		
		ItemStack withdraw = new ItemStack(Material.GREEN_DYE);
		ItemMeta withdrawMeta = withdraw.getItemMeta();
		withdrawMeta.setDisplayName("§aWithdraw Money");
		withdraw.setItemMeta(withdrawMeta);
		
		ItemStack deposit = new ItemStack(Material.RED_DYE);
		ItemMeta depositMeta = deposit.getItemMeta();
		depositMeta.setDisplayName("§cDeposit Money");
		deposit.setItemMeta(depositMeta);
		
		ItemStack transfer = new ItemStack(Material.BOOK);
		ItemMeta transferMeta = transfer.getItemMeta();
		transferMeta.setDisplayName("§6Transfer to Other Player");
		transfer.setItemMeta(transferMeta);
		
		ItemStack withdrawcheque = new ItemStack(Material.PAPER);
		ItemMeta withdrawChequeMeta = transfer.getItemMeta();
		withdrawChequeMeta.setDisplayName("§fWithdraw a Cheque");
		withdrawcheque.setItemMeta(withdrawChequeMeta);
		
		Inventory inv = Bukkit.createInventory(p, 36, p.getName() + "'s Account");
		
		inv.setItem(11, balance);
		inv.setItem(12, withdraw);
		inv.setItem(13, deposit);
		inv.setItem(14, transfer);
		inv.setItem(15, withdrawcheque);
		
		
		return inv;
	}
	
	
	public void openAccount() {
		Inventory account = createAccountGUI();
		p.openInventory(account);
	}
	
	public void openWithdraw() {
		Inventory withdraw = createWithdrawGUI("Withdraw");
		p.openInventory(withdraw);
	}
	
	public void openTransfer(Player target) {
		Inventory transfer = createTransferGUI(target.getName());
		p.openInventory(transfer);
	}
	
	public void openDeposit() {
		Inventory deposit = createWithdrawGUI("Deposit");
		p.openInventory(deposit);
	}
	
	
	public void addToBuffer(int amount) {
		this.withdrawlBuffer += amount;
	}
	
	public void takeFromBuffer(int amount) {
		this.withdrawlBuffer -= amount;
	}
	
	public int getBuffer() {
		return this.withdrawlBuffer;
	}
	
}
