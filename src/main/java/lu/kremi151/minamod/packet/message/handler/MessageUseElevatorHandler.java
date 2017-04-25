package lu.kremi151.minamod.packet.message.handler;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockElevatorFloor;
import lu.kremi151.minamod.packet.message.MessageUseElevator;
import lu.kremi151.minamod.packet.message.handler.abstracts.AbstractServerMessageHandler;
import lu.kremi151.minamod.util.FeatureList;
import lu.kremi151.minamod.util.Task;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.TaskRepeat;
import lu.kremi151.minamod.util.TextHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseElevatorHandler extends AbstractServerMessageHandler<MessageUseElevator> {

	@Override
	public IMessage handleServerMessage(EntityPlayer player, MessageUseElevator message, MessageContext ctx) {
		BlockPos standingPos = new BlockPos(player.posX, player.posY - 1, player.posZ);
		World world = player.world;
		int relative = message.getRelativeFloor();
		if (world.getBlockState(standingPos).getBlock() == MinaBlocks.ELEVATOR_FLOOR) {
			if (relative > 0) {
				for (int y = standingPos.getY() + 3; y < world.getHeight(); y++) {
					BlockPos npos = new BlockPos(standingPos.getX(), y, standingPos.getZ());
					if (BlockElevatorFloor.isValidLevelFloor(world.getBlockState(npos))) {
						relative--;
					}
					if (relative == 0) {
						if (FeatureList.enable_advanced_elavator && MinaMod.getMinaConfig().useFancyElevatorMovement()) {
							TaskTransportPlayer.perform((EntityPlayerMP) player, standingPos, npos);
						} else {
							teleport(player, (double) npos.getX() + 0.5, (double) npos.getY() + 1d,
									(double) npos.getZ() + 0.5);
						}
						return null;
					}
				}
			} else if (relative < 0) {
				for (int y = standingPos.getY() - 3; y >= 0; y--) {
					BlockPos npos = new BlockPos(standingPos.getX(), y, standingPos.getZ());
					if (BlockElevatorFloor.isValidLevelFloor(world.getBlockState(npos))) {
						relative++;
					}
					if (relative == 0) {
						// teleport(player, (double)npos.getX() + 0.5,
						// (double)npos.getY() + 1d, (double)npos.getZ() + 0.5);
						if (FeatureList.enable_advanced_elavator && MinaMod.getMinaConfig().useFancyElevatorMovement()) {
							TaskTransportPlayer.perform((EntityPlayerMP) player, standingPos, npos);
						} else {
							teleport(player, (double) npos.getX() + 0.5, (double) npos.getY() + 1d,
									(double) npos.getZ() + 0.5);
						}
						return null;
					}
				}
			}
		}
		return null;
	}

	private void teleport(EntityPlayer player, double x, double y, double z) {
		player.setPositionAndUpdate(x, y, z);
		player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 1f);
		player.world.playSound(x, y, z, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f, false);
	}

	private static class TaskTransportPlayer implements ITaskRunnable {

		EntityPlayerMP p;
		GameType bkp;
		BlockPos dest, src;
		double y, dy;
		boolean init = false, fly, canfly;

		private TaskTransportPlayer(EntityPlayerMP p, BlockPos src, BlockPos dest) {
			this.p = p;
			this.dest = dest;
			this.src = src;
			y = src.getY();
			if (y <= dest.getY()) {
				dy = 0.2d;
			} else {
				dy = -0.2d;
			}
		}

		@Override
		public void run(Task t) {
			if(p != null){
				try{
					if (!init) {
						bkp = p.interactionManager.getGameType();
						fly = p.capabilities.isFlying;
						canfly = p.capabilities.allowFlying;
						p.setGameType(GameType.SPECTATOR);
						init = true;
					} else {
						y += dy;
						if ((dy >= 0 && y >= dest.getY() + 1.0d) || (dy < 0 && y <= dest.getY() + 1.0d)) {
							t.setCanExecuteAgain(false);
							y = dest.getY() + 1.1d;
							p.setGameType(bkp);
							p.capabilities.allowFlying = canfly;
							p.capabilities.isFlying = fly;
							p.sendPlayerAbilities();
							
							/*p.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 1f);
							p.worldObj.playSound(dest.getX(), y, dest.getZ(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f, false);*/
							p.world.playSound(null, new BlockPos(dest.getX(), y, dest.getZ()), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 1f);
							//System.out.println("Arrived");
						}
						p.setPositionAndUpdate(dest.getX() + 0.5d, y, dest.getZ() + 0.5d);
					}
					
					if(!t.canExecuteAgain()){
						p = null;//Dereferencing to avoid memory leak
					}
				}catch(Exception e){
					MinaMod.getLogger().error("Elevator task failed to execute", e);
					t.setCanExecuteAgain(false);
					p.setPositionAndUpdate(src.getX(), src.getY(), src.getZ());
					TextHelper.sendTranslateableErrorMessage(p, "msg.misc.unexpected_error");
				}
			}
		}

		private static void perform(EntityPlayerMP player, BlockPos standingPos, BlockPos destPos) {
			MinaMod.getMinaMod().getTickHandler().addServerTask(new TaskRepeat(System.currentTimeMillis(), 5,
					new TaskTransportPlayer((EntityPlayerMP) player, standingPos, destPos)));
		}

	}

}
