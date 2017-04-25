package lu.kremi151.minamod;

import lu.kremi151.minamod.fluid.FluidWaterClear;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class MinaFluids {
	
	public static final FluidWaterClear CLEAR_WATER = new FluidWaterClear("clear_water", new ResourceLocation(MinaMod.MODID, "blocks/water_clear_still"), new ResourceLocation(MinaMod.MODID, "blocks/water_clear_flow"));

	static void registerFluids(){
		//FluidRegistry.registerFluid(CLEAR_WATER);
	}
}
