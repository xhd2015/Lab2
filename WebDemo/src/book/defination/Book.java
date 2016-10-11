package book.defination;

import java.util.HashMap;

public class Book {
	private String ISBN;
	private String Title;
	private int AuthorID;
	private String Publisher;
	private String Publish;
	private double Price;
	private String AuthorName;
	public String getAuthorName() {
		return AuthorName;
	}
	

	public void setAuthorName(String authorName) {
		AuthorName = authorName;
	}
	public Book(String iSBN, String title, int authorID, String publisher, String publishDate, double price) {
		super();
		ISBN = iSBN;
		Title = title;
		AuthorID = authorID;
		Publisher = publisher;
		Publish= publishDate;
		Price = price;
	}
	@Override
	public String toString() {
		return "Book [ISBN=" + ISBN + ", Title=" + Title + ", AuthorID=" + AuthorID + ", Publisher=" + Publisher
				+ ", PublishDate=" + Publish + ", Price=" + Price + "]";
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public int getAuthorID() {
		return AuthorID;
	}
	public void setAuthorID(int authorID) {
		AuthorID = authorID;
	}
	public String getPublisher() {
		return Publisher;
	}
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	public String getPublishDate() {
		return Publish;
	}
	public void setPublishDate(String publishDate) {
		Publish = publishDate;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		this.Price = price;
	}
}
