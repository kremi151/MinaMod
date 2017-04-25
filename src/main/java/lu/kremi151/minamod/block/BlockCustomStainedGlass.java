package lu.kremi151.minamod.block;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCustomStainedGlass extends BlockStainedGlass{

	public static final String milkyGlassVariantNames[];
	public static final String litMilkyGlassVariantNames[];
	
	static{
		EnumDyeColor[] colors = EnumDyeColor.values();

		milkyGlassVariantNames = new String[colors.length];
		litMilkyGlassVariantNames = new String[colors.length];
		for(int i = 0 ; i < colors.length ; i++){
			milkyGlassVariantNames[i] = "milky_glass_" + colors[i].getName();
			litMilkyGlassVariantNames[i] = "lit_milky_glass_" + colors[i].getName();
		}
	}

	public BlockCustomStainedGlass() {
		super(Material.GLASS);
		setSoundType(SoundType.GLASS);
		setHardness(0.3f);
	}
	
    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos)
    {
    	if(this == MinaBlocks.MILKY_GLASS){
    		EnumDyeColor c = EnumDyeColor.byMetadata(getMetaFromState(state));
        	
        	switch(c){
        	case ORANGE:
        		return new float[]{1, 0.7333f, 0.5686f};
        	case MAGENTA:
        		return new float[]{0.8627f, 0.6f, 0.9098f};
        	case LIGHT_BLUE:
        		return new float[]{0.7412f, 0.9451f, 1f};
        	case YELLOW:
        		return new float[]{1f, 1f, 0.6588f};
        	case LIME:
        		return new float[]{0.7333f, 1f, 0.6588f};
        	case PINK:
        		return new float[]{1f, 0.7412f, 0.9765f};
        	case GRAY:
        		return new float[]{0.8118f, 0.8118f, 0.8118f};
        	case SILVER:
        		return new float[]{0.7608f, 0.7608f, 0.7608f};
        	case CYAN:
        		return new float[]{0.6588f, 0.9647f, 1f};
        	case PURPLE:
        		return new float[]{0.9098f, 0.7098f, 1f};
        	case BLUE:
        		return new float[]{0.7098f, 0.7216f, 1f};
        	case BROWN:
        		return new float[]{0.7020f, 0.4902f, 0.4902f};
        	case GREEN:
        		return new float[]{0.5686f, 0.7490f, 0.5765f};
        	case RED:
        		return new float[]{1f, 0.7020f, 0.7020f};
        	case BLACK:
        		return new float[]{0.6f, 0.6f, 0.6f};
        	default:
        		return new float[]{1,1,1};
        	}
    	}
    	return null;
    }

}
