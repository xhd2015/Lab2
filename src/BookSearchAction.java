
//测试url : 
public class BookSearchAction{
	private String type;
	private String text;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String execute()
	{
		System.out.println(getText()+" "+getType());
		return "success";
	}
}
