package lu.kremi151.minamod.client.overlay;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OverlayableString extends Overlayable{
	
	private String text;
	private int color = MinaUtils.convertRGBToDecimal(255, 255, 255);
	private FontRenderer fr;

	public OverlayableString(long id, String text){
		super(id);
		this.text = text;
	}

	@Override
	public void draw(Gui gui, int supposedX, int supposedY) {
		if(fr==null){
			this.fr = Minecraft.getMinecraft().fontRenderer;
		}
		gui.drawString(fr, text, supposedX + 2, supposedY + 2, color);
	}

	@Override
	public int getHeight() {
		return fr.FONT_HEIGHT + 4;
	}

}
