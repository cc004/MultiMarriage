package rabbit.multimarry.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rabbit.multimarry.MPlayer;
import rabbit.multimarry.lang.Messages;
import rabbit.multimarry.util.EcoUtil;


public class DivorceCommand extends CommandBase {
	@Override
	public void perform(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		MPlayer mp = plugin.getMPlayer(player);
		if (args.length == 2 && !mp.getPartner().contains(args[1]))
		{
			error(player, Messages.NOT_PARTNER.replace("{USER}", args[1]));
			return;
		}
		String partner = (args.length == 2) ? args[1] : mp.getMaster();

		if(mp.getPartner().isEmpty()) {
			error(player, Messages.NO_PARTNER);
			return;
		}

		if(!sender.hasPermission("marry.divorce." + partner)) {
			 error(sender, Messages.NO_PERMISSION);
			//return;
		}
		if(plugin.eco) {
			double a = EcoUtil.getPriceFromConfig("divorce");
			if(a != 0.0) {
				if(EcoUtil.withrawMoneyIfEnough(player, a)) {
					return;
				}
			}
		}
		
		String user = player.getName();
		MPlayer tp = plugin.getMPlayer(partner);
		mp.removePartner(partner);
		tp.removePartner(user);
		String msg = Messages.DIVORCED.replace("{USER1}", user).replace("{USER2}", partner);
		Bukkit.getServer().broadcastMessage(ChatColor.RED + msg);
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean playersOnly() {
		return true;
	}
}
