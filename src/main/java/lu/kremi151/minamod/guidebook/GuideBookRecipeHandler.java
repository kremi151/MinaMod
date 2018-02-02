package lu.kremi151.minamod.guidebook;

import java.util.ArrayList;

import com.creysys.guideBook.api.DrawableRecipe;
import com.creysys.guideBook.api.RecipeHandler;

import lu.kremi151.minamod.MinaMod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuideBookRecipeHandler extends RecipeHandler{

	@Override
	public String getName() {
		return MinaMod.MODID;
	}

	@Override
	public ArrayList<DrawableRecipe> getRecipes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTabIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int recipesPerPage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
