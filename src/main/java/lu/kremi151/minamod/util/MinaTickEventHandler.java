package lu.kremi151.minamod.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockEffectBush;
import lu.kremi151.minamod.block.abstracts.BlockCustomCrops;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MinaTickEventHandler {
	
	private long last_save_all = -1l;
	
	private Ticker server_tasks;
	@SideOnly(Side.CLIENT)
	private Ticker client_tasks;
	
	private TaskGreenifyWorld greenifyWorld;
	
	public MinaTickEventHandler(MinaMod mod){
		server_tasks = new Ticker();
		
		MinaMod.getProxy().executeClientSide(() -> {client_tasks = new Ticker();});
		
		addDefaultTasks();
	}
	
	private void addDefaultTasks(){
		greenifyWorld = new TaskGreenifyWorld.Builder()
				.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.POISONOUS), 20.0)
				.add(MinaBlocks.EFFECT_BUSH.getDefaultState().withProperty(BlockEffectBush.VARIANT, BlockEffectBush.EnumType.SPEEDY), 5.0)
				.add(MinaBlocks.STRAWBERRY_CROP.getDefaultState().withProperty(BlockCustomCrops.AGE, Integer.valueOf(7)), 2.0)
				.add(MinaBlocks.CHILI_CROP.getDefaultState().withProperty(BlockCustomCrops.AGE, Integer.valueOf(7)), 2.0)
				.add(MinaBlocks.NAMIE_FLOWER.getDefaultState().withProperty(BlockCustomCrops.AGE, Integer.valueOf(7)), 1.0)
				.build();
		server_tasks.addTask(new TaskRepeat(System.currentTimeMillis() + 600000l, 600000l, greenifyWorld));
		server_tasks.addTask(new TaskRepeat(System.currentTimeMillis(), 100l, TaskOreInjector.instance()));
	}
	
	public void addServerTask(Task t){ // NO_UCD (unused code)
		server_tasks.addTask(t);
	}
	
	@SideOnly(Side.CLIENT)
	public void addClientTask(Task t){ // NO_UCD (unused code)
		client_tasks.addTask(t);
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) { // NO_UCD (unused code)
		if(last_save_all == -1l){
			last_save_all = System.currentTimeMillis() + (long)MinaMod.getMinaConfig().getSaveInterval();
		}
		if(System.currentTimeMillis() - last_save_all >= (long)MinaMod.getMinaConfig().getSaveInterval() * 60 * 1000){
			last_save_all = System.currentTimeMillis();
			if(MinaMod.getMinaMod().saveAll(event.side) > 0){
				long end = System.currentTimeMillis() - last_save_all;
				MinaMod.println("Saving completed, it took %d ms", end);
			}
		}
		server_tasks.iterateTasks();
	}
	
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) { // NO_UCD (unused code)
		client_tasks.iterateTasks();
	}
	
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) { // NO_UCD (unused code)

	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event) { // NO_UCD (unused code)
		
	}
	
	private class Ticker{

		private final List<Task> tasks;
		
		private Ticker(){
			tasks = Collections.synchronizedList(new LinkedList<Task>());
		}
		
		public void iterateTasks(){
			synchronized(tasks){
				if(tasks.size() > 0){
					Iterator<Task> it = tasks.iterator();
					while(it.hasNext()){
						Task t = it.next();
						if(t.canExecuteNow()){
							t.start();
							if(!t.canExecuteAgain()){
								it.remove();
							}
						}
					}
				}
			}
		}
		
		public boolean addTask(Task t){
			return tasks.add(t);
		}
	}
}
