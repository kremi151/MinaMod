package lu.kremi151.minamod.block;

import lu.kremi151.minamod.block.tileentity.TileEntityEnergySource;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Testing class. Will be deleted for final release.
 * @author michm
 *
 */
public class BlockEnergySource extends Block{

	public BlockEnergySource() {
		super(Material.IRON);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityEnergySource();
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(!world.isRemote) {
			((TileEntityEnergySource) world.getTileEntity(pos)).toggleProducingEnergy(player);
		}
        return false;
    }

}
