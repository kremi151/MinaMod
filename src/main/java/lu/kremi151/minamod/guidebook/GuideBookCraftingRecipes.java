package lu.kremi151.minamod.guidebook;

import java.util.ArrayList;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.RecipeHandler;
import com.creysys.guideBook.plugin.vanilla.recipe.DrawableRecipeCrafting;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemKey;
import lu.kremi151.minamod.item.ItemKey.ExtendedKeyCapability;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuideBookCraftingRecipes extends RecipeHandler{
	
	private final ArrayList<DrawableRecipe> recipes;
	
	public GuideBookCraftingRecipes() {
		recipes = new ArrayList<>();
		
		recipes.add(new DrawableRecipeCrafting(
				new ItemStack(MinaItems.MIXTURE),
				NonNullList.from(Ingredient.EMPTY, 
						Ingredient.fromStacks(new ItemStack(MinaItems.MILK_BOTTLE)), 
						Ingredient.fromItem(MinaItems.POWDER)),
				3));
		final ItemStack keyStack = ItemKey.rawKey();
		((ExtendedKeyCapability)keyStack.getCapability(IKey.CAPABILITY_KEY, null)).setState(ItemKey.State.NORMAL);
		recipes.add(new DrawableRecipeCrafting(
				keyStack,
				NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(ItemKey.rawKey())),
				3));
		recipes.add(new DrawableRecipeCrafting(
				new ItemStack(MinaItems.DRILL),
				NonNullList.from(Ingredient.EMPTY,
						Ingredient.fromStacks(new ItemStack(MinaItems.DRILL, 1, 40)),
						Ingredient.fromItem(Items.IRON_NUGGET)),
				3));
	}

	@Override
	public String getName() {
		return "guidebook.minamod.dynamic_crafting";
	}

	@Override
	public ArrayList<DrawableRecipe> getRecipes() {
		return recipes;
	}

	@Override
	public Object getTabIcon() {
		return new ItemStack(MinaItems.HERB, 1, (int)EnumHerb.RED.getHerbId());
	}

	@Override
	public int recipesPerPage() {
		return 2;
	}

}
