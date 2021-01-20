package tab2mxl;
import java.io.IOException;  // Import the IOException class to handle errors


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.FileWriter;   // Import the FileWriter class
public class txtAnalyzing {

	public static void main(String[] args) throws FileNotFoundException {
		//JUST IMPLEMENTED FOR TESTING
		//finds path of txt file
		String path = System.getProperty("user.dir")+"/src/main/java/tab.txt";
		
		//creates a file object with the path
		File file = new File(path);
		
		//initializes new scanner of the file object
		Scanner scan = new Scanner(file);
		
		//String to hold lines of one measure
		String lines = "";
		
		//goes through each line the file has in it
		while(scan.hasNextLine()) {
			
			//stores the current line in lines
			lines += "\n" + scan.nextLine();

		}
		
		try {
		      FileWriter myWriter = new FileWriter(System.getProperty("user.dir") + "/testTab.txt");
		      myWriter.write(lines);
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		//outputs the final string 
		//test
		System.out.println(lines);
		
	}

}
