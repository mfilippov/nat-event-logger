package me.filippov.nat.event.logger

import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.server.Server
import java.util.Properties
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.bootstrap.Bootstrap
import io.netty.channel.nio.NioEventLoopGroup
import org.glassfish.jersey.servlet.ServletContainer
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.util.log.StdErrLog
import java.nio.file.Files
import java.nio.file.Paths


fun main(args : Array<String>) {
    val group = NioEventLoopGroup();

    try {
        val b = Bootstrap();
        b.group(group)
                .channel(javaClass<NioDatagramChannel>())
                .handler(LogChannelInitializer());
        if (!Files.exists(Paths.get("logs"))) {
            Files.createDirectory(Paths.get("logs"))
        }
        val p = Properties();
        p.setProperty("org.eclipse.jetty.LEVEL", "WARN");
        StdErrLog.setProperties(p);

        val server = Server(8888)

        val ctx = WebAppContext(javaClass<LogResource>().getClassLoader().getResource("webapp/").toExternalForm(), "/")
        ctx.setWelcomeFiles(array("index.html"))

        server.setHandler(ctx)

        val jerseyServlet = ctx.addServlet(javaClass<ServletContainer>(), "/api/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages","me.filippov.nat.event.logger");

        val staticServlet = ctx.addServlet(javaClass<DefaultServlet>(),"/*");
        staticServlet.setInitOrder(1);

        server.start()

        b.bind(514).sync().channel().closeFuture().await();
    } finally {
        group.shutdownGracefully();
    }
}
