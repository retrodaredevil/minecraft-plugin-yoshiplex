package com.yoshiplex;



import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.earth2me.essentials.Essentials;
import com.yoshiplex.commands.HatCommand;
import com.yoshiplex.commands.NameColor;
import com.yoshiplex.commands.OneUpCommand;
import com.yoshiplex.commands.ParticleCommand;
import com.yoshiplex.commands.ProfileCommand;
import com.yoshiplex.commands.StopCoinBoast;
import com.yoshiplex.commands.TheCommands;
import com.yoshiplex.customevents.CustomEventManager;
import com.yoshiplex.eventlisteners.Cannon;
import com.yoshiplex.eventlisteners.Jump;
import com.yoshiplex.eventlisteners.PlayerChat;
import com.yoshiplex.eventlisteners.PlayerListener;
import com.yoshiplex.games.GameManager;
import com.yoshiplex.hats.DiamondHat;
import com.yoshiplex.hats.DragonHat;
import com.yoshiplex.hats.GoldenHat;
import com.yoshiplex.hats.Hats;
import com.yoshiplex.hats.IronHat;
import com.yoshiplex.hats.RainbowHat;
import com.yoshiplex.loops.Loop;
import com.yoshiplex.loops.menu.ScoreboardSetter;
import com.yoshiplex.loops.menu.tablist.TabListSetter;
import com.yoshiplex.nocredit.YmlMaker;
import com.yoshiplex.packetlisteners.PacketListenerManager;
import com.yoshiplex.particles.FlameParticle;
import com.yoshiplex.particles.HeartParticle;
import com.yoshiplex.particles.MagicParticle;
import com.yoshiplex.particles.Particles;
import com.yoshiplex.particles.RainbowParticle;
import com.yoshiplex.particles.loop.PeeParticle;
import com.yoshiplex.parties.PartyManager;
import com.yoshiplex.reload.ReloadManager;
import com.yoshiplex.shops.CoinShopGUI;
import com.yoshiplex.shops.CosmeticsGUI;
import com.yoshiplex.shops.DeadendShop;
import com.yoshiplex.shops.HatShop;
import com.yoshiplex.shops.ParticleShop;
import com.yoshiplex.shops.ShopCommand;
import com.yoshiplex.teleportation.TeleportationManager;
import com.yoshiplex.teleportation.WorldManager;
import com.yoshiplex.teleportation.eventlisteners.WorldChange;
import com.yoshiplex.teleportation.eventlisteners.ping.PingListener;
import com.yoshiplex.util.CalledTwiceException;
import com.yoshiplex.util.YPLibrary;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
	
	public static ProtocolManager protocolManager;
	public static JavaPlugin plugin;
	public static Economy economy = null;
	public static Chat chat = null;
	public static FileConfiguration config = null;
	public static Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
	private YmlMaker chatlog = null;
	private static Main instance = null;
	
	
	private GameManager manager = null;
	//private Economy econ = null;
	
	@Override
	public void onEnable() { //
		instance = this;
		plugin = this;
		getLogger().info("The main plugin for YoshiPlex has been enabled!");
		protocolManager = ProtocolLibrary.getProtocolManager();
		if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		if (!setupChat()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
		}
		
		
		Loop task = new Loop();
		task.runTaskTimer(this, 0, 1);
		
		try {
			YPLibrary.init(this);
		} catch (CalledTwiceException e) {
			e.printStackTrace();
		}
		
		loadConfiguration();
		createLoops();
		createConfigs();

		
		new ReloadManager(this);
		
		new TeleportationManager(this);
		
		
		//new FlyManager();
		
		
		new PlayerListener(this);
		new WorldChange(this);
		//new GriefWars(this);
		new PlayerChat(this);
		new NameColor(this, true);
		new Jump(this);
		new Cannon(this);
		
		new CosmeticsGUI(this);
		new CoinShopGUI(this);
		new DeadendShop(this);
		
		
		this.manager = new GameManager(this);
		new WorldManager(); // just registers a loop
		
		new CustomEventManager();
		
		new PacketListenerManager();
		
		new PartyManager(this);
		
		getCommand("test").setExecutor(new TestClass());
		registerCommands();
	}

	private void createLoops(){ //Wait 0 ticks before executing for the first time. Wait 1 tick between each consecutive execution
		
		
		ScoreboardSetter boardSetter = new ScoreboardSetter();
		boardSetter.runTaskTimer(this, 20 * 3, 1);
		
		TabListSetter tabSetter = new TabListSetter(this);
		tabSetter.runTaskTimer(this, 20, 30 * 20);
		
		new YPTime(); // registers to loop
		
		
		createHats();
		createParticles();
	}
	
	private void createHats(){
		int delay = 3 * 20;
		RainbowHat rainbow = new RainbowHat();
		DiamondHat diamond = new DiamondHat();
		GoldenHat gold = new GoldenHat();
		IronHat iron = new IronHat();
		DragonHat dragon = new DragonHat();
		Hats.setAll(Arrays.asList(rainbow, diamond, gold, iron, dragon));
		rainbow.runTaskTimer(this, delay, 3);
		diamond.runTaskTimer(this, delay, 20);
		gold.runTaskTimer(this, delay, 20);
		iron.runTaskTimer(this, delay, 20);
		dragon.runTaskTimer(this, delay, 20);
		
		Hats hat = new Hats();
		hat.runTaskTimer(this, delay, 40);
		new HatShop(this);
	}
	private void createParticles(){
		int delay = 3 * 20;
		RainbowParticle rainbow = new RainbowParticle();
		HeartParticle heart = new HeartParticle();
		FlameParticle flame = new FlameParticle();
		MagicParticle magic = new MagicParticle();
		//HuluHoopParticle hulu = new HuluHoopParticle();
		Particles.setAll(Arrays.asList(rainbow, heart, flame, magic));
		rainbow.runTaskTimer(this, delay, 1);
		heart.runTaskTimer(this, delay, 3);
		flame.runTaskTimer(this, delay, 1);
		magic.runTaskTimer(this, delay, 3);
		
		//hulu.runTaskTimer(this, 4 * 20, 2);
		
		Particles part = new Particles();
		part.runTaskTimer(this, delay, 40);
		new ParticleShop(this);
		
		new PeeParticle();
		
	}
	private void createConfigs() {
		chatlog = new YmlMaker(this, "chatlog.yml");
		
	}
	public static YmlMaker getChatLog(){
		
		return instance.chatlog;
	}



	@Override
	public void onDisable() {
		this.getConfig().set("pingcount", PingListener.getPingCount());
		getLogger().info("The main plugin for YoshiPlex has been disabled! CTLR + F: findme101");
		config.options().copyDefaults(true);
		saveConfig();
		this.manager.disable();
	}	
	private void registerCommands() {
		packetListen();
		getCommand("help").setExecutor(new TheCommands());
		getCommand("info").setExecutor(new TheCommands());
		getCommand("day").setExecutor(new TheCommands());
		getCommand("night").setExecutor(new TheCommands());
		getCommand("dawn").setExecutor(new TheCommands());
		getCommand("night").setExecutor(new TheCommands());
		getCommand("dusk").setExecutor(new TheCommands());
		getCommand("hub").setExecutor(new TheCommands());
		getCommand("register").setExecutor(new TheCommands());
		getCommand("disconnect").setExecutor(new TheCommands());
		getCommand("yoface").setExecutor(new TheCommands());
		getCommand("new").setExecutor(new TheCommands());
		getCommand("prun").setExecutor(new TheCommands());
		
		//getCommand("test").setExecutor(new TheCommands());
		getCommand("get").setExecutor(new TheCommands());
		getCommand("ab").setExecutor(new TheCommands());
		getCommand("show").setExecutor(new TheCommands());
		getCommand("spawn").setExecutor(new TheCommands());

		//getCommand("game").setExecutor(new OldGameCommand());
		
		getCommand("color").setExecutor(new NameColor(null, false));
		
		getCommand("noupdate").setExecutor(new StopCoinBoast());
		
		getCommand("hat").setExecutor(new HatCommand());
		
		getCommand("shop").setExecutor(new ShopCommand(this));
		
		getCommand("oneup").setExecutor(new OneUpCommand());

		getCommand("particle").setExecutor(new ParticleCommand());
		
		getCommand("profile").setExecutor(new ProfileCommand());
		
	}
	public ProtocolManager getManager(){
		return protocolManager;
	}
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }
	
	private void packetListen() {
		protocolManager.addPacketListener(
				new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if (event.getPacketType() == PacketType.Play.Server.CHAT) {
							if(event.getPacket().getChatComponentArrays().toString().equals(ChatColor.GOLD + "[Connect" + ChatColor.RED + "Four] ")){
								event.setCancelled(true);
							}
							//if(event.getPacket().get)
						}
					}
				});
	}
	

	public void loadConfiguration() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		if (config == null) {
			config = plugin.getConfig();
		}
	}
	public static Main getInstance(){
		return instance;
	}
	public static FileConfiguration getConfigVar(){
		return config;
	}
	public Economy getEcon(){
		return economy;
	}

}
