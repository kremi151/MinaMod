package lu.kremi151.minamod.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IEntityAIHerbivoreListener {

	public void onBlockEaten(World world, BlockPos pos);
	public boolean canEat();
	
}
