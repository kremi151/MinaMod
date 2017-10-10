package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.annotations.OreInjector;
import lu.kremi151.minamod.block.BlockBerryCrop;
import lu.kremi151.minamod.block.BlockChiliCrop;
import lu.kremi151.minamod.block.BlockEffectBush;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockMinaSapling;
import lu.kremi151.minamod.block.BlockStrawberryCrop;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class WorldGenerators {
	
	public static final WorldGenHoneyTrees HONEY_TREES = new WorldGenHoneyTrees();
	public static final WorldGenSurfaceTree SURFACE_TREE = new WorldGenSurfaceTree();
	public static final WorldGenStructure WOOKIE_HOUSE = new WorldGenWookieHouseStructure();
	
	@OreInjector("citrin_ore")
	public static final WorldGenNewOre CITRIN_ORE = new WorldGenNewOre(MinaBlocks.CITRIN_ORE.getDefaultState(), 5, 8).setRange(6, 12);
	
	@OreInjector("platinum_ore")
	public static final WorldGenNewOre PLATINUM_ORE = new WorldGenNewOre(MinaBlocks.PLATINUM_ORE.getDefaultState(), 4, 14).setRange(10, 20);
	
	@OreInjector("ruby_ore")
	public static final WorldGenNewOre RUBY_ORE = new WorldGenNewOre(MinaBlocks.RUBY_ORE.getDefaultState(), 5, 8).setRange(6, 18);
	
	@OreInjector("sapphire_ore")
	public static final WorldGenNewOre SAPPHIRE_ORE = new WorldGenNewOre(MinaBlocks.SAPPHIRE_ORE.getDefaultState(), 5, 8).setRange(6, 18);
	
	@OreInjector("rare_earth")
	public static final WorldGenNewOre RARE_SOIL_ORE = new WorldGenNewOre(MinaBlocks.RARE_EARTH_ORE.getDefaultState(), 5, 8).setRange(15, 30);
	
	public static final WorldGenSurfacePlant SURFACE_PLANTS = new WorldGenSurfacePlant.Builder()
			.beginSection(70.0)
			.add(MinaBlocks.RHUBARB_PLANT.getDefaultState(), 4, 60)
			.add(MinaBlocks.LITTLE_BUSH.getDefaultState(), 10)
			.add(MinaBlocks.OPAQUE_BUSH.getDefaultState(), 10)
			.beginSection(10.0)
			.add(MinaBlocks.STRAWBERRY_CROP.getDefaultState().withProperty(BlockStrawberryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.KEVIKUS_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.TRACIUS_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.DOGE_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 7)
			.add(MinaBlocks.CHILI_CROP.getDefaultState().withProperty(BlockChiliCrop.AGE, Integer.valueOf(7)), 2, 10)
			.beginSection(10.0)
			.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.POISONOUS), 3)
			.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.SPEEDY), 1)
			.beginSection(10.0)
			.add(new RandomSaplingPlant(BlockMinaPlanks.EnumType.CHERRY), 5)
			.add(new RandomSaplingPlant(BlockMinaPlanks.EnumType.CHESTNUT), 10)
			.add(new RandomSaplingPlant(BlockMinaPlanks.EnumType.COTTON), 2)
			.add(new RandomSaplingPlant(BlockMinaPlanks.EnumType.PEPPEL), 2)
			.build();
	
	public static final WorldGenQuicksand QUICKSAND = new WorldGenQuicksand();
	
	private WorldGenerators() {}
	
	private static boolean init = false;
	
	public static void registerWorldGenerators(){
		if (init)throw new RuntimeException("Duplicate call of function");
		
		if(!MinaMod.getMinaConfig().useNewHoneycombGeneration())GameRegistry.registerWorldGenerator(HONEY_TREES, 1);
		if(FeatureList.enable_wookie_houses)GameRegistry.registerWorldGenerator(WOOKIE_HOUSE, 1);
		
		GameRegistry.registerWorldGenerator(SURFACE_TREE, 2);
		GameRegistry.registerWorldGenerator(QUICKSAND, 2);
		
		GameRegistry.registerWorldGenerator(CITRIN_ORE, 3);
		GameRegistry.registerWorldGenerator(PLATINUM_ORE, 3);
		GameRegistry.registerWorldGenerator(RUBY_ORE, 3);
		GameRegistry.registerWorldGenerator(SAPPHIRE_ORE, 3);
		GameRegistry.registerWorldGenerator(RARE_SOIL_ORE, 3);

		GameRegistry.registerWorldGenerator(SURFACE_PLANTS, 4);
		
		if(FeatureList.enable_ice_altar){
			GameRegistry.registerWorldGenerator(new WorldGenFrostTemple(), 1);
		}
		GameRegistry.registerWorldGenerator(new WorldGenRandomHerbs(30), 5);
	
		init = true;
	}
	
	private static class RandomSaplingPlant extends WorldGenSurfacePlant.Plant{
		
		private RandomSaplingPlant(BlockMinaPlanks.EnumType type){
			super(MinaBlocks.SAPLING.getDefaultState().withProperty(BlockMinaSapling.TYPE, type), 0);
		}
		
		@Override
		protected void spread(int chunkX, int chunkZ, World world, Random random){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x,y,z);
			if(plant.getBlock().canPlaceBlockAt(world, pos)){
				((BlockMinaSapling)plant.getBlock()).generateTree(world, pos, plant, random);
			}
		}
	}
}
