package lu.kremi151.minamod.worldgen;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.annotations.OreInjector;
import lu.kremi151.minamod.block.BlockBerryCrop;
import lu.kremi151.minamod.block.BlockChiliCrop;
import lu.kremi151.minamod.block.BlockEffectBush;
import lu.kremi151.minamod.block.BlockStrawberryCrop;
import lu.kremi151.minamod.util.FeatureList;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenerators {
	
	public static final WorldGenHoneyTrees HONEY_TREES = new WorldGenHoneyTrees();
	public static final WorldGenSurfaceTree SURFACE_TREE = new WorldGenSurfaceTree();
	//public static final WorldGenSchematic WOOKIE_HOUSE = new WorldGenWookieHouse();
	public static final WorldGenStructure WOOKIE_HOUSE = new WorldGenWookieHouseStructure();
	
	@OreInjector(chunkVersion = 1)
	public static final WorldGenNewOre CITRIN_ORE = new WorldGenNewOre(MinaBlocks.CITRIN_ORE.getDefaultState(), 5, 2).setRange(6, 12);
	
	@OreInjector(chunkVersion = 1)
	public static final WorldGenNewOre PLATINUM_ORE = new WorldGenNewOre(MinaBlocks.PLATINUM_ORE.getDefaultState(), 3, 4).setRange(10, 18);
	
	@OreInjector(chunkVersion = 1)
	public static final WorldGenNewOre RUBY_ORE = new WorldGenNewOre(MinaBlocks.RUBY_ORE.getDefaultState(), 5, 2).setRange(6, 18);
	
	@OreInjector(chunkVersion = 1)
	public static final WorldGenNewOre SAPPHIRE_ORE = new WorldGenNewOre(MinaBlocks.SAPPHIRE_ORE.getDefaultState(), 5, 2).setRange(6, 18);
	
	@OreInjector(chunkVersion = 1)
	public static final WorldGenSurfacePlant SURFACE_PLANTS = new WorldGenSurfacePlant.Builder()
			.beginSection(80.0)
			.add(MinaBlocks.RHUBARB_PLANT.getDefaultState(), 4, 60)
			.beginSection(10.0)
			.add(MinaBlocks.STRAWBERRY_CROP.getDefaultState().withProperty(BlockStrawberryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.KEVIKUS_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.TRACIUS_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 10)
			.add(MinaBlocks.DOGE_CROP.getDefaultState().withProperty(BlockBerryCrop.AGE, Integer.valueOf(7)), 2, 7)
			.add(MinaBlocks.CHILI_CROP.getDefaultState().withProperty(BlockChiliCrop.AGE, Integer.valueOf(7)), 2, 10)
			.beginSection(10.0)
			.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.POISONOUS), 3)
			.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.SPEEDY), 1)
			.build();
	
	private static boolean init = false;
	
	public static void registerWorldGenerators(){
		if (init)throw new RuntimeException("Duplicate call of function");
		
		if(!MinaMod.getMinaConfig().useNewHoneycombGeneration())GameRegistry.registerWorldGenerator(HONEY_TREES, 1);
		if(FeatureList.enable_wookie_houses)GameRegistry.registerWorldGenerator(WOOKIE_HOUSE, 1);
		
		GameRegistry.registerWorldGenerator(SURFACE_TREE, 2);
		
		GameRegistry.registerWorldGenerator(CITRIN_ORE, 3);
		GameRegistry.registerWorldGenerator(PLATINUM_ORE, 3);
		GameRegistry.registerWorldGenerator(RUBY_ORE, 3);
		GameRegistry.registerWorldGenerator(SAPPHIRE_ORE, 3);

		GameRegistry.registerWorldGenerator(SURFACE_PLANTS, 4);
		
		if(FeatureList.enable_ice_altar){
			GameRegistry.registerWorldGenerator(new WorldGenFrostTemple(), 1);
		}
		if(FeatureList.enable_mixtures)GameRegistry.registerWorldGenerator(new WorldGenRandomHerbs(30), 5);
	
		init = true;
	}
}
