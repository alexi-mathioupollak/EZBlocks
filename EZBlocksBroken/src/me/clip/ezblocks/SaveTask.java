package me.clip.ezblocks;




public class SaveTask implements Runnable {

	private EZBlocks plugin;
	private String uuid;
	private int toSave;
	
	public SaveTask(EZBlocks instance, String uuid, int amt) {
		plugin = instance;
		this.uuid = uuid;
		toSave = amt;
	}
	
	@Override
	public void run() {
		
		
		plugin.playerconfig.savePlayer(uuid, toSave);

	}
}
