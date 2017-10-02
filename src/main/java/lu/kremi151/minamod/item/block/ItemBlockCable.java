package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockCable;
import lu.kremi151.minamod.block.tileentity.TileEntityWallCable;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockCable extends ItemBlock{

	public ItemBlockCable(BlockCable block) {
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        
        if(!player.isSneaking() && iblockstate.isBlockNormalCube() && !block.hasTileEntity(iblockstate)) {
        	TileEntityWallCable wc = new TileEntityWallCable();
        	worldIn.setBlockState(pos, MinaBlocks.WALL_CABLE.getDefaultState());
        	worldIn.setTileEntity(pos, wc);
        	wc.setWallModel(iblockstate);
        	wc.getCapability(IEnergyNetworkProvider.CAPABILITY, null).getNetwork();//Initialize
            return EnumActionResult.SUCCESS;
        }else {
        	return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
    }

}
