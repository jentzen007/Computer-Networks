import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class EchoProgram {
    public static void main(String[] args) {
        Scanner get = new Scanner(System.in);
        System.out.println("Choose the Model:\n1.Run as a Server\n2.Run as a Client");
        int choice = get.nextInt();

        if(choice == 1) {
            runServer();
        } else if(choice == 2) {
            runClient();
        }
        else {
            System.out.println("Select a valid choice next time. Re-run this code.");
        }
        get.close();
    }

    public static void runServer() {
        try(DatagramSocket s = new DatagramSocket(9876)) {
            byte[] received = new byte[1024];
            byte[] sent;

            System.out.println("Server is running...");
            String val = "control";

            while (!val.equals("exit()")) { 
                DatagramPacket rp = new DatagramPacket(received, received.length);
                s.receive(rp);

                String rData = new String(rp.getData(), 0, rp.getLength());
                val = rData;
                System.out.println("Received Data:>> " + rData);

                sent = rData.getBytes();
                DatagramPacket sp = new DatagramPacket(sent, sent.length,rp.getAddress(),rp.getPort());
                s.send(sp);
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public static void runClient() {
        try(DatagramSocket s = new DatagramSocket()) {
            InetAddress sAddr = InetAddress.getByName("localhost");
            byte[] sent = new byte[1024];
            byte[] received = new byte[1024];
            Scanner get = new Scanner(System.in);
            String message = "control";

            while (!message.equals("exit()")) { 
                System.out.println("Enter a message to send to server: ");
                message = get.nextLine();
                sent = message.getBytes();
    
                DatagramPacket sp = new DatagramPacket(sent, sent.length, sAddr, 9876);
                s.send(sp);
    
                DatagramPacket rp = new DatagramPacket(received, received.length);
                s.receive(rp);
    
                String data = new String(rp.getData(), 0, rp.getLength());
                System.out.println("Received from server: " + data);
    
                if(message.equals(data)) {
                    System.out.println("Succesfully verified! :)");
                } else {
                    System.out.println("Conflicting data! :(");
                } 
            }
            get.close();
            
         } catch(Exception e) {
            e.printStackTrace();
        }
    }
}