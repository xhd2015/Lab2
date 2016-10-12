package book.defination;

public class Author {

	public Author(int authorID, String name, int age, String country) {
		super();
		AuthorID = authorID;
		Name = name;
		Age = age;
		Country = country;
	}
	@Override
	public String toString() {
		return "Author [AuthorID=" + AuthorID + ", Name=" + Name + ", Age=" + Age + ", Country=" + Country + "]";
	}
	public int getAuthorID() {
		return AuthorID;
	}
	public void setAuthorID(int authorID) {
		AuthorID = authorID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public String getCountry() {
		return Country;
	}
	public void setCountry(String country) {
		Country = country;
	}
	private int AuthorID;
	private String Name;
	private int Age;
	private String Country;
}
