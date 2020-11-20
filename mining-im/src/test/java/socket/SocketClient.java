package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by admin on 2020/8/20.
 */
public class SocketClient {
    public static void main(String [] args)
    {
        String serverName = "20.0.29.47";
        int port = 9876;
        try
        {
            System.out.println("连接到主机：" + serverName + " ，端口号：" + port);
            Socket client = new Socket(serverName, port);
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());
//            SocketAddress address = new InetSocketAddress(serverName, port);
//            client.connect(address);
//            OutputStream outToServer = client.getOutputStream();
//            DataOutputStream out = new DataOutputStream(outToServer);
//
//            out.writeUTF("Hello from " + client.getLocalSocketAddress());
//            InputStream inFromServer = client.getInputStream();
//            DataInputStream in = new DataInputStream(inFromServer);
//            System.out.println("服务器响应： " + in.readUTF());
//            client.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
