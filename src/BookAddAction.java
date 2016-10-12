
/**
 * 			"---Select Input Type---" 	:"prompt",
			"Book Name"					:"book_name",
			"Author Name"				:"author_name",
			"ISBN"						:"isbn",
			"Price"						:"price",
			"Publisher"					:"publisher",
			"Publish Year"				:"publishyear",
			"Author Age"				:"author_age",
			"Author Country"			:"author_country",
			"Author ID"					:"author_id"
 * @author x
 *
 */
public class BookAddAction {
	private String book_name,author_name,isbn;
	private double price;
	private String publisher,publishyear;
	private int 	author_age;
	private String author_country;
	private int		author_id;
	
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
	public int getAuthor_age() {
		return author_age;
	}
	public void setAuthor_age(int author_age) {
		this.author_age = author_age;
	}
	public String getAuthor_country() {
		return author_country;
	}
	public void setAuthor_country(String author_country) {
		this.author_country = author_country;
	}
	public int getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	
	public String execute()
	{
		System.out.println("in add action");
		return "success";
	}
}
