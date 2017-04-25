package lu.kremi151.minamod.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GuiCustomContainer extends GuiContainer{
	
	private static Method method_drawItemStack;
	
	static{
		Method mm[] = GuiContainer.class.getDeclaredMethods();
		for(Method m : mm){
			if(m.getName().equals("drawItemStack") || m.getName().equals("func_146982_a")){
				method_drawItemStack = m;
				method_drawItemStack.setAccessible(true);
				break;
			}
		}
		if(method_drawItemStack == null){
			throw new RuntimeException("Method not found");
		}
	}

	public GuiCustomContainer(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}
	
    protected void drawItemStack(ItemStack stack, int x, int y, String altText){
    	try {
			method_drawItemStack.invoke(this, stack, x, y, altText);
		} catch (Throwable t){
			t.printStackTrace();
			throw new RuntimeException("Error working with drawItemStack");
		}
    }

}
