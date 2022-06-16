package me.uraniumape.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.uraniumape.commands.OnlineBank;
import me.uraniumape.commands.bankadmin;
import me.uraniumape.events.PurchaseEvents;
import me.uraniumape.events.RegisterEvents;
import me.uraniumape.events.TellerEvents;
import net.milkbowl.vault.economy.Economy;

public class MCTrust extends JavaPlugin{
   private static Economy econ = null;
   private static MCTrust instance;
   private StorageClass sClass;
   public static final String permissionPrefix = "mctrust";
   public static final long bankFeeInterval = 72000; //Every hour
   public static final double bankFee = 25;
   public static String currencyType = "";
   
   @Override
    public void onEnable() {
	   
	   saveDefaultConfig();

	   
	   if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
	   
	   this.getCommand("onlinebank").setExecutor(new OnlineBank());
	   this.getCommand("bank-admin").setExecutor(new bankadmin());
	   
	   getServer().getPluginManager().registerEvents(new TellerEvents(),this);
	   getServer().getPluginManager().registerEvents(new RegisterEvents(),this);
	   getServer().getPluginManager().registerEvents(new PurchaseEvents(),this);
	   instance = this;
	   loadStorage();
	   
	   currencyType = econ.currencyNameSingular();
	   ConfigValues.initializeValues();
   	}
	
   	
	    
	private boolean setupEconomy() {
	    if (getServer().getPluginManager().getPlugin("Vault") == null) {
	        return false;
	    }
	    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
	    if (rsp == null) {
	        return false;
	    }
	    econ = rsp.getProvider();
	    return econ != null;
	}
	
	
    public static Economy getEconomy() {
        return econ;
    }
    
    public static MCTrust getInstance() {
    	return instance;
    }
    
	public void loadStorage() {
		sClass = new StorageClass();
		sClass.create();
	}
	
	
}
