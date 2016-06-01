package me.filippov.nat.event.logger

import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.util.log.StdErrLog
import org.eclipse.jetty.webapp.WebAppContext
import org.glassfish.jersey.servlet.ServletContainer
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


fun main(args: Array<String>) {
    val group = NioEventLoopGroup();

    var defaultLogPort = 514
    var defaultWebPort = 8888

    if (args.size == 1 && (args[0] == "--help" || args[0] == "-help")) {
        println("Usage: <Program> [logPort] [webPort]")
        return
    }

    if (args.size == 2) {
        defaultLogPort = try {
            Integer.parseInt(args[0])
        } catch(ex: NumberFormatException) {
            514
        }
        defaultWebPort = try {
            Integer.parseInt(args[1])
        } catch(ex: NumberFormatException) {
            8888
        }
    }

    try {
        val b = Bootstrap();
        b.group(group)
                .channel(NioDatagramChannel::class.java)
                .handler(LogChannelInitializer());
        if (!Files.exists(Paths.get("logs"))) {
            Files.createDirectory(Paths.get("logs"))
        }
        val p = Properties();
        p.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        StdErrLog.setProperties(p);

        val server = Server(defaultWebPort)

        val ctx = WebAppContext(LogResource::class.java.classLoader.getResource("webapp/").toExternalForm(), "/")
        ctx.welcomeFiles = arrayOf("index.html")

        server.handler = ctx

        val jerseyServlet = ctx.addServlet(ServletContainer::class.java, "/api/*");
        jerseyServlet.initOrder = 0;
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "me.filippov.nat.event.logger");

        val staticServlet = ctx.addServlet(DefaultServlet::class.java, "/*");
        staticServlet.initOrder = 1;

        server.start()

        b.bind(defaultLogPort).sync().channel().closeFuture().await();
    } finally {
        group.shutdownGracefully();
    }
}
