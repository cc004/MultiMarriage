package rabbit.multimarry;

import java.util.List;

public interface MPlayer {
	List<String> getPartner();

	void addPartner(String user);

	void removePartner(String user);

	String getMaster();

	void setMaster(String user);

	PlayerConfig getConfig();

}
