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


fun main(args : Array<String>) {
    val group = NioEventLoopGroup();

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

        val server = Server(8888)

        val ctx = WebAppContext(LogResource::class.java.classLoader.getResource("webapp/").toExternalForm(), "/")
        ctx.welcomeFiles = arrayOf("index.html")

        server.handler = ctx

        val jerseyServlet = ctx.addServlet(ServletContainer::class.java, "/api/*");
        jerseyServlet.initOrder = 0;
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages","me.filippov.nat.event.logger");

        val staticServlet = ctx.addServlet(DefaultServlet::class.java,"/*");
        staticServlet.initOrder = 1;

        server.start()

        b.bind(514).sync().channel().closeFuture().await();
    } finally {
        group.shutdownGracefully();
    }
}
