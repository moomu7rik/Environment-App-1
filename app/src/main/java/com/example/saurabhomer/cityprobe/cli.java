package com.example.saurabhomer.cityprobe;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class cli{
    String msg;

    public cli()
    {
      msg="";
    }
    public String client(String ip) throws IOException {
        try {


            Socket s = new Socket(ip, 8080);
            Scanner sc1 = new Scanner(s.getInputStream());
            msg = sc1.nextLine();
            s.close();

        }
        catch (IOException e)
        {

        }
        return msg;
    }
}
