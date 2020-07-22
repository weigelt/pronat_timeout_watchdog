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
		assertTrue(graph.hasNodeType("TERM_SIGNAL_TYPE"));
	}

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
		assertFalse(graph.hasNodeType("TERM_SIGNAL_TYPE"));
	}
}