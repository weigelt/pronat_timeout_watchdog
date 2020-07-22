package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.Luna;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.ParseGraph;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import org.junit.Before;
import org.junit.BeforeClass;
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
	private static final Properties lunaProps = ConfigManager.getConfiguration(Luna.class);

	private IGraph graph;
	private TimeoutWatchdog towd;

	@BeforeClass
	public static void SetUp() {
		lunaProps.setProperty("TERM_SIGNAL_TYPE", "terminate");
	}

	@Before
	public void beforeTest() {
		graph = new ParseGraph();
		towd = new TimeoutWatchdog();
		towd.init();
	}

	/**
	 * Tests whether the agent has added the term node type after the timeout
	 * exceeded.
	 */
	@Test
	public void testExec5000ms() {

		wdProps.setProperty("TIMEOUT_THRESHOLD", "5000");
		towd.setGraph(graph);
		towd.exec();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		towd.setGraph(graph);
		towd.exec();
		assertTrue(graph.hasNodeType("terminate"));
	}

	/**
	 * Tests whether the agent doesn't add the term node before the timeout is
	 * exceeded.
	 */
	@Test
	public void testExec5000msFail() {

		wdProps.setProperty("TIMEOUT_THRESHOLD", "5000");
		towd.setGraph(graph);
		towd.exec();
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		towd.setGraph(graph);
		towd.exec();
		assertFalse(graph.hasNodeType("terminate"));
	}

	/**
	 * Tests whether the agent has added the term node type after 5 runs and the
	 * timeout exceeded.
	 */
	@Test
	public void testMultExec5000ms() {

		wdProps.setProperty("TIMEOUT_THRESHOLD", "5000");
		for (int i = 0; i < 5; i++) {
			towd.setGraph(graph);
			towd.exec();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		towd.setGraph(graph);
		towd.exec();
		assertTrue(graph.hasNodeType("terminate"));
	}
}