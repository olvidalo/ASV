package nl.mpi.metadatabrowser.wicket;

import org.apache.wicket.util.time.Duration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class Start {

    public static void main(String[] args) throws Exception {
	int timeout = (int) Duration.ONE_HOUR.getMilliseconds();

	Server server = new Server();
	SocketConnector connector = new SocketConnector();

	// Set some timeout options to make debugging easier.
	connector.setMaxIdleTime(timeout);
	connector.setSoLingerTime(-1);
	connector.setPort(8090);
	server.addConnector(connector);

	WebAppContext bb = new WebAppContext();
	bb.setServer(server);
	bb.setContextPath("/");
	bb.setWar("src/main/webapp");
	// Mock spring beans are in 'mock' profile
	bb.setInitParameter("spring.profiles.active", "mock");

	// START JMX SERVER
	// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
	// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
	// server.getContainer().addEventListener(mBeanContainer);
	// mBeanContainer.start();

	server.setHandler(bb);

	try {
	    System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
	    server.start();
	    System.in.read();
	    System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
	    server.stop();
	    server.join();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}
