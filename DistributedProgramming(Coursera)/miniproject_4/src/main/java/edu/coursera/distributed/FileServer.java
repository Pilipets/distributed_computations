package edu.coursera.distributed;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
public final class FileServer {
    private void processRequest(final Socket listenerSocket, final PCDPFilesystem fs) throws IOException {
        InputStreamReader inReader = new InputStreamReader(listenerSocket.getInputStream());
        BufferedReader inBuffer = new BufferedReader(inReader);

        String line = inBuffer.readLine();
        assert line != null;

        final String[] cmds = line.split(" "); // GET /path/to/file HTTP/1.1
        assert cmds[0].equals("GET");
        OutputStream wStream = listenerSocket.getOutputStream();
        PrintWriter streamWriter = new PrintWriter(wStream);
        final PCDPPath fPath = new PCDPPath(cmds[1]);
        final String content = fs.readFile(fPath);
        if(content != null) {
            streamWriter.write("HTTP/1.0 200 OK\r\n");
            streamWriter.write("\r\n");
            streamWriter.write("\r\n");
            streamWriter.write(String.format("%s\r\n", content));
        } else {
            streamWriter.write("HTTP/1.0 404 Not Found\r\n");
            streamWriter.write("\r\n");
            streamWriter.write("\r\n");
        }
        streamWriter.close();
    }
    public void run(final ServerSocket socket, final PCDPFilesystem fs,
                    int ncores) throws IOException {
        final ExecutorService executor = Executors.newFixedThreadPool(ncores);
        while (true){
            Socket listenerSocket = socket.accept();
            executor.submit(()-> {
                try {
                    processRequest(listenerSocket, fs);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
