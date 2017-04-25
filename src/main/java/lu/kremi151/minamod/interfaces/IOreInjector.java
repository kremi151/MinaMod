package lu.kremi151.minamod.interfaces;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public interface IOreInjector {

	void injectOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkProvider);
}
