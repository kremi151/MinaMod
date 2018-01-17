package lu.kremi151.minamod.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.stats.CapabilityStatsImpl;
import lu.kremi151.minamod.capabilities.stats.ICapabilityStats;
import lu.kremi151.minamod.capabilities.stats.types.StatType;
import lu.kremi151.minamod.capabilities.stats.util.Stat;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class GuiPlayerStats extends GuiNoInventory{

	public static final ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/player_stats/default.png");
	
	private int statsCenterX = 88, statsCenterY = 79;
	
	private final Point statPoint[], statVPoint[];
	
	private final static double START_ANGLE = 30.0;
	private final double BETWEEN_ANGLE;
	private final int MAX_POINTS_TOTAL;
	
	private final CapabilityStatsImpl playerStats;
	private final StatType statTypes[];
	
	public GuiPlayerStats(){
		playerStats = (CapabilityStatsImpl) Minecraft.getMinecraft().player.getCapability(ICapabilityStats.CAPABILITY, null);
		Collection<StatType> statTypes = playerStats.listSupportedStatTypes();
		this.statTypes = statTypes.toArray(new StatType[statTypes.size()]);
		statPoint = new Point[this.statTypes.length];
		statVPoint = new Point[this.statTypes.length];
		BETWEEN_ANGLE = 360.0 / (double)this.statTypes.length;
		
		int maxPoints = 0;
		for(StatType type : statTypes)maxPoints += playerStats.getStat(type).getActual().getMaxValue();
		MAX_POINTS_TOTAL = maxPoints;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		int adjMouseX = mouseX - guiLeft;
		int adjMouseY = mouseY - guiTop;
		
		if(adjMouseX >= 7 && adjMouseX <= 168){
			if(adjMouseY >= 182 && adjMouseY <= 187){
				List<String> lines = Arrays.asList(
						TextFormatting.BLUE + I18n.translateToLocal("gui.player_stats.effort_bar.title"), 
						I18n.translateToLocal("gui.player_stats.effort_bar.desc")
						);
				this.drawHoveringText(lines, mouseX, mouseY);
			}else if(adjMouseY >= 190 && adjMouseY <= 195){
				List<String> lines = Arrays.asList(
						TextFormatting.DARK_GREEN + I18n.translateToLocal("gui.player_stats.points_left.title"), 
						I18n.translateToLocal("gui.player_stats.points_left.desc")
						);
				this.drawHoveringText(lines, mouseX, mouseY);
			}
		}
		for(int i = 0 ; i < statTypes.length ; i++){
			StatType type = statTypes[i];
			Stat stat = playerStats.getStat(type);
			Point sp = statVPoint[i];
			if(adjMouseX >= sp.x() - 13 && adjMouseX <= sp.x() + 13 && adjMouseY >= sp.y() - 6 && adjMouseY <= sp.y() + 7){
				String desc;
				int training = stat.getTraining().get();
				int actual = stat.getActual().get();
				if(training > 0){
					desc = "gui.player_stats.value.might_rise";
				}else if(training < 0){
					desc = "gui.player_stats.value.might_fall";
				}else if(actual == stat.getActual().getMaxValue()){
					desc = "gui.player_stats.value.max";
				}else if(actual == stat.getActual().getMinValue()){
					desc = "gui.player_stats.value.min";
				}else{
					desc = "gui.player_stats.value.constant";
				}
				List<String> lines = Arrays.asList(
						TextFormatting.GOLD + I18n.translateToLocal(type.getUnlocalizedName()), 
						I18n.translateToLocal(desc)
						);
				this.drawHoveringText(lines, mouseX, mouseY);
				break;
			}
		}
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		float expPerc = MathHelper.clamp((float)playerStats.getEffort().get() / (float)playerStats.getEffort().getMaxValue(), 0.0f, 1.0f);
		this.drawTexturedModalRect(15, 183, 0, 220, (int)(expPerc * 153), 4);
		
		
		expPerc = MathHelper.clamp((float)playerStats.pointsLeft() / (float)MAX_POINTS_TOTAL, 0.0f, 1.0f);
		this.drawTexturedModalRect(15, 191, 0, 224, (int)(expPerc * 153), 4);

		fontRenderer.drawString(I18n.translateToLocal("gui.player_stats.title"), 8, 10, 4210752);
		
		int actuals[] = new int[statTypes.length], trainings[] = new int[statTypes.length], ctrainings[] = new int[statTypes.length],
				mins[] = new int[statTypes.length], maxs[] = new int[statTypes.length];
		
		for(int i = 0 ; i < statTypes.length ; i++){
			Stat stat = playerStats.getStat(statTypes[i]);
			actuals[i] = stat.getActual().get();
			trainings[i] = stat.getTraining().get();
			ctrainings[i] = actuals[i] + trainings[i];
			mins[i] = stat.getActual().getMinValue();
			maxs[i] = stat.getActual().getMaxValue();
		}

		drawStatsPoints();
		drawStats(ctrainings, 0f, 1f, 1f, false);
		drawStats(actuals, 1f, 1f, 0f, true);
		
		double angle = START_ANGLE;
		for(int i = 0 ; i < statTypes.length ; i++){
			StatType type = statTypes[i];
			fontRenderer.drawStringWithShadow(I18n.translateToLocalFormatted(getMultiplicatorString(type), round((float)actuals[i] / 127.5f)), 10, 133 + (i * 12), type.getColor());

			statPoint[i] = calculateAnglePoint(statPoint[i], statsCenterX, statsCenterY, 55, angle);
			statVPoint[i] = calculateAnglePoint(statVPoint[i], statsCenterX, statsCenterY, 68, 55, angle);
			angle += BETWEEN_ANGLE;
			drawStatValue(actuals[i], trainings[i], mins[i], maxs[i], statVPoint[i].x(), statVPoint[i].y());
		}
	}
	
	private String getMultiplicatorString(StatType type){
		return "gui.player_stats." + type.getId() + "_multiplicator";
	}
	
	private float round(float f){
		return Math.round(f * 1000) / 1000f;
	}
	
	private void drawStatsPoints(){
		int dist = 40;
		float grayness = 0.12f;

		double angle = START_ANGLE;
		for(int i = 0 ; i < statTypes.length ; i++){
			statPoint[i] = calculateAnglePoint(statPoint[i], statsCenterX, statsCenterY, dist, angle);
			angle += BETWEEN_ANGLE;
		}
		
		/*attackX = statsCenterX + (int)(dist * Math.cos(0));
		attackY = statsCenterY + (int)(dist * Math.sin(0));

		defenseX = statsCenterX + (int)(dist * Math.cos(MinaUtils.CONST_M * 120.0));
		defenseY = statsCenterY + (int)(dist * Math.sin(MinaUtils.CONST_M * 120.0));

		speedX = statsCenterX + (int)(dist * Math.cos(MinaUtils.CONST_M * 240.0));
		speedY = statsCenterY + (int)(dist * Math.sin(MinaUtils.CONST_M * 240.0));*/
		
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);

	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glColor4f(grayness, grayness, grayness, 1f);   // change this for your colour
	    GL11.glLineWidth(1.0F);
	    GL11.glDepthMask(false);
		
		for(int i = 0 ; i < statTypes.length ; i++){
			GlStateManager.glBegin(GL11.GL_LINES);
			GlStateManager.glVertex3f(statPoint[i].x(), statPoint[i].y(), 30.f);
			GlStateManager.glVertex3f(statsCenterX, statsCenterY, 30.f);
			GlStateManager.glEnd();
		}

	    GL11.glDepthMask(true);
	    GL11.glPopAttrib();
		
		drawPoint(statsCenterX, statsCenterY, grayness, grayness, grayness);
		
		for(int i = 0 ; i < statTypes.length ; i++){
			StatType type = statTypes[i];
			drawPoint(statPoint[i].x(), statPoint[i].y(), type.getColorF()[0], type.getColorF()[1], type.getColorF()[2]);
		}
	}
	
	private void drawStats(int stats[], float red, float green, float blue, boolean fillTriangle){
		int maxDist = 40, dist[] = new int[stats.length];
		
		double angle = START_ANGLE;
		for(int i = 0 ; i < statTypes.length ; i++){
			dist[i] = (int) (maxDist * (stats[i] / 255.0));
			statPoint[i] = calculateAnglePoint(statPoint[i], statsCenterX, statsCenterY, dist[i], angle);
			angle += BETWEEN_ANGLE;
		}
		
		drawTriforce(statPoint, red, green, blue, fillTriangle);
	}
	
	private void drawTriforce(Point vertices[], float red, float green, float blue, boolean fill){
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);

	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    //GL11.glColor4f(red, green, blue, 1f);
	    GL11.glLineWidth(2.0F);
	    GL11.glDepthMask(false);
	    
	    if(fill){
		    GL11.glColor4f(red, green, blue, 0.5f);
	    	GlStateManager.glBegin(GL11.GL_TRIANGLES);
		    for(int i = 0 ; i < vertices.length ; i++){
				GlStateManager.glVertex3f(vertices[i].x(), vertices[i].y(), 30.f);
		    }
			GlStateManager.glEnd();
	    }
	    GL11.glColor4f(red, green, blue, 1f);
		
		for(int i = 0 ; i < vertices.length ; i++){
			if(i == vertices.length - 1){
				GlStateManager.glBegin(GL11.GL_LINES);
				GlStateManager.glVertex3f(vertices[i].x(), vertices[i].y(), 30.f);
				GlStateManager.glVertex3f(vertices[0].x(), vertices[0].y(), 30.f);
				GlStateManager.glEnd();
			}else{
				GlStateManager.glBegin(GL11.GL_LINES);
				GlStateManager.glVertex3f(vertices[i].x(), vertices[i].y(), 30.f);
				GlStateManager.glVertex3f(vertices[i+1].x(), vertices[i+1].y(), 30.f);
				GlStateManager.glEnd();
			}
		}

	    GL11.glDepthMask(true);
	    GL11.glPopAttrib();
	}
	
	private void drawPoint(int x, int y, float red, float green, float blue){
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);

	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glColor4f(red, green, blue, 1f);   // change this for your colour
	    GL11.glPointSize(8f);
	    GL11.glDepthMask(false);
		
		GlStateManager.glBegin(GL11.GL_POINTS);
		GlStateManager.glVertex3f(x, y, 30.f);
		GlStateManager.glEnd();

	    GL11.glDepthMask(true);
	    GL11.glPopAttrib();
	}
	
	private void drawStatValue(int actual, int training, int min, int max, int cx, int cy){
		String caption = "" + actual;
		int width = fontRenderer.getStringWidth(caption);
		int x = cx - (width / 2);
		int y = cy - (fontRenderer.FONT_HEIGHT / 2);
		int color;
		if(training > 0){
			color = MinaUtils.convertRGBToDecimal(128, 255, 128);
		}else if(training < 0){
			color = MinaUtils.convertRGBToDecimal(255, 128, 128);
		}else if(actual == max){
			color = MinaUtils.convertRGBToDecimal(255, 255, 0);
		}else if(actual == min){
			color = MinaUtils.convertRGBToDecimal(64, 64, 64);
		}else{
			color = MinaUtils.COLOR_WHITE;
		}
		this.mc.renderEngine.bindTexture(guiTextures);
		this.drawTexturedModalRect(cx - 13, cy - 6, 0, 207, 26, 13);
		fontRenderer.drawStringWithShadow(caption, x, y, color);//TODO
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		// draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(guiTextures);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
	
	@Override
	public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		
	}
	
	private Point calculateAnglePoint(Point recycle, int cx, int cy, int d, double degrees){
		return calculateAnglePoint(recycle, cx, cy, d, d, degrees);
	}
	
	private Point calculateAnglePoint(Point recycle, int cx, int cy, int w, int h, double degrees){
		if(recycle == null)recycle = new Point();
		recycle.setX(cx + (int)(w * Math.cos(MinaUtils.CONST_M * degrees)));
		recycle.setY(cy + (int)(h * Math.sin(MinaUtils.CONST_M * degrees)));
		return recycle;
	}

}
