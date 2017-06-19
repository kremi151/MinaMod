package lu.kremi151.minamod.block;

import java.util.HashMap;
import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockMinaPlanks.EnumType;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMinaLeafBase extends BlockLeaves{

	private static final HashMap<BlockMinaPlanks.EnumType, IBlockState> TYPE_TO_LEAF = new HashMap<>();

	public BlockMinaLeafBase(){
		super();
	}
	
    @Override
    @Deprecated
    public final BlockPlanks.EnumType getWoodType(int meta)
    {
        throw new UnsupportedOperationException("This method is not supported for MinaMod leaves. Use getMinaWoodType instead.");
    }
	
	public abstract BlockMinaPlanks.EnumType getMinaWoodType(int meta);

	public static IBlockState getDefaultStateFor(BlockMinaPlanks.EnumType type){
		return TYPE_TO_LEAF.get(type);
	}

	protected static void registerLeafBlock(BlockMinaPlanks.EnumType type, IBlockState defaultState){
		if(TYPE_TO_LEAF.containsKey(type)){
			throw new IllegalArgumentException("Leaf type is already registered");
		}
		TYPE_TO_LEAF.put(type, defaultState);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return MinaMod.getProxy().tryGetClientSideResult(() -> !net.minecraft.client.Minecraft.getMinecraft().gameSettings.fancyGraphics).orElse(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setGraphicsLevel(boolean fancy){}

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return net.minecraft.client.Minecraft.getMinecraft().gameSettings.fancyGraphics ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !net.minecraft.client.Minecraft.getMinecraft().gameSettings.fancyGraphics && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : Blocks.LEAVES.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
    	return Item.getItemFromBlock(MinaBlocks.SAPLING);
    }
}
