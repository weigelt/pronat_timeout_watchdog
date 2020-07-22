package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.Luna;
import edu.kit.ipd.parse.luna.agent.AbstractAgent;
import edu.kit.ipd.parse.luna.graph.IArcType;
import edu.kit.ipd.parse.luna.graph.INodeType;
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
	private static final Properties lunaProps = ConfigManager.getConfiguration(Luna.class);

	private static final String PROP_TIMEOUT_THRESHOLD = "TIMEOUT_THRESHOLD";
	private static final String PROP_TERM_SIGNAL_TYPE = "TERM_SIGNAL_TYPE";

	private long currTime = -1;
	private long lastTime = -1;

	private long to_threshold;

	@Override
	public void init() {
		setId(ID);
		to_threshold = Long.parseLong(wdProps.getProperty(PROP_TIMEOUT_THRESHOLD));
	}

	@Override
	protected void exec() {

		currTime = System.currentTimeMillis();

		if (!(lastTime == -1) && checkTimeout()) {
			graph.createNodeType(PROP_TERM_SIGNAL_TYPE);
		}

		lastTime = currTime;

	}

	private boolean checkTimeout() {
		if (currTime - lastTime >= to_threshold) {
			return true;
		} else {
			return false;
		}
	}

}
