
public class BookUpdateAction {
	private String book_name,author_name,isbn;
	private double price;
	private String publisher,publishyear;
	public String execute()
	{
		return "success";
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getAuthor_name() {
		return author_name;
	}
	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishyear() {
		return publishyear;
	}
	public void setPublishyear(String publishyear) {
		this.publishyear = publishyear;
	}
}

