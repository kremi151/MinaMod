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
	public static final MinaSoundEvent soundDrillTurn;
	
	static{
		soundIceGolhemSay = new MinaSoundEvent(MinaMod.MODID, "mob.ice_golhem.say");
		soundIceGolhemHurt = new MinaSoundEvent(MinaMod.MODID, "mob.ice_golhem.hurt");
		soundIceGolhemDeath = new MinaSoundEvent(MinaMod.MODID, "mob.ice_golhem.death");
		soundTurtleHello = new MinaSoundEvent(MinaMod.MODID, "mob.turtle.hello");
		soundPenguinSay = new MinaSoundEvent(MinaMod.MODID, "mob.penguin.say");
		soundDrillTurn = new MinaSoundEvent(MinaMod.MODID, "item.drill.turn");
	}
	
	private static boolean init = false;
	
	static void init(){
		if(init)throw new RuntimeException("Duplicate call of function");

		registerSound(soundIceGolhemSay);
		registerSound(soundIceGolhemHurt);
		registerSound(soundIceGolhemDeath);
		registerSound(soundTurtleHello);
		registerSound(soundPenguinSay);
		registerSound(soundDrillTurn);
		
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
		
		public MinaSoundEvent(String modId, String name) {
			this(new ResourceLocation(modId, name));
		}
		
	}
}
