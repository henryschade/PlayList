import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Comparator;
import java.util.Collections;
import java.io.*;

/**
 * CS 121 Project 2: Play List
 *
 * Manages a user's songs in a simplified playlist.
 *
 * @author Marissa Schmidt
 * @author Henry J. Schade
 */
public class PlayList {
	//Global variables and such
	private static Scanner objKBD = new Scanner(System.in);
    private static ArrayList<Song> objPlayList = new ArrayList<Song>();

	//Routines / methods
	public static void clearScreen(){
		/*
		* Routine/method to clear the screen.
		* http://stackoverflow.com/questions/2979383/java-clear-the-console
		*/

		try {
			if (System.getProperty("os.name").contains("Windows")){
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				//Runtime.getRuntime().exec("clear");
				System.out.print("\033[H\033[2J"); 
				System.out.flush();
			}
		} catch (IOException | InterruptedException ex) {}
	}

	//http://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
    public static Comparator<Song> sortSongNameAsc = new Comparator<Song>() {
		/*
		* method to sort Songs by Name Ascending.
		*/

		public int compare(Song sSong1, Song sSong2) {
		   String strSongName1 = sSong1.getTitle().toUpperCase();
		   String strSongName2 = sSong2.getTitle().toUpperCase();

		   //ascending order
		   return strSongName1.compareTo(strSongName2);
		}
	};
    public static Comparator<Song> sortSongNameDesc = new Comparator<Song>() {
		/*
		* method to sort Songs by Name Descending.
		*/

		public int compare(Song sSong1, Song sSong2) {
		   String strSongName1 = sSong1.getTitle().toUpperCase();
		   String strSongName2 = sSong2.getTitle().toUpperCase();

		   //descending order
		   return strSongName2.compareTo(strSongName1);
		}
	};
    public static Comparator<Song> sortSongTimeAsc = new Comparator<Song>() {
		/*
		* method to sort Songs by Time Ascending.
		*/

		public int compare(Song sSong1, Song sSong2) {
		   int intSongTime1 = sSong1.getPlayTime();
		   int intSongTime2 = sSong2.getPlayTime();

		   //ascending order
		   return intSongTime1 - intSongTime2;
		}
	};
    public static Comparator<Song> sortSongTimeDesc = new Comparator<Song>() {
		/*
		* method to sort Songs by Time Descending.
		*/

		public int compare(Song sSong1, Song sSong2) {
		   int intSongTime1 = sSong1.getPlayTime();
		   int intSongTime2 = sSong2.getPlayTime();

		   //descending order
		   return intSongTime2 - intSongTime1;
		}
	};
	
	public static void displayAvgPlay(){
		/*
		* routine/method to display the average play time of all songs.
		*/

		double dblPlayTime;
		int intX;
		Song objSong = new Song("","",0,"");

		dblPlayTime = 0.0;
		for (intX = 0; intX < objPlayList.size(); intX++){
			//System.out.println("reading song " + (intX + 1) + " info");
			objSong = objPlayList.get(intX);

			dblPlayTime = dblPlayTime + objSong.getPlayTime();
		}
		if (objPlayList.size() > 0){
			dblPlayTime = dblPlayTime / objPlayList.size();
		}
		//System.out.println("The average play time is: " + intPlayTime + " seconds");		//I think this is a better format, but the assignemnt wants the next line.
		System.out.println("Average play time: " + dblPlayTime + "\r\n");
	}

	public static String displayMenu(){
		/*
		* Routine / method that displays the menu system so the end user knows what actions are available.
		* returns a string (incase the menu gets into the double digits) of the option selected.
		*/

		String strTemp;

		clearScreen();

		System.out.println("1 - Input song information");
		System.out.println("2 - Calculate average play time");
		System.out.println("3 - Find song with play time closest to 4 minutes");
		System.out.println("4 - Sort list, and display songs");
		System.out.println("5 - Display songs");
		System.out.println("6 - Export play list");
		System.out.println("7 - Import play list (overwrites current)");
		System.out.println("8 - Automated testing process");
		System.out.println("Q - Quit" + "\r\n");

		System.out.println("Enter the option, from the action above, that you would like to do.");
		strTemp = objKBD.nextLine();

		System.out.println("");

		return strTemp;
	}

