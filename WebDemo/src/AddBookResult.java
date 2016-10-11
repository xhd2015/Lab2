import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

import book.manipulation.BookManipulation;

public class AddBookResult implements Result{
	
	/**
	 * *返回的JSON数据类型的结构:
			*{
			*	type:
			*}
			*type=1,success_Book成功添加图书信息,data=java.Book
			*type=2,success_BookAuthor成功添加图书信息和作者信息
			*type=3,failed_ISBNAlreadyExist,failed_ISBNAlreadyExist;failed_AuthorInfoLessToAdd,faild_AuthorNotSelectable
			*错误,data(info):图书已经存在,无需添加 
			*						图书作者不存在,需要添加作者信息,但作者信息不完整
			*					  图书作者不唯一,无法选择作者,请给定更多限定信息
	 * @throws Exception 
	 */
	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		
		System.out.println("in add result");
		Connection conn=BookManipulation.getConnection();
		if(conn==null)
		{
			throw new Exception("[add result]connection got is null.");
		}
		//只要能提供一个get信息,都能执行操作
		BookAddAction bad=(BookAddAction)invocation.getAction();
		String isbn=bad.getIsbn();
		String title=bad.getBook_name();
		Statement stmt;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new Exception("[add result] get statement error.");
		}
		System.out.println(isbn);//OK
		ResultSet rs;
		try {
			rs = stmt.
				executeQuery("Select ISBN From Book Where ISBN=\""+isbn+"\";");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new Exception("[add result] execute query error.");
		}
		int counts=BookManipulation.countQuery(rs);
		String rtnType="";
		if(counts>0)//第一种情况:已经存在ISBN
		{
			rtnType="failed_ISBNAlreadyExist";
		}else{
			System.out.println("in else");
			String name=bad.getAuthor_name();
			int age=bad.getAuthor_age();
			String country=bad.getAuthor_country();
			String sqlExec="Select AuthorID From Author Where ";
			sqlExec+="Name=\""+name+"\" ";
			if(age>0)
			{
				sqlExec+="and Age="+age+" ";
			}
			if(!country.isEmpty())
			{
				sqlExec+="and Country=\""+country+"\" ";
			}
			sqlExec+=";";
			System.out.println(sqlExec);
			try {
				rs=stmt.executeQuery(sqlExec);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new Exception("[add result] execute query for AuthorID failed.");
			}
			System.out.println("after rs1");
			ArrayList<ArrayList<String>> resarr;
			try {
				resarr = BookManipulation.dumpResult(rs, 1);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				throw new Exception("[add result] dump res failed.");
			}
			if(resarr.size()<=1)
			{
				String id="";
				if(resarr.size()==0)//第二种情况:作者是不存在的,除了添加图书信息,还需添加作者信息
				{
					//作者尚不存在
					//使用max来更新
					System.out.println("Not exist");
					try{
						rs=stmt.executeQuery("Select Max(AuthorID) From Author;");
					}catch(Exception e)
					{
						System.out.println("Error insert author");
						e.printStackTrace();
					}
					int newid=1;
					System.out.println("in set newid, rs is"+rs);
					try {
						resarr=BookManipulation.dumpResult(
								rs, 1
								);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new Exception("[add result] dump res failed.");
					}
					if(resarr.size()>0 && resarr.get(0).get(0)!=null){
						System.out.println(resarr.get(0).get(0));
						newid=Integer.parseInt(resarr.get(0).get(0))+1;
					}
					System.out.println("after set newid");
					
					
					String authorSqlInsert="Insert Into Author Values("+
							newid+",\""+name+"\","+age+",\""+country
							+ "\");";
					System.out.println(authorSqlInsert);
					stmt.executeUpdate(authorSqlInsert);
					id+=newid;
					System.out.println(newid);
					rtnType="success_ISBNAuthorAdded";
				}else{
					id=resarr.get(0).get(0);
				}
				String sqlInsert="Insert Into Book(ISBN,Title,AuthorID,Publisher,Publish,Price) VALUES({Vals});";
				String Vals="";
				Vals+="\""+isbn+"\",";
				Vals+="\""+title+"\",";
				Vals+=id+",";
				Vals+="\""+bad.getPublisher()+"\",";
				String date=bad.getPublishyear();
				if(date.length()!=4)
				{
					date="0000";
				}
				Vals+="\""+date+"0101\",";
				Vals+=bad.getPrice();
				sqlInsert=sqlInsert.replace("{Vals}", Vals);
			
				System.out.println(sqlInsert);
				int rows=stmt.executeUpdate(sqlInsert);
				if(rows==1)
				{
					if(rtnType.isEmpty())
						rtnType="success_ISBNAdded";
				}else{
					rtnType="failed_DatabaseInserationFailed";
				}
			}else{//第四种情况:作者不唯一,无法选定,返回错误情况
				rtnType="faild_AuthorNotSelectable";
			}
			
		}
		ServletActionContext.getResponse().setContentType("text/plain; charset=utf-8");
		PrintWriter stream=ServletActionContext.getResponse().getWriter();
		stream.print(rtnType);
		
		rs.close();
		stmt.close();
		BookManipulation.closeConn(conn);
		System.out.println("type:"+rtnType);
	}

}
