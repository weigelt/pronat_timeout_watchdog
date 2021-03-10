package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.ILuna;
import edu.kit.ipd.parse.luna.Luna;
import edu.kit.ipd.parse.luna.event.AbortEvent;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.ParseGraph;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sebastian Weigelt
 * 
 */
public class TimeoutWatchdogTest {

	private static final Properties wdProps = ConfigManager.getConfiguration(TimeoutWatchdog.class);

	private IGraph graph;
	private ILuna luna;
	private TimeoutWatchdog towd;

	@Before
	public void beforeTest() {
		graph = new ParseGraph();
		luna = Luna.getInstance();
		luna.updateGraph(graph);
		towd = new TimeoutWatchdog();
	}

	/**
	 * Tests whether the agent initiates the termination after the timeout exceeded.
	 */
	@Test
	public void testExec5000ms() {

		wdProps.setProperty("TIMEOUT_THRESHOLD", "5000");
		luna.register(towd);
		towd.initAbstract(luna);
		long start = System.currentTimeMillis();
		long curr = start;
		while (curr - start < 5001) {
			towd.setGraph(luna.getMainGraph());
			towd.exec();
			curr = System.currentTimeMillis();
		}
		assertTrue(towd.getCurrEvent() instanceof AbortEvent);
	}

	/**
	 * Tests whether the agent doesn't initiate the termination before the timeout
	 * is exceeded.
	 */
	@Test
	public void testExec5000msFail() {

		wdProps.setProperty("TIMEOUT_THRESHOLD", "5000");
		luna.register(towd);
		towd.initAbstract(luna);
		long start = System.currentTimeMillis();
		long curr = start;
		while (curr - start < 4000) {
			towd.setGraph(luna.getMainGraph());
			towd.exec();
			curr = System.currentTimeMillis();
		}
		assertFalse(towd.getCurrEvent() instanceof AbortEvent);
	}
}