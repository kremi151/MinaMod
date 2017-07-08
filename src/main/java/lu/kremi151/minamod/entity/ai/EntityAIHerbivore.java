package lu.kremi151.minamod.entity.ai;

import lu.kremi151.minamod.MinaMod;
import lu.kremi151.minamod.interfaces.IEntityAIHerbivoreListener;
import lu.kremi151.minamod.util.MinaUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIHerbivore extends EntityAIBase{
	
	EntityLiving entity;
	BlockPos target = null;
	IEntityAIHerbivoreListener l = null;
	int ticksUntilNextTry = 300;
	
	public EntityAIHerbivore(EntityLiving entity){
		this.entity = entity;
		this.setMutexBits(1);
	}
	
	private void resetTries(){
		ticksUntilNextTry = 250;
	}

	@Override
	public boolean shouldExecute() {
		boolean b1 = ticksUntilNextTry <= 0 && entity.world.getGameRules().getBoolean("mobGriefing");
		boolean flag;
		if(l!=null){
			flag = l.canEat() && b1;
		}else{
			flag = b1;
		}
		ticksUntilNextTry--;
		if(entity.getRNG().nextInt(10) == 0 && flag){
			resetTries();
			int tries = 4;
			while((tries--) > 0){
				int x = (int)entity.posX - 2 + entity.getRNG().nextInt(5);
				int z = (int)entity.posZ - 2 + entity.getRNG().nextInt(5);
				int y = MinaUtils.getHeightValue(entity.world, x, z) - 1;
				BlockPos pos = new BlockPos(x,y,z);
				IBlockState state = entity.world.getBlockState(pos);
				if(isTargetableBlock(state) && Math.abs((int)(entity.posY - (double)y)) <= 1){
					this.target = pos;
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void startExecuting(){
		this.entity.getMoveHelper().setMoveTo(target.getX(), target.getY(), target.getZ(), 1.1f);
	}
	
	@Override
    public boolean shouldContinueExecuting(){
		return target != null;
	}

	@Override
    public void updateTask()
    {
		if(target != null){
			int ex = (int)entity.posX;
			int ey = (int)entity.posY;
			int ez = (int)entity.posZ;
			if(ex >= target.getX() - 1 && ex <= target.getX() + 1
					&& ey >= target.getY() - 1 && ey <= target.getY() + 1 
					&& ez >= target.getZ() - 1 && ez >= target.getZ() - 1){
				entity.world.destroyBlock(target, false);
				if(l!=null)l.onBlockEaten(entity.world, target);
				target = null;
			}
		}
    }
	
	public EntityAIHerbivore setListener(IEntityAIHerbivoreListener l){
		this.l = l;
		return this;
	}
	
	private boolean isTargetableBlock(IBlockState state){
		return state.getBlock() instanceof BlockLeaves || state.getBlock() instanceof net.minecraftforge.common.IPlantable;
	}

}
