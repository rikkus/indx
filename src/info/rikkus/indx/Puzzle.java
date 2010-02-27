package info.rikkus.indx;

import java.util.SortedMap;

public class Puzzle
{
	public final SortedMap<Integer, String> acrossClues;
	public final SortedMap<Integer, String> downClues;
	public final int width;
	public final int height;
	public final CellData[][] gridData;

	public Puzzle(
		int width,
		int height,
		CellData[][] gridData,
		SortedMap<Integer, String> acrossClues,
		SortedMap<Integer, String> downClues
	)
	{
		this.width = width;
		this.height = height;
		this.gridData = gridData;
		this.acrossClues = acrossClues;
		this.downClues = downClues;
	}
}

