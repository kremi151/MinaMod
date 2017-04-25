package lu.kremi151.minamod.biome;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeVeld extends Biome{
	
	final static BiomeProperties PROPERTIES = new BiomeProperties("Veld").setBaseHeight(0.120f).setHeightVariation(0.05f).setTemperature(0.5f).setWaterColor(MinaUtils.convertRGBToDecimal(0, 20, 180));

	private int color;
	
	public BiomeVeld() {
		super(PROPERTIES);
		this.theBiomeDecorator = new BiomeDecoratorVeld();
		this.color = MinaUtils.convertRGBToDecimal(125, 159, 32);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos)
    {
        return this.color;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getFoliageColorAtPos(BlockPos pos)
    {
        return this.color;
    }
	
	private class BiomeDecoratorVeld extends BiomeDecorator{
    	
		BiomeDecoratorVeld(){
//    		super();
    	}
    
    	@Override
    	protected void genDecorations(Biome biome, World world, Random random)
        {
    		Chunk chunk = world.getChunkFromBlockCoords(this.chunkPos);
    		int chunk_X = chunk.xPosition;
    		int chunk_Z = chunk.zPosition;
            this.generateOres(world, random);
    		int a = random.nextInt(20);
    		for(int aa = 0 ; aa < a ; aa++){
    			if(random.nextInt(2) == 0){
    				int x = (chunk_X * 16) + random.nextInt(16);
    				int z = (chunk_Z * 16) + random.nextInt(16);
        			int y = MinaUtils.getHeightValue(world, x, z);
        			BlockPos npos = new BlockPos(x,y,z);
        			int type = random.nextInt(2);
        			Block b = type == 0 ? MinaBlocks.LITTLE_BUSH : MinaBlocks.OPAQUE_BUSH;
        			if(world.isAirBlock(npos) && b.canPlaceBlockAt(world, npos)){
        				try{
        					world.setBlockState(npos, b.getDefaultState());
    					}catch(Throwable t){
    						
    					}
        			}
    			}
    		}
    		if(random.nextInt(26) == 0){
    			int x = (chunk_X * 16) + random.nextInt(16) + 8;
    			int z = (chunk_Z * 16) + random.nextInt(16) + 8;
    			int minx = x - 1 - random.nextInt(4);
    			int maxx = x + random.nextInt(4);
    			int minz = z - 1 - random.nextInt(4);
    			int maxz = z + random.nextInt(4);
    			for(int x1 = minx ; x1 < maxx ; x1++){
					for(int z1 = minz ; z1 < maxz ; z1++){
    					int y = MinaUtils.getHeightValue(world, x1, z1) - 1;
    					BlockPos mpos = new BlockPos(x1, y, z1);
						if(world.getBlockState(mpos).getBlock() == biome.topBlock){
		    				try{
								world.setBlockState(mpos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
							}catch(Throwable t){
								
							}
		    			}
		    			
					}
				}
    			
    		}
        }
	}
	
}
