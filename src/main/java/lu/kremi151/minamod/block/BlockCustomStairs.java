package lu.kremi151.minamod.block;

import java.util.ArrayList;

import lu.kremi151.minamod.util.registration.BlockRegistrationHandler;
import lu.kremi151.minamod.util.registration.IRegistrationInterface;
import net.minecraft.block.Block;
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
	
	public static void registerStairBlocks(IRegistrationInterface<Block, BlockRegistrationHandler> registry){
		if(init)return;
		for(int i = 0 ; i < stairs.size() ; i++){
			registry.register(stairs.get(i), stairs.get(i).getUnlocalizedName().substring(5)).submit();
		}
		init = true;
	}
	
	public static void registerFireInfos(){
		for(int i = 0 ; i < stairs.size() ; i++){
	        Blocks.FIRE.setFireInfo(stairs.get(i), 5, 20);
		}
	}

}
