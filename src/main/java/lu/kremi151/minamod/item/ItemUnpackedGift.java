package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.block.tileentity.TileEntityGiftBox;
import lu.kremi151.minamod.container.ContainerSelectItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemUnpackedGift extends Item{

	public ItemUnpackedGift() {
		setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for(int i = 0 ; i < EnumDyeColor.values().length ; i++) {
        	subItems.add(new ItemStack(this, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		tooltip.add(I18n.translateToLocal("item.unpacked_gift.lore"));
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(hand == EnumHand.MAIN_HAND) {
			final int heldPos = player.inventory.currentItem;
			ContainerSelectItem.selectItem(player, heldPos, ContainerSelectItem.TITLE_PACK_GIFT, res -> {
				ItemStack original = player.inventory.getCurrentItem();
				ItemStack selected = player.inventory.getStackInSlot(res.selected);
				if(!original.isEmpty() && original.getItem() == MinaItems.UNPACKED_GIFT && original != selected) {
					ItemStack gift = new ItemStack(MinaBlocks.GIFT_BOX, 1, original.getMetadata());
					((IItemHandler)gift.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).insertItem(0, selected.copy(), false);
					player.inventory.setInventorySlotContents(res.selected, ItemStack.EMPTY);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, gift);
				}
			});
	        return EnumActionResult.SUCCESS;
		}else {
			return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
    }
}
