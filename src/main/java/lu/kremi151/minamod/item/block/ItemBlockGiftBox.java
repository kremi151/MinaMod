package lu.kremi151.minamod.item.block;

import java.util.List;

import lu.kremi151.minamod.block.tileentity.TileEntityGiftBox;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockGiftBox extends ItemCloth{
	
	public ItemBlockGiftBox(Block block) {
		super(block);
	}

    @SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		NBTTagCompound inbt = stack.getSubCompound(TileEntityGiftBox.GIFT_ITEM_TAG);
		if(inbt != null){
			ItemStack stack2 = new ItemStack(inbt);
			tooltip.add(I18n.translateToLocalFormatted("tile.gift_box.contains", stack2.getCount(), stack2.getDisplayName()));
		}else{
			tooltip.add(I18n.translateToLocal("tile.gift_box.no_item"));
		}
    }
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName();
    }

}
