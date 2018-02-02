package lu.kremi151.minamod.guidebook;

import java.util.ArrayList;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.RecipeHandler;
import com.creysys.guideBook.api.RecipeManager;
import com.creysys.guideBook.plugin.vanilla.recipe.DrawableRecipeCrafting;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.IKey;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemKey;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuideBookRecipeHandler extends RecipeHandler{
	
	private final ArrayList<DrawableRecipe> recipes;
	
	public GuideBookRecipeHandler() {
		recipes = new ArrayList<>(8);
		
		recipes.add(new DrawableRecipeCrafting(
				new ItemStack(MinaItems.MIXTURE),
				NonNullList.from(Ingredient.EMPTY, 
						Ingredient.fromStacks(new ItemStack(MinaItems.MILK_BOTTLE)), 
						Ingredient.fromItem(MinaItems.POWDER)),
				3));
		for(EnumDyeColor color : EnumDyeColor.values()) {
			final Ingredient ingDye = Ingredient.fromStacks(new ItemStack(Items.DYE, 1, color.getDyeDamage()));
			recipes.add(new DrawableRecipeCrafting(
					MinaItems.COLORED_BOOK.setBookColor(new ItemStack(MinaItems.COLORED_BOOK, 1), color.getColorValue()),
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
		return MinaMod.MODNAME;
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
	
	public static void register() {
		RecipeManager.registerHandler(new GuideBookRecipeHandler());
	}

}
