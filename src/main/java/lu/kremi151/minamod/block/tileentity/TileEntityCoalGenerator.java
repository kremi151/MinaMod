package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockCoalGenerator;
import lu.kremi151.minamod.interfaces.IEnergySupplier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityCoalGenerator extends TileEntitySidedInventory implements ITickable, IEnergySupplier{
	
	public TileEntityCoalGenerator() {
		super(1, "container.minamod.coalgen");
	}

	private static final int PERIOD_MAX = 50;

	private float heating = 0.0f;
	private int period = 0;
	
	private EnumFacing getOutputFacing() {
		return this.world.getBlockState(pos).getValue(BlockCoalGenerator.FACING).getOpposite();
	}

	@Override
	public boolean canCableConnect(EnumFacing face, TileEntity entity, BlockPos pos, IBlockAccess world) {
		return face == getOutputFacing();
	}

	@Override
	public void update() {
		if(world.getWorldTime() % 10 == 0) {
			if(heating < 0.0f) {
				heating = 0.0f;
			}else if(heating > 1.0f) {
				heating = 1.0f;
			}
			if(heating > 0.0f) {
				heating = (heating * 0.95f) - 0.0001f;
				EnumFacing outputFacing = getOutputFacing();
				TileEntity te = world.getTileEntity(pos.offset(outputFacing));
				if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, outputFacing.getOpposite())) {
					IEnergyStorage prov = te.getCapability(CapabilityEnergy.ENERGY, outputFacing.getOpposite());
					int energy = MathHelper.floor(20 * heating);
					int rest = energy - prov.receiveEnergy(energy, false);
					heating *= 1.0f + (((float)rest / (float)energy) / 25f);
				}
			}
			ItemStack stack = getStackInSlot(0);
			if(!stack.isEmpty()) {
				int fuel = GameRegistry.getFuelValue(stack);
				if(true || fuel > 0) {//TODO: Fix coal not found as fuel
					if(period <= 0) {
						stack.shrink(1);
						period = PERIOD_MAX;
					}
					period--;
					heating = (heating + 0.05f) * 1.1f;
				}
			}
		}
	}
	
	public float getProgress() {
		return 1.0f - ((float)period / (float)PERIOD_MAX);
	}
	
	public float getHeating() {
		return MathHelper.clamp(heating, 0f, 1.0f);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return stack.isEmpty() || GameRegistry.getFuelValue(stack) > 0;
	}

	@Override
	public void onCraftMatrixChanged() {}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		loadItems(nbt);
		heating = nbt.getFloat("Heating");
		period = nbt.getInteger("Period");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		saveItems(nbt);
		nbt.setFloat("Heating", heating);
		nbt.setInteger("Period", period);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setFloat("Heating", heating);
		nbt.setInteger("Period", period);
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
		IBlockState state = world.getBlockState(getPos());
		world.notifyBlockUpdate(getPos(), state, MinaBlocks.SIEVE.getActualState(state, world, pos), 3);
	}
	
}
