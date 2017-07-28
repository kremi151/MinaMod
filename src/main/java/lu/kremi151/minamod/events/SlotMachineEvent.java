package lu.kremi151.minamod.events;

import javax.annotation.Nonnull;

import lu.kremi151.minamod.util.slotmachine.SlotMachineEconomyHandler;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SlotMachineEvent extends Event{
	
	SlotMachineEvent() {
	}
	
	public static class SetEconomyHandler extends SlotMachineEvent{
		
		private final SlotMachineEconomyHandler originalHandler;
		private SlotMachineEconomyHandler newHandler;

		public SetEconomyHandler(SlotMachineEconomyHandler originalHandler) {
			super();
			this.originalHandler = originalHandler;
			this.newHandler = originalHandler;
		}
		
		public SlotMachineEconomyHandler getOriginalHandler() {
			return this.originalHandler;
		}
		
		public void setNewHandler(@Nonnull SlotMachineEconomyHandler handler) {
			if(handler == null)throw new NullPointerException();
			this.newHandler = handler;
		}
		
		public SlotMachineEconomyHandler getNewHandler() {
			return this.newHandler;
		}
	}

}
