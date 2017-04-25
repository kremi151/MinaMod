package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChair extends BlockCustomHorizontal{
	
	protected static final AxisAlignedBB blockBounds = new AxisAlignedBB(0.1875d, 0d, 0.1875d, 0.8125d, 0.6875d, 0.8125d);

	public BlockChair() {
		super(Material.WOOD);
		this.setHardness(2F);
		this.setCreativeTab(MinaCreativeTabs.FURNISHING);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(world.isRemote)return false;
		return MinaUtils.makePlayerSitOnBlock(player, world, pos);
    }
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}

}
