package lu.kremi151.minamod.block;

import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomCake extends BlockCake
{
	
	private AxisAlignedBB[] blockBounds;

	int foodAmountPerSlice = 2;
	double cakeHeight = 0.5f;
	
	public BlockCustomCake(){
		this.setCreativeTab(CreativeTabs.FOOD);
		this.bakeBoundingBoxes();
		setHardness(0.5F);
		setSoundType(SoundType.CLOTH);
	}
	
	public BlockCustomCake setFoodAmountPerSlice(int amount){
		this.foodAmountPerSlice = amount;
		return this;
	}
	
	public BlockCustomCake setBlockHeight(double height){
		this.cakeHeight = height;
		this.bakeBoundingBoxes();
		return this;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
        this.eatCake(world, pos, state, player);
        return true;
    }
	
	@Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.eatCake(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }
	
	private void eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.canEat(false))
        {
            player.getFoodStats().addStats(foodAmountPerSlice, 0.1F);
            int i = ((Integer)state.getValue(BITES)).intValue();

            if (i < 6)
            {
                worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                worldIn.setBlockToAir(pos);
            }
        }
    }
	
	private void bakeBoundingBoxes(){
		blockBounds = new AxisAlignedBB[] {new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.1875D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.3125D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.5625D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.6875D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D), new AxisAlignedBB(0.8125D, 0.0D, 0.0625D, 0.9375D, cakeHeight, 0.9375D)};
	}
	
	@Override
    @SideOnly(Side.CLIENT)
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds[(Integer)state.getValue(BITES).intValue()];
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        return blockBounds[(Integer)state.getValue(BITES).intValue()];
    }

	//TODO: 1.9
//    /**
//     * Sets the block's bounds for rendering it as an item
//     */
//    @Override
//    public void setBlockBoundsForItemRender()
//    {
//        float f = 0.0625F;
//        this.setBlockBounds(f, 0.0F, f, 1.0F - f, cakeHeight, 1.0F - f);
//    }


}
