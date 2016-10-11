package book.manipulation;

public class BookManipulationException extends Exception {
	String message;
	public BookManipulationException(String msg)
	{
		this.message=msg;
	}
	public String getMessage()
	{
		return message;
	}
}
