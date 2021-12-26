package com.yoshiplex.nocredit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.yoshiplex.Main;

public class YmlMaker // I do not take credit for this class : https://www.spigotmc.org/threads/util-ymlmaker-configuration-util.127023/
{
    Main instance;
    public String fileName;
    private JavaPlugin plugin;
    public File configFile;
    private FileConfiguration config;

    public YmlMaker(Main Plugin)
    {
        this.instance = Plugin;
    }

    public YmlMaker(JavaPlugin plugin, String fileName)
    {
        if (plugin == null) {
            throw new IllegalArgumentException("plugin cannot be null");
        }
        this.plugin = plugin;
        this.fileName = fileName;
        File dataFolder = plugin.getDataFolder();
        if (dataFolder == null) {
            throw new IllegalStateException();
        }
        this.configFile = new File(dataFolder.toString() + File.separatorChar + this.fileName);
    }

    @SuppressWarnings("deprecation")
	public void reloadConfig()
    {
        try
        {
            this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(new FileInputStream(this.configFile), "UTF-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null)
        {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.config.setDefaults(defConfig);
        }
    }

    public FileConfiguration getConfig()
    {
        if (this.config == null) {
            reloadConfig();
        }
        return this.config;
    }

    public void saveConfig()
    {
        if ((this.config == null) || (this.configFile == null)) {
            return;
        }
        try
        {
            getConfig().save(this.configFile);
        }
        catch (IOException ex)
        {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, ex);
        }
    }

    public void saveDefaultConfig()
    {
        if (!this.configFile.exists()) {
            this.plugin.saveResource(this.fileName, false);
        }
    }
}