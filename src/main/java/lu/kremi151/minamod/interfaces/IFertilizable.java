package lu.kremi151.minamod.interfaces;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFertilizable {

	public boolean onFertilize(World world, BlockPos pos, Random r);
	
}
