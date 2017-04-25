package lu.kremi151.minamod.block;

import lu.kremi151.minamod.capabilities.MinaCapabilities;
import lu.kremi151.minamod.capabilities.coinhandler.ICoinHandler;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCasinoBlock extends BlockCustomHorizontal{
	
	protected static final AxisAlignedBB aabb = new AxisAlignedBB(2.0 / 16.0, 0d, 2.0 / 16.0, 14.0 / 16.0, 12.0 / 16.0, 14.0 / 16.0);

	public BlockCasinoBlock() {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote && player.hasCapability(ICoinHandler.CAPABILITY_COIN_HANDLER, null)){
			ICoinHandler ch = player.getCapability(ICoinHandler.CAPABILITY_COIN_HANDLER, null);
			if(ch.withdrawCoins(18)){
				TextHelper.sendChatMessage(player, "Currently under construction, but thank you for your 18 coins!");
			}else{
				TextHelper.sendChatMessage(player, "Do you have some coins for me please?");
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
