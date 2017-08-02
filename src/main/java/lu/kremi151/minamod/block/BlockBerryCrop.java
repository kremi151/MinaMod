package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBerryCrop extends BlockCustomCrops{
	
	private final BerryType type;
	
	public BlockBerryCrop(BerryType type){
		this.type = type;
	}

	@Override
	public ItemStack getCrop(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.BERRY, 1, type.getCropMeta());
	}

	@Override
	public ItemStack getSeed(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ItemStack(MinaItems.BERRY_SEEDS, 1, type.getSeedMetadata());
	}
	
	public static enum BerryType{
		DOGE(0),
		KEVIKUS(1),
		TRACIUS(2);
		
		private int crop_meta;
		
		private BerryType(int crop_meta){
			this.crop_meta = crop_meta;
		}
		
		public int getCropMeta(){
			return crop_meta;
		}
		
		public int getSeedMetadata(){
			if(this == DOGE){
				return 0;
			}else if(this == KEVIKUS){
				return 1;
			}else if(this == TRACIUS){
				return 2;
			}
			return 0;
		}
	}

}
