package lu.kremi151.minamod.util;

import lu.kremi151.minamod.util.Task.ITaskRunnable;

public class TaskRepeat extends Task{
	
	long interval;
	long last_executed = -1l;

	public TaskRepeat(long date, long interval, ITaskRunnable r) {
		super(date, r);
		this.interval = interval;
		this.last_executed = date - interval;
		this.setCanExecuteAgain(true);
	}
	
	@Override
	public void start(){
		super.start();
		last_executed = System.currentTimeMillis();
	}

	@Override
	public boolean canExecuteNow(){
		if(last_executed == -1l){
			return super.canExecuteNow();
		}else{
			return (System.currentTimeMillis() - last_executed) >= interval;
		}
	}

}
