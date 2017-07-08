package lu.kremi151.minamod.biome;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LAKE_WATER;
import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE;

import java.util.Random;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeRural extends Biome{
	
	final static BiomeProperties PROPERTIES = new BiomeProperties("Rural").setBaseHeight(0.100f).setHeightVariation(0.025f).setTemperature(0.45f).setWaterColor(MinaUtils.convertRGBToDecimal(0, 90, 120));

	private int color;
	
	public BiomeRural() {
		super(PROPERTIES);
		this.color = MinaUtils.convertRGBToDecimal(54, 151, 81);
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
	
	@Override
    public WorldGenAbstractTree getRandomTreeFeature(Random rand){
    	//return rand.nextInt(3) == 0 ? WorldGenCustomTree.treeGenPeppel : WorldGenCustomTree.treeGenChestnut;
		//TODO: Trees
		return super.getRandomTreeFeature(rand);
	}

	
	public static boolean allow(Decorate event){ // NO_UCD (unused code)
		switch(event.getType()){
		case FLOWERS:
		case GRASS:
		case LAKE_WATER:
		case PUMPKIN:
		case TREE:
		case CUSTOM:
			return true;
		default:
			return false;
		}
	}
	
	private class BiomeDecoratorRural extends BiomeDecorator{
    	
		BiomeDecoratorRural(){
			super();
    	}
    
    	@Override
    	protected void genDecorations(Biome biome, World worldIn, Random random)
        {
    		super.genDecorations(biome, worldIn, random);
    		Chunk chunk = worldIn.getChunkFromBlockCoords(this.chunkPos);
    		int chunk_X = chunk.x;
    		int chunk_Z = chunk.z;
    		int base_X = chunk_X * 16;
    		int base_Z = chunk_Z * 16;
            
            
        }
    	
	}
	
}
