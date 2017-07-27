package lu.kremi151.minamod.util;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockHoneycomb;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HoneycombUtils {

	/**
	 * Tries to regenerate a honeycomb around the origin position
	 * @param world The world
	 * @param origin The origin position
	 * @param rand The random number generator
	 * @param chance The chance to actually try to generate a new honeycomb. The value is expected to be between 0 and 1 inclusive.
	 */
	public static void tickHoneycombRegeneration(World world, BlockPos origin, Random rand, float chance) {
		if(chance > 0.0f && rand.nextFloat() < chance) {
			for(int n = 0 ; n <3 ; n++) {
				int x = origin.getX() + rand.nextInt(8) - 4;
				int y = origin.getY() + rand.nextInt(4);
				int z = origin.getZ() + rand.nextInt(8) - 4;
				BlockPos pos = new BlockPos(x, y, z);
				if(isHonecombCompatible(world.getBlockState(pos).getBlock())) {
					EnumFacing face = EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)];
					if(world.isAirBlock(pos.offset(face))) {
						world.setBlockState(pos.offset(face), MinaBlocks.HONEYCOMB.getDefaultState().withProperty(BlockHoneycomb.FACING, face).withProperty(BlockHoneycomb.HAS_BEES, true));
						break;
					}
				}
			}
		}
	}
	
	private static boolean isHonecombCompatible(Block target) {
		return target instanceof BlockLog;
	}
}
