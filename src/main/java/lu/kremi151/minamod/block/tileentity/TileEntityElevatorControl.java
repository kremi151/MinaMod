package lu.kremi151.minamod.block.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityElevatorControl extends TileEntity{
	
	private String title = null;
	
	public void setName(String name) {
		this.title = name;
	}
	
	public String getName() {
		return title;
	}
	
	public boolean hasName() {
		return title != null;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		if(nbt.hasKey("Title", 8)){
			title = nbt.getString("Title");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(title != null){
			nbt.setString("Title", title);
		}
		return nbt;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		if(title != null){
			nbt.setString("Title", title);
		}
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}
}
