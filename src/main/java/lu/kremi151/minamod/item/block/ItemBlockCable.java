package lu.kremi151.minamod.item.block;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockCable;
import lu.kremi151.minamod.block.tileentity.TileEntityWallCable;
import lu.kremi151.minamod.capabilities.energynetwork.IEnergyNetworkProvider;
import lu.kremi151.minamod.interfaces.IDrillItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockCable extends ItemBlock implements IDrillItem{

	public ItemBlockCable(BlockCable block) {
		super(block);
	}

	@Override
	public boolean onDrillUsed(World world, BlockPos target, EntityPlayer player, ItemStack stack) {
		IBlockState state = world.getBlockState(target);
		if(!player.isSneaking() && !stack.isEmpty() && state.isBlockNormalCube() && !state.getBlock().hasTileEntity(state)) {
        	TileEntityWallCable wc = new TileEntityWallCable();
        	world.setBlockState(target, MinaBlocks.WALL_CABLE.getDefaultState());
        	world.setTileEntity(target, wc);
        	wc.setWallModel(state);
        	wc.getCapability(IEnergyNetworkProvider.CAPABILITY, null).getNetwork();//Initialize
        	stack.shrink(1);
            return true;
        }else {
        	return false;
        }
	}

}
