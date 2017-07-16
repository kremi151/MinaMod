package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.IOreInjector;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.util.weightedlist.WeightedItem;
import lu.kremi151.minamod.util.weightedlist.WeightedList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenSurfacePlant implements IWorldGenerator, IOreInjector{
	
	private final WeightedList<Plant> list;
	private final int chance;
	
	private WorldGenSurfacePlant(int chance, WeightedItem<Plant>... plants){
		this.list = WeightedList.from(plants);
		this.chance = chance;
	}
	
	private WorldGenSurfacePlant(int chance, WeightedList<Plant> list){
		this.list = list;
		this.chance = chance;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		injectOre(random, chunkX, chunkZ, world, chunkProvider);
	}
	
	public static void spreadFlower(IBlockState plant, int chunkX, int chunkZ, World world, int vein, Random random){
		for(int i = 0 ; i < vein ; i++){
			int x = (chunkX * 16) + random.nextInt(16);
			int z = (chunkZ * 16) + random.nextInt(16);
			int y = MinaUtils.getHeightValue(world, x, z);
			BlockPos pos = new BlockPos(x, y, z);
	    	boolean doGen = TerrainGen.decorate(world, random, pos, Decorate.EventType.FLOWERS);
			if(world.isAirBlock(pos) && plant.getBlock().canPlaceBlockAt(world, pos) && doGen){
				world.setBlockState(pos, plant, 1);
				if(MinaMod.getMinaConfig().canLogWorldGenInfo()){
					WorldGenLogger.get().logBuilding(plant.getBlock().getRegistryName().toString(), x, y, z);
				}
			}
		}
	}

	@Override
	public void injectOre(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkProvider) {
		if(world.provider.getDimensionType() == DimensionType.OVERWORLD && random.nextInt(chance) == 0){
			list.randomElement(random).spread(chunkX, chunkZ, world, random);
		}
	}
	
	public static class Plant{
		private final int vein;
		protected final IBlockState plant;
		
		protected Plant(IBlockState plant, int vein){
			this.plant = plant;
			this.vein = vein;
		}
		
		protected void spread(int chunkX, int chunkZ, World world, Random random){
			spreadFlower(plant, chunkX, chunkZ, world, vein, random);
		}
	}
	
	public static class Builder{
		
		private WeightedList.DistributedBuilder<Plant> builder;
		
		public Builder(){
			builder = new WeightedList.DistributedBuilder<Plant>();
		}
		
		public Builder add(IBlockState plant, int vein, double weight){
			builder.add(new Plant(plant,vein), weight);
			return this;
		}
		
		public Builder add(IBlockState plant, double weight){
			return add(plant, 3, weight);
		}
		
		public Builder add(Plant plant, double weight){
			builder.add(plant, weight);
			return this;
		}
		
		public Builder beginSection(double percentage){
			builder.beginSection(percentage);
			return this;
		}
		
		public WorldGenSurfacePlant build(int chance){
			return new WorldGenSurfacePlant(chance, builder.build());
		}
		
		public WorldGenSurfacePlant build(){
			return build(25);
		}
	}

}
