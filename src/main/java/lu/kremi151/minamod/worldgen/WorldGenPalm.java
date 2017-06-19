package lu.kremi151.minamod.worldgen;

import java.util.Random;
import java.util.function.Predicate;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.block.BlockCombined;
import lu.kremi151.minamod.block.BlockMinaLeafBase;
import lu.kremi151.minamod.block.BlockMinaPlanks;
import lu.kremi151.minamod.block.BlockPalmLog;
import lu.kremi151.minamod.block.BlockStandaloneLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class WorldGenPalm extends WorldGenBiomeTree{
	
	private static final float PI_F = (float)Math.PI;
	private final Predicate<Block> soil;
	
	public WorldGenPalm(boolean notify){
		this(notify, block -> block == Blocks.SAND);
	}

	public WorldGenPalm(boolean notify, Predicate<Block> soil) {
		super(notify);
		this.soil = soil;
	}

	@Override
	public boolean generate(World world, Random var2, BlockPos pos) {
		return generatePalm(pos, world, var2, soil) != null;
	}
	
	public static BlockPos generatePalm(BlockPos pos, World world, Random random){
		return generatePalm(pos, world, random, block -> true);
	}
	
	public static BlockPos generatePalm(BlockPos pos, World world, Random random, Predicate<Block> soil){
		if(!soil.test(world.getBlockState(pos.down()).getBlock())){
			return null;
		}
		IBlockState wood = MinaBlocks.LOG_PALM.getDefaultState();
		IBlockState leaf = BlockMinaLeafBase.getDefaultStateFor(BlockMinaPlanks.EnumType.PALM).withProperty(BlockLeaves.CHECK_DECAY, false).withProperty(BlockLeaves.DECAYABLE, true);
		final int dirLock = random.nextInt(3);
		float ax = dirLock == 1 ? random.nextFloat() - 0.5f : 0f;
		float az = dirLock == 2 ? random.nextFloat() - 0.5f : 0f;
		final int height = 5 + random.nextInt(5);
		int prevX = 0, prevZ = 0;
		boolean bark = false;
		for(int y = 0; y < height ; y++){
			int x = MathHelper.floor(y * ax);
			int z = MathHelper.floor(y * az);
			BlockPos logpos = pos.add(x, y, z);
			IBlockState logState = bark ? wood.withProperty(BlockStandaloneLog.LOG_AXIS, BlockLog.EnumAxis.NONE) : wood;
			if(y == height - 1){
				logState = logState.withProperty(BlockPalmLog.HEAD, true).withProperty(BlockStandaloneLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
			}
			world.setBlockState(logpos, logState);
			bark = false;
			if(prevX != x || prevZ != z){
				world.setBlockState(logpos.down(), wood.withProperty(BlockStandaloneLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
				world.setBlockState(pos.add(prevX, y - 1, prevZ), wood.withProperty(BlockStandaloneLog.LOG_AXIS, BlockLog.EnumAxis.NONE));
//				world.setBlockState(new BlockPos(prevX, y - 1, prevZ), Blocks.NETHERRACK.getDefaultState());
				prevX = x;
				prevZ = z;
				bark = true;
			}
		}
		int leaves = 6 + random.nextInt(4);
		int leaves_length = 5 + random.nextInt(2);
		float ay = (random.nextFloat() * 0.3f) - 0.15f;
		float q = (PI_F * 2f) / leaves;
		for(int i = 0 ; i < leaves ; i++){
			float angle = i * q;
			ax = MathHelper.cos(angle);
			az = MathHelper.sin(angle);
			int offsetY = 1;
			for(int j = 0 ; j < leaves_length ; j++){
				/*int offsetY = j / ((leaves_length >= 7)?4:3);
				offsetY *= offsetY;*/
				if(j > 1)offsetY += random.nextInt(2);
				int x = prevX + MathHelper.floor(j * ax);
				int z = prevZ + MathHelper.floor(j * az);
				BlockPos leafpos = pos.add(x, height - (offsetY / 2), z);
				world.setBlockState(leafpos, leaf.withProperty(BlockCombined.TYPE, (offsetY % 2 == 0) ? BlockCombined.EnumBlockMode.TOP : BlockCombined.EnumBlockMode.BOTTOM));
			}
		}
		world.setBlockState(pos.add(prevX, height, prevZ), leaf.withProperty(BlockCombined.TYPE, BlockCombined.EnumBlockMode.FULL));
		return pos.add(prevX, height, prevZ);
	}

	@Override
	boolean canGenerateAtBiome(Biome biome) {
		return biome == Biomes.BEACH;
	}

}
