package lu.kremi151.minamod.client.util;

import lu.kremi151.minamod.client.GuiMinaOverlay;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ScreenLayer {

	private GuiMinaOverlay parent;
	
	ScreenLayer(GuiMinaOverlay parent){
		this.parent = parent;
	}
	
	protected GuiMinaOverlay getParent(){
		return parent;
	}
	
	public abstract void draw(int width, int height);
}
