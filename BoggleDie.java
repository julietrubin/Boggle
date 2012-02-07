public class BoggleDie
{
	String letters;
	int facing;
	public BoggleDie(String letters, int facing)
	{
		if (letters.length() != 6 || facing < 0 || facing > 5)
		{
			throw new IllegalArgumentException("Die Constructor");  
		}
		this.letters = letters;
		this.facing = facing;

	}
	public BoggleDie(String letters)
	{
		this(letters,0);        
	}

	public char getTop()
	{
		return letters.charAt(facing);
	}

	public void setFacing(int facing)
	{
		this.facing = facing;
	}    
}
