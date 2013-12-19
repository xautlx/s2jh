package lab.s2jh;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


/**
 * Main Class for standalone running.
 * 
 */
public class MainExecutor {

    public static void main(String[] args) throws Exception {
        createStartServer(MainExecutor.class);
    }

    protected static void createStartServer(Class<?> classloader) {

        String context = System.getProperty("context", "s2jh");
        int port = Integer.getInteger("port", 8080);

        //use Eclipse JDT compiler
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "true");

        Server server = new Server(port);
        server.setStopAtShutdown(true);

        ProtectionDomain protectionDomain = classloader.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();

        String warFile = location.toExternalForm();
        WebAppContext webAppContext = new WebAppContext(warFile, "/" + context);
        webAppContext.setServer(server);

        //设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
        String currentDir = new File(location.getPath()).getParent();
        File workDir = new File(currentDir, "work");
        webAppContext.setTempDirectory(workDir);

        server.setHandler(webAppContext);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }

    }
}
