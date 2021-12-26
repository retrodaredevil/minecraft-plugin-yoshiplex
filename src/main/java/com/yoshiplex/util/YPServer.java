package com.yoshiplex.util;

import org.bukkit.Bukkit;

import net.minecraft.server.v1_9_R2.MinecraftServer;

public class YPServer {
	@SuppressWarnings("deprecation")
	public static MinecraftServer getMinecraftServer(){
		return MinecraftServer.getServer();
	}
	public static String getPackageName(){
		return "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("")[3];
	}
}
