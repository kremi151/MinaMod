package lu.kremi151.minamod.client;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.stats.types.StatTypes;
import lu.kremi151.minamod.enums.EnumHerb;
import lu.kremi151.minamod.item.ItemHerbGuide;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHerbBook extends GuiNoInventory{

	private static ResourceLocation guiTexturesLeft = new ResourceLocation(MinaMod.MODID, "textures/gui/herb_guide_left.png");
	private static ResourceLocation guiTexturesRight = new ResourceLocation(MinaMod.MODID, "textures/gui/herb_guide_right.png");

	private static final int COLOR_GREEN = MinaUtils.convertRGBToDecimal(20, 160, 40);
	private static final int COLOR_RED = MinaUtils.convertRGBToDecimal(200, 20, 20);
	private static final int COLOR_GOLD = MinaUtils.convertRGBToDecimal(132, 100, 5);
	private static final int INTERMEDIATE_PAGES = 1;
	private static final int BASE_PAGES = 27 + INTERMEDIATE_PAGES;
	
	private final Rectangle backP, forwP, bookmAAAP, bookmBBBP, bookmCCCP, bookmIP, bookmSP;
	private static final int ARROW_WIDTH = 18, ARROW_HEIGHT = 10;
	private int page_index = 0, special_pages;
	private final EnumHand hand;
	
	public GuiHerbBook(EnumHand hand){
		this.xSize = 270;
		this.ySize = 180;
		this.hand = hand;
		this.special_pages = specialPagesCount();
		this.backP = new Rectangle(0, 0, ARROW_WIDTH, ARROW_HEIGHT);
		this.forwP = new Rectangle(0, 0, ARROW_WIDTH, ARROW_HEIGHT);
		this.bookmAAAP = new Rectangle(0, 0, 21, 21);
		this.bookmBBBP = new Rectangle(0, 0, 21, 21);
		this.bookmCCCP = new Rectangle(0, 0, 21, 21);
		this.bookmIP = new Rectangle(0, 0, 21, 21);
		this.bookmSP = new Rectangle(0, 0, 21, 21);
	}
	
	private ItemStack getStack(){
		return Minecraft.getMinecraft().player.getHeldItem(hand);
	}
	
	private int specialPagesCount(){
		int count = 0;
		for(byte i = 27 ; i < 64 ; i++){
			if(ItemHerbGuide.hasSeen(getStack(), i))count++;
		}
		return count;
	}
	
	//1 - 27 :	Normal herbs
	//28 :		Crossing
	//29+:		Special herbs
	private final int pageCount(){
		return BASE_PAGES + special_pages;
	}
	
	private String getClassification(EnumHerb herb){
		if(herb.isOriginal()){
			int atk = herb.getStatEffect(StatTypes.ATTACK);
			int def = herb.getStatEffect(StatTypes.DEFENSE);
			int spd = herb.getStatEffect(StatTypes.SPEED);
			return ((atk < 0)?"A":((atk > 0)?"C":"B")) + ((def < 0)?"A":((def > 0)?"C":"B")) + ((spd < 0)?"A":((spd > 0)?"C":"B"));
		}else{
			return "???";
		}
	}
	
	private boolean testMouseIn(Rectangle rect, int mouseX, int mouseY){
		return rect.contains(mouseX, mouseY);
		//return (mouseX >= p.getX() && mouseX < p.getX() + width && mouseY >= p.getY() && mouseY < p.getY() + height);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final int x = (width - xSize) / 2;
		final int y = (height - ySize) / 2;
		this.mc.renderEngine.bindTexture(guiTexturesLeft);
		this.drawTexturedModalRect(x, y, 0, 1, 132, ySize);
		this.mc.renderEngine.bindTexture(guiTexturesRight);
		this.drawTexturedModalRect(x + 132, y, 0, 1, 138, ySize);
		
		if(canGo(true) && testMouseIn(backP, mouseX, mouseY)){
			this.drawTexturedModalRect(backP.getX(), backP.getY(), 26, 207, ARROW_WIDTH, ARROW_HEIGHT);
		}else{
			this.drawTexturedModalRect(backP.getX(), backP.getY(), 3, 207, ARROW_WIDTH, ARROW_HEIGHT);
		}
		if(canGo(false) && testMouseIn(forwP, mouseX, mouseY)){
			this.drawTexturedModalRect(forwP.getX(), forwP.getY(), 26, 194, ARROW_WIDTH, ARROW_HEIGHT);
		}else{
			this.drawTexturedModalRect(forwP.getX(), forwP.getY(), 3, 194, ARROW_WIDTH, ARROW_HEIGHT);
		}

		if(testMouseIn(bookmAAAP, mouseX, mouseY)){
			this.drawTexturedModalRect(bookmAAAP.getX(), bookmAAAP.getY(), 139, 23, 21, 21);
		}else{
			this.drawTexturedModalRect(bookmAAAP.getX(), bookmAAAP.getY(), 139, 1, 21, 21);
		}
		if(testMouseIn(bookmBBBP, mouseX, mouseY)){
			this.drawTexturedModalRect(bookmBBBP.getX(), bookmBBBP.getY(), 139, 23, 21, 21);
		}else{
			this.drawTexturedModalRect(bookmBBBP.getX(), bookmBBBP.getY(), 139, 1, 21, 21);
		}
		if(testMouseIn(bookmCCCP, mouseX, mouseY)){
			this.drawTexturedModalRect(bookmCCCP.getX(), bookmCCCP.getY(), 139, 23, 21, 21);
		}else{
			this.drawTexturedModalRect(bookmCCCP.getX(), bookmCCCP.getY(), 139, 1, 21, 21);
		}
		if(testMouseIn(bookmIP, mouseX, mouseY)){
			this.drawTexturedModalRect(bookmIP.getX(), bookmIP.getY(), 139, 23, 21, 21);
		}else{
			this.drawTexturedModalRect(bookmIP.getX(), bookmIP.getY(), 139, 1, 21, 21);
		}
		if(special_pages > 0){
			if(testMouseIn(bookmSP, mouseX, mouseY)){
				this.drawTexturedModalRect(bookmSP.getX(), bookmSP.getY(), 139, 23, 21, 21);
			}else{
				this.drawTexturedModalRect(bookmSP.getX(), bookmSP.getY(), 139, 1, 21, 21);
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int adjMouseX = mouseX - guiLeft;
		final int adjMouseY = mouseY - guiTop;
		
		ItemStack stack = getStack();
		
		if(page_index < 0)page_index = 0;
		if(page_index < 27){
			boolean seen = ItemHerbGuide.hasSeen(stack, EnumHerb.getByHerbId((byte)page_index));
			ItemStack icon;
			String title;
			final EnumHerb herb = EnumHerb.getByHerbId((byte)page_index);
			if(seen){
				icon = new ItemStack(MinaItems.HERB, 1, page_index);
				title = icon.getDisplayName();
				
				int atk = herb.getStatEffect(StatTypes.ATTACK);
				int def = herb.getStatEffect(StatTypes.DEFENSE);
				int spd = herb.getStatEffect(StatTypes.SPEED);
				
				if(ItemHerbGuide.includeStats(stack)){
					this.fontRenderer.drawString(I18n.translateToLocalFormatted("item.mixture.attack.info", atk), 25, 65, (atk>0)?COLOR_GREEN:((atk<0)?COLOR_RED:MinaUtils.COLOR_BLACK));
					this.fontRenderer.drawString(I18n.translateToLocalFormatted("item.mixture.defense.info", def), 25, 75, (def>0)?COLOR_GREEN:((def<0)?COLOR_RED:MinaUtils.COLOR_BLACK));
					this.fontRenderer.drawString(I18n.translateToLocalFormatted("item.mixture.speed.info", spd), 25, 85, (spd>0)?COLOR_GREEN:((spd<0)?COLOR_RED:MinaUtils.COLOR_BLACK));
				}else{
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.herb_guide.stats_unk"), 25, 65, 100, MinaUtils.COLOR_BLACK);
				}
				
				if(ItemHerbGuide.includeMutability(stack)){
					this.fontRenderer.drawString(I18n.translateToLocalFormatted("gui.herb_guide.mutability", herb.getDefaultMutability() * 100.0f), 25, 105, MinaUtils.COLOR_BLACK);
				}
			}else{
				icon = new ItemStack(MinaItems.HERB, 1, EnumHerb.WHITE.getHerbId());
				title = "???";
			}
			
			if(ItemHerbGuide.includeCrossing(stack)){
				this.fontRenderer.drawString(I18n.translateToLocalFormatted("gui.herb_guide.classification", getClassification(herb)), 25, 50, MinaUtils.COLOR_BLACK);
			}
			this.itemRender.renderItemIntoGUI(icon, 24, 24);
			this.fontRenderer.drawSplitString(title, 45, 27, 88, MinaUtils.COLOR_BLACK);
		}else{
			int extra_idx = page_index - 27;
			if(extra_idx < INTERMEDIATE_PAGES){
				if(extra_idx == 0 && ItemHerbGuide.includeCrossing(stack)){
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.herb_guide.crossingl"), 25, 15, 105, MinaUtils.COLOR_BLACK);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.herb_guide.crossingr"), 148, 15, 105, MinaUtils.COLOR_BLACK);
				}
			}else{
				try{
					EnumHerb sherb = EnumHerb.getByHerbId((byte)(27 + (extra_idx - INTERMEDIATE_PAGES)));
					ItemStack icon = new ItemStack(MinaItems.HERB, 1, sherb.getHerbId());
					this.itemRender.renderItemIntoGUI(icon, 24, 24);
					this.fontRenderer.drawSplitString(icon.getDisplayName(), 45, 27, 88, COLOR_GOLD);
					this.fontRenderer.drawSplitString(I18n.translateToLocal("gui.herb_guide.undocumented"), 25, 65, 105, MinaUtils.COLOR_BLACK);
					
				}catch(IndexOutOfBoundsException e){
					this.page_index = 0;
				}
			}
		}
		this.fontRenderer.drawString(I18n.translateToLocalFormatted("gui.herb_guide.page", page_index + 1, pageCount()), 45, ySize - 28, MinaUtils.COLOR_BLACK);

		this.drawCenteredString(this.fontRenderer, "I", bookmAAAP.getX() + 10 - guiLeft, bookmAAAP.getY() + 6 - guiTop, MinaUtils.COLOR_GRAY);
		this.drawCenteredString(this.fontRenderer, "II", bookmBBBP.getX() + 10 - guiLeft, bookmBBBP.getY() + 6 - guiTop, MinaUtils.COLOR_GRAY);
		this.drawCenteredString(this.fontRenderer, "III", bookmCCCP.getX() + 10 - guiLeft, bookmCCCP.getY() + 6 - guiTop, MinaUtils.COLOR_GRAY);
		this.drawCenteredString(this.fontRenderer, "IV", bookmIP.getX() + 10 - guiLeft, bookmIP.getY() + 6 - guiTop, MinaUtils.COLOR_GRAY);
		if(special_pages > 0)this.drawCenteredString(this.fontRenderer, "V", bookmSP.getX() + 10 - guiLeft, bookmSP.getY() + 6 - guiTop, MinaUtils.COLOR_GRAY);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if(canGo(true) && testMouseIn(backP, mouseX, mouseY)){
			page_index--;
			playClickSound();
		}
		if(canGo(false) && testMouseIn(forwP, mouseX, mouseY)){
			page_index++;
			playClickSound();
		}
		if(testMouseIn(bookmAAAP, mouseX, mouseY)){
			page_index = 0;
			playClickSound();
		}
		if(testMouseIn(bookmBBBP, mouseX, mouseY)){
			page_index = 9;
			playClickSound();
		}
		if(testMouseIn(bookmCCCP, mouseX, mouseY)){
			page_index = 18;
			playClickSound();
		}
		if(testMouseIn(bookmIP, mouseX, mouseY)){
			page_index = 27;
			playClickSound();
		}
		if(special_pages > 0 && testMouseIn(bookmSP, mouseX, mouseY)){
			page_index = 27 + INTERMEDIATE_PAGES;
			playClickSound();
		}
    }
	
	@Override
	public void initGui() {
		super.initGui();
		setupPositions();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h)
    {
		super.onResize(mcIn, w, h);
		setupPositions();
    }
	
	private void setupPositions(){
		backP.setX(guiLeft + 20);
		backP.setY((guiTop + ySize) - ARROW_HEIGHT - 20);
		
		forwP.setX((guiLeft + xSize) - ARROW_WIDTH - 20);
		forwP.setY(backP.getY());
		
		bookmAAAP.setX((guiLeft + xSize) - 11);
		bookmAAAP.setY(guiTop + 18);
		
		bookmBBBP.setX(bookmAAAP.getX());
		bookmBBBP.setY(guiTop + 42);
		
		bookmCCCP.setX(bookmAAAP.getX());
		bookmCCCP.setY(guiTop + 66);
		
		bookmIP.setX(bookmAAAP.getX());
		bookmIP.setY(guiTop + 90);
		
		bookmSP.setX(bookmAAAP.getX());
		bookmSP.setY(guiTop + 114);
	}
	
	private boolean canGo(boolean back){
		if(back){
			return page_index > 0;
		}else{
			return page_index < pageCount() - 1;
		}
	}
	
}
