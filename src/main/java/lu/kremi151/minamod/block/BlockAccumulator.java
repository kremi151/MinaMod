package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.block.tileentity.TileEntityAccumulator;
import lu.kremi151.minamod.interfaces.IDiagnosable;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockAccumulator extends BlockCustomHorizontal implements IDiagnosable{
	
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.9375, 0.9375);
	
	private static final PropertyInteger CHARGE = PropertyInteger.create("charge", 0, 4);

	public BlockAccumulator() {
		super(Material.IRON, MapColor.SNOW);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te.getClass() == TileEntityAccumulator.class) {
			return state.withProperty(CHARGE, ((TileEntityAccumulator)te).getStateCharge());
		}else {
			return state.withProperty(CHARGE, 0);
		}
	}

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return AABB;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState bs)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState bs)
    {
        return new TileEntityAccumulator();
    }

	@Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, CHARGE});
    }

	@Override
	public void onDiagnose(IBlockAccess world, BlockPos pos, ICommandSender subject) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityAccumulator) {
			TileEntityAccumulator accu = (TileEntityAccumulator) te;
			IEnergyStorage nrj = accu.getEnergy();
			TextHelper.sendTranslateableChatMessage(subject, "msg.accumulator.charged", nrj.getEnergyStored(), nrj.getMaxEnergyStored(), ((float)nrj.getEnergyStored() / (float)nrj.getMaxEnergyStored()) * 100.0f);
		}
	}

}
