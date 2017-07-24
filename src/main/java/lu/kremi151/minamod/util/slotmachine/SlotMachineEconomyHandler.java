package lu.kremi151.minamod.util.slotmachine;

import net.minecraft.entity.player.EntityPlayer;

public interface SlotMachineEconomyHandler {

	boolean rewardPlayer(EntityPlayer player, int amount);
	boolean withdrawCoins(EntityPlayer player, int amount);
	int getCredits(EntityPlayer player);
}
