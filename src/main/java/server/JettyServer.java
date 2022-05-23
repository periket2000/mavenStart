package server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.xml.XmlConfiguration;

import servlets.BlockingServlet;

public class JettyServer {
    private Server server;

    public void start() throws Exception {
        server = new Server();

        final Resource xml = Resource.newResource(System.getProperty("jetty.xml.config", "C:/Users/blanc/Devel/tmp/test/src/main/resources/jetty.xml"));
        final XmlConfiguration config = new XmlConfiguration(xml);
        config.configure(server);

/*        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[] {connector});*/
        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);
        servletHandler.addServletWithMapping(BlockingServlet.class, "/status");
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}