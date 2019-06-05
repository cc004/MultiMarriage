package rabbit.multimarry.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rabbit.multimarry.MPlayer;


public class InfoCommand extends CommandBase {

	@Override
	public void perform(CommandSender sender, String[] args) {
		boolean isAdmin = sender.hasPermission("marry.admin");
		ChatColor g = ChatColor.GRAY;
		ChatColor l = ChatColor.GREEN;
		ChatColor r = ChatColor.RED;
		inform(sender, g + "==========-{"+l+" Marriage "+g+"}-==========");
		inform(sender, g + "Version: "+l+plugin.getDescription().getVersion());
		inform(sender, g + "Authors: "+l+plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""));
		inform(sender, l + "/marry <player> "+g+"- Marry a player");
		inform(sender, l + "/marry accept "+g+"- Accept a marriage request");
		inform(sender, l + "/marry divorce [player]"+g+"- Divorce a partner");
		inform(sender, l + "/marry list "+g+"- See all pairs");
		inform(sender, l + "/marry tp [player]"+g+"- Teleport to your partner");
		inform(sender, l + "/marry gift [player]"+g+"- Gift your partner the item in your hand");
		inform(sender, l + "/marry chat [player]"+g+"- Private chat with your partner");
		inform(sender, l + "/marry seen [player]"+g+"- Check your partner's last login");
		inform(sender, l + "/marry main <player>"+g+"- Set a partner to main");
		if(sender.hasPermission("marry.reload") || isAdmin) {
			inform(sender, l + "/marry reload"+g+" - Reload all config files");
		}

		if(this.isPlayer()) {
			Player player = (Player) sender;
			MPlayer mp = plugin.getMPlayer(player);
			if(!mp.getPartner().isEmpty()) {
				inform(sender, "Married: "+l+mp.getPartner().toString());
			}else {
				inform(sender, "Married: "+r+"No");
			}
		}
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public boolean playersOnly() {
		return false;
	}
}
