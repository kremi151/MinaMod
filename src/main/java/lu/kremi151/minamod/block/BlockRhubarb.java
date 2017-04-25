package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaItems;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRhubarb extends BlockBush{
	
	protected static final AxisAlignedBB blockBounds = new AxisAlignedBB(0d, 0d, 0d, 1d, 0.8d, 1d);
	
	public BlockRhubarb() {
		super(Material.PLANTS);
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setSoundType(SoundType.PLANT);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds;
	}
	
	@Override
	public Item getItemDropped(IBlockState a1, Random a2, int a3){
		return MinaItems.RHUBARB;
	}
	
	@Override
	public int quantityDropped(Random r){
		return 1 + r.nextInt(3);
	}
	
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
            //TODO: knolle droppen
        }
    }
}
