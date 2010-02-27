package info.rikkus.indx;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Main
{
	private final static String URL = "file:/Users/rik/testxword.bin";

	public static void main(String[] arguments)
	{
		try
		{
			read(new URL(URL));
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void read(URL url) throws IOException
	{
		DataInputStream inputStream = null;

		try
		{
			inputStream = new DataInputStream(url.openStream());
			PuzzleReader reader = new PuzzleReader();
			Puzzle puzzle = reader.read(inputStream);
			print(puzzle);
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	private static void print(Puzzle puzzle)
	{
		for (int row = 0; row < puzzle.height; row++)
		{
			for (int column = 0; column < puzzle.width; column++)
			{
				int solutionIndex = puzzle.gridData[row][column].solutionIndex;
				char cellContent = puzzle.gridData[row][column].cellContent;

				if (solutionIndex == 0)
				{
					if (cellContent == '?')
					{
						System.out.print("  ");
					}
					else
					{
						System.out.print("/\\");
					}	
				}
				else
				{
					System.out.print(String.format("%2d", puzzle.gridData[row][column].solutionIndex));
				}
			}

			System.out.println();

			for (int column = 0; column < puzzle.width; column++)
			{
				char cellContent = puzzle.gridData[row][column].cellContent;

				if (cellContent == '?')
				{
					System.out.print(puzzle.gridData[row][column].solutionCharacter);
					System.out.print(' ');
				}
				else
				{
					System.out.print("\\/");
				}

			}
			System.out.println();
		}

		System.out.println("-- Across --");

		for (Map.Entry<Integer, String> entry: puzzle.acrossClues.entrySet())
		{
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}

		System.out.println("-- Down --");

		for (Map.Entry<Integer, String> entry: puzzle.downClues.entrySet())
		{
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}