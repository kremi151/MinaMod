package lu.kremi151.minamod.util;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class CookingRecipes {
	
	private final Map<ItemStack, ItemStack> cookingList = Maps.<ItemStack, ItemStack>newHashMap();
	
	private static final CookingRecipes INSTANCE = new CookingRecipes();
	
	private CookingRecipes(){
		register(Blocks.SAPLING, new ItemStack(Blocks.TALLGRASS, 1, 0));
	}
	
	public void register(ItemStack raw, ItemStack cooked){
		if(!raw.isEmpty() && !cooked.isEmpty()){
			cookingList.put(raw, cooked);
		}
	}
	
	public void register(Item raw, ItemStack cooked){
		register(new ItemStack(raw, 1, 32767), cooked);
	}
	
	public void register(Block raw, ItemStack cooked){
		register(new ItemStack(raw, 1, 32767), cooked);
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }
	
	@Deprecated
	public ItemStack fetchStrict(ItemStack raw){
		for(Map.Entry<ItemStack, ItemStack> entry : cookingList.entrySet()){
			if (this.compareItemStacks(raw, (ItemStack)entry.getKey()))
            {
                return (ItemStack)entry.getValue();
            }
		}
		return ItemStack.EMPTY;
	}
	
	public ItemStack fetch(ItemStack raw){
		ItemStack res = fetchStrict(raw);
		if(!res.isEmpty()){
			return res;
		}else{
			return FurnaceRecipes.instance().getSmeltingResult(raw);
		}
	}
	
	public static CookingRecipes instance(){
		return INSTANCE;
	}
}
