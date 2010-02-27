package info.rikkus.indx;

class CellData
{
	char cellContent;
	char solutionCharacter;
	int solutionIndex;
	byte flags;

	CellData
		(
			char cellContent,
			int solutionIndex,
			byte flags
		)
	{
		this.cellContent = cellContent;
		this.solutionIndex = solutionIndex;
		this.flags = flags;
	}

	boolean isBlank()
	{
		return ((this.cellContent != '#') && (this.cellContent != ' '));
	}
}