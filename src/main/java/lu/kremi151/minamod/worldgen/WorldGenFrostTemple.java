package lu.kremi151.minamod.worldgen;

import java.util.Random;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockIceAltar;
import lu.kremi151.minamod.enums.EnumFrostTempleType;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worlddata.MinaWorld;
import lu.kremi151.minamod.worlddata.data.FrostTempleMeta;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenFrostTemple implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		int chunkPosX = chunkX * 16;
		int chunkPosZ = chunkZ * 16;
		FrostTempleMeta ftm = MinaWorld.forWorld(world).getFrostTempleMeta();
		if (Math.sqrt(Math.pow(chunkPosX - world.getSpawnPoint().getX(), 2)
				+ Math.pow(chunkPosZ - world.getSpawnPoint().getZ(), 2)) < ftm.getDistanceFromSpawn()) {
			return;
		}
		if (random.nextInt(80) != 0)
			return;
		if (ftm.isLimited() && ftm.getBlackFrostTempleLeft() == 0 && ftm.getWhiteFrostTempleLeft() == 0)
			return;
		int x = chunkPosX + random.nextInt(8);
		int z = chunkPosZ + random.nextInt(8);
		int y = MinaUtils.getHeightValue(world, x, z);

		for (int x_ = x; x_ < x + 9; x_++) {
			for (int z_ = z; z_ < z + 9; z_++) {
				int y_ = MinaUtils.getHeightValue(world, x_, z_);
				if (y_ < y)
					y = y_;
			}
		}

		y -= 5;

		BlockPos bpos = new BlockPos(x, y, z);
		boolean doGen = TerrainGen.decorate(world, random, bpos, Decorate.EventType.CUSTOM);
		Biome b = world.getBiomeForCoordsBody(bpos);
		if (b == Biomes.ICE_PLAINS && doGen) {
			EnumFrostTempleType type;
			if (bpos.getZ() > 0) {
				if (ftm.isLimited() && ftm.getBlackFrostTempleLeft() > 0) {
					type = EnumFrostTempleType.BLACK;
				} else {
					return;
				}
			} else {
				if (ftm.isLimited() && ftm.getWhiteFrostTempleLeft() > 0) {
					type = EnumFrostTempleType.WHITE;
				} else {
					return;
				}
			}
			if (generateTemple(world, x, y, z, type, random, true)) {
				if (ftm.isLimited()) {
					switch (type) {
					case BLACK:
						int l1 = ftm.getBlackFrostTempleLeft();
						ftm.setBlackFrostTempleLeft(l1 - 1);
						break;
					case WHITE:
						int l2 = ftm.getWhiteFrostTempleLeft();
						ftm.setWhiteFrostTempleLeft(l2 - 1);
						break;
					}
				}
			}
		}
	}

	public static boolean generateTemple(World world, int x, int y, int z, EnumFrostTempleType type, Random r,
			boolean registerTemple) {
		FrostTempleMeta ftm = MinaWorld.forWorld(world).getFrostTempleMeta();

		// Part1 (Upper part)
		for (int cx = x; cx < x + 9; cx++) {
			for (int cy = y; cy < y + 5; cy++) {
				for (int cz = z; cz < z + 9; cz++) {
					world.setBlockToAir(new BlockPos(cx, cy, cz));
				}
			}
		}
		for (int cx = x; cx < x + 9; cx++) {
			for (int cz = z; cz < z + 9; cz++) {
				world.setBlockState(new BlockPos(cx, y, cz), MinaBlocks.FROZEN_STONE.getDefaultState());
				world.setBlockState(new BlockPos(cx, y + 4, cz), MinaBlocks.FROZEN_STONE.getDefaultState());
			}
		}

		for (int cy = y + 1; cy < y + 4; cy++) {
			// Outer pillars
			for (int i = 0; i <= 8; i += 2) {
				world.setBlockState(new BlockPos(x + i, cy, z), MinaBlocks.FROZEN_BRICK.getDefaultState());
				world.setBlockState(new BlockPos(x + i, cy, z + 8), MinaBlocks.FROZEN_BRICK.getDefaultState());
			}

			/*
			 * world.setBlockState(new BlockPos(x, cy, z),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+2, cy, z),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+4, cy, z),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+6, cy, z),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+8, cy, z),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * 
			 * world.setBlockState(new BlockPos(x, cy, z+8),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+2, cy, z+8),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+4, cy, z+8),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+6, cy, z+8),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+8, cy, z+8),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 */

			for (int i = 2; i <= 6; i += 2) {
				world.setBlockState(new BlockPos(x, cy, z + i), MinaBlocks.FROZEN_BRICK.getDefaultState());
				world.setBlockState(new BlockPos(x + 8, cy, z + i), MinaBlocks.FROZEN_BRICK.getDefaultState());
			}

			/*
			 * world.setBlockState(new BlockPos(x, cy, z+2),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x, cy, z+4),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x, cy, z+6),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * 
			 * world.setBlockState(new BlockPos(x+8, cy, z+2),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+8, cy, z+4),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 * world.setBlockState(new BlockPos(x+8, cy, z+6),
			 * MinaBlocks.FROZEN_BRICK.getDefaultState());
			 */

			// Inner pillars
			world.setBlockState(new BlockPos(x + 2, cy, z + 2), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 3, cy, z + 2), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 2, cy, z + 3), MinaBlocks.FROZEN_BRICK.getDefaultState());

			world.setBlockState(new BlockPos(x + 6, cy, z + 2), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 5, cy, z + 2), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 6, cy, z + 3), MinaBlocks.FROZEN_BRICK.getDefaultState());

			world.setBlockState(new BlockPos(x + 2, cy, z + 6), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 3, cy, z + 6), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 2, cy, z + 5), MinaBlocks.FROZEN_BRICK.getDefaultState());

			world.setBlockState(new BlockPos(x + 6, cy, z + 6), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 5, cy, z + 6), MinaBlocks.FROZEN_BRICK.getDefaultState());
			world.setBlockState(new BlockPos(x + 6, cy, z + 5), MinaBlocks.FROZEN_BRICK.getDefaultState());
		}
		for (int cx = x; cx < x + 9; cx++) {
			for (int cz = z; cz < z + 9; cz++) {
				BlockPos spos = new BlockPos(cx, y + 5, cz);
				if (r.nextInt(3) != 0 && world.isAirBlock(spos))
					world.setBlockState(spos, Blocks.SNOW_LAYER.getDefaultState());
			}
		}
		world.setBlockState(new BlockPos(x + 1, y, z + 1), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 7, y, z + 1), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 7, y, z + 7), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 1, y, z + 7), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 4, y, z + 2), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 6, y, z + 4), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 4, y, z + 6), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(new BlockPos(x + 2, y, z + 4), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());

		BlockPos altar_pos = new BlockPos(x + 4, y + 1, z + 4);
		world.setBlockState(altar_pos,
				MinaBlocks.ICE_ALTAR.getDefaultState()
						.withProperty(BlockIceAltar.BLACK_PEARL, type == EnumFrostTempleType.BLACK)
						.withProperty(BlockIceAltar.WHITE_PEARL, type == EnumFrostTempleType.WHITE)
						.withProperty(BlockIceAltar.CAN_SPAWN_BOSS, false));

		// Part 1 end
		// Part 2 (Lower part)
		BlockPos ref2 = new BlockPos(x, y - 15, z);
		for (int cx = ref2.getX(); cx < ref2.getX() + 9; cx++) {
			for (int cz = ref2.getZ(); cz < ref2.getZ() + 9; cz++) {
				for (int cy = ref2.getY(); cy < ref2.getY() + 5; cy++) {
					world.setBlockToAir(new BlockPos(cx, cy, cz));
				}
			}
		}
		for (int cx = ref2.getX(); cx < ref2.getX() + 9; cx++) {
			for (int cy = ref2.getY(); cy < ref2.getY() + 5; cy++) {
				world.setBlockState(new BlockPos(cx, cy, ref2.getZ()), MinaBlocks.FROZEN_BRICK.getDefaultState());
				world.setBlockState(new BlockPos(cx, cy, ref2.getZ() + 8), MinaBlocks.FROZEN_BRICK.getDefaultState());
			}
		}
		for (int cz = ref2.getZ(); cz < ref2.getZ() + 9; cz++) {
			for (int cy = ref2.getY(); cy < ref2.getY() + 5; cy++) {
				world.setBlockState(new BlockPos(ref2.getX(), cy, cz), MinaBlocks.FROZEN_BRICK.getDefaultState());
				world.setBlockState(new BlockPos(ref2.getX() + 8, cy, cz), MinaBlocks.FROZEN_BRICK.getDefaultState());
			}
		}
		for (int cy = ref2.getY(); cy < ref2.getY() + 5; cy += 4) {
			for (int cx = ref2.getX(); cx < ref2.getX() + 9; cx++) {
				for (int cz = ref2.getZ(); cz < ref2.getZ() + 9; cz++) {
					world.setBlockState(new BlockPos(cx, cy, cz), MinaBlocks.FROZEN_BRICK.getDefaultState());
				}
			}
		}
		world.setBlockState(ref2.add(2, 0, 2), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(ref2.add(6, 0, 2), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(ref2.add(6, 0, 6), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());
		world.setBlockState(ref2.add(2, 0, 6), MinaBlocks.FROZEN_GLOWSTONE.getDefaultState());

		BlockPos sppos = ref2.add(4, 1, 4);
		world.setBlockState(sppos, Blocks.CHEST.getDefaultState());
		TileEntityChest tc = (TileEntityChest) world.getTileEntity(sppos);
		int c = 2 + r.nextInt(5);
		for (int i = 0; i < c; i++) {
			boolean co = true;
			while (co) {
				int pos = r.nextInt(tc.getSizeInventory());
				if (tc.getStackInSlot(pos).isEmpty()) {
					co = false;
					int ii = r.nextInt(3);
					switch (ii) {
					case 0:
						tc.setInventorySlotContents(pos, new ItemStack(MinaItems.EVER_SNOW, 1 + r.nextInt(2)));
						break;
					case 1:
						tc.setInventorySlotContents(pos, new ItemStack(MinaItems.NAMIE_SEEDS, 2 + r.nextInt(4)));
						break;
					case 2:
						tc.setInventorySlotContents(pos, new ItemStack(MinaBlocks.FROZEN_GLOWSTONE, 6 + r.nextInt(6)));
						break;
					}
				}
			}
		}

		// Part 2 end
		// Part 3 (Pillars between 2 parts)
		for (int cx = x; cx < x + 8; cx += 7) {
			for (int cz = z; cz < z + 8; cz += 7) {
				for (int cy = ref2.getY() + 5; cy < y; cy++) {
					BlockPos ppos = new BlockPos(cx, cy, cz);
					world.setBlockState(ppos, MinaBlocks.FROZEN_BRICK.getDefaultState());
					world.setBlockState(ppos.add(1, 0, 0), MinaBlocks.FROZEN_BRICK.getDefaultState());
					world.setBlockState(ppos.add(1, 0, 1), MinaBlocks.FROZEN_BRICK.getDefaultState());
					world.setBlockState(ppos.add(0, 0, 1), MinaBlocks.FROZEN_BRICK.getDefaultState());
				}
			}
		}
		//

		MinaMod.debugPrintln("temple " + x + " " + y + " " + z + " type: " + type);

		if (registerTemple)
			ftm.addFrostTempleToList(type, altar_pos);
		return true;
	}

}
