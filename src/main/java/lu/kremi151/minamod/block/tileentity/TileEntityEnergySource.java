package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.block.BlockPipeBase;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Testing class. Will be deleted for final release.
 * @author michm
 *
 */
public class TileEntityEnergySource extends TileEntity implements ITickable{

	private int deliveringEnergy = 9999;
	public long lastClickCooldown = 0;
	
	public TileEntityEnergySource(){
		super();
	}
	
	private BlockPipeBase getCable(){
		return (BlockPipeBase) this.blockType;
	}
	
	private IBlockState getActualState(){
		return ((BlockPipeBase) this.blockType).getActualState(world.getBlockState(pos), world, pos);
	}
	
	public void toggleProducingEnergy(EntityPlayer player) {
		if(System.currentTimeMillis() - lastClickCooldown > 500L) {
			lastClickCooldown = System.currentTimeMillis();
			if(deliveringEnergy >= 9999) {
				deliveringEnergy = 0;
			}else if(deliveringEnergy >= 999) {
				deliveringEnergy = 9999;
			}else if(deliveringEnergy >= 99) {
				deliveringEnergy = 999;
			}else if(deliveringEnergy >= 9) {
				deliveringEnergy = 99;
			}else {
				deliveringEnergy = 9;
			}
			TextHelper.sendChatMessage(player, "Producing energy has been set to " + deliveringEnergy);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Producing", 99)) {
			this.deliveringEnergy = nbt.getInteger("Producing");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setInteger("Producing", deliveringEnergy);
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

	@Override
	public void update() {
		for(EnumFacing face : EnumFacing.values()){
			BlockPos pos = this.pos.offset(face);
			TileEntity te = this.world.getTileEntity(pos);
			if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, null)){
				((IEnergyStorage)te.getCapability(CapabilityEnergy.ENERGY, null)).receiveEnergy(deliveringEnergy, false);
			}
		}
	}

}
