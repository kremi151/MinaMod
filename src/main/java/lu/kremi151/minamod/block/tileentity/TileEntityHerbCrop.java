package lu.kremi151.minamod.block.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lu.kremi151.minamod.enums.EnumHerb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityHerbCrop extends TileEntity{
	
	private EnumHerb type = EnumHerb.WHITE, mutation = null;
	private float mutability = 0.0f;

	public TileEntityHerbCrop(){
		super();
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		type = EnumHerb.getByHerbId(nbt.getByte("Type"));
		mutability = nbt.getFloat("Mutability");
		
		if(nbt.hasKey("Mutation", 99)){
			mutation = EnumHerb.getByHerbId(nbt.getByte("Mutation"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setByte("Type", type.getHerbId());
		nbt.setFloat("Mutability", mutability);
		
		if(mutation != null){
			nbt.setByte("Mutation", mutation.getHerbId());
		}
		
		return nbt;
	}
	
	public EnumHerb getType(){
		return type;
	}
	
	public void setType(@Nonnull EnumHerb type){
		if(type == null){
			throw new NullPointerException();
		}else if(this.type != type){
			this.type = type;
			this.markDirty();
		}
	}
	
	@Nullable
	public EnumHerb getMutation(){
		return mutation;
	}
	
	public void setMutation(@Nullable EnumHerb mutation){
		if(this.mutation != mutation){
			this.mutation = mutation;
			this.markDirty();
		}
	}
	
	public float getMutability(){
		return mutability;
	}
	
	public void setMutability(float v){
		this.mutability = v;
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setByte("Type", type.getHerbId());
		return nbt;
    }

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		/*NBTTagCompound tagPacket = new NBTTagCompound();
		writeToNBT(tagPacket);
		return new SPacketUpdateTileEntity(this.pos, 0, tagPacket);*/
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		readFromNBT(packet.getNbtCompound());
	}
}
