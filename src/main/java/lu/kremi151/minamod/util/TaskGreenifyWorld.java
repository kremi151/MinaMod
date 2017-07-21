package lu.kremi151.minamod.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import lu.kremi151.minamod.util.weightedlist.WeightedItem;
import lu.kremi151.minamod.util.weightedlist.WeightedList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class TaskGreenifyWorld implements ITaskRunnable{
	
	private final WeightedList<IBlockState> list;
	private final Random r = new Random();
	
	private TaskGreenifyWorld(WeightedItem<IBlockState> plants[]){
		if(plants.length == 0)throw new RuntimeException("Cannot create TaskGreenifyWorld with 0 plants");
		this.list = WeightedList.from(plants).immutable();
		r.setSeed(System.currentTimeMillis());
	}

	@Override
	public void run(Task t, ProgressDispatcher progressDispatcher) {
		WorldServer[] wsa = MinaMod.getMinaMod().getMinecraftServer().worlds;
		for(WorldServer ws : wsa){
			if(ws.provider.getDimension() == 0 && ws.getGameRules().getBoolean("minamod.bushPopulating")){
				List<EntityPlayer> list = ws.playerEntities;
				if(list.size() > 0){
					IBlockState plant = this.list.randomElement(r);
					int i = ws.rand.nextInt(list.size());
					EntityPlayer p = list.get(i);
					int x = (int)p.posX - 10 + ws.rand.nextInt(20);
					int z = (int)p.posZ - 10 + ws.rand.nextInt(20);
					int y = MinaUtils.getHeightValue(ws, x, z);
					BlockPos pos = new BlockPos(x,y,z);
					if(ws.isAirBlock(pos) && plant.getBlock().canPlaceBlockAt(ws, pos)){
						ws.setBlockState(pos, plant);
					}
				}
			}
		}
	}
	
	public static class Builder{
		private ArrayList<WeightedItem<IBlockState>> wlist = new ArrayList<WeightedItem<IBlockState>>();
		
		public Builder(){}
		
		public Builder add(IBlockState plant, double weight){
			wlist.add(new WeightedItem<IBlockState>(plant, weight));
			return this;
		}
		
		public TaskGreenifyWorld build(){
			WeightedItem<IBlockState> plants[] = new WeightedItem[wlist.size()];
			plants = wlist.toArray(plants);
			return new TaskGreenifyWorld(plants);
		}
	}

}
