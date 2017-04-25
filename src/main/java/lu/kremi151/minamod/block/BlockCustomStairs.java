package lu.kremi151.minamod.block;

import java.util.ArrayList;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockCustomStairs extends BlockStairs{
	
	private static ArrayList<BlockCustomStairs> stairs = new ArrayList<BlockCustomStairs>();
	private static boolean init = false;

	public BlockCustomStairs(IBlockState modelState) {
		super(modelState);
		stairs.add(this);
	}
	
	public static void registerStairBlocks(){
		if(init)return;
		for(int i = 0 ; i < stairs.size() ; i++){
			MinaMod.getProxy().registerBlock(stairs.get(i), stairs.get(i).getUnlocalizedName().substring(5));
		}
		init = true;
	}
	
	public static void registerFireInfos(){
		for(int i = 0 ; i < stairs.size() ; i++){
	        Blocks.FIRE.setFireInfo(stairs.get(i), 5, 20);
		}
	}

}
