
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PongServer extends Thread {

	static int ballDirection;
	static final Random RANDOM=new Random();
	static final List<String> MOVES = new ArrayList<String>(Arrays.asList("Rup", "Rdown", "Pup", "Pdown"));
	static final List<String> BOOT_MOVES = new ArrayList<String>(Arrays.asList("BRup", "BRdown", "BPup", "BPdown"));
	

    public static void main(String[] args) throws IOException {		
        	new PongServer().start();
	}
	@Override
	public void run() {
		ServerSocket server;
		try {
			server = new ServerSocket(1234);
			System.out.println("waiting player 1");
			Socket player1= server.accept();
			System.out.println("waiting player 2");
			Socket player2= server.accept();
			startGame(player1, player2);
			while(true){
				sendData(player1, player2);
			}		
				
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}	

	public void sendData(Socket player1, Socket player2){
		try {
			InputStream in = player1.getInputStream();
			OutputStream out = player1.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			InputStream in2 = player2.getInputStream();
			OutputStream out2 = player2.getOutputStream();
			PrintWriter writer2 = new PrintWriter(out2, true);
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(in2)); 
			//System.out.println("get goal reader 1");
			if(in.available()>0){
				String move = reader.readLine();
				if(move.equals("goalp1"))
				{
					System.out.println("get goal reader 1");
					writer.println("resetp1");
					writer2.println("resetp1");
				}
				if(move.equals("goalp2"))
				{
					System.out.println("get goal reader 1");
					writer.println("resetp2");
					writer2.println("resetp2");
				}
				if(MOVES.contains(move)){
					int index = MOVES.indexOf(move);
					writer2.println(BOOT_MOVES.get(index));
					writer.println(move);
					System.out.println(move);
					System.out.println(BOOT_MOVES.get(index));
				}
			}
			if(in2.available()>0){	
				String move = reader2.readLine();
				if(move.equals("goalp2"))
				{
					System.out.println("get goal reader 2");
					writer.println("resetp2");
					writer2.println("resetp2");
				}
				if(move.equals("goalp1"))
				{
					System.out.println("get goal reader 2");
					writer.println("resetp1");
					writer2.println("resetp1");
				}
				if(MOVES.contains(move)){	
					int index = MOVES.indexOf(move);
					writer.println(BOOT_MOVES.get(index));
					writer2.println(move);
					System.out.println(move);
					System.out.println(BOOT_MOVES.get(index));
				}
			}
			reader2=null;
			reader=null;
			writer=null;
			writer2=null;
			in=null;
			in2=null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	
	public void startGame(Socket player1 , Socket player2) throws IOException{

			OutputStream out = player1.getOutputStream();
			DataOutputStream dataout = new DataOutputStream(out);
			
			OutputStream out2 = player2.getOutputStream();
			DataOutputStream dataout2 = new DataOutputStream(out2);

			//Settings Players id
			dataout.writeInt(2);
			dataout2.writeInt(2);
			dataout.writeInt(1);
			dataout2.writeInt(2);

			dataout=null;
			dataout2=null;
			out=null;
			out2=null;
	}
}

