package lu.kremi151.minamod.item;

import java.util.List;

import lu.kremi151.minamod.item.amulet.AmuletBase;
import lu.kremi151.minamod.item.amulet.AmuletRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAmulet extends Item{

	public ItemAmulet(){
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void getSubItems(Item a1, CreativeTabs a2, NonNullList<ItemStack> a3){
		for(int i = 0 ; i < AmuletRegistry.amuletCount() ; i++){
			a3.add(new ItemStack(a1,1,i));
		}
	}
	
	@Override
	public int getMetadata(ItemStack stack)
    {
        return (stack.getItemDamage() < AmuletRegistry.amuletCount())?stack.getItemDamage():0;
    }
	
	protected final NBTTagCompound getAmuletData(ItemStack stack) {
		return stack.getOrCreateSubCompound("amulet");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean hasEffect(ItemStack stack){
		return getAmuletType(stack.getMetadata()).hasEffect(getAmuletData(stack));
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
    {
        return getAmuletType(stack.getMetadata()).hasDurability(getAmuletData(stack));
    }
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
    {
		NBTTagCompound data = getAmuletData(stack);
		AmuletBase amulet = getAmuletType(stack.getMetadata());
        return amulet.hasDurability(data) ? amulet.getDurability(data) : super.getDurabilityForDisplay(stack);
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
		getAmuletType(stack.getMetadata()).addInformation(getAmuletData(stack), player, tooltip, advanced);
    }
	
	private AmuletBase getAmuletType(int meta){
		if(meta >= 0 && meta < AmuletRegistry.amuletCount()){
	        return AmuletRegistry.getById(meta);
		}else{
			return AmuletRegistry.getById(0);
		}
	}
	
	@Override
    public String getUnlocalizedName(ItemStack stack)
    {
		return super.getUnlocalizedName(stack) + "." + getAmuletType(stack.getMetadata()).getUnlocalizedName();
    }
}
