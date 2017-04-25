package lu.kremi151.minamod.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemColoredWrittenBook extends ItemWrittenBook{

	public int getBookColor(ItemStack is){
		if(is.hasTagCompound() && is.getTagCompound().hasKey("color", 3)){
			return is.getTagCompound().getInteger("color");
		}
		return MinaUtils.COLOR_WHITE;
	}
	
	public void setBookColor(ItemStack is, int color){
		NBTTagCompound tag;
		if(!is.hasTagCompound()){
			tag = new NBTTagCompound();
			is.setTagCompound(tag);
		}else{
			tag = is.getTagCompound();
		}
		tag.setInteger("color", color);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote)
        {
			Method method = ReflectionHelper.findMethod(ItemWrittenBook.class, this, new String[] {
                    "resolveContents", "func_179229_a" }, ItemStack.class, EntityPlayer.class);
			try {
				method.invoke(this, itemStackIn, playerIn);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
        }

        MinaMod.getProxy().openBook(itemStackIn);
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }
}
