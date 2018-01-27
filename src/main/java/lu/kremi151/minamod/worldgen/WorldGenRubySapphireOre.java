package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.interfaces.IOreInjector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenRubySapphireOre implements IWorldGenerator, IOreInjector{
	
	int minY;
	int maxY;
	int chance;

	private WorldGenTestMinable wrapped1;
	private WorldGenTestMinable wrapped2;
	
	public WorldGenRubySapphireOre(IBlockState ore1, IBlockState ore2, int minY, int maxY, int vein, int chance){
		this.minY = minY;
		this.maxY = maxY;
		this.chance = chance;
		this.wrapped1 = new WorldGenTestMinable(ore1, vein);
		this.wrapped2 = new WorldGenTestMinable(ore2, vein);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		injectOre(random, chunkX, chunkZ, world, chunkProvider);
	}

	@Override
	public void injectOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkProvider) {
		if(random.nextInt(chance) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int y = minY + random.nextInt(maxY - minY);
			int z = (chunkZ * 16) + random.nextInt(16);
			BlockPos pos = new BlockPos(x, y, z);
			if(random.nextFloat() <= 0.5f){
				wrapped1.generate(world, random, pos);
			}else{
				wrapped2.generate(world, random, pos);
			}
		}
	}

}
