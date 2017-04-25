package lu.kremi151.minamod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MinaSounds {

	public static final MinaSoundEvent soundIceGolhemSay;
	public static final MinaSoundEvent soundIceGolhemHurt;
	public static final MinaSoundEvent soundIceGolhemDeath;
	public static final MinaSoundEvent soundTurtleHello;
	public static final MinaSoundEvent soundPenguinSay;
	
	static{
		soundIceGolhemSay = new MinaSoundEvent(new ResourceLocation(MinaMod.MODID, "mob.ice_golhem.say"));
		soundIceGolhemHurt = new MinaSoundEvent(new ResourceLocation(MinaMod.MODID, "mob.ice_golhem.hurt"));
		soundIceGolhemDeath = new MinaSoundEvent(new ResourceLocation(MinaMod.MODID, "mob.ice_golhem.death"));
		soundTurtleHello = new MinaSoundEvent(new ResourceLocation(MinaMod.MODID, "mob.turtle.hello"));
		soundPenguinSay = new MinaSoundEvent(new ResourceLocation(MinaMod.MODID, "mob.penguin.say"));
	}
	
	private static boolean init = false;
	
	static void init(){
		if(init)throw new RuntimeException("Duplicate call of function");

		registerSound(soundIceGolhemSay);
		registerSound(soundIceGolhemHurt);
		registerSound(soundIceGolhemDeath);
		registerSound(soundTurtleHello);
		registerSound(soundPenguinSay);
		
		init = true;
	}
	
	private static void registerSound(MinaSoundEvent event){
		GameRegistry.register(event);
	}
	
	private static class MinaSoundEvent extends SoundEvent{

		public MinaSoundEvent(ResourceLocation soundNameIn) {
			super(soundNameIn);
			this.setRegistryName(soundNameIn);
		}
		
	}
}
