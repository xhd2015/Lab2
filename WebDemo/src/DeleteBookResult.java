import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

import book.manipulation.BookManipulation;

public class DeleteBookResult implements Result{

	@Override
	public void execute(ActionInvocation invocation) throws IOException  {
		
		HttpServletResponse resp=ServletActionContext.getResponse();
		resp.setContentType("text/plain; charset=utf-8");
		PrintWriter stream=resp.getWriter();
		
		Connection conn=BookManipulation.getConnection();
		String ISBN=invocation.getStack().findString("isbn");
		String sqlDelete="Delete From Book Where ISBN=\""+ISBN+"\";";
		int cols=0;
		try {
			cols = conn.createStatement().executeUpdate(sqlDelete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Deleted:"+cols);
		if(cols>0)
		{
			stream.print(BookManipulation.toJSONString("isbn",ISBN));
		}else{
			stream.print(BookManipulation.toJSONString("isbn",-1));
		}
		
		BookManipulation.closeConn(conn);
	}

}
