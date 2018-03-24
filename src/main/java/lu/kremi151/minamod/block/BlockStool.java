package lu.kremi151.minamod.block;

import java.util.ArrayList;
import java.util.Iterator;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaCreativeTabs;
import lu.kremi151.minamod.item.block.ItemBlockStool;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import lu.kremi151.minamod.util.registration.ItemRegistrationHandler;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStool extends BlockColored{
	
	protected static final AxisAlignedBB blockBounds = new AxisAlignedBB(0.125d, 0d, 0.125d, 0.875d, 0.6875d, 0.875d);
	public static final String DUMMY_MOUNTED_NAME = "___mounted-entity";
	
	private static final ArrayList<BlockStool> stool_blocks = new ArrayList<BlockStool>();
	
	public BlockStool() {
		super(Material.WOOD);
		this.setHardness(2F);
		this.setCreativeTab(MinaCreativeTabs.FURNISHING);
		stool_blocks.add(this);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos){
		return blockBounds;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){
		if(world.isRemote)return false;
		return MinaUtils.makePlayerSitOnBlock(player, world, pos);
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState ibs){
		return false;
	}
	
	public static Iterator<BlockStool> getStools(){
		return stool_blocks.iterator();
	}
	
	public static void registerStoolItems(IRegistrationInterface<Item, ItemRegistrationHandler> registry){
		EnumDyeColor[] colors = EnumDyeColor.values();
		String[] vn_oak;
		String[] vn_dark_oak;
		String[] vn_acacia;
		int colors_length = colors.length;
		vn_oak = new String[colors_length];
		vn_dark_oak = new String[colors_length];
		vn_acacia = new String[colors_length];
		for(int i = 0 ; i < colors_length ; i++){
			vn_oak[i] = colors[i].getName().toLowerCase() + "_stool_oak";
			vn_dark_oak[i] = colors[i].getName().toLowerCase() + "_stool_dark_oak";
			vn_acacia[i] = colors[i].getName().toLowerCase() + "_stool_acacia";
		}
		
		registry.register(new ItemBlockStool(MinaBlocks.OAK_STOOL).setRegistryName(MinaBlocks.OAK_STOOL.getRegistryName()), "stool_oak").variantNames(vn_oak).submit();
		registry.register(new ItemBlockStool(MinaBlocks.DARK_OAK_STOOL).setRegistryName(MinaBlocks.DARK_OAK_STOOL.getRegistryName()), "stool_dark_oak").variantNames(vn_dark_oak).submit();
		registry.register(new ItemBlockStool(MinaBlocks.ACACIA_STOOL).setRegistryName(MinaBlocks.ACACIA_STOOL.getRegistryName()), "stool_acacia").variantNames(vn_acacia).submit();
    }
	
}
