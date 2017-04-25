package lu.kremi151.minamod.potion;

import lu.kremi151.minamod.MinaMod;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CustomPotion extends Potion{
	
	private static ResourceLocation potionTextures = new ResourceLocation(
			MinaMod.MODID, "textures/gui/potion/wowness.png");

	public CustomPotion(boolean isBadEffect, int color) {
		super(isBadEffect, color);
	}
	
	@Override
	public Potion setIconIndex(int par1, int par2) {
		super.setIconIndex(par1, par2);
		return this;
	}
	
	@Override
	public boolean hasStatusIcon()
    {
        return false;
    }
	
	@SideOnly(Side.CLIENT)
    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
    	mc.getTextureManager().bindTexture(potionTextures); 
//        mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 0, 0, 64, 64);
        mc.currentScreen.drawTexturedModalRect(x + 1, y + 0, 0, 0, 25, 25);
	}

    @SideOnly(Side.CLIENT)
    public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
    	mc.getTextureManager().bindTexture(potionTextures); 
//      mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, 0, 0, 64, 64);
    	mc.ingameGUI.drawTexturedModalRect(x + 1, y + 0, 0, 0, 25, 25);
    }

}
