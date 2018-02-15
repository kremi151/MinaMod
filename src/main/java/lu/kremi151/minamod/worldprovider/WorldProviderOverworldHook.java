package lu.kremi151.minamod.worldprovider;

import lu.kremi151.minamod.util.ReflectionLoader;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderOverworldHook extends WorldProviderSurface{

	public WorldProviderOverworldHook(){
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
    {
		Vec3d color = super.getSkyColor(cameraEntity, partialTicks);
		if(world.getDifficulty() != EnumDifficulty.PEACEFUL && getBloodMoonPhase(world.getWorldTime()) == 0){
			color = new Vec3d((color.x + 0.75) / 2.0, (color.y + 0.3) / 2.0, (color.z + 0.3) / 2.0);
		}
        return color;
    }
	
	private static int getBloodMoonPhase(long worldTime)
    {
        return (int)(worldTime / 24000L % 24L + 8L) % 24;
    }
	
	private int getBloodMoonPhase()
    {
		return getBloodMoonPhase(world.getWorldTime());
    }
	
	public void setToNextBloodMoon() {
		long nextDayStart = world.getWorldTime() + (24000L - (world.getWorldTime() % 24000L));
		while(true) {
			if(getBloodMoonPhase(nextDayStart) == 0) {
				break;
			}
			nextDayStart += 24000L;
		}
		world.setWorldTime(nextDayStart);
	}
	
	public static boolean isBloodMoon(World world){
		return world.provider instanceof WorldProviderOverworldHook && world.getDifficulty() != EnumDifficulty.PEACEFUL && ((WorldProviderOverworldHook)world.provider).getBloodMoonPhase(world.getWorldTime()) == 0;
	}
	
	public static void hookIn(){
		try {
			ReflectionLoader.DimensionType_setClazz(DimensionType.OVERWORLD, WorldProviderOverworldHook.class);
		} catch (RuntimeException e) {
			System.err.println("Could not register hook for overworld provider");
			e.printStackTrace();
		}
	}
}
