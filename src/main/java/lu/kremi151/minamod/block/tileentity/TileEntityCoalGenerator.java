package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.block.BlockCoalGenerator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityCoalGenerator extends TileEntitySidedInventory implements ITickable{
	
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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		saveItems(nbt);
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound tagPacket = new NBTTagCompound();
		writeToNBT(tagPacket);
		return new SPacketUpdateTileEntity(this.pos, 0, tagPacket);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}
	
}
