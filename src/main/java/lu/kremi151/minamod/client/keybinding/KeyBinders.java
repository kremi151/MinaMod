package lu.kremi151.minamod.client.keybinding;

import org.lwjgl.input.Keyboard;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBinders {

	public static final KeyBinding JETPACK;
	
	static{
		JETPACK = new KeyBinding("gui.key.jetpack.desc", Keyboard.KEY_J, "gui.key.category.jetpack");
	}
	
	public static void init(){
		ClientRegistry.registerKeyBinding(JETPACK);
	}
}
