package lu.kremi151.minamod.client.overlay;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;


public class OverlayableArgumentString extends Overlayable{ // NO_UCD (unused code)

	private String text;
	private int color = MinaUtils.convertRGBToDecimal(255, 255, 255);
	private FontRenderer fr;
	private String[] args;

	public OverlayableArgumentString(long id, String text){
		super(id);
		this.text = text;
	}
	
	public OverlayableArgumentString updateArguments(String... args){
		this.args = args;
		return this;
	}

	@Override
	public void draw(Gui gui, int supposedX, int supposedY) {
		if(fr==null){
			this.fr = Minecraft.getMinecraft().fontRenderer;
		}
		String ptext = new String(text);
		if(args!=null){
			for(int i = 0 ; i < args.length ; i++){
				ptext = ptext.replace("%" + (i+1), args[i]);
			}
		}
		gui.drawString(fr, ptext, supposedX + 2, supposedY + 2, color);
	}

	@Override
	public int getHeight() {
		return fr.FONT_HEIGHT + 4;
	}

}
