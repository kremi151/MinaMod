package lu.kremi151.minamod.worldgen;

import java.util.Random;

import javax.annotation.Nullable;

import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenStructure implements IWorldGenerator{
	
	private final ResourceLocation structure;

	public WorldGenStructure(ResourceLocation structure){
		this.structure = structure;
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if(canGenerate(random, world, chunkX, chunkZ)){
			WorldServer worldserver = (WorldServer) world;
			MinecraftServer minecraftserver = world.getMinecraftServer();
			TemplateManager templatemanager = worldserver.getStructureTemplateManager();
			Template template = templatemanager.get(minecraftserver, structure);
			
			if (template != null) {
				BlockPos pos = findPosition(random, world, chunkX, chunkZ, template.getSize().getX(), template.getSize().getZ(), template.getSize().getY());
				if(pos != null){
					PlacementSettings placementsettings = getPlacementSettings(random);
					template.addBlocksToWorldChunk(world, pos, placementsettings);
					onPostGenerate(pos, placementsettings);
				}
			}else{
				System.out.println("Unable to find structure: " + structure);
			}
		}
	}
	
	protected void onPostGenerate(BlockPos pos, PlacementSettings placementSettings){}
	
	protected boolean canGenerate(Random random, World world, int chunkX, int chunkZ){
		return true;
	}
	
	@Nullable
	protected BlockPos findPosition(Random random, World world, int chunkX, int chunkZ, int strWidth, int strLength, int strHeight) {
		int x = (chunkX * 16) + random.nextInt(16);
		int z = (chunkZ * 16) + random.nextInt(16);
		int y = MinaUtils.getHeightValue(world, x, z);
		return new BlockPos(x, y, z);
	}
	
	protected PlacementSettings getPlacementSettings(Random rand){
		return (new PlacementSettings()).setMirror(getMirror(rand))
				.setRotation(getRotation(rand)).setIgnoreEntities(false).setChunk((ChunkPos) null)
				.setReplacedBlock((Block) null).setIgnoreStructureBlock(false);
	}
	
	protected Rotation getRotation(Random rand){
		return Rotation.values()[rand.nextInt(Rotation.values().length)];
	}
	
	protected Mirror getMirror(Random rand){
		return Mirror.NONE;
	}
}
