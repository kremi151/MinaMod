package lu.kremi151.minamod.client;

import org.lwjgl.opengl.GL11;

import lu.kremi151.minamod.util.Point;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiUtils {

	static boolean isHovering(int mouseX, int mouseY, GuiButton element) {
		return isHovering(mouseX, mouseY, element.x, element.y, element.x + element.width, element.y + element.height);
	}
	
	static boolean isHovering(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2;
	}
	
	static void drawLines(float r, float g, float b, Point... points) {
		drawLines(r, g, b, 1f, points);
	}
	
	static void drawLines(float r, float g, float b, float a, Point... points) {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
	    GL11.glDisable(GL11.GL_CULL_FACE);
	    GL11.glDisable(GL11.GL_LIGHTING);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);

	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    //GL11.glColor4f(red, green, blue, 1f);
	    GL11.glLineWidth(4.0F);
	    GL11.glDepthMask(false);
	    
	    GL11.glColor4f(r, g, b, 1f);
	    
		for(int i = 0 ; i < points.length - 1 ; i++) {
			Point p1 = points[i];
			Point p2 = points[i+1];
			GlStateManager.glBegin(GL11.GL_LINES);
			GlStateManager.glVertex3f(p1.x(), p1.y(), 400.f);
			GlStateManager.glVertex3f(p2.x(), p2.y(), 400.f);
			GlStateManager.glEnd();
		}

	    GL11.glDepthMask(true);
	    GL11.glPopAttrib();
	}
}
