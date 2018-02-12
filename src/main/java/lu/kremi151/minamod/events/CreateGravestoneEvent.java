package lu.kremi151.minamod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CreateGravestoneEvent extends Event{
	
	private final NonNullList<ItemStack> mutableItemList;
	private final EntityPlayer player;
	private final BlockPos pos;
	
	public CreateGravestoneEvent(NonNullList<ItemStack> mutableItemList, EntityPlayer player, BlockPos pos) {
		this.mutableItemList = mutableItemList;
		this.player = player;
		this.pos = pos;
	}
	
	/**
	 * The mutable item list to be stored inside the gravestone
	 * @return
	 */
	public NonNullList<ItemStack> getItems(){
		return mutableItemList;
	}
	
	/**
	 * The block position of the gravestone
	 * @return
	 */
	public BlockPos getPos() {
		return pos;
	}
	
	/**
	 * The affected player who died
	 * @return
	 */
	public EntityPlayer getPlayer() {
		return player;
	}
	
	/**
	 * The world where the gravestone will be created
	 * @return
	 */
	public World getWorld() {
		return player.world;
	}

}
