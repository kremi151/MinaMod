package lu.kremi151.minamod.entity.ai;

import java.util.ArrayList;
import java.util.List;

import lu.kremi151.minamod.entity.EntityIceGolhem;
import lu.kremi151.minamod.worlddata.MinaWorld;
import lu.kremi151.minamod.worlddata.data.FrostTempleMeta;
import lu.kremi151.minamod.worlddata.data.FrostTemplePosition;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAILookToNearestFrostTemple extends EntityAIBase{
	
	EntityIceGolhem e;
    private int lookTime = 0;
	BlockPos lp = null;
	
	public EntityAILookToNearestFrostTemple(EntityIceGolhem e){
		this.e = e;
		this.setMutexBits(7);
	}

	@Override
	public boolean shouldExecute() {
		FrostTempleMeta ftm = MinaWorld.forWorld(this.e.world).getFrostTempleMeta();
		return (ftm.getBlackTempleCount() + ftm.getWhiteTempleCount()) > 0 && this.e.getRNG().nextFloat() <= 0.15f;
	}
	
	@Override
    public void startExecuting()
    {
		this.lp = null;
        this.lookTime = 40 + this.e.getRNG().nextInt(40);
		FrostTempleMeta ftm = MinaWorld.forWorld(this.e.world).getFrostTempleMeta();
		List<FrostTemplePosition> temples = new ArrayList<FrostTemplePosition>();
		temples.addAll(ftm.getBlackTemples());
		temples.addAll(ftm.getWhiteTemples());
		BlockPos tpos = null;
		double dist = -1d;
		for(int i = 0 ; i < temples.size() ; i++){
			FrostTemplePosition ftp = temples.get(i);
			double px = Math.abs((double)ftp.getAltarPosition().getX() - e.posX);
			double py = Math.abs((double)ftp.getAltarPosition().getY() - e.posY);
			double pz = Math.abs((double)ftp.getAltarPosition().getZ() - e.posZ);
			double tdist = Math.sqrt(Math.pow(px, 2) + Math.pow(py, 2) + Math.pow(pz, 2));
			if(dist < 0){
				dist = tdist;
				tpos = ftp.getAltarPosition();
			}else if(tdist < dist){
				dist = tdist;
				tpos = ftp.getAltarPosition();
			}
		}
		this.lp = tpos;
		this.e.effectHealing();
    }
	
	@Override
    public boolean shouldContinueExecuting(){
		return this.lookTime > 0 && lp != null;
	}

	@Override
    public void updateTask()
    {
        if(lp!=null){
        	this.e.getLookHelper().setLookPosition((double)lp.getX() + 0.5d, (double)lp.getY() + 0.5d, (double)lp.getZ() + 0.5d, 10.0F, (float)this.e.getVerticalFaceSpeed());
            --this.lookTime;
        }else{
        	this.lookTime = 0;
        }
    }

}
