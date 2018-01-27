package lu.kremi151.minamod.item;

import java.util.List;

import javax.annotation.Nullable;

import lu.kremi151.minamod.MinaCreativeTabs;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemChip extends Item{

	public ItemChip(){
		this.setHasSubtypes(true);
		this.setCreativeTab(MinaCreativeTabs.TECHNOLOGY);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items){
		for(ChipType type : ChipType.values()){
			items.add(new ItemStack(this,1,type.meta));
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		ChipType type = ChipType.getByMeta(stack.getItemDamage());
		if(type.has_tooltip)tooltip.add(I18n.translateToLocal("item.chip.info." + type.name));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
        if(ChipType.getByMeta(stack.getItemDamage()) != ChipType.PROCESSOR_UNIT) {
        	return super.getUnlocalizedName(stack);
        }else {
        	return "item.processor_unit";
        }
    }
	
	public static String[] getVariantNames() {
		String res[] = new String[ChipType.values().length];
		for(ChipType type : ChipType.values()) {
			res[type.ordinal()] = type.name;
		}
		return res;
	}
	
	public static enum ChipType{
		TYPE_A(0, "chip_a"),
		TYPE_B(1, "chip_b"),
		TYPE_C(2, "chip_c"),
		PROCESSOR_UNIT(3, "processor_unit", false);
		
		private static final ChipType LOOKUP[];
		
		static {
			LOOKUP = new ChipType[ChipType.values().length];
			for(ChipType type : ChipType.values()) {
				LOOKUP[type.meta] = type;
			}
		}
		
		public final int meta;
		private final String name;
		private final boolean has_tooltip;
		
		private ChipType(int meta, String name) {
			this(meta, name, true);
		}
		
		private ChipType(int meta, String name, boolean has_tooltip) {
			this.meta = meta;
			this.name = name;
			this.has_tooltip = has_tooltip;
		}
		
		public static ChipType getByMeta(int meta) {
			if(meta < 0 || meta >= LOOKUP.length) {
				return LOOKUP[0];
			}else {
				return LOOKUP[meta];
			}
		}
	}
	
	
}
