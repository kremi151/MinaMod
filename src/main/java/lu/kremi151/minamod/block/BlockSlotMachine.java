package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.MinaPermissions;
import lu.kremi151.minamod.block.tileentity.TileEntitySlotMachine;
import lu.kremi151.minamod.capabilities.MinaCapabilities;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.IDRegistry;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.server.permission.PermissionAPI;

public class BlockSlotMachine extends BlockCustomHorizontal{
	
	protected static final AxisAlignedBB aabb = new AxisAlignedBB(2.0 / 16.0, 0d, 2.0 / 16.0, 14.0 / 16.0, 12.0 / 16.0, 14.0 / 16.0);

	public BlockSlotMachine() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
    {
		return new TileEntitySlotMachine();
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote && player.hasCapability(ICoinHandler.CAPABILITY, null)){
			if(PermissionAPI.hasPermission(player, MinaPermissions.ALLOW_GAMBLING)) {
				TileEntitySlotMachine te = (TileEntitySlotMachine) world.getTileEntity(pos);
				if(te.setCurrentPlayer(player).isPresent()) {
					player.openGui(MinaMod.getMinaMod(), IDRegistry.guiIdSlotMachine, world, pos.getX(), pos.getY(), pos.getZ());
				}else {
					TextHelper.sendChatMessage(player, "This slot machine is currently in use");
				}
			}else {
				TextHelper.sendTranslateableErrorMessage(player, "msg.gambling.not_allowed");
			}
		}
		return true;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return aabb;
	}

	@Override
	public boolean isOpaqueCube(IBlockState ibs)
    {
        return false;
    }
	
	@Override
    public boolean isFullCube(IBlockState ibs)
    {
        return false;
    }
}
