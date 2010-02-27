package info.rikkus.indx;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class PuzzleReader
{
	private static final int StartOfSolution = 1;

	private static final String CharSet = "Cp1252";

	private boolean flagSet(int setOfFlags, int flag)
	{
		return (setOfFlags & flag) != 0;
	}

	private void discardLongString(DataInputStream inputStream)
		throws IOException
	{
		readLongString(inputStream);
	}

	private String readShortString(DataInputStream inputStream)
		throws IOException
	{
		byte[] buf = new byte[inputStream.readByte() & 0xFF];
		inputStream.readFully(buf);
		return new String(buf, CharSet);
	}

	private String readLongString(DataInputStream inputStream)
		throws IOException
	{
		byte[] buf = new byte[inputStream.readShort() & 0xFFFF];
		inputStream.readFully(buf);
		return new String(buf, CharSet);
	}

	private void discardBoolean(DataInputStream stream)
		throws IOException
	{
		stream.readBoolean();
	}

	private void discardByte(DataInputStream stream)
		throws IOException
	{
		discardByte(stream, 1);
	}

	private void discardByte(DataInputStream stream, int count)
		throws IOException
	{
		for (int i = 0; i < count; i++)
		{
			stream.readByte();
		}
	}

	private void discardInt(DataInputStream stream)
		throws IOException
	{
		discardInt(stream, 1);
	}

	private void discardInt(DataInputStream stream, int count)
		throws IOException
	{
		for (int i = 0; i < count; i++)
		{
			stream.readInt();
		}
	}

	private String readString(DataInputStream inputStream, int length)
		throws IOException
	{
		byte[] a = new byte[length];
		inputStream.readFully(a);
		return new String(a, CharSet);
	}

	public Puzzle read(DataInputStream inputStream) throws IOException
	{
		int buttonTextDataLength = inputStream.readByte();

		discardByte(inputStream); // Flags

		readAndDiscardButtonTexts(inputStream, buttonTextDataLength);

		discardLongString(inputStream); // Victory congratulations

		readAndIgnoreSomeValue(inputStream);

		int width = (int)inputStream.readByte();
		int height = (int)inputStream.readByte();

		CellData[][] gridData = readGridData(width, height, inputStream);

		discardByte(inputStream);
		discardInt(inputStream);
		discardByte(inputStream, 3);
		discardInt(inputStream, 2);

		SortedMap<Integer, String> downClues = readClues(inputStream);
		SortedMap<Integer, String> acrossClues = readClues(inputStream);

		fixNumbering(width, height, gridData, acrossClues, downClues);

		return new Puzzle(width, height, gridData, acrossClues, downClues);
	}

	private void fixNumbering(
		int width,
		int height,
		CellData[][] gridData,
		SortedMap<Integer, String> acrossClues,
		SortedMap<Integer, String> downClues
	)
	{
		for (int row = 0; row < height; row++)
		{
			for (int column = 0 ; column < width; column++)
			{
					
			}
		}
	}

	private SortedMap<Integer, String> readClues(DataInputStream inputStream) throws IOException
	{
		SortedMap<Integer, String> clues = new TreeMap<Integer, String>();

		readShortString(inputStream);

		int clueCount = inputStream.readInt();

		for (int i = 0; i < clueCount; i++)
		{
			discardByte(inputStream, 2);

			int index = Integer.parseInt(readShortString(inputStream));
			String clue = readLongString(inputStream);

			System.out.println(index + ": " + clue);
			clues.put(index, clue);
		}

		return clues;
	}

	private void readAndIgnoreSomeValue(DataInputStream inputStream)
		throws IOException
	{
		if ((inputStream.readByte() & 0x40) != 0)
		{
			inputStream.readByte();
		}
	}

	private void readAndDiscardButtonTexts(DataInputStream inputStream, int buttonTextDataLength)
		throws IOException
	{
		if (buttonTextDataLength == 0)
		{
			return;
		}

		for (int i = 0; i < 7; ++i)
		{
			if ((buttonTextDataLength & 1 << i) == 0)
			{
				continue;
			}

			readShortString(inputStream);
		}
	}

	private CellData[][] readGridData(int width, int height, DataInputStream inputStream) throws IOException
	{
		discardByte(inputStream, 3);
		discardInt(inputStream, 5); // Colours

		String map = readString(inputStream, width * height);

		byte[] cellFlagsMap = new byte[width * height];
		inputStream.readFully(cellFlagsMap);

		CellData[][] cellData = new CellData[width][height];

		int l = 0;
		int i1 = 0;

		for (int y = 0; y < height; ++y)
		{
			for (int x = 0; x < width; ++x)
			{
				int cellIndex = y * width + x;
				byte cellFlags = cellFlagsMap[cellIndex];

				int solutionIndex = 0;

				if (flagSet(cellFlags, StartOfSolution))
				{
					solutionIndex = l++;
				}

				char c = map.charAt(cellIndex);

				if ((c != ' ') && (c != '#'))
				{
					++i1;
				}

				cellData[x][y] = new CellData(c, solutionIndex, cellFlagsMap[cellIndex]);
			}
		}

		discardBoolean(inputStream);

		String solutionText = readString(inputStream, i1);

		int solutionTextIndex = 0;

		for (int y = 0; y < height; ++y)
		{
			for (int x = 0; x < width; ++x)
			{
				CellData cell = cellData[x][y];

				if (cell.isBlank())
				{
					cell.solutionCharacter = solutionText.charAt(solutionTextIndex);
					++solutionTextIndex;
				}
			}
		}

		return cellData;
	}
}
