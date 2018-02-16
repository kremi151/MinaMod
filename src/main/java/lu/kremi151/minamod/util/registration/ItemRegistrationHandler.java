package lu.kremi151.minamod.util.registration;

import net.minecraft.item.Item;

public abstract class ItemRegistrationHandler extends CommonRegistrationHandler<Item>{
	
	protected boolean vanillaRegistration = false;

	public ItemRegistrationHandler(String name, Item obj) {
		super(name, obj);
	}
	
	public void submitSimple() {
		this.vanillaRegistration = true;
		submit();
	}

	public ItemRegistrationHandler autoname() {
		this.obj.setUnlocalizedName(name);
		return this;
	}
}
