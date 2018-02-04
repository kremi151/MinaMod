package lu.kremi151.minamod.guidebook;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.IGuiAccessor;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.recipe.combiner.ICombinerRecipe;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DrawableCombinerRecipe extends DrawableRecipe {

	public static final ResourceLocation combinerRecipeTexture = new ResourceLocation(MinaMod.MODID, "textures/gui/combiner/guidebook.png");

    public static DrawableCombinerRecipe parse(ICombinerRecipe recipe) {
        if(!recipe.isDynamic()) {
        	return new DrawableCombinerRecipe(recipe.getOutput(), recipe.getIngredients());
        }
        return null;
    }

    public final ItemStack output;
    public final NonNullList<Ingredient> input;

    public DrawableCombinerRecipe(ItemStack output, NonNullList<Ingredient> input) {
    	if(input.size() > 3) {
    		throw new IllegalArgumentException("Combiner recipe cannot have more than 3 ingredients");
    	}
        this.output = output.copy();
        this.input = input;
    }

    @Override
    public NonNullList<Ingredient> getInput() {
        return input;
    }

    @Override
    public ItemStack getOutput() {
        return output;
    }

    @Override
    public void draw(IGuiAccessor gui, int pageRecipeIndex) {
        if(pageRecipeIndex == 0) drawRecipe(gui, gui.getLeft() + 38,  gui.getTop() + 14);
        else if(pageRecipeIndex == 1) drawRecipe(gui, gui.getLeft() + 38,  gui.getTop() + 94);
    }

    @Override
    public void drawForeground(IGuiAccessor gui, int pageRecipeIndex, int mouseX, int mouseY) {
        if(pageRecipeIndex == 0) drawRecipeTooltip(gui, gui.getLeft() + 38,  gui.getTop() + 14, mouseX, mouseY);
        else if(pageRecipeIndex == 1) drawRecipeTooltip(gui, gui.getLeft() + 38,  gui.getTop() + 94, mouseX, mouseY);
    }

    @Override
    public void mouseClick(IGuiAccessor gui, int pageRecipeIndex, int mouseX, int mouseY, int mouseButton) {
        if(pageRecipeIndex == 0) clickRecipe(gui, gui.getLeft() + 38,  gui.getTop() + 14, mouseX, mouseY, mouseButton);
        else if(pageRecipeIndex == 1) clickRecipe(gui, gui.getLeft() + 38,  gui.getTop() + 94, mouseX, mouseY, mouseButton);
    }

    private void drawRecipe(IGuiAccessor gui, int left, int top) {
        gui.getMc().getTextureManager().bindTexture(combinerRecipeTexture);
        RenderHelper.disableStandardItemLighting();
        Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 0, 112, 54, 126, 54);

        drawItemStack(gui, output, left + 91, top + 19, true);
        for(int i = 0; i < input.size(); i++) {
        	Ingredient ingredient = input.get(i);
            if(ingredient != null) {
            	drawIngredient(gui, ingredient, left + 1, top + i * 18 + 1, false);
            }
        }
    }

    private void drawRecipeTooltip(IGuiAccessor gui, int left, int top, int mouseX, int mouseY) {
    	drawItemStackTooltip(gui, output, left + 91, top + 19, mouseX, mouseY);
        for(int i = 0; i < input.size(); i++) {
        	Ingredient ingredient = input.get(i);
            if(ingredient != null) {
            	int x = left;
                int y = top + i * 18 + 1;
                drawIngredientTooltip(gui, ingredient, x, y, mouseX, mouseY);
            }
        }
    }

    private void clickRecipe(IGuiAccessor gui, int left, int top, int mouseX, int mouseY, int mouseButton) {
        clickItemStack(gui, output,left + 91, top + 19, mouseX, mouseY, mouseButton);
        for(int i = 0; i < input.size(); i++) {
        	Ingredient ingredient = input.get(i);
            if(ingredient != null) {
            	int x = left + 1;
                int y = top + i * 18 + 1;
                clickIngredient(gui,ingredient, x, y, mouseX, mouseY, mouseButton);
            }
        }
    }

}
