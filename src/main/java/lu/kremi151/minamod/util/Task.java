package lu.kremi151.minamod.util;

import java.util.List;

import lu.kremi151.minamod.MinaBlocks;
import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.block.BlockEffectBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class Task {
	long date;
	private ITaskRunnable r;
	
	private boolean executeAgain = false;

	public Task(long date, ITaskRunnable r) {
		this.date = date;
		this.r = r;
	}
	
	public void start(){
		try{
			this.run(this);
		}catch(Throwable t){
			MinaMod.getLogger().error("Error executing server task", t);
		}
	}
	
	public void run(Task task){
		r.run(task);
	}
	
	public boolean canExecuteAgain(){
		return executeAgain;
	}
	
	public void setCanExecuteAgain(boolean v){
		executeAgain = v;
	}
	
	public boolean canExecuteNow(){
		return System.currentTimeMillis() - date >= 0;
	}
	
	public static interface ITaskRunnable{
		
		public void run(Task t);
		
	}
}
