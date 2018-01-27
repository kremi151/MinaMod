package lu.kremi151.minamod.worlddata.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lu.kremi151.minamod.enums.EnumFrostTempleType;
import lu.kremi151.minamod.util.MinaUtils;
import lu.kremi151.minamod.worlddata.MinaWorld;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class FrostTempleMeta {
	
	private final MinaWorld parent;

	private int black_frost_temple_left = 1;
	private int white_frost_temple_left = 1;
	private boolean limited = true;
	private double distanceFromSpawn = 1000.0;
	private final ArrayList<FrostTemplePosition> whiteTemples = new ArrayList<FrostTemplePosition>();
	private final ArrayList<FrostTemplePosition> blackTemples = new ArrayList<FrostTemplePosition>();
	
	public FrostTempleMeta(MinaWorld parent){
		this.parent = parent;
	}
	
	public void writeToNBT(NBTTagCompound nbt){
		nbt.setBoolean("limited", limited);
		nbt.setInteger("whiteTemplesLeft", white_frost_temple_left);
		nbt.setInteger("blackTemplesLeft", black_frost_temple_left);
		nbt.setDouble("distanceFromSpawn", distanceFromSpawn);
		List<FrostTemplePosition> wtul = Collections.unmodifiableList(whiteTemples);
		NBTTagList wtl = new NBTTagList();
		if(wtul.size() > 0){
			for(int i = 0 ; i < wtul.size() ; i++){
				NBTTagCompound ftpnbt = new NBTTagCompound();
				wtul.get(i).writeToNBT(ftpnbt);
				wtl.appendTag(ftpnbt);
			}
		}
		List<FrostTemplePosition> btul = Collections.unmodifiableList(blackTemples);
		NBTTagList btl = new NBTTagList();
		if(btul.size() > 0){
			for(int i = 0 ; i < btul.size() ; i++){
				NBTTagCompound ftpnbt = new NBTTagCompound();
				btul.get(i).writeToNBT(ftpnbt);
				btl.appendTag(ftpnbt);
			}
		}
		nbt.setTag("whiteTemples", wtl);
		nbt.setTag("blackTemples", btl);
	}
	
	public void readFromNBT(NBTTagCompound nbt){
		this.whiteTemples.clear();
		this.blackTemples.clear();
		if(nbt.hasKey("limited", 99)){
			this.limited = nbt.getBoolean("limited");
		}
		if(nbt.hasKey("whiteTemplesLeft", 3)){
			this.white_frost_temple_left = nbt.getInteger("whiteTemplesLeft");
		}
		if(nbt.hasKey("blackTemplesLeft", 3)){
			this.black_frost_temple_left = nbt.getInteger("blackTemplesLeft");
		}
		if(nbt.hasKey("whiteTemples", 9)){
			NBTTagList wtl = nbt.getTagList("whiteTemples", 10);
			for(int i = 0 ; i < wtl.tagCount() ; i++){
				NBTTagCompound wnbt = wtl.getCompoundTagAt(i);
				Optional<FrostTemplePosition> ftp = FrostTemplePosition.createFromNBT(this, wnbt);
				if(ftp.isPresent()){
					this.whiteTemples.add(ftp.get());
				}
			}
		}
		if(nbt.hasKey("blackTemples", 9)){
			NBTTagList btl = nbt.getTagList("blackTemples", 10);
			for(int i = 0 ; i < btl.tagCount() ; i++){
				NBTTagCompound bnbt = btl.getCompoundTagAt(i);
				Optional<FrostTemplePosition> ftp = FrostTemplePosition.createFromNBT(this, bnbt);
				if(ftp.isPresent()){
					this.blackTemples.add(ftp.get());
				}
			}
		}
		if(nbt.hasKey("distanceFromSpawn", 99)){
			this.distanceFromSpawn = nbt.getDouble("distanceFromSpawn");
		}
	}
	
	public int getBlackFrostTempleLeft(){
		return black_frost_temple_left;
	}
	
	public int getBlackTempleCount(){
		return this.blackTemples.size();
	}
	
	public double getDistanceFromSpawn(){
		return this.distanceFromSpawn;
	}
	
	public void setBlackFrostTempleLeft(int v){
		this.black_frost_temple_left = v;
		parent.setDirty(true);
	}
	
	public int getWhiteFrostTempleLeft(){
		return white_frost_temple_left;
	}
	
	public int getWhiteTempleCount(){
		return this.whiteTemples.size();
	}
	
	public void setWhiteFrostTempleLeft(int v){
		this.white_frost_temple_left = v;
		parent.setDirty(true);
	}
	
	MinaWorld getExtent(){
		return parent;
	}
	
	public boolean isLimited(){
		return limited;
	}
	
	public List<FrostTemplePosition> getWhiteTemples(){
		return Collections.unmodifiableList(this.whiteTemples);
	}
	
	public List<FrostTemplePosition> getBlackTemples(){
		return Collections.unmodifiableList(this.blackTemples);
	}
	
	public void addFrostTempleToList(EnumFrostTempleType type, BlockPos altar_pos){
		switch(type){
		case BLACK:
			this.blackTemples.add(new FrostTemplePosition(this, altar_pos, type));
			break;
		case WHITE:
			this.whiteTemples.add(new FrostTemplePosition(this, altar_pos, type));
			break;
		}
		parent.markDirty();
	}
	
	public FrostTemplePosition getNearest(double x, double y, double z){
		FrostTemplePosition last = null;
		for(FrostTemplePosition p : whiteTemples){
			if(last == null){
				last = p;
			}else if(MinaUtils.getDistance(x, y, z, p.getAltarPosition().getX(), p.getAltarPosition().getY(), p.getAltarPosition().getZ()) < MinaUtils.getDistance(x, y, z, last.getAltarPosition().getX(), last.getAltarPosition().getY(), last.getAltarPosition().getZ())){
				last = p;
			}
		}
		for(FrostTemplePosition p : blackTemples){
			if(last == null){
				last = p;
			}else if(MinaUtils.getDistance(x, y, z, p.getAltarPosition().getX(), p.getAltarPosition().getY(), p.getAltarPosition().getZ()) < MinaUtils.getDistance(x, y, z, last.getAltarPosition().getX(), last.getAltarPosition().getY(), last.getAltarPosition().getZ())){
				last = p;
			}
		}
		return last;
	}
	
}
