package lu.kremi151.minamod.block;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockCopper_Block extends Block {

	public BlockCopper_Block() {
			super(Material.IRON);
			String unlocalizedName = "copper_block";
			this.setUnlocalizedName(unlocalizedName);
	        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	        this.setHardness(3.0F);
	        this.setSoundType(blockSoundType.METAL);
	        this.setResistance(5.0F);
	        this.setHarvestLevel("pickaxe", 1);
	       
		}
		@Override
		public boolean isOpaqueCube(IBlockState state){
			return false;
		}
		@Override
		public int quantityDropped(Random rand){
			return rand.nextInt(1)+1;
		}
		@Override
		public Item getItemDropped(IBlockState state, Random rand, int fortune)
	    {
			return Item.getItemFromBlock(MinaBlocks.COPPER_BLOCK);
			
	    }
}
