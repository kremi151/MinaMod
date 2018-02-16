package lu.kremi151.minamod.guidebook;

import com.creysys.guideBook.api.RegisterRecipeHandlersEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuideBookPlugin {
	
	private GuideBookPlugin() {}

	public static void register() {
		MinecraftForge.EVENT_BUS.register(new GuideBookPlugin());
	}
	
	@SubscribeEvent
	public void onRegisterRecipeHandlers(RegisterRecipeHandlersEvent event) {
		event.registerHandler(new GuideBookCraftingRecipes());
		event.registerHandler(new GuideBookColorRecipes());
		event.registerHandler(new GuideBookCombinerRecipes());
	}
	
}
