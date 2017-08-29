package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.tileentity.TileEntityAutoCrafter;
import lu.kremi151.minamod.util.IDRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoCrafter extends BlockContainer{

	public BlockAutoCrafter() {
		super(Material.IRON, MapColor.BLACK);
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAutoCrafter();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote){
        	if (player.isSneaking()) {
            	return false;
            }
    	    player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdAutoCrafter, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

}
