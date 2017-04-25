package lu.kremi151.minamod.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayableTexture extends Overlayable{
	
	private final ResourceLocation texture;
	private final int u, v, width, height;

	public OverlayableTexture(long id, ResourceLocation texture, int u, int v, int width, int height) {
		super(id);
		this.texture = texture;
		this.u = u;
		this.v = v;
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Gui gui, int supposedX, int supposedY) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		
		gui.drawTexturedModalRect(supposedX, supposedY, u, v, width, height);
	}

	@Override
	public int getHeight() {
		return height;
	}

}
