package lu.kremi151.minamod.block;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

public class BlockBerryCrop extends BlockCustomCrops{
	
	private final BerryType type;
	
	public BlockBerryCrop(BerryType type){
		this.type = type;
	}
	
	@Override
    protected Item getSeed()
    {
        return MinaItems.BERRY_SEEDS;
    }

	@Override
    protected Item getCrop()
    {
        return MinaItems.BERRY;
    }
	
	@Override
	public int getCropMetadata(IBlockState state) {
		return type.getCropMeta();
	}
	
	@Override
	public int getSeedMetadata(IBlockState state) {
		return type.getSeedMetadata();
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
