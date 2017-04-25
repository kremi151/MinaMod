package lu.kremi151.minamod.worldgen;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenHoneyTrees implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(world.provider.getDimensionType() == DimensionType.OVERWORLD && random.nextInt(50) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x,y,z);
			Biome biome = world.getBiomeForCoordsBody(pos);
			BiomeDecorator decor = biome.theBiomeDecorator;
			boolean doGen = TerrainGen.decorate(world, random, pos, TREE);
			if(!doGen || decor.treesPerChunk <= 0)return;
			WorldGenAbstractTree treegen = biome.genBigTreeChance(random);//random treegen
			if(treegen == null)return;
			treegen.setDecorationDefaults();
			generateHoneyTree(world, new BlockPos(x, y, z), random, treegen);
			if(MinaMod.getMinaConfig().canLogWorldGenInfo()){
				WorldGenLogger.get().logBuilding("Honey tree", x, y, z);
			}
		}
	}
	
	public static void generateHoneyTree(World world, BlockPos pos, Random random, WorldGenAbstractTree base) {
		try{
			base.generate(world, random, pos);
		}catch(Throwable t){
			return;
		}
		for(int i = pos.getX() - 3 ; i <= pos.getX() + 3 ; i++){
			for(int j = pos.getY() + 8 ; j >= pos.getY() ; j--){
				for(int k = pos.getZ() - 3 ; k <= pos.getZ() + 3 ; k++){
					BlockPos npos = new BlockPos(i,j,k);
					Block b = world.getBlockState(npos).getBlock();
					EnumFacing ef = EnumFacing.getHorizontal(random.nextInt(4));
					BlockPos hpos = npos.add(ef.getDirectionVec());
					if((b instanceof BlockLog) && world.isAirBlock(hpos)){
						world.setBlockState(hpos, MinaBlocks.HONEYCOMB.getDefaultState().withProperty(BlockHoneycomb.FACING, ef).withProperty(BlockHoneycomb.HAS_HONEY, true).withProperty(BlockHoneycomb.HAS_BEES, true));
						return;
					}
				}
			}
		}
	}

}
