package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.interfaces.IOreInjector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;


public class WorldGenOre implements IWorldGenerator, IOreInjector{
	
	int minY;
	int maxY;
	int chance;
	
	private WorldGenTestMinable wrapped;
	private DimensionType dimension;
	
	public WorldGenOre(DimensionType dimension, IBlockState ore, int minY, int maxY, int vein, int chance){
		this.minY = minY;
		this.maxY = maxY;
		this.chance = chance;
		this.wrapped = new WorldGenTestMinable(ore, vein);
		this.dimension = dimension;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		injectOre(random, chunkX, chunkZ, world, chunkProvider);
	}

	@Override
	public void injectOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkProvider) {
		if(world.provider.getDimensionType() == dimension && random.nextInt(chance) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int y = minY + random.nextInt(maxY - minY);
			int z = (chunkZ * 16) + random.nextInt(16);
//			if(world.getBlock(x, y, z) == Blocks.stone && canGenerate(world,y)){
//				world.setBlock(x, y, z, ore);
//				System.out.println("Ore " + ore.getUnlocalizedName() + " generated at " + x + " " + y + " " + z);
//			}
			BlockPos pos = new BlockPos(x, y, z);
			wrapped.generate(world, random, pos);
		}
	}
	
}
