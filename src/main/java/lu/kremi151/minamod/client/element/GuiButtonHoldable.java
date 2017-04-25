package lu.kremi151.minamod.client.element;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonHoldable extends GuiButton{
	
	private Listener l;

	public GuiButtonHoldable(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);
	}

	public GuiButtonHoldable(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public GuiButtonHoldable setListener(Listener listener){
		this.l = listener;
		return this;
	}
	
	/**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
	@Override
    public void mouseReleased(int mouseX, int mouseY)
    {
		if(l != null){
			l.onRelease(id);
		}
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
	@Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean res = super.mousePressed(mc, mouseX, mouseY);
        if(res && l != null){
        	l.onHold(id);
        }
        return res;
    }
	
	public static interface Listener{
		void onHold(int id);
		void onRelease(int id);
	}

}
