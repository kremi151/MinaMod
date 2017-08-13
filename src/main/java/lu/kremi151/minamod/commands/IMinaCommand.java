package lu.kremi151.minamod.commands;

import java.util.Optional;

public interface IMinaCommand {

	String accumulativeUsageName();
	String getDescription();
	Optional<String> getPermissionNode();
	
}
