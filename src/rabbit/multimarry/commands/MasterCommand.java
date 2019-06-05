package rabbit.multimarry.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rabbit.multimarry.MPlayer;
import rabbit.multimarry.lang.Messages;

public class MasterCommand extends CommandBase {
    @Override
    public void perform(CommandSender sender, String[] args) {
        final Player player = (Player) sender;
        MPlayer mp = plugin.getMPlayer(player);
        if (args.length == 1)
        {
            if(mp.getPartner().isEmpty()) {
                error(player, Messages.NO_PARTNER);
                return;
            }
            sender.sendMessage(ChatColor.GREEN + "Main: " + mp.getMaster());
        }
        else {
            Player op = plugin.getPlayer(args[1]);
            if (mp.getMaster().equals(args[1]))
            {
                error(player, Messages.ALREADY_MAIN.replace("{USER}", args[1]));
                return;
            }
            if (op == null || !op.isOnline()) {
                error(player, Messages.NOT_ONLINE);
                return;
            }
            if (!mp.getPartner().contains(args[1]))
            {
                error(player, Messages.NOT_PARTNER.replace("{USER}", args[1]));
                return;
            }
            mp.setMaster(args[1]);
            Bukkit.getServer().broadcastMessage(ChatColor.GREEN + Messages.CHANGE_MAIN.replace("{USER1}", player.getName()).replace("{USER2}", args[1]));
        }
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
