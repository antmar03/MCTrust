package me.uraniumape.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StorageClass {

	private MCTrust plug = MCTrust.getInstance();
	private File storeFile;
	private FileConfiguration sConfig; 

	public StorageClass() {

		
	}

	public void create() {	
		if(!plug.getDataFolder().exists()) {
			plug.getDataFolder().mkdir();
		}
		
		storeFile = new File(plug.getDataFolder(), "store.yml");
		
		if(!storeFile.exists()) {
			try {
				storeFile.createNewFile();
			}catch(IOException e){
				Bukkit.getLogger().info("Couldn't create store.yml.. Please contact the developer");
			}
		}
		
		sConfig = YamlConfiguration.loadConfiguration(storeFile);
		
		try {
			sConfig.save(storeFile);
			sConfig = YamlConfiguration.loadConfiguration(storeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public FileConfiguration getStore() {
		storeFile = new File(plug.getDataFolder(), "store.yml");
		sConfig = YamlConfiguration.loadConfiguration(storeFile);
		return sConfig;
	}
	
	
	public void reloadStore() {
		try {
			sConfig.load(storeFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}
	
	public void saveStore() {
		try {
			sConfig.save(storeFile);
			sConfig = YamlConfiguration.loadConfiguration(storeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
	
}
