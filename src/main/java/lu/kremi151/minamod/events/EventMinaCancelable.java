package lu.kremi151.minamod.events;

import lu.kremi151.minamod.capabilities.sketch.ISketch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public abstract class EventMinaCancelable extends Event{

	private final EntityPlayer p;
	private final World world;
	private final BlockPos pos;
	
	public EventMinaCancelable(EntityPlayer p, World world, BlockPos pos){
		this.p = p;
		this.world = world;
		this.pos = pos;
	}
	
	public EntityPlayer getPlayer(){
		return p;
	}
	
	public World getWorld(){
		return world;
	}
	
	public BlockPos getPos(){
		return pos;
	}
	
	public static class EventOpenCardCoder extends EventMinaCancelable{

		public EventOpenCardCoder(EntityPlayer p, World world, BlockPos pos) {
			super(p, world, pos);
		}
		
	}
}
