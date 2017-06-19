package lu.kremi151.minamod.block;

import java.util.HashMap;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.BlockStairs;

public class BlockMinaWoodStairs extends BlockStairs{
	
	private static final HashMap<BlockMinaPlanks.EnumType, BlockMinaWoodStairs> TYPE_MAP = new HashMap<>();

	public BlockMinaWoodStairs(BlockMinaPlanks.EnumType type) {
		super(MinaBlocks.PLANKS.getDefaultState().withProperty(BlockMinaPlanks.VARIANT, type));
		TYPE_MAP.put(type, this);
	}
	
	public static BlockMinaWoodStairs getForType(BlockMinaPlanks.EnumType type){
		return TYPE_MAP.get(type);
	}

}
