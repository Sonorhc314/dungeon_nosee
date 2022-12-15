/**
 * Reads and contains in memory the map of the game.
 *
 */
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Map {

	/* Representation of the map */
	private char[][] map;
	
	/* Map name */
	private String mapName;
	
	/* Gold required for the human player to win */
	private int goldRequired;
	
	/**
	 * Default constructor, creates the default map "Very small Labyrinth of doom".
	 */
	public Map() {
		mapName = "Very small Labyrinth of Doom";
		goldRequired = 2;
		map = new char[][]{
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'},
		{'#','#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','G','.','.','.','.','.','.','.','.','.','E','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','E','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','G','.','.','.','.','.','.','#'},
		{'#','.','#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','.','#'},
		{'#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#','#'}
		};
	}
	
	/**
	 * Constructor that accepts a map to read in from.
	 *
	 * @param : The filename of the map file.
	 */
	public Map(String fileName) {
		readFileMap(fileName);
	}


    /**
     * Reads the map from file.
     *
     * @param : Name of the map's file.
     */
    public void readMap() {
		for (int i = 0; i<map.length; i++) {
			System.out.print(map[i]);
			System.out.println();
		}
	}


	public char[][] returnMap()
	{
		return map;
	}

	public void readFileMap(String fileName) {//gets map from file
		//map = new char[][];
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj); //used to actually fetch data
			Scanner myReader_all_lines = new Scanner(myObj);//counts all lines so we can find out columns for map array
			int all_lines = 0;
			int row_number = 0; //how many characters in a row of a map
			int line =0;//current line
			while (myReader_all_lines.hasNextLine()) {
				String data_rows=myReader_all_lines.nextLine();
				if(all_lines==2)//all maps start from row 2
				{
					row_number = data_rows.length(); //characters in a row(its the same for all rows so count once)
				}
				all_lines++; // characters in a column
			}
			myReader_all_lines.close();

			map = new char[all_lines-2][row_number];//now we know rows/cols of our map
			int map_row =0;//current row
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(line==0)
				{
					mapName = data;//all maps have name on row 0
				}
				else if(line==1)
				{//get integer out of string (ex.: win 13 -> 13)
					goldRequired = Integer.parseInt(data.replaceAll("[^0-9]", ""));
					//all maps have number of required gold on row 1
				}
				else
				{
					char[] current_row = data.toCharArray();//convert current string row to 1d char array
					for(int map_col =0;map_col<row_number;map_col++)
					{
						map[map_row][map_col] = current_row[map_col];
					}
					map_row++;
				}
				line++;
			}
			myReader.close();
			} 
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
			}
	}
	//-------------Getters------

	int getGold()
	{
		return goldRequired;
	}

	String getMapName()
	{
		return mapName;
	}

	//--------------------------

}
