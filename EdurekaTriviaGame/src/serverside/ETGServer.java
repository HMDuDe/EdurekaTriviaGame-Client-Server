package serverside;

import java.io.*;
import java.net.*;
import java.util.*;

public class ETGServer {
	private char[] operators = {'+', '-', '/', '*'};
	private int randNo1, randNo2;
	private char[] randomOps = this.randomizeArr(this.operators);
	
	public ETGServer() throws IOException {
		System.out.println("server is starting");
		
		ServerSocket etgServerSoc = new ServerSocket(8000);
		Socket etgSocket = etgServerSoc.accept();
		
		DataOutputStream writeWelcomeMsg = new DataOutputStream(etgSocket.getOutputStream());
		writeWelcomeMsg.writeUTF("Welcome to Edureka Trivia Game");
		
		String userResponse = "";
		
		do {
			//Generate problem
			String[] problem = this.generateProblem();
			
			DataOutputStream writeProblemMsg = new DataOutputStream(etgSocket.getOutputStream());
			writeProblemMsg.writeUTF(problem[0]);
			
			DataOutputStream writeProblemAnswerMsg = new DataOutputStream(etgSocket.getOutputStream());
			writeProblemAnswerMsg.writeUTF(problem[1]);
			
			DataInputStream readUserResponse = new DataInputStream(etgSocket.getInputStream());
			
			try {
				userResponse = readUserResponse.readUTF();
			}catch(SocketException e) {
				etgSocket.close();
				System.out.println("Server closing...");
				break;
			}
			
			if(userResponse != "stop") {
				System.out.println("Answer from client: " + userResponse);
			}
			problem = new String[2];
		}while(userResponse != "stop");
	}
	
	public char[] randomizeArr(char[] arr) {
		char[] randomized = new char[this.operators.length];
		TreeMap<Integer, Character> ops = new TreeMap<>();
		ArrayList<Integer> checked = new ArrayList<>();
		
		//Giving each element a random index position
		for(char currentChar : arr) {
			
			Random randIndex = new Random();
			
			int randomNo = randIndex.nextInt(4);
			
			while(checked.contains(randomNo)) {
				randomNo = randIndex.nextInt(4);
			}
			
			checked.add(randomNo);
			ops.put(randomNo, currentChar);
		}
		
		//Add operator at given randomized index position
		for(Map.Entry<Integer, Character> operator: ops.entrySet()) {
			randomized[operator.getKey()] = operator.getValue();
		}
		
		return randomized;
	}
	
	//Method returns an array where arr[0] = math problem and arr[1] = math problem answer
	public String[] generateProblem() {
		String[] problem = new String[2];
		
		//Generate problem
		this.randNo1 = 1 + (int) (Math.random() * ((10 - 1) + 1));
		this.randNo2 = 1 + (int) (Math.random() * ((10 - 1) + 1));
		Random opsIndex = new Random();
		char operator = this.randomOps[opsIndex.nextInt(4)];
		
		String mathProblem = this.randNo1 + " " + operator + " " + this.randNo2;
		
		//Generate answer
		int answer = -1;
		
		if(operator == '+') {
			answer = this.randNo1 + this.randNo2;
		}else if(operator == '-') {
			
			if(this.randNo1 < this.randNo2) {
				mathProblem = this.randNo2 + " " + operator + " " + this.randNo1;
				answer = this.randNo2 - this.randNo1;
			}else {
				answer = this.randNo1 - this.randNo2;
			}
		}else if(operator == '*') {
			answer = this.randNo1 * this.randNo2;
		}else {
			if(this.randNo1 < this.randNo2) {
				mathProblem = this.randNo2 + " " + operator + " " + this.randNo1;
				answer = Math.round(this.randNo2 / this.randNo1);
			}else {
				answer = Math.round(this.randNo1 / this.randNo2);
			}
		}
		
		problem[0] = mathProblem;
		problem[1] = "" + answer;
		
		return problem;
	}
	
	public static void main(String[] args) throws IOException {
		new ETGServer();
	}
}
