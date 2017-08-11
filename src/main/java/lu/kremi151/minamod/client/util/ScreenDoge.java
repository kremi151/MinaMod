package lu.kremi151.minamod.client.util;

import java.util.Random;

import lu.kremi151.minamod.MinaPotions;
import lu.kremi151.minamod.client.GuiMinaOverlay;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ScreenDoge extends ScreenLayer{
	
	private static final String adjectives[];
	private static final String words[];

	private static FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	
	static{
		adjectives = new String[]{
			"much",
			"many",
			"such",
			"so",
			"very"
		};
		words = new String[]{
			"amaze",
			"trip",
			"wtf",
			"cool",
			"wow",
			"effects",
			"blocks",
			"crafting",
			"pixels",
			"fun",
			"colors",
			"text",
			"random",
			"yolo",
			"swag",
			"splash",
			"mines",
			"ores",
			"creepers",
			"zombies",
			"enjoy",
			"awesomness",
			"gg",
			"eZ"
		};
		
	}
	
	private final Random rng;
	
	private final Sentence text[] = new Sentence[400];
	private int cooldown = 5;
	private int index = 0;
	private boolean infinite;

	public ScreenDoge(GuiMinaOverlay parent, boolean infinite) {
		super(parent);
		rng = new Random(System.currentTimeMillis());
		this.infinite = infinite;
	}

	@Override
	public void draw(int width, int height) {
		/*if(fontRenderer == null){
			fontRenderer = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
			fontRenderer.FONT_HEIGHT = 24;
		}*/
		if(!infinite && !Minecraft.getMinecraft().player.isPotionActive(MinaPotions.DOGE)){
			getParent().resetLayers(ScreenDoge.class);
			return;
		}
		
		if(--cooldown <= 0){
			text[index] = generate(text[index], rng, width, height);
			
			if(++index >= text.length){
				index = 0;
			}
			cooldown = 5;
		}
		
		for(Sentence s : text){
			if(s != null){
				getParent().drawString(fontRenderer, s.sentence, s.x, s.y, s.color);
			}
		}
	}
	
	private static Sentence generate(Sentence in, Random rng, int width, int height){
		Sentence s = null;
		if(in == null){
			s = new Sentence();
		}else{
			s = in;
		}
		if(rng.nextInt(10) == 0){
			s.sentence = "wow";
			s.color = MinaUtils.convertRGBToDecimal(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
		}else{
			StringBuffer str = new StringBuffer();
			str.append(adjectives[rng.nextInt(adjectives.length)]);
			str.append(" ");
			str.append(words[rng.nextInt(words.length)]);
			s.sentence = str.toString();
			s.color = MinaUtils.convertRGBToDecimal(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
		}
		s.x = rng.nextInt(width - fontRenderer.getStringWidth(s.sentence));
		s.y = rng.nextInt(height - fontRenderer.FONT_HEIGHT);
		return s;
	}
	
	private static class Sentence{
		String sentence;
		int color, x, y;
	}

}
