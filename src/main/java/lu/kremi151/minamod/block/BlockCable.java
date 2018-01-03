package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.block.tileentity.TileEntityCable;
import lu.kremi151.minamod.capabilities.energynetwork.EnergyNetworkHelper;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetwork;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import lu.kremi151.minamod.capabilities.energynetwork.NetworkProviderImpl;
import lu.kremi151.minamod.interfaces.IEnergySupplier;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class BlockCable extends BlockPipeBase{
	
	public BlockCable() {
		super(Material.CIRCUITS);
		setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[EnumFacing.VALUES.length]));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityCable();
    }

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		TileEntity myte = worldIn.getTileEntity(pos);
		if(myte != null) {
			EnergyNetworkHelper.scanForClients(myte.getCapability(IEnergyNetworkProvider.CAPABILITY, null).getNetwork(), pos);//TODO: Make direction specific
		}else {
			System.err.println("Something went wrong");
		}
    }
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		TileEntity nte = world.getTileEntity(neighbor);
		int ox = neighbor.getX() - pos.getX();
		int oy = neighbor.getY() - pos.getY();
		int oz = neighbor.getZ() - pos.getZ();
		EnumFacing.AxisDirection dir = null;
		EnumFacing.Axis axis = null;
		if(ox != 0) {
			axis = EnumFacing.Axis.X;
			dir = (ox < 0) ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
		}else if(oy != 0) {
			axis = EnumFacing.Axis.Y;
			dir = (oy < 0) ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
		}else if(oz != 0) {
			axis = EnumFacing.Axis.Z;
			dir = (oz < 0) ? EnumFacing.AxisDirection.NEGATIVE : EnumFacing.AxisDirection.POSITIVE;
		}
		EnumFacing face = null;
		for(EnumFacing f : EnumFacing.VALUES) {
			if(f.getAxis() == axis && f.getAxisDirection() == dir) {
				face = f;
				break;
			}
		}
		if(face == null) {
			System.err.println("Neighbour face could not be determined");
			return;
		}
		TileEntity myte = world.getTileEntity(pos);
		if(nte != null && nte.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite())) {
			if(!nte.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite())) {
				if(myte != null) {
					myte.getCapability(IEnergyNetworkProvider.CAPABILITY, face).getNetwork().registerClient(neighbor, face.getOpposite());
				}
			}
		}else {
			if(myte != null) {
				IEnergyNetwork myNetwork = myte.getCapability(IEnergyNetworkProvider.CAPABILITY, face).getNetwork();
				if(nte == null || !nte.hasCapability(IEnergyNetworkProvider.CAPABILITY, face.getOpposite())) {
					myNetwork.unregisterClient(neighbor, face.getOpposite());
					EnergyNetworkHelper.executeSplit(myNetwork, neighbor);
				}
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if(!world.isRemote) {
			IEnergyNetworkProvider nrj = world.getTileEntity(pos).getCapability(IEnergyNetworkProvider.CAPABILITY, null);
			((NetworkProviderImpl)nrj).printDebugInformation(player);
		}
        return true;
    }
	
	/**
	 * Can this pipe connect to the neighbouring block?
	 *
	 * @param ownState           This pipe's state
	 * @param worldIn            The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Can this pipe connect?
	 */
	@Override
	protected boolean canConnectTo(IBlockState ownState, IBlockAccess worldIn, BlockPos ownPos, EnumFacing neighbourDirection) {
		BlockPos destPos = ownPos.offset(neighbourDirection);
		TileEntity dest = worldIn.getTileEntity(destPos);
		return (dest != null && (dest.hasCapability(CapabilityEnergy.ENERGY, neighbourDirection.getOpposite()) || (dest instanceof IEnergySupplier && ((IEnergySupplier)dest).canCableConnect(neighbourDirection.getOpposite(), dest, destPos, worldIn)))) || super.canConnectTo(ownState, worldIn, ownPos, neighbourDirection);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.VALUES) {
			state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, pos, facing));
		}

		return state;
	}
}
