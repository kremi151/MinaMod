package lu.kremi151.minamod.sponge.slotmachine;

import org.spongepowered.api.entity.living.player.Player;

import lu.kremi151.minamod.util.slotmachine.SlotMachineEconomyHandler;
import net.minecraft.entity.player.EntityPlayer;

public abstract class SpongeSlotMachineEconomyHandler implements SlotMachineEconomyHandler{

	@Override
	public final boolean rewardPlayer(EntityPlayer player, int amount) {
		return rewardPlayer((Player)player, amount);
	}

	@Override
	public final boolean withdrawCoins(EntityPlayer player, int amount) {
		return withdrawCoins((Player)player, amount);
	}

	@Override
	public final int getCredits(EntityPlayer player) {
		return getCredits((Player)player);
	}

	protected abstract boolean rewardPlayer(Player player, int amount);
	protected abstract boolean withdrawCoins(Player player, int amount);
	protected abstract int getCredits(Player player);

}
