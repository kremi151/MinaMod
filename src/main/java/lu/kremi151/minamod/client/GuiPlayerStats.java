package lu.kremi151.minamod.client;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.capabilities.CapabilityPlayerStats;
import lu.kremi151.minamod.capabilities.IPlayerStats;
import lu.kremi151.minamod.enums.EnumPlayerStat;
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

	public static ResourceLocation guiTextures = new ResourceLocation(MinaMod.MODID, "textures/gui/player_stats.png");
	
	private int statsCenterX = 88, statsCenterY = 79;
	
	private final Point statPoint[] = new Point[EnumPlayerStat.values().length], statVPoint[] = new Point[EnumPlayerStat.values().length];
	
	private final static double START_ANGLE = 30.0, BETWEEN_ANGLE = 360.0 / (double)EnumPlayerStat.values().length;
	
	private final IPlayerStats playerStats;
	
	public GuiPlayerStats(){
		playerStats = Minecraft.getMinecraft().player.getCapability(CapabilityPlayerStats.CAPABILITY, null);
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
		for(EnumPlayerStat ps : EnumPlayerStat.values()){
			Point sp = statVPoint[ps.ordinal()];
			if(adjMouseX >= sp.x() - 13 && adjMouseX <= sp.x() + 13 && adjMouseY >= sp.y() - 6 && adjMouseY <= sp.y() + 7){
				String desc;
				int training = playerStats.getTrainingStats(ps);
				int actual = playerStats.getStats(ps);
				if(training > 0){
					desc = "gui.player_stats.value.might_rise";
				}else if(training < 0){
					desc = "gui.player_stats.value.might_fall";
				}else if(actual == CapabilityPlayerStats.NORMAL_STAT_MAXIMUM){
					desc = "gui.player_stats.value.max";
				}else if(actual == CapabilityPlayerStats.NORMAL_STAT_MINIMUM){
					desc = "gui.player_stats.value.min";
				}else{
					desc = "gui.player_stats.value.constant";
				}
				List<String> lines = Arrays.asList(
						TextFormatting.GOLD + I18n.translateToLocal(ps.getUnlocalizedName()), 
						I18n.translateToLocal(desc)
						);
				this.drawHoveringText(lines, mouseX, mouseY);
				break;
			}
		}
    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		float expPerc = MathHelper.clamp((float)playerStats.getEffortBar() / (float)playerStats.getMaxEffortBar(), 0.0f, 1.0f);
		this.drawTexturedModalRect(15, 183, 0, 220, (int)(expPerc * 153), 4);
		
		expPerc = MathHelper.clamp((float)playerStats.getPointsLeft() / (float)playerStats.getMaxDistributablePoints(), 0.0f, 1.0f);
		this.drawTexturedModalRect(15, 191, 0, 224, (int)(expPerc * 153), 4);

		fontRenderer.drawString(I18n.translateToLocal("gui.player_stats.title"), 8, 10, 4210752);
		
		int actuals[] = new int[EnumPlayerStat.values().length], trainings[] = new int[EnumPlayerStat.values().length], ctrainings[] = new int[EnumPlayerStat.values().length];
		
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			actuals[stat.ordinal()] = playerStats.getStats(stat);
			trainings[stat.ordinal()] = playerStats.getTrainingStats(stat);
			ctrainings[stat.ordinal()] = actuals[stat.ordinal()] + trainings[stat.ordinal()];
		}

		drawStatsPoints();
		drawStats(ctrainings, 0f, 1f, 1f, false);
		drawStats(actuals, 1f, 1f, 0f, true);
		
		double angle = START_ANGLE;
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			fontRenderer.drawStringWithShadow(I18n.translateToLocalFormatted(stat.getMultiplicatorString(), round((float)actuals[stat.ordinal()] / 127.5f)), 10, 133 + (stat.ordinal() * 12), stat.getDisplayColor());

			statPoint[stat.ordinal()] = calculateAnglePoint(statPoint[stat.ordinal()], statsCenterX, statsCenterY, 55, angle);
			statVPoint[stat.ordinal()] = calculateAnglePoint(statVPoint[stat.ordinal()], statsCenterX, statsCenterY, 68, 55, angle);
			angle += BETWEEN_ANGLE;
			drawStatValue(actuals[stat.ordinal()], trainings[stat.ordinal()], statVPoint[stat.ordinal()].x(), statVPoint[stat.ordinal()].y());
		}
	}
	
	private float round(float f){
		return Math.round(f * 1000) / 1000f;
	}
	
	private void drawStatsPoints(){
		int dist = 40;
		float grayness = 0.12f;

		double angle = START_ANGLE;
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			statPoint[stat.ordinal()] = calculateAnglePoint(statPoint[stat.ordinal()], statsCenterX, statsCenterY, dist, angle);
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
		
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			GlStateManager.glBegin(GL11.GL_LINES);
			GlStateManager.glVertex3f(statPoint[stat.ordinal()].x(), statPoint[stat.ordinal()].y(), 30.f);
			GlStateManager.glVertex3f(statsCenterX, statsCenterY, 30.f);
			GlStateManager.glEnd();
		}

	    GL11.glDepthMask(true);
	    GL11.glPopAttrib();
		
		drawPoint(statsCenterX, statsCenterY, grayness, grayness, grayness);
		
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			drawPoint(statPoint[stat.ordinal()].x(), statPoint[stat.ordinal()].y(), stat.getDisplayColorF()[0], stat.getDisplayColorF()[1], stat.getDisplayColorF()[2]);
		}
	}
	
	private void drawStats(int stats[], float red, float green, float blue, boolean fillTriangle){
		int maxDist = 40, dist[] = new int[stats.length];
		
		double angle = START_ANGLE;
		for(EnumPlayerStat stat : EnumPlayerStat.values()){
			dist[stat.ordinal()] = (int) (maxDist * (stats[stat.ordinal()] / 255.0));
			statPoint[stat.ordinal()] = calculateAnglePoint(statPoint[stat.ordinal()], statsCenterX, statsCenterY, dist[stat.ordinal()], angle);
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
	
	private void drawStatValue(int actual, int training, int cx, int cy){
		String caption = "" + actual;
		int width = fontRenderer.getStringWidth(caption);
		int x = cx - (width / 2);
		int y = cy - (fontRenderer.FONT_HEIGHT / 2);
		int color;
		if(training > 0){
			color = MinaUtils.convertRGBToDecimal(128, 255, 128);
		}else if(training < 0){
			color = MinaUtils.convertRGBToDecimal(255, 128, 128);
		}else if(actual == CapabilityPlayerStats.NORMAL_STAT_MAXIMUM){
			color = MinaUtils.convertRGBToDecimal(255, 255, 0);
		}else if(actual == CapabilityPlayerStats.NORMAL_STAT_MINIMUM){
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