	public static void displayPlayList(){
		/*
		* Routine / method to display the current playlist songs using the Song Class toString() method.
		*/

		int intX;
		Song objSong = new Song("","",0,"");

		System.out.println("==============================================================================");
		System.out.println("Title                Artist               File Name                  Play Time");
		System.out.println("==============================================================================");
		for (intX = 0; intX < objPlayList.size(); intX++){
			//System.out.println("reading song " + (intX + 1) + " info");
			objSong = objPlayList.get(intX);
			//System.out.println(objSong.getTitle() + "" + objSong.getArtist() + "" + objSong.getFilePath() + "" + objSong.getPlayTime());
			System.out.println(objSong.toString());
		}
		System.out.println("==============================================================================" + "\r\n");
	}

	public static void doAutoTest(){
		/*
		* Routine / method used for the automated testing process.
		*/

		int intX;
		Song objSong = new Song("","",0,"");

		for (intX = 0; intX <= 2; intX++){
			objSong = getNewSongInfo();
			objPlayList.add(objSong);
		}

		displayAvgPlay();

		findClosestTo(4.0);

		Collections.sort(objPlayList, PlayList.sortSongTimeAsc);

		displayPlayList();
	}

	public static void findClosestTo(double dblPlayTime){
		/*
		* Routine / method that finds the song with the Play Time closes to dblPlayTime.
		* @param dblPlayTime = # Minutes.  (i.e. 4.0 or 3.5)
		*/

		int intX;
		Song objSong = new Song("","",0,"");
		Song objClosestSong = new Song("","",0,"");

		dblPlayTime = (dblPlayTime * 60);		//240 sec
		for (intX = 0; intX < objPlayList.size(); intX++){
			//System.out.println("reading song " + (intX + 1) + " info");
			//On first pass through setup "Closest song".
			if (intX == 0){
				objClosestSong = objPlayList.get(intX);
				objSong = new Song("","",0,"");
			}
			//Get the song to compare/check
			objSong = objPlayList.get(intX);

			//Check songs compared to "closest song"
			if (objSong.getPlayTime() != 0){
				if ((objSong.getPlayTime() - dblPlayTime) == 0){
					//Exact match.
					objClosestSong = objPlayList.get(intX);
					break;
				}else{
					//Check if there is Math.absolute()
					if (Math.abs(objSong.getPlayTime() - dblPlayTime) < Math.abs(objClosestSong.getPlayTime() - dblPlayTime)){
						objClosestSong = objPlayList.get(intX);
					}
				}
			}
		}
		System.out.println("Song with play time closest to " + dblPlayTime + " secs is: " + objClosestSong.getTitle() + "\r\n");
	}

	public static Song getNewSongInfo(){
		/*
		* Routine / method to get individual song info from the user.
		* Returns a Song object.
		*/

		//declare variables
		String strSongTitle;
		String strSongArtist;
		String strSongTime;
		String strSongPath;
		int intSongSec;
		String arrSongTime[] = {"0","0","0"};

		//Get user input
		System.out.print("Enter Title (i.e. Indestructible):                ");
		strSongTitle = objKBD.nextLine();
		if (strSongTitle.equals("")){
			//If blank use defaults
			strSongTitle = "Indestructible";
		}
		System.out.print("Enter Artist (i.e. Disturbed):                    ");
		strSongArtist = objKBD.nextLine();
		if (strSongArtist.equals("")){
			//If blank use defaults
			strSongArtist = "Disturbed";
		}
		System.out.print("Enter Play Time (mm:ss) (i.e. 4:38):              ");
		strSongTime = objKBD.nextLine();
		if (strSongTime.equals("")){
			//If blank use defaults
			strSongTime = "4:38";
		}
		System.out.print("Enter File Path and Name (i.e. /sounds/inds.mp3): ");
		strSongPath = objKBD.nextLine();
		if (strSongPath.equals("")){
			//If blank use defaults
			strSongPath = "/sounds/inds.mp3";
		}

		//Manipulate data gotten.  Convert hh:mm:ss to seconds.
		intSongSec = 0;
		//http://beginnersbook.com/2013/12/java-strings/
		//http://beginnersbook.com/2013/12/java-string-split-method-example/
		arrSongTime = strSongTime.split(":");
		switch (arrSongTime.length){
			case 3:
				//hh:mm:ss
				intSongSec = (Integer.parseInt(arrSongTime[0]) * 60 * 60) + (Integer.parseInt(arrSongTime[1]) * 60) + Integer.parseInt(arrSongTime[2]);
				strSongTime = "   Hour: " + arrSongTime[0] + "\r\n" + "    Min: " + arrSongTime[1] + "\r\n" + "    Sec: " + arrSongTime[2];
				break;
			case 2:
				//mm:ss
				intSongSec = (Integer.parseInt(arrSongTime[0]) * 60) + Integer.parseInt(arrSongTime[1]);
				strSongTime = "   Min: " + arrSongTime[0] + "\r\n" + "   Sec: " + arrSongTime[1];
				break;
			case 1:
				//ss
				intSongSec = Integer.parseInt(arrSongTime[0]);
				strSongTime = "   Sec: " + arrSongTime[0];
				break;
			default:
				strSongTime = "   Invalid data/format provided. (" + strSongTime + ")";
				break;
		}

		System.out.print("\r\n");
		//Populate a Song object, to be returned.
		Song objSong = new Song(strSongTitle, strSongArtist, intSongSec, strSongPath);

		return objSong;
	}

