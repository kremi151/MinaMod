package lu.kremi151.minamod.block.tileentity;

import com.mojang.authlib.GameProfile;

import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.BlockLetterbox;
import lu.kremi151.minamod.inventory.InventoryLetterboxIn;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class TileEntityLetterbox extends TileEntitySidedInventory implements ITickable{
	
	private static final byte MAX_COOLDOWN = 10;
	
	GameProfile owner;
	
	InventoryLetterboxIn invIn;
	//InventoryLetterboxStorage invStorage;
	private byte cooldown = 0;
	
	boolean was_empty = true;

	public TileEntityLetterbox(){
		super(6, "gui.letterbox.storage");
		invIn = new InventoryLetterboxIn(this);
		//invStorage = new InventoryLetterboxStorage(this);
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		//TODO: Remove "inventory" part sooner or later
		if(nbt.hasKey("inventory")){
			NBTTagCompound lnbt = nbt.getCompoundTag("inventory");
			if(lnbt.hasKey("slot_in")){
				NBTTagCompound inbt = lnbt.getCompoundTag("slot_in");
				ItemStack is = new ItemStack(inbt);
				invIn.setInventorySlotContents(0, is);
			}
			for(int i = 0 ; i < 6 ; i++){
				if(lnbt.hasKey("slot_"+i)){
					NBTTagCompound inbt = lnbt.getCompoundTag("slot_"+i);
					ItemStack is = new ItemStack(inbt);
					this.setInventorySlotContents(i, is);
				}
			}
		}else{
			if(nbt.hasKey("ItemIn", 10)){
				NBTTagCompound inbt = nbt.getCompoundTag("ItemIn");
				ItemStack is = new ItemStack(inbt);
				invIn.setInventorySlotContents(0, is);
			}
			loadItems(nbt);
		}
		
		if(nbt.hasKey("empty", 99)){
			setEmpty(nbt.getBoolean("empty"));
		}else{
			was_empty = checkIsEmpty();
		}
		
		
		if(nbt.hasKey("owner")){
			GameProfile owner = NBTUtil.readGameProfileFromNBT(nbt.getCompoundTag("owner"));
			this.owner = owner;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		if(owner != null){
			NBTTagCompound onbt = new NBTTagCompound();
			NBTUtil.writeGameProfile(onbt, owner);
			nbt.setTag("owner", onbt);
		}
		
		if(!invIn.getStackInSlot(0).isEmpty()){
			NBTTagCompound inbt = new NBTTagCompound();
			invIn.getStackInSlot(0).writeToNBT(inbt);
			nbt.setTag("ItemIn", inbt);
		}
		
		saveItems(nbt);
		
		return nbt;
	}
	
	public boolean isOwnerSet(){
		return owner != null;
	}
	
	public GameProfile getOwner(){
		return owner;
	}
	
	public void setOwner(GameProfile owner){
		this.owner = owner;
		this.markDirty();
	}
	
	public boolean checkOwner(GameProfile comp){
		if(owner == null)return true;
		return owner.equals(comp);
	}
	
	public boolean canAccess(EntityPlayer player){
		return PermissionAPI.hasPermission(player, MinaPermissions.WATCH_LETTERBOX_CONTENT) || checkOwner(player.getGameProfile());
	}
	
	public IInventory getInputInventory(){
		return invIn;
	}
	
	public IInventory getStorageInventory(){
		return this;
	}
	
	public boolean isEmpty(){
		return was_empty;
	}
	
	public void setEmpty(boolean v){
		this.was_empty = v;
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setBoolean("empty", isEmpty());
        return nbt;
    }
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		return new SPacketUpdateTileEntity(this.pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void update() {
		if(!this.world.isRemote){
			if(cooldown-- <= 0){
				cooldown = MAX_COOLDOWN;
				ItemStack itemIn = invIn.getStackInSlot(0);
				if(!itemIn.isEmpty()){
					invIn.setInventorySlotContents(0, MinaUtils.addItemStackToInventory(itemIn, this));
					onCraftMatrixChanged();
				}
			}
		}
	}
	
	private boolean checkIsEmpty(){
		if(!invIn.getStackInSlot(0).isEmpty())return false;
		for(int i = 0 ; i < getSizeInventory() ; i++){
			if(!getStackInSlot(i).isEmpty())return false;
		}
		return true;
	}
	
	@Override
	public void onCraftMatrixChanged(){
		boolean b = checkIsEmpty();
		if(b != was_empty && this.world != null){
			was_empty = b;
			//updateFlagState();
			IBlockState old = this.world.getBlockState(pos);
			world.notifyBlockUpdate(pos, old, old.withProperty(BlockLetterbox.EMPTY, was_empty), 3);
		}
	}
	
	/*public void updateFlagState(){
		if(this.world == null)return;
		IBlockState old = this.world.getBlockState(pos);//Error here
		IBlockState new_ = old.withProperty(BlockLetterbox.EMPTY, was_empty);
		this.world.setBlockState(pos, new_, 3);
		this.world.markBlockRangeForRenderUpdate(pos, pos);
	}*/
	
}
