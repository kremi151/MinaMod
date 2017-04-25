package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldGenWookieHouseStructure extends WorldGenStructure{

	public WorldGenWookieHouseStructure() {
		super(new ResourceLocation(MinaMod.MODID, "wookiehouse"));
	}
	
	private boolean checkMinaWorldSettings(Random random, World world, int chunkX, int chunkZ) {
		MinaWorld mw = MinaWorld.forWorld(world);
		return mw.getConfiguration().shouldGenerateWookieHouses() && world
				.getBiome(new BlockPos((chunkX * 16) + random.nextInt(16), 0, (chunkZ * 16) + random.nextInt(16))) == mw
						.getConfiguration().getBiomeForWookieHouses();
	}
	
	@Override
	protected boolean canGenerate(Random random, World world, int chunkX, int chunkZ) {
		return random.nextInt(10) == 0
				&& checkMinaWorldSettings(random, world, chunkX, chunkZ);
	}
	
	@Override
	protected BlockPos findPosition(Random random, World world, int chunkX, int chunkZ, int strWidth, int strLength, int strHeight) {
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkZ * 16) + random.nextInt(16);
		int y = MinaUtils.getHeightValue(world, x, z);
		BlockPos pos = new BlockPos(x, y, z);
		for (int i = 0; i < strWidth; i++) {
			for (int j = 0; j < strLength; j++) {
				IBlockState state = world.getBlockState(new BlockPos(x + i, y - 1, z + j));
				if (state.getBlock() != Blocks.DIRT && state.getBlock() != Blocks.GRASS) {
					return null;
				}
			}
		}
		return pos;
	}

}
