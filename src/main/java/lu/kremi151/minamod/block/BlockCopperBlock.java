package lu.kremi151.minamod.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCopperBlock extends Block {

	public BlockCopperBlock() {
		super(Material.IRON);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(3.0F);
		setSoundType(blockSoundType.METAL);
		setResistance(5.0F);
		setHarvestLevel("pickaxe", 1);
	}
	
}
