package lu.kremi151.minamod.worldgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSurfaceTree implements IWorldGenerator{
	
	private final static List<WorldGenBiomeTree> trees;
	
	static{
		List<WorldGenBiomeTree> list = new ArrayList<WorldGenBiomeTree>();
		//list.add(new WorldGenPalm(false));
		
		trees = Collections.unmodifiableList(list);
	}
	
	public WorldGenSurfaceTree(){
		
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(!world.isRemote && trees.size() > 0 && random.nextInt(10) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x,y,z);
			Biome biome = world.getBiomeForCoordsBody(new BlockPos(x,y,z));
			BiomeDecorator decor = biome.decorator;
	    	boolean doGen = TerrainGen.decorate(world, random, pos, Decorate.EventType.TREE);
			if(doGen && decor.treesPerChunk > 0){
				int rn = random.nextInt(trees.size());
				WorldGenBiomeTree treegen = trees.get(rn);
				if(treegen.canGenerateAtBiome(biome)){
					treegen.generate(world, random, pos);
					if(MinaMod.getMinaConfig().canLogWorldGenInfo()){
						WorldGenLogger.get().logBuilding("Tree", pos);
					}
				}
			}
		}
	}
	
}
