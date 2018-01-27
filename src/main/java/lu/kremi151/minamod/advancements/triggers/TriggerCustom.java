package lu.kremi151.minamod.advancements.triggers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

public class TriggerCustom implements ICriterionTrigger {

	private final ResourceLocation id;
	private final HashMap<PlayerAdvancements, Listeners> listeners = new HashMap<>();

	public TriggerCustom(String domain, String name) {
		super();
		id = new ResourceLocation(domain, name);
	}

	public TriggerCustom(String parString) {
		super();
		id = new ResourceLocation(parString);
	}

	public TriggerCustom(ResourceLocation id) {
		super();
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn, Listener listener) {
		Listeners listeners = this.listeners.get(playerAdvancementsIn);
		if(listeners == null) {
			listeners = new Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, listeners);
		}
		listeners.listeners.add(listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn, Listener listener) {
		Listeners listeners = this.listeners.get(playerAdvancementsIn);
		if(listeners != null) {
			listeners.listeners.remove(listener);
		}
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		this.listeners.remove(playerAdvancementsIn);
	}

	@Override
	public ICriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new Instance(id);
	}
	
	public void trigger(EntityPlayerMP parPlayer)
    {
        Listeners listeners = this.listeners.get(parPlayer.getAdvancements());

        if (listeners != null)
        {
        	listeners.trigger(parPlayer);
        }
    }
	
	public static class Instance extends AbstractCriterionInstance
    {
        
        /**
         * Instantiates a new instance.
         */
        public Instance(ResourceLocation parID)
        {
            super(parID);
        }

        /**
         * Test.
         *
         * @return true, if successful
         */
        public boolean test()
        {
            return true;
        }
    }
	
	private class Listeners{
		private final PlayerAdvancements playerAdvancements;
		private final HashSet<Listener<Instance>> listeners = new HashSet<>();
		
		private Listeners(PlayerAdvancements playerAdvancements) {
			this.playerAdvancements = playerAdvancements;
		}
		
		public void trigger(EntityPlayerMP player)
        {
            List<ICriterionTrigger.Listener<Instance>> list = null;

            for (ICriterionTrigger.Listener<Instance> listener : this.listeners)
            {
                if (listener.getCriterionInstance().test())
                {
                    if (list == null)
                    {
                        list = new LinkedList<>();
                    }

                    list.add(listener);
                }
            }

            if (list != null)
            {
                for (ICriterionTrigger.Listener listener1 : list)
                {
                    listener1.grantCriterion(this.playerAdvancements);
                }
            }
        }
	}

}
