package lu.kremi151.minamod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCustom extends Block{
	
	private AxisAlignedBB blockBounds = null;

	public BlockCustom(Material material) {
		super(material);
	}
	
	public BlockCustom(Material material, MapColor mapColor) {
		super(material, mapColor);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds!=null?blockBounds:super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public Block setSoundType(SoundType sound)
    {
        return super.setSoundType(sound);
    }

}
