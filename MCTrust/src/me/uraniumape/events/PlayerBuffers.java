package me.uraniumape.events;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class PlayerBuffers {
	
	private static HashMap<Player, Integer> playerBuffers = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> playerSchedulers = new HashMap<Player, Integer>();
	
	public static int getBuffer(Player p) {
		return playerBuffers.get(p);
	}
	
	public static void setBuffer(Player p, int amount) {
		playerBuffers.put(p, amount);
	}
	
	public static void initializeBuffer(Player p) {
		playerBuffers.put(p, 0);
	}
	
	public static void addToBuffer(Player p, int amount) {
		playerBuffers.put(p, playerBuffers.get(p) + amount);
	}
	
	
	public static void takeFromBuffer(Player p, int amount) {
		playerBuffers.put(p, playerBuffers.get(p) - amount);
	}
	
	public static int getScheduler(Player p) {
		return playerSchedulers.get(p);
	}
	
	public static void setScheduler(Player p, int scheduleID) {
		playerSchedulers.put(p, scheduleID);
	}
}
