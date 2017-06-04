package lu.kremi151.minamod.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenQuicksand implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(random.nextInt(20) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z) - 1;
			BlockPos pos = new BlockPos(x, y, z);
			Biome biome = world.getBiome(pos);
			if(biome == Biomes.DESERT){
				generateQuicksandSphere(world, pos, 4 + random.nextInt(4), MinaBlocks.QUICKSAND.getDefaultState(), block -> block == Blocks.SAND || block == Blocks.SANDSTONE || block == Blocks.STONE);
			}
		}
	}
	
	private void generateQuicksandSphere(World world, BlockPos center, int radius, IBlockState state, Predicate<Block> replaceable){
		for(int x = center.getX() - radius ; x <= center.getX() + radius ; x++){
			for(int z = center.getZ() - radius ; z <= center.getZ() + radius ; z++){
				for(int y = center.getY() - radius ; y <= center.getY() + radius ; y++){
					double dist = Math.sqrt(Math.pow(Math.abs(center.getX() - x), 2.0) + Math.pow(Math.abs(center.getY() - y), 2.0) + Math.pow(Math.abs(center.getZ() - z), 2.0));
					if(dist <= radius){
						BlockPos pos = new BlockPos(x, y, z);
						IBlockState old = world.getBlockState(pos);
						if(replaceable.test(old.getBlock())){
							world.setBlockState(pos, state);
						}
					}
				}
			}
		}
	}

}
