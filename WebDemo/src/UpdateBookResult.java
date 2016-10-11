import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

import book.manipulation.BookManipulation;

public class UpdateBookResult implements Result{

	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		
		BookUpdateAction action=(BookUpdateAction) invocation.getAction();
		
		String isbn=action.getIsbn();
		//String[] keys=new String[]{"book_name","author_name","price","publisher","publishyear","isbn"};
		Connection conn=BookManipulation.getConnection();
		Statement stmt=conn.createStatement();
		
		String rtnType="";
		String rtnData="";
		String[] res=BookManipulation.findAuthorID(stmt, action.getAuthor_name());
		//notexist alreadyexist multiexist
		if(res[0].equals("notexist"))
		{
			//插入新的作者
			BookManipulation.addAuthor(stmt,res[1], action.getAuthor_name(), "0", "");
			rtnType="success_newAuthorAdded";
			rtnData=res[1];
		}else if(res[0].equals("multiexist"))
		{
			rtnType="failed_authorInfoExistsNotOnly";
		}
		
		if(!res[0].equals("multiexist"))
		{
			if(rtnType.isEmpty())
			{
				rtnType="success_bookInfoModified";
			}
			BookManipulation.updateBook(stmt, isbn, action.getBook_name(), res[1], action.getPublisher(), action.getPublishyear(), ""+action.getPrice());
		}
		ServletActionContext.getResponse().setContentType("text/plain; charset=utf-8");
		PrintWriter stream=ServletActionContext.getResponse().getWriter();
		//"success_newAuthorAdded"   "failed_authorInfoExistsNotOnly"  "success_bookInfoModified"
		stream.print(BookManipulation.toJSONString("type",rtnType,"data",rtnData));
		
	}

}
