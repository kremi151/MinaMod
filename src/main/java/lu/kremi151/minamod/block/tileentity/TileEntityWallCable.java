package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileEntityWallCable extends TileEntityAbstractCable{
	
	private IBlockState wallModel;
	
	public TileEntityWallCable(){
		super();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }
	
	@Nullable
	public IBlockState getWallModel() {
		return wallModel;
	}
	
	public void setWallModel(IBlockState state) {
		this.wallModel = state;
		this.markDirty();
        state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	private void saveWallModel(NBTTagCompound nbt) {
		if(wallModel != null) {
			nbt.setString("ModelId", wallModel.getBlock().getRegistryName().toString());
			nbt.setInteger("ModelMeta", wallModel.getBlock().getMetaFromState(wallModel));
		}
	}
	
	private void loadWallModel(NBTTagCompound nbt) {
		if(nbt.hasKey("ModelId", 8)) {
			ResourceLocation resid = new ResourceLocation(nbt.getString("ModelId"));
			int meta = nbt.getInteger("ModelMeta");
			Block block = ForgeRegistries.BLOCKS.getValue(resid);
			if(block != null) {
				wallModel = block.getStateFromMeta(meta);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		loadWallModel(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		saveWallModel(nbt);
		return nbt;
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound nbt = super.getUpdateTag();
		saveWallModel(nbt);
		return nbt;
	}

}