	public static void getSongs(){
		/*
		* Routine / method that asks if the user would like to enter some song info, in a loop.
		*/

		boolean bolDone = false;
		//Get Song data
		Song objSong = new Song("","",0,"");
		String strTemp;

		//Loop until user indicates they are done
		while (!bolDone){
			clearScreen();

			objSong = getNewSongInfo();
			objPlayList.add(objSong);

			//System.out.print("\r\n" + "Do you want to view a summary of that data? (Yes or Y to view)");
			//strTemp = objKBD.nextLine();
			//if ((strTemp.toLowerCase().equals("y")) || (strTemp.toLowerCase().equals("yes"))){
			//	//User wants to see a summary
			//	//Output
			//	System.out.println("\r\n" + "Here is the Song info we recorded:");
			//	System.out.println("strSongTitle: " + objSong.getTitle());
			//	System.out.println("strSongArtist: " + objSong.getArtist());
			//	System.out.println("strSongTime: " + objSong.getPlayTime());
			//	System.out.println("strSongPath: " + objSong.getFilePath());
			//}

			System.out.print("\r\n" + "Are you done entering songs? (Yes or Y to quit)");
			strTemp = objKBD.nextLine();
			if ((strTemp.toLowerCase().equals("y")) || (strTemp.toLowerCase().equals("yes"))){
				//User wants to quit adding songs
				bolDone = true;
			}
			System.out.println("\r\n");
		}
	}

	public static void waitForKeyPress(){
		/*
		* Routine / method to replicate the DOS pause command.
		*/

		String strTemp;

		System.out.println("\r\n" + "Press RETURN to continue...");
		strTemp = objKBD.nextLine();
	}

