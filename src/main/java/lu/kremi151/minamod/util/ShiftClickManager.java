package lu.kremi151.minamod.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import scala.collection.generic.BitOperations.Int;

public class ShiftClickManager {
	
	private static final Method methodMergeItemStack;
	private IHandler handlers[];
	
	static{
		methodMergeItemStack = ReflectionHelper.findMethod(Container.class, "mergeItemStack", "func_75135_a", ItemStack.class, int.class, int.class, boolean.class);
	}
	
	private ShiftClickManager(){
	}

	public ItemStack handle(Container container, EntityPlayer player, int slot){
		try{
			Slot _slot = container.getSlot(slot);
			ItemStack stack = ItemStack.EMPTY;
			if(_slot != null && _slot.getHasStack()){
				stack = _slot.getStack();
				ItemStack copy = stack.copy();
				
				boolean handled = false;
				for(IHandler handler : handlers){
					if(handler.accepts(slot)){
						if(handler.handle(container, stack) == ItemStack.EMPTY){
							return ItemStack.EMPTY;
						}
						handled = true;
						break;
					}
				}
				
				if(!handled){
					return ItemStack.EMPTY;
				}
				
				if(!ItemStack.areItemStacksEqual(stack, copy)){
					_slot.onSlotChange(stack, copy);
					_slot.onTake(player, stack);
				}
				return stack;
			}else{
				return ItemStack.EMPTY;
			}
		}catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Reflection error");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("Reflection error");
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("Reflection error");
		}
	}
	
	public static Builder builder(){
		return new Builder();
	}
	
	private static interface IHandler{
		
		ItemStack handle(Container container, ItemStack stack) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
		boolean accepts(int slot);
		
	}
	
	private static class Handler implements IHandler{
		private final int srcMinIncl, srcMaxExcl, destMinIncl, destMaxExcl;
		private final boolean inverse;
		private final Predicate<ItemStack> predicate;
		
		private Handler(int srcMinIncl, int srcMaxExcl, int destMinIncl, int destMaxExcl, Predicate<ItemStack> predicate){
			this(srcMinIncl, srcMaxExcl, destMinIncl, destMaxExcl, true, predicate);
		}
		
		public Handler(int srcMinIncl, int srcMaxExcl, int destMinIncl, int destMaxExcl, boolean inverse, Predicate<ItemStack> predicate){
			this.srcMinIncl = srcMinIncl;
			this.srcMaxExcl = srcMaxExcl;
			this.destMinIncl = destMinIncl;
			this.destMaxExcl = destMaxExcl;
			this.inverse = inverse;
			this.predicate = predicate;
		}

		@Override
		public ItemStack handle(Container container, ItemStack stack) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
			boolean result = predicate.test(stack) && (Boolean)methodMergeItemStack.invoke(container, stack, destMinIncl, destMaxExcl, inverse);
			if(result){
				return stack;
			}else{
				return ItemStack.EMPTY;
			}
		}

		@Override
		public boolean accepts(int slot) {
			return srcMinIncl <= slot && slot < srcMaxExcl;
		}
	}
	
	private static class DefaultHandler extends Handler{
		
		public DefaultHandler(int srcMinIncl, int srcMaxExcl, int destMinIncl, int destMaxExcl, boolean inverse) {
			super(srcMinIncl, srcMaxExcl, destMinIncl, destMaxExcl, inverse, stack -> true);
		}

		@Override
		public boolean accepts(int slot) {
			return true;
		}
		
	}
	
	private static class EmptyHandler implements IHandler{

		@Override
		public ItemStack handle(Container container, ItemStack stack)
				throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			return ItemStack.EMPTY;
		}

		@Override
		public boolean accepts(int slot) {
			return true;
		}
		
	}
	
	public static class Builder{
		
		private ArrayList<IHandler> handlers = new ArrayList<IHandler>();
		private IHandler defaultHandler = new EmptyHandler();
		
		private Builder(){
		}
		
		public Builder addTransfer(int fromMinIncl, int fromMaxExcl, int toMinIncl, int toMaxExcl, boolean reverse, Predicate<ItemStack> predicate){
			handlers.add(new Handler(fromMinIncl, fromMaxExcl, toMinIncl, toMaxExcl, reverse, predicate));
			return this;
		}
		
		public Builder addTransfer(int fromMinIncl, int fromMaxExcl, int toMinIncl, int toMaxExcl, boolean reverse){
			return addTransfer(fromMinIncl, fromMaxExcl, toMinIncl, toMaxExcl, reverse, stack -> true);
		}
		
		public Builder addTransfer(int fromMinIncl, int fromMaxExcl, int toMinIncl, int toMaxExcl, Predicate<ItemStack> predicate){
			handlers.add(new Handler(fromMinIncl, fromMaxExcl, toMinIncl, toMaxExcl, true, predicate));
			return this;
		}
		
		public Builder addTransfer(int fromMinIncl, int fromMaxExcl, int toMinIncl, int toMaxExcl){
			return addTransfer(fromMinIncl, fromMaxExcl, toMinIncl, toMaxExcl, true, stack -> true);
		}
		
		public Builder defaultTransfer(int toMinIncl, int toMaxExcl, boolean reverse){
			defaultHandler = new DefaultHandler(0, Integer.MAX_VALUE, toMinIncl, toMaxExcl, reverse);
			return this;
		}
		
		public Builder defaultTransfer(int toMinIncl, int toMaxExcl){
			defaultHandler = new DefaultHandler(0, Integer.MAX_VALUE, toMinIncl, toMaxExcl, true);
			return this;
		}
		
		public ShiftClickManager build(){
			ShiftClickManager sch = new ShiftClickManager();
			sch.handlers = handlers.toArray(new IHandler[handlers.size() + 1]);
			if(defaultHandler == null){
				throw new NullPointerException();
			}
			sch.handlers[sch.handlers.length - 1] = defaultHandler;
			return sch;
		}
		
	}
}
