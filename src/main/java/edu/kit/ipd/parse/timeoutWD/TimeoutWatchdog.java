package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.agent.AbstractAgent;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Sebastian Weigelt
 *
 */

@MetaInfServices(AbstractAgent.class)
public class TimeoutWatchdog extends AbstractAgent {

	private static final String ID = "timeoutWatchdog";
	private static final Logger logger = LoggerFactory.getLogger(TimeoutWatchdog.class);
	private static final Properties wdProps = ConfigManager.getConfiguration(TimeoutWatchdog.class);

	@Override
	public void init() {
		setId(ID);
	}

	@Override
	protected void exec() {

	}
}
