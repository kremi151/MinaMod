package lu.kremi151.minamod.worldgen;

import java.util.Random;

import com.google.common.base.Predicate;

import lu.kremi151.minamod.interfaces.IOreInjector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenNewOre extends WorldGenMinable implements IWorldGenerator, IOreInjector{
	
	private DimensionType dimension;
	private int minY = 6, maxY = 40, chancesToSpawn;

	public WorldGenNewOre(DimensionType dimension, IBlockState state, int blockCount, int chancesToSpawn) {
		super(state, blockCount);
		this.dimension = dimension;
		this.chancesToSpawn = chancesToSpawn;
	}
	
	public WorldGenNewOre(DimensionType dimension, IBlockState state, int blockCount, int chancesToSpawn, Predicate<IBlockState> filter) {
		super(state, blockCount, filter);
		this.dimension = dimension;
		this.chancesToSpawn = chancesToSpawn;
	}

	public WorldGenNewOre(IBlockState state, int blockCount, int chancesToSpawn) {
		this(DimensionType.OVERWORLD, state, blockCount, chancesToSpawn);
	}
	
	public WorldGenNewOre(IBlockState state, int blockCount, int chancesToSpawn, Predicate<IBlockState> filter) {
		this(DimensionType.OVERWORLD, state, blockCount, chancesToSpawn, filter);
	}
	
	public WorldGenNewOre setRange(int minY, int maxY){
		this.minY = minY;
		this.maxY = maxY;
		return this;
	}

	@Override
	public void injectOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkProvider) {
		if(world.provider.getDimensionType() == dimension){
			for(int c = 0 ; c < chancesToSpawn ; c++){
				int x = (chunkX * 16) + random.nextInt(16);
				int y = minY + random.nextInt(maxY - minY);
				int z = (chunkZ * 16) + random.nextInt(16);
				generate(world, random, new BlockPos(x, y, z));
			}
		}
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		injectOre(random, chunkX, chunkZ, world, chunkProvider);
	}

}
