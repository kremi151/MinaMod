package lu.kremi151.minamod.client;

import java.util.ArrayList;
import java.util.List;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.MinaModConfiguration;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMinaModConfiguration extends GuiConfig {

	public GuiMinaModConfiguration(GuiScreen parent) 
    {
        super(parent,
                regroupElements(),
                MinaMod.MODID, 
                false, 
                false, 
                I18n.translateToLocalFormatted("gui.minaconfig.title", MinaMod.MODNAME));
        titleLine2 = MinaMod.getMinaConfig().getConfiguration().getConfigFile().getAbsolutePath();
    }
    
    /*@Override
    public void initGui()
    {
        // You can add buttons and initialize fields here
        super.initGui();
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // You can do things like create animations, draw additional elements, etc. here
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        // You can process any additional buttons you may have added here
        super.actionPerformed(button);
    }*/
    
    private static List<IConfigElement> regroupElements(){
    	return MinaUtils.mergeLists(
    			new ArrayList<IConfigElement>(),
    			new ConfigElement(
    					MinaMod.getMinaConfig().getConfiguration().getCategory(Configuration.CATEGORY_GENERAL))
                	.getChildElements(),
                new ConfigElement(
                        MinaMod.getMinaConfig().getConfiguration().getCategory(Configuration.CATEGORY_CLIENT))
                    .getChildElements(),
                Minecraft.getMinecraft().isIntegratedServerRunning()?getSinglePlayerSettings():null
    		);
    }
    
    private static List<IConfigElement> getSinglePlayerSettings(){
    	return new ConfigElement(MinaMod.getMinaConfig().getConfiguration().getCategory(MinaModConfiguration.CATEGORY_FEATURES)).getChildElements();
    }
	
}
