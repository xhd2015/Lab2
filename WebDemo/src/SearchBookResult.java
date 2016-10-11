

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;

import book.manipulation.BookManipulation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SearchBookResult implements Result{

	@Override
	public void execute(ActionInvocation invocation) throws IOException {
		// TODO Auto-generated method stub
		ServletActionContext.getResponse().setContentType("text/plain; charset=utf-8");
		
		Connection conn=BookManipulation.getConnection();
		if(conn==null)return;
		PrintWriter stream = ServletActionContext.getResponse().getWriter();
		
		ValueStack vs=invocation.getStack();
		String type=(String)vs.findString("type");
		String text=(String)vs.findString("text");
		System.out.println(text);
		//根据开放数据接口 , 搜索者提供type和text两个属性进行搜索
		String searchCol="";
		String searchTable="Book , Author";
		String searchValue="";
		String searchOp="like";
		if(type.equals("book_name"))
		{
			searchCol="Title";
			searchValue="\"%"+text+"%\"";
		}else if(type.equals("author_name"))
		{
			searchCol="Name";
			searchValue="\"%"+text+"%\"";
		}else if(type.equals("isbn")){
			searchCol="ISBN";
			searchValue="\"%"+text+"%\"";
		}else if(type.equals("publishyear")){
			searchCol="Publish";
			searchOp="=";
			searchValue=text;
		}
		
		String sqlExec=
		"Select * From {table} where Book.AuthorID=Author.AuthorID and {type} {op} {value};";
		
		sqlExec=sqlExec.replace("{table}", searchTable);
		sqlExec=sqlExec.replace("{type}", searchCol);
		sqlExec=sqlExec.replace("{value}", searchValue);
		sqlExec=sqlExec.replace("{op}", searchOp);
		System.out.println(sqlExec);
		//ISBN Title	AuthorID	Publisher Publish	Price , AuthorID Name Age Country
		ResultSet rs=BookManipulation.query(conn, sqlExec);
		JSONArray jrs=new JSONArray();
		if(rs!=null)
		{
			try {
				while(rs.next())
				{
					
					book.defination.Book b=
							new book.defination.Book(
									rs.getString("ISBN"),
									rs.getString("Title"),
									rs.getInt("Book.AuthorID"),
									rs.getString("Publisher"),
									rs.getString("Publish"),
									rs.getDouble("Price"));
					b.setAuthorName(rs.getString("Author.name"));
					jrs.add(b);
					
				}
			} catch (SQLException e) {
				//do nothing
			}
		}
		System.out.print(jrs);
		stream.print(jrs);
		
		BookManipulation.closeConn(conn);
	}
}
