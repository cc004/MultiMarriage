package rabbit.multimarry.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rabbit.multimarry.MPlayer;
import rabbit.multimarry.lang.Messages;
import rabbit.multimarry.util.EcoUtil;

public class MarryCommand extends CommandBase {

	@Override
	public void perform(CommandSender sender, String[] args) {
		if(args.length == 1) {
			final Player player = (Player) sender;
			Player op = plugin.getPlayer(args[0]);
			if(op != null) {
				if(op.isOnline()) {
					MPlayer mp = plugin.getMPlayer(player);
					MPlayer tp = plugin.getMPlayer(op);
					
					if(op.getName().equals(player.getName())) {
						error(player, Messages.NOT_YOURSELF);
						return;
					}
					
					if(!mp.getPartner().isEmpty() && !sender.hasPermission("marry.multiple")) {
						error(player, Messages.ALREADY_MARRIED);
						return;
					}
					if(mp.getPartner().contains(args[0])){
						error(player, Messages.ALREADY_MARRIED2.replace("{USER}", args[0]));
						return;
					}
					if(!tp.getPartner().isEmpty() && !op.hasPermission("marry.multiple")) {
						String msg = Messages.HAS_PARTNER.replace("{USER}", op.getName());
						error(player, msg);
						return;
					}
					
					if(plugin.req.containsValue(player.getName())) {
						error(player, Messages.ALREADY_QUEUED);
						return;
					}
					
					if(plugin.eco) {
						double a = EcoUtil.getPriceFromConfig(mp.getPartner().isEmpty()?"marry":"multimarry");
						if(a != 0.0) {
							if(EcoUtil.withrawMoneyIfEnough(player, a)) {
								return;
							}
						}
					}
					
					inform(player, Messages.REQUEST_SENT);
					String cmd = ChatColor.LIGHT_PURPLE + "/marry accept" + ChatColor.GREEN;
					String msg = Messages.REQUEST_RECEIVED.replace("{USER}", player.getName()).replace("{COMMAND}", cmd);
					inform(op, msg);
					plugin.req.put(op.getName(), player.getName());
					
					//Unqueue
					final String oname = op.getName();
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {
							plugin.req.remove(oname);
							if(player.isOnline()) {
								inform(player, Messages.REQUEST_EXPIRED);
							}
						}
					}, plugin.getConfig().getInt("settings.request-expire") * 20);
					
					return;
				}
			}
		}
	}

	@Override
	public String getPermission() {
		return "marry.marry";
	}

	@Override
	public boolean playersOnly() {
		return true;
	}
}
