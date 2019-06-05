package rabbit.multimarry.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rabbit.multimarry.MPlayer;
import rabbit.multimarry.lang.Messages;
import rabbit.multimarry.util.EcoUtil;


public class TpCommand extends CommandBase {
	
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
		Player op = Bukkit.getServer().getPlayer(partner);
		
		if(mp.getPartner().isEmpty()) {
			error(player, Messages.NO_PARTNER);
			return;
		}
		
		if(op == null || !op.isOnline()) {
			error(player, Messages.NOT_ONLINE);
			return;
		}
		
		if(plugin.eco) {
			double a = EcoUtil.getPriceFromConfig("tp");
			if(a != 0.0) {
				if(EcoUtil.withrawMoneyIfEnough(player, a)) {
					return;
				}
			}
		}
		
		inform(player, Messages.TELEPORTING + "...");
		inform(op, Messages.PARTNER_TELEPORTING);
		player.teleport(op.getLocation());
	}

	@Override
	public String getPermission() {
		return "marry.tp";
	}

	@Override
	public boolean playersOnly() {
		return true;
	}
}
