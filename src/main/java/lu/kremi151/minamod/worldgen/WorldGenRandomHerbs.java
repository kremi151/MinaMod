package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.tileentity.TileEntityHerbCrop;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenRandomHerbs implements IWorldGenerator{
	int chance;
	
	public WorldGenRandomHerbs(int chance){
		this.chance = chance;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(random.nextInt(chance) == 0){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x, y, z);
			if(world.getBlockState(pos.down()).getBlock() != Blocks.GRASS)return;
	    	boolean doGen = TerrainGen.decorate(world, random, pos, Decorate.EventType.FLOWERS);
	    	EnumHerb eh = EnumHerb.getRandomNatural(random);
	    	IBlockState plant = MinaBlocks.HERB_CROP.withAge(7);
			if(world.isAirBlock(pos) && plant.getBlock().canPlaceBlockAt(world, pos) && doGen){
				world.setBlockState(pos, plant);
				TileEntityHerbCrop te = new TileEntityHerbCrop();
				te.setType(eh);
				world.setTileEntity(pos, te);
				plant = plant.getActualState(world, pos);
				world.notifyBlockUpdate(pos, plant, plant, 3);
			}
		}
	}

}
