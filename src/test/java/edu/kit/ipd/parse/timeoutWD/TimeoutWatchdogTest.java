package edu.kit.ipd.parse.timeoutWD;

import edu.kit.ipd.parse.luna.Luna;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.ParseGraph;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Properties;

@RunWith(JUnit4.class)
public class TimeoutWatchdogTest extends TestCase {

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