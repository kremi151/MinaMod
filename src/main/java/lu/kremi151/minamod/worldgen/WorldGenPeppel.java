package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenPeppel extends WorldGenBiomeTree{
	
	boolean strictCoords = false;
	
// TODO from UCDetector: Constructor "WorldGenPeppel.WorldGenPeppel(boolean,boolean)" has 0 references
	public WorldGenPeppel(boolean notify, boolean strictCoords) { // NO_UCD (unused code)
		super(notify);
		this.strictCoords = strictCoords;
	}

	@Override
	public boolean generate(World world, Random var2, BlockPos pos) {
		
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		
		int var7 = !strictCoords ? i + var2.nextInt(8) - var2.nextInt(8) : i;
        int var8 = !strictCoords ? j + var2.nextInt(4) - var2.nextInt(4) : j;
        int var9 = !strictCoords ? k + var2.nextInt(8) - var2.nextInt(8) : k;
        
        BlockPos apos = new BlockPos(var7, var8 - 1, var9);
        while(world.isAirBlock(apos)){
        	apos = apos.down();
        }
        
        if(world.getBlockState(apos).getBlock() != Blocks.GRASS && world.getBlockState(apos).getBlock() != Blocks.DIRT)return true;
        
        BlockPos bpos = apos.up();
        
        if (world.isAirBlock(bpos) && (!world.provider.hasNoSky() || var8 < 127))
        {
        	world.setBlockState(apos, Blocks.DIRT.getDefaultState());
        	try{
        		generateTree(world, var2, var7, var8, var9);
        	}catch(Throwable t){
	        	MinaMod.errorln("Peppel tree could not be generated");
        		t.printStackTrace(System.err);
        	}
        	
        }

        return true;
	}
	
	public static void generateTree(World world, Random rand, int i, int j, int k){
		int height = 15 + rand.nextInt(9);
		IBlockState leaves = MinaBlocks.LEAVES_PEPPEL.getDefaultState();
		IBlockState log = MinaBlocks.LOG_PEPPEL.getDefaultState();
		
		if(MinaMod.getMinaConfig().canLogWorldGenInfo()){
			WorldGenLogger.get().logBuilding("Peppel", i, j, k);
		}
		
		for(int y = j ; y < j + height ; y++){
			BlockPos cpos = new BlockPos(i, y, k);
        	world.setBlockState(cpos, log, 3);
        	
        	if(y >= j + 2){
	        	world.setBlockState(cpos.add(0, 0, -1), leaves, 3);
	        	world.setBlockState(cpos.add(1, 0, 0), leaves, 3);
	        	world.setBlockState(cpos.add(0, 0, 1), leaves, 3);
	        	world.setBlockState(cpos.add(-1, 0, 0), leaves, 3);
        	}
        	if(y >= j + 3 && y < j + height - 1){
	        	world.setBlockState(cpos.add(-1, 0, -1), leaves, 3);
	        	world.setBlockState(cpos.add(1, 0, -1), leaves, 3);
	        	world.setBlockState(cpos.add(1, 0, 1), leaves, 3);
	        	world.setBlockState(cpos.add(-1, 0, 1), leaves, 3);
        	}
        	if(y >= j + 3 && y < j + height - 1){
	        	world.setBlockState(cpos.add(0, 0, -2), leaves, 3);
	        	world.setBlockState(cpos.add(2, 0, 0), leaves, 3);
	        	world.setBlockState(cpos.add(0, 0, 2), leaves, 3);
	        	world.setBlockState(cpos.add(-2, 0, 0), leaves, 3);
        	}
        	if(y >= j + 5 && y < j + height - 5){
	        	world.setBlockState(cpos.add(-2, 0, -2), leaves, 3);
	        	world.setBlockState(cpos.add(2, 0, -2), leaves, 3);
	        	world.setBlockState(cpos.add(2, 0, 2), leaves, 3);
	        	world.setBlockState(cpos.add(-2, 0, 2), leaves, 3);

	        	world.setBlockState(cpos.add(-1, 0, -2), leaves, 3);
	        	world.setBlockState(cpos.add(1, 0, -2), leaves, 3);
	        	world.setBlockState(cpos.add(2, 0, -1), leaves, 3);
	        	world.setBlockState(cpos.add(2, 0, 1), leaves, 3);
	        	world.setBlockState(cpos.add(1, 0, 2), leaves, 3);
	        	world.setBlockState(cpos.add(-1, 0, 2), leaves, 3);
	        	world.setBlockState(cpos.add(-2, 0, 1), leaves, 3);
	        	world.setBlockState(cpos.add(-2, 0, -1), leaves, 3);
        	}
        	if(y >= j + 5 && y < j + height - 5){
	        	world.setBlockState(cpos.add(0, 0, -3), leaves, 3);
	        	world.setBlockState(cpos.add(3, 0, 0), leaves, 3);
	        	world.setBlockState(cpos.add(0, 0, 3), leaves, 3);
	        	world.setBlockState(cpos.add(-3, 0, 0), leaves, 3);
        	}
    	}
		
		BlockPos dpos = new BlockPos(i, j + height, k);
		
    	world.setBlockState(dpos, leaves, 3);
    	world.setBlockState(dpos.add(0, 0, -1), leaves, 3);
    	world.setBlockState(dpos.add(1, 0, 0), leaves, 3);
    	world.setBlockState(dpos.add(0, 0, 1), leaves, 3);
    	world.setBlockState(dpos.add(-1, 0, 0), leaves, 3);
    	world.setBlockState(dpos.add(0, 1, 0), leaves, 3);
	}

	@Override
	boolean canGenerateAtBiome(Biome biome) {
		return biome == Biomes.TAIGA;
	}

}
