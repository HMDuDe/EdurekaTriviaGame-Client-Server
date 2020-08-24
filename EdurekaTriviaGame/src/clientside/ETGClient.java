package clientside;

import java.io.*;
import java.net.*;
import java.util.*;

public class ETGClient {
	private int answer;
	private Scanner input = new Scanner(System.in);
	private Socket etgClientSoc = new Socket("localhost", 8000);
	
	public ETGClient() throws UnknownHostException, IOException {
		
		DataInputStream welcomeMsg = new DataInputStream(etgClientSoc.getInputStream());
		System.out.println(welcomeMsg.readUTF());
		
		this.processUserResponse();
	}
	
	//Recursive method
	public void processUserResponse() throws IOException {
		
		DataInputStream receiveProblemMsg = new DataInputStream(etgClientSoc.getInputStream());
		System.out.println("Question from server: What is " + receiveProblemMsg.readUTF() + "?");
		
		DataInputStream receiveProblemAnswerMsg = new DataInputStream(etgClientSoc.getInputStream());
		this.answer = Integer.parseInt(receiveProblemAnswerMsg.readUTF());
		
		String userAnswer = this.input.next();
		
		DataOutputStream writeUserResponse = new DataOutputStream(this.etgClientSoc.getOutputStream());
		
		while(userAnswer != "stop") {
			try {
				if(Integer.parseInt(userAnswer) == this.answer) {
					System.out.println("Well done, that's correct!");
					writeUserResponse.writeUTF(userAnswer);
					processUserResponse();
					break;
				}else if(userAnswer != "stop" && Integer.parseInt(userAnswer) != this.answer){
					System.out.println("Wrong Answer! The correct answer is: " + this.answer);
					writeUserResponse.writeUTF(userAnswer);
					processUserResponse();
					break;
				}
			}catch(NumberFormatException e) {
				System.out.println("Thank you for playing, Good Bye!");
				break;
			}
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		new ETGClient();
	}
}
