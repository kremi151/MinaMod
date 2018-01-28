package lu.kremi151.minamod.client;

import lu.kremi151.minamod.util.ReflectionLoader;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiCustomContainer extends GuiContainer{

	public GuiCustomContainer(Container inventorySlotsIn) {
		super(inventorySlotsIn);
	}
	
    protected void drawItemStack(ItemStack stack, int x, int y, String altText){
    	ReflectionLoader.GuiContainer_drawItemStack(this, stack, x, y, altText);
    }

    @SideOnly(Side.CLIENT)
    public static abstract class Inventory extends GuiCustomContainer{

		public Inventory(Container inventorySlotsIn) {
			super(inventorySlotsIn);
		}

	    @Override
	    public void drawScreen(int mouseX, int mouseY, float partialTicks)
	    {
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }
	    
    }

}
