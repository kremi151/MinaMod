package lu.kremi151.minamod.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class CapabilityPlayerJetpack implements ICapabilityJetpack{
	
	private final EntityPlayer player;

	private final static DataParameter<Boolean> IS_USING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);
	private final static DataParameter<Boolean> WAS_FLYING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);
	
	public CapabilityPlayerJetpack(EntityPlayer player){
		this.player = player;

		this.player.getDataManager().register(IS_USING, false);
		this.player.getDataManager().register(WAS_FLYING, false);
	}

	@Override
	public void setUsingJetpack(boolean v) {
		player.getDataManager().set(IS_USING, v);
		if(v){
			player.getDataManager().set(WAS_FLYING, player.capabilities.isFlying);
		}else{
			player.capabilities.isFlying = true;
			player.sendPlayerAbilities();
		}
	}

	@Override
	public boolean isUsingJetpack() {
		return player.getDataManager().get(IS_USING);
	}

}
