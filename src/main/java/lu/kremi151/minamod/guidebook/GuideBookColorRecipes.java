package lu.kremi151.minamod.guidebook;

import java.util.ArrayList;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.RecipeHandler;
import com.creysys.guideBook.plugin.vanilla.recipe.DrawableRecipeCrafting;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.item.ItemColoredWrittenBook;
import lu.kremi151.minamod.item.ItemKey;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class GuideBookColorRecipes extends RecipeHandler{
	
	private final ArrayList<DrawableRecipe> recipes;
	private ItemStack icon = null;
	
	public GuideBookColorRecipes() {
		recipes = new ArrayList<>();
		
		for(EnumDyeColor color : EnumDyeColor.values()) {
			final Ingredient ingDye = Ingredient.fromStacks(new ItemStack(Items.DYE, 1, color.getDyeDamage()));
			recipes.add(new DrawableRecipeCrafting(
					ItemColoredWrittenBook.setBookColor(new ItemStack(MinaItems.COLORED_BOOK, 1), color.getColorValue()),
					NonNullList.from(Ingredient.EMPTY,
							Ingredient.fromStacks(new ItemStack(Items.WRITTEN_BOOK)),
							ingDye),
					3));
			ItemStack keyStack = new ItemStack(MinaItems.KEY);
			IKey keyCap = keyStack.getCapability(IKey.CAPABILITY_KEY, null);
			if(keyCap != null && keyCap instanceof ItemKey.ExtendedKeyCapability) {
				((ItemKey.ExtendedKeyCapability)keyCap).setTint(color.getColorValue());
				recipes.add(new DrawableRecipeCrafting(
						keyStack,
						NonNullList.from(Ingredient.EMPTY,
								Ingredient.fromStacks(new ItemStack(MinaItems.KEY)),
								ingDye),
						3));
			}
		}
	}

	@Override
	public String getName() {
		return "guidebook.minamod.color_crafting";
	}

	@Override
	public ArrayList<DrawableRecipe> getRecipes() {
		return recipes;
	}

	@Override
	public Object getTabIcon() {
		if(icon == null) {
			icon = ItemColoredWrittenBook.setBookColor(new ItemStack(MinaItems.COLORED_BOOK, 1), EnumDyeColor.RED.getColorValue());
		}
		return icon;
	}

	@Override
	public int recipesPerPage() {
		return 2;
	}

}
