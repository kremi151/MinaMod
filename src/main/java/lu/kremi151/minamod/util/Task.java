package lu.kremi151.minamod.util;

import java.util.LinkedList;

import lu.kremi151.minamod.MinaMod;

public class Task {
	public final long date;
	private final ITaskRunnable r;
	
	private boolean executeAgain = false;
	
	private final LinkedList<ITaskListener> listeners = new LinkedList<>();
	private final ProgressDispatcher progressDispatcher = new ProgressDispatcher();

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
		r.run(task, progressDispatcher);
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
	
	public void enqueueServerTask() {
		MinaMod.getMinaMod().getTickHandler().addServerTask(this);
	}
	
	public Task addListener(ITaskListener l) {
		this.listeners.add(l);
		return this;
	}
	
	public static interface ITaskRunnable{
		void run(Task task, ProgressDispatcher progressDispatcher);
	}
	
	public static interface ITaskListener{
		void onPublish(int id, Object data);
	}
	
	public class ProgressDispatcher{
		private ProgressDispatcher() {}
		
		public void publish(int id, Object data) {
			for(ITaskListener l : listeners) {
				l.onPublish(id, data);
			}
		}
	}
}
