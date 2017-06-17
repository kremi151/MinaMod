package lu.kremi151.minamod.capabilities.stats.datasync;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public class StatDataSerializers {

	public static final DataSerializer<StatData> STAT_DATA = new DataSerializer<StatData>()
    {
        public void write(PacketBuffer buf, StatData value)
        {
            buf.writeByte((byte)(value.getActual() & 0xFF));
            buf.writeByte((byte)(value.getTraining() & 0xFF));
        }
        public StatData read(PacketBuffer buf) throws IOException
        {
            StatData data = new StatData();
            data.setActual(buf.readByte() & 0xFF);
            data.setTraining(buf.readByte() & 0xFF);
            return data;
        }
        public DataParameter<StatData> createKey(int id)
        {
            return new DataParameter(id, this);
        }
    };
	
}
