package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.AbstractLuna;
import edu.kit.ipd.parse.luna.agent.AbstractAgent;
import edu.kit.ipd.parse.luna.agent.AbstractWatchdog;
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
public class TimeoutWatchdog extends AbstractWatchdog {

	private static final String ID = "timeoutWatchdog";
	private static final Logger logger = LoggerFactory.getLogger(TimeoutWatchdog.class);
	private static final Properties wdProps = ConfigManager.getConfiguration(TimeoutWatchdog.class);
	private static final Properties lunaProps = ConfigManager.getConfiguration(AbstractLuna.class);

	private static final String PROP_TIMEOUT_THRESHOLD = "TIMEOUT_THRESHOLD";
	private static final String PROP_TERM_SIGNAL_TYPE = "TERM_SIGNAL_TYPE";

	private long currTime = -1;
	private long firstTime = -1;

	private long to_threshold;
	private String termSignalType;

	public TimeoutWatchdog() {
		setId(ID);
	}

	/**
	 * Initializes the agent. Simply sets the ID and fetches the timeout threshold
	 * from the config file.
	 */
	@Override
	public void init() {
		to_threshold = Long.parseLong(wdProps.getProperty(PROP_TIMEOUT_THRESHOLD));
		termSignalType = lunaProps.getProperty(PROP_TERM_SIGNAL_TYPE);
	}

	/**
	 * Executes the agent and checks whether the timeout threshold is exceeded. If
	 * so, a term type is added to the graph to signal termination.
	 */
	@Override
	protected void exec() {

		currTime = System.currentTimeMillis();
		if (firstTime == -1) {
			firstTime = currTime;
		}

		if (checkTimeout()) {
			graph.createNodeType(termSignalType);
		}
	}

	private boolean checkTimeout() {
		if (currTime - firstTime >= to_threshold) {
			return true;
		} else {
			return false;
		}
	}

}
