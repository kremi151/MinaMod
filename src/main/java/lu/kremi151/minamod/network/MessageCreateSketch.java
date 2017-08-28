package lu.kremi151.minamod.network;

import java.util.Optional;

import io.netty.buffer.ByteBuf;
import lu.kremi151.minamod.MinaItems;
import lu.kremi151.minamod.capabilities.sketch.ISketch;
import lu.kremi151.minamod.network.abstracts.AbstractServerMessageHandler;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class MessageCreateSketch implements IMessage{

	@Override
	public void fromBytes(ByteBuf buf) {}

	@Override
	public void toBytes(ByteBuf buf) {}
	
	public static class Handler extends AbstractServerMessageHandler<MessageCreateSketch>{
		
		private Optional<IRecipe> findMatchingRecipe(InventoryCrafting inv, World worldIn) {
			CraftingManager cm = CraftingManager.getInstance();
			for(IRecipe recipe : cm.getRecipeList()) {
				if(recipe.matches(inv, worldIn)) {
					return Optional.of(recipe);
				}
			}
			return Optional.empty();
		}

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageCreateSketch message, MessageContext ctx) {
			if(player.openContainer instanceof ContainerWorkbench) {
				ContainerWorkbench crafting = (ContainerWorkbench) player.openContainer;
				if(!crafting.craftResult.isEmpty() && player.inventory.hasItemStack(new ItemStack(Items.PAPER))) {
					IRecipe recipe = findMatchingRecipe(crafting.craftMatrix, player.world).orElse(null);
					if(recipe != null && MinaUtils.consumeInventoryItems(player.inventory, Items.PAPER, 1)) {
						ItemStack sketch = new ItemStack(MinaItems.SKETCH);
						ISketch cap = sketch.getCapability(ISketch.CAPABILITY, null);
						
						NonNullList<ItemStack> order = NonNullList.withSize(9, ItemStack.EMPTY);
						for(int i = 0 ; i < crafting.craftMatrix.getSizeInventory() ; i++) {
							order.set(i, crafting.craftMatrix.getStackInSlot(i));
						}
						cap.setOrder(order);
						cap.setCachedRecipe(recipe);
						
						sketch = ItemHandlerHelper.insertItem(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), sketch, false);
						if(!sketch.isEmpty()) {
							player.dropItem(sketch, true);
						}
					}
				}
			}
			return null;
		}
		
	}

}
