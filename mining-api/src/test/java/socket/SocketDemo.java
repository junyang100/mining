package socket;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SocketDemo {
    static ExecutorService executor = Executors.newFixedThreadPool(100);

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {//主线程死循环等待新连接到来
            Socket socket = serverSocket.accept();
            executor.submit(new ConnectIOnHandler(socket));//为新的连接创建新的线程
//            new ConnectIOnHandler(socket).start();
        }
    }

    static class ConnectIOnHandler extends Thread {
        private Socket socket;

        public ConnectIOnHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String someThing = br.readLine();//读取数据
                System.out.println(someThing);
                //处理数据
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                StringBuffer result = new StringBuffer();
                result.append("HTTP/1.1 200 ok \n");
                result.append("Content-Type:text/html;charset=utf-8 \n");
                result.append("\r\n" + "socket http demo,timstamp=" + System.currentTimeMillis());
                bw.write(result.toString());
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}