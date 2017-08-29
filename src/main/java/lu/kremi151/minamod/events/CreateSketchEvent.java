package lu.kremi151.minamod.events;

import lu.kremi151.minamod.capabilities.sketch.ISketch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class CreateSketchEvent extends Event{
	
	private final ItemStack original;
	private ItemStack newSketch;

	public CreateSketchEvent(EntityPlayer p, World world, ItemStack originalSketch) throws IllegalArgumentException{
		super();
		validateStack(originalSketch);
		this.original = originalSketch;
		this.newSketch = originalSketch;
	}
	
	private void validateStack(ItemStack stack) {
		if(!stack.hasCapability(ISketch.CAPABILITY, null)) {
			throw new IllegalArgumentException("Invalid sketch item");
		}
	}
	
	public void setSketchItem(ItemStack stack) throws IllegalArgumentException{
		validateStack(stack);
		this.newSketch = stack;
	}
	
	public ItemStack getOriginalSketch() {
		return original;
	}
	
	public ItemStack getNewSketch() {
		return newSketch;
	}

}
