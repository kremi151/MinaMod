package lu.kremi151.minamod.block.tileentity;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockSieve;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntitySieve extends TileEntity {
	
	private BlockSieve.MaterialType material = BlockSieve.MaterialType.AIR;
	private float levelF = 0.0f;

	public TileEntitySieve() {
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
		material = BlockSieve.MaterialType.parse(nbt.getString("SieveMaterial"));
		levelF = nbt.getFloat("Level");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

		nbt.setString("SieveMaterial", material.getName());
		nbt.setFloat("Level", levelF);
		
		return nbt;
	}
	
	public BlockSieve.MaterialType getMaterial(){
		return material;
	}
	
	public BlockSieve.MaterialType getActualMaterial(){
		return levelF > 0.0f ? material : BlockSieve.MaterialType.AIR;
	}
	
	public void setNewMaterial(BlockSieve.MaterialType material){
		this.material = material;
		this.levelF = 1.0f;
		this.markDirty();
	}
	
	public void setMaterial(BlockSieve.MaterialType material){
		this.material = material;
		this.markDirty();
	}
	
	public float getLevel(){
		return MathHelper.clamp(levelF, 0.0f, 1.0f);
	}
	
	public void setLevel(float level){
		this.levelF = MathHelper.clamp(level, 0.0f, 1.0f);
		this.markDirty();
	}
	
	public void decrementLevel(float amount){
		float levelF = MathHelper.clamp(this.levelF - amount, 0.0f, 1.0f);
		if(this.levelF != levelF){
			IBlockState state = world.getBlockState(pos).getActualState(world, pos);
			this.levelF = levelF;
			this.markDirty();
			this.world.notifyBlockUpdate(pos, state, state.getActualState(world, pos), 3);
		}
	}
	
	public boolean isSieveEmpty(){
		return this.material == BlockSieve.MaterialType.AIR || this.levelF == 0.0f;
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setString("SieveMaterial", material.getName());
		nbt.setFloat("Level", levelF);
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