	public static void main(String[] args) {
		/*
		* Main entry point and the start of cool things to happen.
		*/

		//Declare variables
		boolean bolDone = false;
		String strMenuChoise = "";
		String strFile = "PlayList.dat";

		//Loop until user indicates they are done
		while (!bolDone){
			clearScreen();

			//Display the menu and get the users choice.
			strMenuChoise = displayMenu();

			//Let the user know how many songs we currently have, unless they are quiting or importing.
			if ((!(strMenuChoise.toLowerCase().equals("q"))) && (!(strMenuChoise.toLowerCase().equals("7")))){
				System.out.println("There are " + objPlayList.size() + " song(s) currently in the list.");
			}

			switch (strMenuChoise.toLowerCase()){
				case "1":
					//Input song information
					getSongs();
					break;
				case "2":
					//Calc avg play time
					if (objPlayList.size() > 0){
						displayAvgPlay();
					}
					else{
						System.out.println("There is nothing to calculate.");
					}

					waitForKeyPress();
					break;
				case "3":
					//Find the song with play time closest to 4 minutes
					if (objPlayList.size() > 0){
						findClosestTo(4.0);
					}
					else{
						System.out.println("There is nothing to compare.");
					}

					waitForKeyPress();
					break;
				case "4":
					//Sort Play List
					if (objPlayList.size() > 0){
						String strSortField = "";
						String strSortDir = "";

						do{
							//What field to sort by
							clearScreen();
							strSortField = "";
							System.out.println("What field to sort by? [T]itle or [P]lay Time");
							strSortField = objKBD.nextLine();
						} while (!((strSortField.toLowerCase().equals("t")) || (strSortField.toLowerCase().equals("p"))));

						do{
							//How to sort
							clearScreen();
							strSortDir = "";
							System.out.println("How would you like to sort? [A]scending or [D]escending");
							strSortDir = objKBD.nextLine();
						} while (!((strSortDir.toLowerCase().equals("a")) || (strSortDir.toLowerCase().equals("d"))));

						//Now do the actual sorting
						if (strSortField.toLowerCase().equals("t")){
							//Sort by Title
							if (strSortDir.toLowerCase().equals("a")){
								Collections.sort(objPlayList, PlayList.sortSongNameAsc);
							}else{
								Collections.sort(objPlayList, PlayList.sortSongNameDesc);
							}
						}
						if (strSortField.toLowerCase().equals("p")){
							//Sort by Play Time
							if (strSortDir.toLowerCase().equals("a")){
								Collections.sort(objPlayList, PlayList.sortSongTimeAsc);
							}else{
								Collections.sort(objPlayList, PlayList.sortSongTimeDesc);
							}
						}
					}
					else{
						System.out.println("There is nothing to Sort.");
					}

					//Fall through to now display the sorted list
					//break;
				case "5":
					//Display Play List, if there are songs in it.
					if (objPlayList.size() > 0){
						displayPlayList();
					}

					waitForKeyPress();
					break;
				case "6":
					//Export Play List
					if (objPlayList.size() > 0){
						Song objSong = new Song("","",0,"");

						//http://www.cs.utexas.edu/~mitra/csSummer2012/cs312/lectures/fileIO.html
						//https://www.tutorialspoint.com/java/java_filewriter_class.htm
						//http://stackoverflow.com/questions/23728493/what-ioexception-do-i-use-for-filewriter

						try {
							FileWriter outStream = new FileWriter (strFile);

							for (int intX = 0; intX < objPlayList.size(); intX++){
								System.out.println("Exporting song " + (intX + 1) + " of " + objPlayList.size() + " (" + objSong.getTitle() + ")");
								objSong = objPlayList.get(intX);
						
								outStream.write(objSong.getTitle() + "\r\n");
								outStream.write(objSong.getArtist() + "\r\n");
								outStream.write(objSong.getPlayTime() + "\r\n");
								outStream.write(objSong.getFilePath() + "\r\n");
							}

							outStream.flush();
							outStream.close();
						} catch (IOException e){
							e.printStackTrace();
						}
					}
					else{
						System.out.println("There is nothing to export to " + strFile + ".");
					}

					waitForKeyPress();
					break;
				case "7":
					//Import a Play List
					//http://stackoverflow.com/questions/13185727/reading-a-txt-file-using-scanner-class-in-java
					//Setup variables
					String strSongTitle = "";
					String strSongArtist = "";
					String strSongTime = "";
					String strSongPath = "";
					int intWhatStep = 0;

					//Make sure the current playlist is empty
					objPlayList = new ArrayList<Song>();

					if ((new File(strFile).isFile())){
						File objSaveFile = new File (strFile);
						try {
							Scanner inStream = new Scanner(objSaveFile);
							while (inStream.hasNextLine()){
								intWhatStep++;
								if (intWhatStep == 1){
									strSongTitle = inStream.nextLine();
								}
								if (intWhatStep == 2){
									strSongArtist = inStream.nextLine();
								}
								if (intWhatStep == 3){
									strSongTime = inStream.nextLine();
								}
								if (intWhatStep == 4){
									strSongPath = inStream.nextLine();
									intWhatStep = 0;
									objPlayList.add(new Song(strSongTitle, strSongArtist, Integer.parseInt(strSongTime), strSongPath));
								}
							}
							inStream.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					else{
						System.out.println("The exported list of songs, " + strFile + ", was not found.");
					}

					System.out.println("There are now " + objPlayList.size() + " song(s) in the list.");

					waitForKeyPress();
					break;
				case "8":
					//Accomodate the Automated testing process
					doAutoTest();

					//waitForKeyPress();
					//break;
					return;		//Should not do this, but the above 2 lines "break" things w/ the input file.
				case "q":
					//Quit
					bolDone = true;
					System.out.println("Good Bye." + "\r\n");
					break;
				default:
					//Choice is not defined, therefore invalid, redisplay menu.
					break;
			}
		}

		objKBD.close();
	}
}
