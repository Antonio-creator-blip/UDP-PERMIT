package com.company;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class Server {
    public static Hashtable<Integer, Integer> my_dict = new Hashtable<Integer, Integer>();
    public static int i = 0;
    public static void main(String args[]) throws Exception{
        DatagramSocket serverSocket = new DatagramSocket(6789);
        boolean attivo = true;
        byte[] bufferIN = new byte[1024];
        byte[] bufferOUT = new byte[1024];

        System.out.println("SERVER avviato...");
        for(;;){
            System.out.println("La I: " + i);
            DatagramPacket receivePacket = new DatagramPacket(bufferIN,bufferIN.length);
            serverSocket.receive(receivePacket);
            String ricevuto = new String(receivePacket.getData());
            int numCaratteri = receivePacket.getLength();
            ricevuto=ricevuto.substring(0,numCaratteri);
            System.out.println("Ricevuto: " + ricevuto);
            InetAddress IPClient = receivePacket.getAddress();
            int portaClient = receivePacket.getPort();

            if(i==0){
                my_dict.put(portaClient,0);
            }


            for (Map.Entry<Integer, Integer> entry : my_dict.entrySet()) {


                if(entry.getKey() == receivePacket.getPort()){
                    if(ricevuto.equals("SI")){
                        if(entry.getValue() > 2){
                            System.out.println("1");
                            String daSpedire = "Non hai il permesso";
                            bufferOUT = daSpedire.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient,portaClient);
                            serverSocket.send(sendPacket);
                            serverSocket.close();
                        }
                        else{
                            System.out.println("2");
                            Date date = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                            String strDate = dateFormat.format(date);
                            System.out.println(strDate);
                            String daSpedire = strDate;
                            bufferOUT = daSpedire.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient,portaClient);
                            serverSocket.send(sendPacket);
                            my_dict.put(entry.getKey(), entry.getValue() + 1);
                        }
                        String ricevutoNuovo = "SI";
                        while(i>0 && ricevutoNuovo.equals("SI")){
                            DatagramPacket receivePacketNuovo = new DatagramPacket(bufferIN,bufferIN.length);
                            serverSocket.receive(receivePacket);
                            ricevutoNuovo = new String(receivePacketNuovo.getData());
                            int numCaratteriNuovo = receivePacket.getLength();
                            ricevuto=ricevutoNuovo.substring(0,numCaratteriNuovo);
                            System.out.println("Ricevuto: " + ricevuto);


                            Date date = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                            String strDate = dateFormat.format(date);
                            //System.out.println(strDate);
                            String daSpedire = strDate;
                            bufferOUT = daSpedire.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient,portaClient);
                            serverSocket.send(sendPacket);
                            my_dict.put(entry.getKey(), entry.getValue() + 1);
                        }
                    }
                    else{
                        serverSocket.close();
                    }

                }

                /*DatagramPacket receivePacketNuovo = new DatagramPacket(bufferIN,bufferIN.length);
                serverSocket.receive(receivePacket);
                String ricevutoNuovo = new String(receivePacket.getData());
                int numCaratteriNuovo = receivePacket.getLength();
                ricevuto=ricevutoNuovo.substring(0,numCaratteriNuovo);
                System.out.println("Ricevuto nuovo: " + ricevutoNuovo);*/
                System.out.println(my_dict);
            }

            /*DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient,portaClient);
                        String daSpedire = ricevuto.toUpperCase() + receivePacket.getPort();
                        bufferOUT = daSpedire.getBytes();
                        serverSocket.send(sendPacket);*/

            //DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient,portaClient);
            //serverSocket.send(sendPacket);

            if(ricevuto.equals("fine")){
                System.out.println("SERVER IN CHIUSURA");
                attivo=false;
                serverSocket.close();
            }
            i++;

        }

    }
}

