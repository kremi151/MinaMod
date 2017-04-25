package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoFeeder;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoFeeder extends BlockContainer{

	public BlockAutoFeeder() {
		super(Material.IRON);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
		setHardness(5.0F);
		setResistance(10.0F);
		setSoundType(SoundType.METAL);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAutoFeeder();
	}
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
        	if (player.isSneaking()) {
            	return false;
            }
    	    player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdAutoFeeder, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

	@Override
    public boolean hasComparatorInputOverride(IBlockState ibs)
    {
        return true;
    }

	@Override
    public int getComparatorInputOverride(IBlockState ibs, World worldIn, BlockPos pos)
    {
		TileEntityAutoFeeder te = (TileEntityAutoFeeder) worldIn.getTileEntity(pos);
        return Container.calcRedstoneFromInventory(te);
    }

}
