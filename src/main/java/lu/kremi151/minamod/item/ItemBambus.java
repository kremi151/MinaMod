package lu.kremi151.minamod.item;

import lu.kremi151.minamod.MinaBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class ItemBambus extends ItemSeeds{

	public ItemBambus() {
		super(MinaBlocks.BAMBUS_CROP, null);
		setCreativeTab(CreativeTabs.MATERIALS);
	}
	
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Plains;
    }

}
