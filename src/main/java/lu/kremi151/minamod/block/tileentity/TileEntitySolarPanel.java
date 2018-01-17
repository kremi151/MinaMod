package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.interfaces.IEnergySupplier;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntitySolarPanel extends BaseTileEntity implements ITickable, IEnergySupplier{
	
	private int position = 2;
	
	public int getPanelPosition() {
		return position;
	}

	@Override
	public void update() {
        if (this.world != null && !this.world.isRemote && this.world.getTotalWorldTime() % 10L == 0L)
        {
        	if (world.provider.hasSkyLight())
            {
                int i = world.getLightFor(EnumSkyBlock.SKY, pos) - world.getSkylightSubtracted();
                float f = world.getCelestialAngleRadians(1.0F);

                if (i > 0)
                {
                    float f1 = f < (float)Math.PI ? 0.0F : ((float)Math.PI * 2F);
                    f = f + (f1 - f) * 0.2F;
                    i = Math.round((float)i * MathHelper.cos(f));
                }

                i = MathHelper.clamp(i, 0, 15);
                
                long daytime = world.getWorldTime() % 24000L;
                int panpos = (daytime < 12000L) ? (int)(daytime / 2400L) : 2;
                
                setAndSyncPosition(MathHelper.clamp(panpos, 0, 4));

                TileEntity te = world.getTileEntity(pos.down());
                if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
                	te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).receiveEnergy(i, false);
                }
            }else {
            	setAndSyncPosition(2);
            }
        }
	}
	
	private void setAndSyncPosition(int position) {
		boolean notify = this.position != position;
		this.position = position;
		if(notify)sync();
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setInteger("PanelPos", position);
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        if(packet.getNbtCompound().hasKey("PanelPos", 99)) {
        	this.position = packet.getNbtCompound().getInteger("PanelPos");
            sync();
        }
	}

}
