package rabbit.multimarry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import rabbit.multimarry.commands.MarryCMD;
import rabbit.multimarry.listeners.PlayerListener;

public class Marriage extends JavaPlugin {
	public static boolean HEROCHAT_ENABLED = false;
	private List<String> partners = new ArrayList<String>();
	private FileConfiguration customConfig = null;
    private File customConfigFile = null;
    public static Marriage instance;
    public HashMap<String, String> req = new HashMap<String, String>();
    public Economy economy;
    public Logger log = Logger.getLogger("Minecraft.MultiMarriage");
    public boolean eco = false;
    private Map<String, MPlayer> players = new WeakHashMap<String, MPlayer>();
    public Map<String, PlayerConfig> configs = new HashMap<String, PlayerConfig>();
	
	@Override
	public void onEnable() {
		instance = this;
		FileConfiguration config = this.getConfig();
		PluginManager pm = this.getServer().getPluginManager();

		//register events/commands
		pm.registerEvents(new PlayerListener(this), this);
		getCommand("marry").setExecutor(new MarryCMD());
		//setup config.yml
		config.addDefault("update-checker", true);
		config.addDefault("settings.private-chat.format", "&a[Partner] &7{Player}&f: &a{Message}");
		config.addDefault("settings.chat-prefix.use", true);
		config.addDefault("settings.chat-prefix.format", "&c&l<3&r {OLD_FORMAT}");
		config.addDefault("settings.chat-prefix.format2", "&a&l<3&r {OLD_FORMAT}");
		config.addDefault("settings.request-expire", 60);
		config.addDefault("settings.enable-chatspy", true);
		config.addDefault("price.{command name}", 10.0);
		config.addDefault("price.marry", 0.0);
		config.addDefault("price.multimarry", 10000.0);
		config.options().copyDefaults(true);
		this.saveConfig();
		
		//setup data.yml
		FileConfiguration cfg = this.getCustomConfig();
		cfg.addDefault("partner1", partners);
		cfg.addDefault("partner2", partners);
		cfg.options().copyDefaults(true);

		//setup vault
		Plugin vault = pm.getPlugin("Vault");
		if(vault != null) {
			if(this.setupEconomy()) {
				String s = economy.getName();
				log.info("[Marriage] Hooked with "+s+" using Vault");
				eco = true;
			}
		}
	}

	public Player getPlayer(String name) {
		Player t = Bukkit.getServer().getPlayer(name);
		if(t != null)
			if(t.isOnline())
				return t;
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			if(player.getName().toLowerCase().startsWith(name) || player.getName().startsWith(name)) {
				return player;
			}
		}
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			if(player.getName().toLowerCase().endsWith(name) || player.getName().endsWith(name)) {
				return player;
			}
		}
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			if(player.getName().toLowerCase().contains(name) || player.getName().contains(name)) {
				return player;
			}
		}
		
		return null;
	}
	
	public void clearPlayer(Player player) {
		players.remove(player.getName());
		configs.remove(player.getName());
	}
	
	public MPlayer getMPlayer(Player player) {
		return getMPlayer(player.getName());
	}

	public MPlayer getMPlayer(String name) {
		if(players.containsKey(name))
			return players.get(name);

		MPlayer mp = new SimpleMPlayer(name);
		players.put(name, mp);

		return mp;
	}
	public Collection<MPlayer> getLoadedPlayers() {
		return players.values();
	}
	
	public PlayerConfig getConfig(String name) {
		if(configs.containsKey(name))
			return configs.get(name);
		
		PlayerConfig cfg = this.getPlayerConfig(name);
		configs.put(name, cfg);
		return cfg;
	}
	
	public PlayerConfig getPlayerConfig(String name) {
		File dir = new File(this.getDataFolder(), "playerdata");
		dir.mkdirs();
		File file = new File(dir, name+".yml");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.log(Level.SEVERE, "[Marriage] Could not create data file for player '" +name + "'", e);
			}
		}
		
		return new PlayerConfig(file);
	}

	public void reloadCustomConfig() {
	    if (customConfigFile == null) {
	    	customConfigFile = new File(getDataFolder(), "data.yml");
	    }
	    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
	    java.io.InputStream defConfigStream = this.getResource("data.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        customConfig.setDefaults(defConfig);
	    }
	}

	public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        this.reloadCustomConfig();
	    }
	    return customConfig;
	}

	public void saveCustomConfig() {
	    if (customConfig == null || customConfigFile == null){
	    	return;
	    } try {
	        getCustomConfig().save(customConfigFile);
	    } catch (IOException ex) {
	        this.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
	}
	
	public String fixColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
