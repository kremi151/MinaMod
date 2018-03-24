package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.block.tileentity.TileEntityGiftBox;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockGiftBox extends BlockColored{
	
	public static String[] getVariantNames(String baseName) {
		String variantNames[] = new String[EnumDyeColor.values().length];
		for(int i = 0 ; i < variantNames.length ; i++){
			variantNames[i] = baseName + "_" + EnumDyeColor.values()[i].getName();
		}
		return variantNames;
	}
	
	protected static final AxisAlignedBB boundings = new AxisAlignedBB(0.125d, 0d, 0.125d, 0.875d, 0.75d, 0.875d);

	public BlockGiftBox() {
		super(Material.CLOTH);
		setSoundType(SoundType.PLANT);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(0.5f);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return boundings;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityGiftBox();
    }
	
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
		return null;
    }
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		if(stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
			ItemStack gift = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).getStackInSlot(0);
			if(!gift.isEmpty()) {
				((TileEntityGiftBox)worldIn.getTileEntity(pos)).setGiftItem(gift.copy());
			}
		}
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityGiftBox)
        {
        	TileEntityGiftBox te = (TileEntityGiftBox)tileentity;
        	if(!te.getGiftItem().isEmpty())InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY() + 0.5d, pos.getZ(), te.getGiftItem());
        }

        super.breakBlock(worldIn, pos, state);
    }

}
