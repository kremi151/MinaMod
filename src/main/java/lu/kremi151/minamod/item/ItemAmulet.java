package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.item.amulet.AmuletBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAmulet extends Item{
	
	private final AmuletBase amuletType;

	public ItemAmulet(AmuletBase amuletType){
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
		this.amuletType = amuletType;
	}
	
	public boolean tryUseAmulet(ItemStack stack, World world, EntityPlayer player) {
		if(!stack.isEmpty() && stack.getItem().getClass() == getClass()) {
			return amuletType.onUse(world, player, stack);
		}else {
			return false;
		}
	}
	
	protected final NBTTagCompound getAmuletData(ItemStack stack) {
		return stack.getOrCreateSubCompound("amulet");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack){
		return amuletType.hasEffect(getAmuletData(stack));
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return amuletType.hasDurability(getAmuletData(stack));
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		NBTTagCompound data = getAmuletData(stack);
        return amuletType.hasDurability(data) ? amuletType.getDurability(data) : super.getDurabilityForDisplay(stack);
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		amuletType.addInformation(getAmuletData(stack), player, tooltip, advanced);
    }
}
