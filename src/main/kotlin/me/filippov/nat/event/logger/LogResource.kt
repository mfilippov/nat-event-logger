package me.filippov.nat.event.logger

import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.QueryParam
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File
import java.io.FileInputStream
import java.net.InetAddress
import me.filippov.utils.Quad
import me.filippov.utils.toInt
import me.filippov.utils.format
import javax.ws.rs.core.Response
import javax.ws.rs.WebApplicationException
import java.io.Writer
import java.io.BufferedWriter
import javax.ws.rs.core.StreamingOutput
import java.io.OutputStream
import java.io.OutputStreamWriter

Path("/logs") public class LogResource {

    GET fun getLogs(QueryParam("date")date: String?, QueryParam("srcIp")srcIp: String?, QueryParam("dstIp")dstIp: String?, QueryParam("eventType")eventType: String?): Response {
        if (date == null || date == "undefined") {
            return Response.ok("Error: Please select date.").build();
        }

        var isSrcIp = !srcIp!!.isEmpty()
        var isDstIp = !dstIp!!.isEmpty()

        if (!isSrcIp && !isDstIp) {
            return Response.ok("Error: Please select source or destination IP.").build();
        }




        val fileName = "logs/${date}.natlog"

        if (!Files.exists(Paths.get(fileName))){
            return Response.ok("Error: File ${fileName} not found.").build();
        }

        val stream = object: StreamingOutput {
            override fun write(output: OutputStream?) {
                val writer = BufferedWriter(OutputStreamWriter(output))
                val length = File(fileName).length()
                val fileStream = FileInputStream(File(fileName))
                var readBytes = 0
                writer.write("<table class=\"table table-striped\">")
                writer.write("<tr><th>Time</th><th>Type</th><th>Source</th><th>Destination</th></tr>")
                do {
                    var buffer = ByteArray(16)
                    readBytes += fileStream.read(buffer)
                    if (readBytes == 0) {
                        break
                    }
                    if (eventType != "all" && eventType != buffer[3].toString()){
                        continue
                    }
                    var h = buffer[0].toInt()
                    var m = buffer[1].toInt()
                    var s = buffer[2].toInt()

                    var t = buffer[3].toInt()

                    var srcBytes = ByteArray(4)
                    srcBytes[0] = buffer[4]
                    srcBytes[1] = buffer[5]
                    srcBytes[2] = buffer[6]
                    srcBytes[3] = buffer[7]

                    var src = InetAddress.getByAddress(srcBytes).getHostAddress();
                    if (isSrcIp) {
                        if (src != srcIp) {
                            continue
                        }
                    }
                    val srcPort = Quad(buffer[8], buffer[9], 0.toByte(), 0.toByte()).toInt()

                    var dstBytes = ByteArray(4)
                    dstBytes[0] = buffer[10]
                    dstBytes[1] = buffer[11]
                    dstBytes[2] = buffer[12]
                    dstBytes[3] = buffer[13]
                    var dst = InetAddress.getByAddress(dstBytes).getHostAddress();

                    if (isDstIp) {
                        if (dst != dstIp) {
                            continue
                        }
                    }

                    val dstPort = Quad(buffer[14], buffer[15], 0.toByte(), 0.toByte()).toInt()
                    val type = when(t) {
                        0 -> "TCP_SYN"
                        1 -> "UDP"
                        2 -> "ICMP"
                        3 -> "TCP_ACK"
                        4 -> "TCP_ACK_PSH"
                        5 -> "PROTO41"
                        else -> "NONE"
                    }
                    writer.write("<tr><td>${h.format("%02d")}:${m.format("%02d")}:${s.format("%02d")}</td><td>${type}</td><td>${src}:${srcPort}</td><td>${dst}:${dstPort}</td></tr>")
                    writer.flush()
                } while (readBytes < length)
                writer.write("</table>")
                writer.flush()
            }
        }

        return Response.ok(stream).build()
    }

}
