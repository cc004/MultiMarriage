package rabbit.multimarry.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import rabbit.multimarry.MPlayer;
import rabbit.multimarry.lang.Messages;
import rabbit.multimarry.util.EcoUtil;


public class GiftCommand extends CommandBase {

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
		
		if(player.getGameMode() == GameMode.CREATIVE) {
			error(player, Messages.GIFT_CREATIVE);
			return;
		}
		
		if(plugin.eco) {
			double a = EcoUtil.getPriceFromConfig("gift");
			if(a != 0.0) {
				if(EcoUtil.withrawMoneyIfEnough(player, a)) {
					return;
				}
			}
		}
		
		ItemStack it = player.getItemInHand();
		if(it != null) {
			if(it.getType() != Material.AIR) {
				inform(player, Messages.GIFT_SENT);
				String item = it.getType().toString().toLowerCase();
				String msg = Messages.GIFT_RECEIVED.replace("{ITEM}", item);
				op.sendMessage(ChatColor.GREEN + msg);
				op.getInventory().addItem(it);
				player.setItemInHand(null);
			}else
				error(player, Messages.INVALID_ITEM);
		}else
			error(player, Messages.INVALID_ITEM);
	}

	@Override
	public String getPermission() {
		return "marry.gift";
	}

	@Override
	public boolean playersOnly() {
		return true;
	}
}
