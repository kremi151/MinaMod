package lu.kremi151.minamod.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.Task.ITaskRunnable;
import lu.kremi151.minamod.util.Task.ProgressDispatcher;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.ChunkDataEvent;

public final class TaskOreInjector implements ITaskRunnable{
	
	private static TaskOreInjector instance = null;
	
	private final List<ChunkDataEvent.Load> chunks = Collections.synchronizedList(new LinkedList<>());
	
	private TaskOreInjector() {}

	@Override
	public void run(Task task, ProgressDispatcher progressDispatcher) {
		synchronized(chunks) {
			if(!chunks.isEmpty()) {
				Iterator<ChunkDataEvent.Load> it = chunks.iterator();
				while(it.hasNext()) {
					ChunkDataEvent.Load event = it.next();
					if(event.getWorld().isChunkGeneratedAt(event.getChunk().x, event.getChunk().z)) {
						NBTTagCompound newnbt = event.getData().getCompoundTag(MinaMod.MODID);
						int last_vcode = 0;
						if(newnbt.hasKey(OreInjectorManager.DATA_TAG, 99)){
							last_vcode = newnbt.getInteger(OreInjectorManager.DATA_TAG);
						}
						OreInjectorManager.performOreInjection(event.getWorld(), event.getChunk(), last_vcode);
						it.remove();
					}
				}
			}
		}
	}
	
	public void enqueueChunk(ChunkDataEvent.Load event) {
		chunks.add(event);
	}
	
	public static TaskOreInjector instance() {
		if(instance == null) {
			instance = new TaskOreInjector();
		}
		return instance;
	}

}
