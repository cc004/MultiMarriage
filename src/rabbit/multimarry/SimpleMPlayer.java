package rabbit.multimarry;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class SimpleMPlayer implements MPlayer {
	private String name;
	private Marriage plugin;
	private FileConfiguration cfg;
	
	public SimpleMPlayer(String name) {
		plugin = Marriage.instance;
		this.name = name;
		this.cfg = plugin.getCustomConfig();
	}


	@Override
	public List<String> getPartner() {
		if(cfg.getStringList("partner1").contains(name) || cfg.getStringList("partner2").contains(name)){
			List<String> par1Str = getConfig().getStringList("partners");
			return par1Str;
		}
		return new ArrayList<>();
	}

	@Override
	public void addPartner(String user){

		boolean virgin = getPartner().isEmpty();

		if (getPartner().contains(user)){
			return;
		}
		List<String> list1 = cfg.getStringList("partner1");
		List<String> list2 = cfg.getStringList("partner2");
		PlayerConfig cfg = this.getConfig();
		List<String> partners = cfg.getStringList("partners");
		int index;
		for (index = 0; index < list1.size(); ++index)
		{
			if (list1.get(index).equals(name) && list2.get(index).equals(user) || list1.get(index).equals(user) && list2.get(index).equals(name)) break;
		}

		if (index == list1.size()) {
			list1.add(name);
			list2.add(user);
		}
		partners.add(user);
		plugin.getCustomConfig().set("partner1", list1);
		plugin.getCustomConfig().set("partner2", list2);
		cfg.set("partners", partners);

		save();
		cfg.save();
		if (virgin) setMaster(user);

	}

	@Override
	public void removePartner(String user){

		boolean override = getMaster().equals(user) && getPartner().size() > 1;

		if (!getPartner().contains(user)){
			return;
		}

		List<String> list1 = cfg.getStringList("partner1");
		List<String> list2 = cfg.getStringList("partner2");
		PlayerConfig cfg = this.getConfig();
		List<String> partners = cfg.getStringList("partners");
		int index;
		for (index = 0; index < list1.size(); ++index)
		{
			if (list1.get(index).equals(name) && list2.get(index).equals(user) || list1.get(index).equals(user) && list2.get(index).equals(name)) break;
		}
		if (index < list1.size()){
			list1.remove(index);
			list2.remove(index);
		}
		partners.remove(user);

		plugin.getCustomConfig().set("partner1", list1);
		plugin.getCustomConfig().set("partner2", list2);
		cfg.set("partners", partners);

		save();
		cfg.save();

		if (override) setMaster(partners.get(0));
	}

	@Override
	public String getMaster(){
		PlayerConfig cfg = this.getConfig();
		if (getPartner().isEmpty()) return "";
		return cfg.getString("master");
	}

	@Override
	public void setMaster(String user){
		PlayerConfig cfg = this.getConfig();
		if (!getPartner().contains(user)) return;
		cfg.set("master", user);
		cfg.save();
	}

	private void save() {
		plugin.saveCustomConfig();
	}

	@Override
	public PlayerConfig getConfig() {
		return plugin.getConfig(name);
	}

}