package lu.kremi151.minamod.biome.decorator;

import java.util.Random;

import lu.kremi151.minamod.worldgen.WorldGenPalm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeDecoratorPalmBeach extends BiomeDecorator{
	
	private final static WorldGenPalm PALM = new WorldGenPalm(false);

	@Override
	protected void genDecorations(Biome biomeIn, World worldIn, Random random)
    {
		super.genDecorations(biomeIn, worldIn, random);
		int k1 = 2;

        if (random.nextFloat() < this.extraTreeChance)
        {
            ++k1;
        }

        if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, chunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE))
        for (int j2 = 0; j2 < k1; ++j2)
        {
            int k6 = random.nextInt(16) + 8;
            int l = random.nextInt(16) + 8;
            WorldGenAbstractTree worldgenabstracttree = PALM;
            worldgenabstracttree.setDecorationDefaults();
            BlockPos blockpos = worldIn.getHeight(this.chunkPos.add(k6, 0, l));

            if (worldgenabstracttree.generate(worldIn, random, blockpos))
            {
                worldgenabstracttree.generateSaplings(worldIn, random, blockpos);
            }
        }
    }
}
