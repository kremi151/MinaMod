package lu.kremi151.minamod.client.overlay;

import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public abstract class Overlayable {
	
	private long id;
	private long expiration = 0;
	private boolean visible = true;
	private boolean forever = false;
	
	public Overlayable(long id){
		this.id = id;
	}

	public abstract void draw(Gui gui, int supposedX, int supposedY);
	public abstract int getHeight();
	
	public final Overlayable setVisible(boolean v){ // NO_UCD (unused code)
		this.visible = v;
		return this;
	}
	
	public final boolean isVisible(){
		return visible;
	}
	
	public final long getId(){
		return id;
	}
	
	public final Overlayable setExpiration(long timestamp){
		this.expiration = timestamp;
		return this;
	}
	
	public final boolean tick(){
		return canStay();
	}
	
	public final boolean canStay(){
		return forever || (expiration - System.currentTimeMillis()) <= 0;
	}
	
	public final Overlayable forever(){
		this.forever = true;
		return this;
	}
	
	public final boolean willStayForever(){
		return forever;
	}
}
