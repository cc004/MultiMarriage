package rabbit.multimarry.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import rabbit.multimarry.MPlayer;
import rabbit.multimarry.Marriage;


public class PlayerListener implements Listener {
	private Marriage plugin;
	public PlayerListener(Marriage i) { plugin = i; }

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		MPlayer mp = plugin.getMPlayer(player);
		if(!mp.getPartner().isEmpty()) {
			//Replace chat with custom prefix
			if(plugin.getConfig().getBoolean("settings.chat-prefix.use")) {
				String format = plugin.getConfig().getString("settings.chat-prefix.format");
				format = plugin.fixColors(format);
				format = format.replace("{OLD_FORMAT}", event.getFormat());
				event.setFormat(format);
			}
		} else {
			//Replace chat with custom prefix
			if(plugin.getConfig().getBoolean("settings.chat-prefix.use")) {
				String format = plugin.getConfig().getString("settings.chat-prefix.format2");
				format = plugin.fixColors(format);
				format = format.replace("{OLD_FORMAT}", event.getFormat());
				event.setFormat(format);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		MPlayer mp = plugin.getMPlayer(player);
		mp.getConfig().set("last-logout", System.currentTimeMillis());
		mp.getConfig().save();
		plugin.clearPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		MPlayer mp = plugin.getMPlayer(player);
		mp.getConfig().set("last-login", System.currentTimeMillis());
		mp.getConfig().save();

	}
}