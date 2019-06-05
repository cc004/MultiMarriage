package rabbit.multimarry.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import rabbit.multimarry.lang.Messages;


public class ListCommand extends CommandBase {

	private String resolveMaster(String user, String partner)
	{
		return ChatColor.GREEN + user + (plugin.getMPlayer(user).getMaster().equals(partner) ? ChatColor.RED + "(*)":"");
	}
	private List<String> getDisplayTest(List<String> pair1, List<String> pair2)
	{
		Iterator<String> s1=pair1.iterator(), s2 = pair2.iterator();
		List<String> res = new ArrayList<>();
		for(;s1.hasNext();)
		{
			String u1 = s1.next(), u2 = s2.next();
			res.add(resolveMaster(u1, u2) + ChatColor.WHITE + " - " + resolveMaster(u2, u1));
			s2.hasNext();
		}
		return res;
	}
	@Override
	public void perform(CommandSender sender, String[] args) {
		int page = 1;
		if(args.length == 2)
			page = Integer.valueOf(args[1]);
		
		List<String> list = getDisplayTest(plugin.getCustomConfig().getStringList("partner1"), plugin.getCustomConfig().getStringList("partner2"));
		
		if(list.isEmpty()) {
			error(sender, Messages.NO_PARTNERS);
			return;
		}
		
		int maxPage = 0;
		if(String.valueOf(list.size()).endsWith("0"))
		{
			maxPage = list.size() / 10;
		}else
			maxPage = Integer.valueOf(((Double) ((double)(list.size() / 10))).intValue() + 1);
		String pages = ChatColor.GOLD + Messages.PAGE + " " + String.valueOf(page) + "/" + String.valueOf(maxPage);
		inform(sender, pages);
		inform(sender, ChatColor.BLUE+ Messages.PARTNERS + ":");
		int msw = page > 1 ? 1 : 0;
		for(int i = page * 10 - 10 - msw; i < page * 10 && i < list.size(); i++) {
			inform(sender, list.get(i));
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
