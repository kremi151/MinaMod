package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockGenerator;
import lu.kremi151.minamod.interfaces.IEnergySupplier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityGenerator extends TileEntitySidedInventory implements ITickable, IEnergySupplier{

	private float heating = 0.0f;
	private int fuel = 0;
	private int maxFuel = 0;
	
	public TileEntityGenerator() {
		super(1, "container.minamod.generator");
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
	
	private EnumFacing getOutputFacing() {
		return this.world.getBlockState(pos).getValue(BlockGenerator.FACING).getOpposite();
	}

	@Override
	public boolean canCableConnect(EnumFacing face, TileEntity entity, BlockPos pos, IBlockAccess world) {
		return face == getOutputFacing();
	}
	
	protected void sync() {
		final IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 3);
	}

	@Override
	public void update() {
		if(world.getWorldTime() % 10 == 0) {
			ItemStack stack = getStackInSlot(0);
			if(fuel <= 0 && !stack.isEmpty()) {
				maxFuel = TileEntityFurnace.getItemBurnTime(stack);
				fuel = maxFuel;
				if(fuel > 0) {
					stack.shrink(1);
				}
			}else if(fuel > 0) {
				fuel -= 5;
				heating += 0.05f;
			}
			if(heating < 0.0f) {
				heating = 0.0f;
			}else if(heating > 1.0f) {
				heating = 1.0f;
			}else if(Float.isNaN(heating)){
				heating = 0.0f;
			}
			if(heating > 0.0f) {
				heating -= 0.02f;
				EnumFacing outputFacing = getOutputFacing();
				TileEntity te = world.getTileEntity(pos.offset(outputFacing));
				if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, outputFacing.getOpposite())) {
					IEnergyStorage prov = te.getCapability(CapabilityEnergy.ENERGY, outputFacing.getOpposite());
					int energy = MathHelper.floor(10 * heating);
					if(energy > 0) {
						int rest = energy - prov.receiveEnergy(energy, false);
						heating += (((float)rest / (float)energy) * 0.0125f);
					}
				}else {
					heating += 0.0125f;
				}
				if(heating < 0.0f) {
					heating = 0.0f;
				}else if(heating > 1.0f) {
					heating = 1.0f;
				}
			}
			sync();
		}
	}
	
	public float getProgress() {
		return maxFuel > 0 ? (float)fuel / (float)maxFuel : 0.0f;
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
		fuel = nbt.getInteger("Fuel");
		maxFuel = nbt.getInteger("MaxFuel");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		saveItems(nbt);
		nbt.setFloat("Heating", heating);
		nbt.setInteger("Fuel", fuel);
		nbt.setInteger("MaxFuel", maxFuel);
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setFloat("Heating", heating);
		nbt.setInteger("Fuel", fuel);
		nbt.setInteger("MaxFuel", maxFuel);
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
		world.notifyBlockUpdate(getPos(), state, MinaBlocks.GENERATOR.getActualState(state, world, pos), 3);
	}
	
}
