package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Score {
	
	private long score = 0;
	private final File f = new File("Score.txt");
	private ArrayList<String> data = new ArrayList<String>();
	private ArrayList<Integer> preScores = new ArrayList<Integer>();
	
	private void readData() throws IOException {
		if(f.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String nl;
			while((nl = reader.readLine()) != null) {
				int arCount = 0;
				for(char c : nl.toCharArray()) if(c == '>') arCount++;
				if(arCount > 1) {
					String[] lns = new String[arCount];
					for(int i = 0; i < arCount; i++) {
						int j = 0;
						while(j < nl.length() && nl.charAt(j) != '>') j++;
						j++;
						if(j >= nl.length()) break;
						while(j < nl.length() && Character.isDigit(nl.charAt(j))) j++;
						if(j >= nl.length()) {
							lns[i] = nl;
							break;
						}
						lns[i] = nl.substring(0, j);
						nl = nl.substring(j);
					}
					for(String s : lns) if(s != null) data.add(s);
				} else {
					data.add(nl);
				}
			}
			for(int j = 0; j < data.size(); j++) {
				int i = 0;
				while(i < data.get(j).length() && data.get(j).charAt(i) != '>') i++;
				i++;
				if(i >= data.get(j).length()) data.remove(j);
				else {
					preScores.add(Integer.parseInt(data.get(j).substring(i)));
				}
			}
			reader.close();
		} else {
			f.createNewFile();
		}
	}
	
	public void writeData() throws IOException {
		readData();
		String name = JOptionPane.showInputDialog("Please enter your name (Please do not begin your name with a number, or include a '>'): ");
		boolean clean = false;
		while(!clean) {
			if(name != null && !name.isEmpty()) {
				System.out.println("Checking Name: [" + name + "]");
				while(Character.isDigit(name.charAt(0))) name = JOptionPane.showInputDialog("Invalid Name, Please enter a name that does not start with a number: ");
				boolean cIllChar = false;//Contains Illegal Character
				for(char c : name.toCharArray()) if(c == '>') cIllChar = true;
				if(cIllChar) name = JOptionPane.showInputDialog("Please enter a name that does not contain a '>': ");
				else if(Character.isDigit(name.charAt(0))) cIllChar = true;
				else clean = true;
			} else {
				name = JOptionPane.showInputDialog("No Name provided, enter no input to discard score (Doing so will be perminent), else enter a name: ");
				if(name == null || name.isEmpty()) break;
			}
		}
		name += ">" + score + "\n";
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		
		if(data == null || data.size() <= 0) writer.write(name);
		else {
			int i = 0;
			while(i < preScores.size() && preScores.get(i) > score) i++;
			for(int j = 0; j < i; j++) writer.write(data.get(j));
			if(clean) writer.write(name);
			for(; i < data.size(); i++) writer.write(data.get(i));
		}
		
		writer.close();
	}
	
	public void addScore(int n) {
		score += n;
	}
	
	public void setScore(long n) {
		score = n;
	}
	
	public long getScore() {
		return score;
	}
	
}