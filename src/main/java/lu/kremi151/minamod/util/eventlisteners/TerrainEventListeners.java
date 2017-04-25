package lu.kremi151.minamod.util.eventlisteners;

import java.util.LinkedList;
import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockHoneycomb;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import lu.kremi151.minamod.worldgen.WorldGenSurfacePlant;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TerrainEventListeners { // NO_UCD (unused code)
	
	private MinaMod mod;
	
	private LinkedList<DecorateHook> decorateHooks;
	
	public TerrainEventListeners(MinaMod mod){
		this.mod = mod;
		this.decorateHooks = new LinkedList<DecorateHook>();
	}
	
//	@SubscribeEvent
//	public void onRegisterBiomes(InitBiomeGens event){
//		
//	}
	
	@SubscribeEvent
	public void onDecorate(DecorateBiomeEvent.Decorate event){
		int chunkX = event.getPos().getX() / 16;
		int chunkZ = event.getPos().getZ() / 16;
		for(DecorateHook h : this.decorateHooks){
			if(event.getRand().nextInt(h.chance) == 0){
				h.generate(chunkX, chunkZ, event.getWorld(), event.getRand());
				event.setResult(Result.DENY);
				break;
			}
		}
	}
	
	@SubscribeEvent
	public void onDecoratePost(DecorateBiomeEvent.Post event){
		if(MinaMod.getMinaConfig().useNewHoneycombGeneration() && event.getRand().nextInt(20) == 0){
			int i = 14 + event.getRand().nextInt(10);
			for(; i > 0 ; i--){
				BlockPos rpos = new BlockPos(
						event.getPos().getX() + event.getRand().nextInt(16),
						60 + event.getRand().nextInt(16),
						event.getPos().getZ() + event.getRand().nextInt(16)
						);
				IBlockState state = event.getWorld().getBlockState(rpos);
				if(state.getBlock() instanceof BlockLog || state.getBlock() instanceof BlockStandaloneLog){
					EnumFacing dir = EnumFacing.HORIZONTALS[event.getRand().nextInt(EnumFacing.HORIZONTALS.length)];
					IBlockState honeycomb = MinaBlocks.HONEYCOMB.getDefaultState().withProperty(BlockHoneycomb.HAS_BEES, true).withProperty(BlockHoneycomb.HAS_HONEY, true).withProperty(BlockHoneycomb.FACING, dir);
					rpos = rpos.offset(dir);
					event.getWorld().setBlockState(rpos, honeycomb);
					if(MinaMod.getMinaConfig().canLogWorldGenInfo()){
						MinaMod.debugPrintln("generated honeycomb at " + rpos);
					}
					break;
				}
			}
		}
	}
	
	public void registerDecorationHook(DecorateHook h){
		this.decorateHooks.add(h);
	}
	
	public static class DecorateHook{
		DecorateBiomeEvent.Decorate.EventType type;
		int chance;
		
		public DecorateHook(DecorateBiomeEvent.Decorate.EventType type, int chance){
			this.type = type;
			this.chance = chance;
		}
		
		public void generate(int chunkX, int chunkZ, World world, Random random){}
	}

	public static class DecorateHookFlower extends DecorateHook{
		
		IBlockState flower;
		int vein;
		
		public DecorateHookFlower(EventType type, int chance, IBlockState flower, int vein) {
			super(type, chance);
			this.flower = flower;
			this.vein = vein;
		}

		@Override
		public void generate(int chunkX, int chunkZ, World world, Random random){
			WorldGenSurfacePlant.spreadFlower(flower, chunkX, chunkZ, world, vein, random);
		}
	}
}
