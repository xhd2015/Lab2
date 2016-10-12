package book.manipulation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

public class BookManipulation {
	
	public static final String DBName="bookdb";//"bookdb";
	public static final String DBAddr="********************";//"mcroechsltjj.rds.sae.sina.com.cn";
	public static final String DBPort="****";//"10296";
	public static final String DBUser="root";
	public static final String DBPassword="******";
	public static Connection getConnection()
	{
		Connection conn=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch(InstantiationException | IllegalAccessException | ClassNotFoundException e){
			e.printStackTrace();
			return null;
		}
		try {
			/*				"jdbc:mysql://localhost/BookDB3?"+
			"useUnicode=true&characterEncoding=utf8&"+
			"useSSL=true&user=root&password=123123"
			*/
			String connection="jdbc:mysql://"+DBAddr+(DBPort.isEmpty()?"":":"+DBPort)+"/"+DBName+"?"+
					"useUnicode=true&characterEncoding=utf8&"+
					"useSSL=true&user="+DBUser+"&password="+DBPassword;
			conn=DriverManager.getConnection(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("get conn is null");
			conn=null;
		}
		return conn;
	}
	
	public static void main(String[] args)
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("End");
	}
	public static void closeConn(Connection conn)
	{
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ResultSet query(Connection conn,String sql)
	{
		Statement s;
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		try {
			return s.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public static int countQuery(ResultSet rs)
	{
		int i=0;
		try {
			while(rs.next())
			{
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	public static ArrayList<ArrayList<String>> dumpResult(ResultSet rs,int cols) throws Exception
	{
		ArrayList<ArrayList<String>> resarr=new ArrayList<>();
		while(rs.next())
		{
			ArrayList<String> temp=new ArrayList<>();
			for(int i=0;i<cols;i++)
			{
				temp.add(rs.getString(i+1));
			}
			resarr.add(temp);
		}
		
		return resarr;
	}
	public static String toJSONString(Object...keyAndvalues)
	{
		TreeMap<String,Object> m=new TreeMap<>();
		for(int i=0;i<keyAndvalues.length;i+=2)
		{
			if(i<keyAndvalues.length)
			{
				m.put(keyAndvalues[i].toString(), keyAndvalues[i+1]);
			}
		}
		return JSONObject.fromObject(m).toString();
	}
	public static int findMaxAuthorID(Statement stmt) throws SQLException
	{
		String sqlMaxID="Select Max(AuthorID) From Author;";
		ResultSet rs=stmt.executeQuery(sqlMaxID);
		if(rs.next())
		{
			return rs.getInt(1);
		}
		else{
			return 1;
		}
	}
	//id name -- necessary
	//age could be "",null
	public static void addAuthor(Statement stmt,String id,String name,String age,String country) throws SQLException
	{
		if(age==null)age="";
		if(country==null)country="";
		String sqlInsert="Insert Into Author Values("+
					id+",\""+
					name+"\","+
					age+",\""+country+"\");";
		stmt.executeUpdate(sqlInsert);
	}
	public static void updateBook(Statement stmt,String isbn,String title,String authorid,String publisher,String publishDate,String price) throws SQLException
	{
		String sqlUpdate="Update Book Set "+
				"ISBN=\""+isbn+"\","+
				"Title=\""+title+"\","+
				"AuthorID="+authorid+","+
				"Publisher=\""+publisher+"\","+
				"Publish=\""+publishDate+"\","+
				"Price="+price+" "+
				"Where ISBN=\""+isbn+"\";";
		stmt.executeUpdate(sqlUpdate);
	}
	public static String[] findAuthorID(Statement stmt,String name,String...args) throws Exception//返回Author的ID
	{
		int rtn=-1;
		String sqlQuery="Select AuthorID From Author Where Name=\""+name+"\"";
		int sz=args.length;
		if(sz>=1 && !args[0].isEmpty())
		{
			sqlQuery+=",Age="+args[0];
		}
		if(sz>=2 && !args[1].isEmpty())
		{
			sqlQuery+=",Country=\""+args[1]+"\"";
		}
		
		sqlQuery+=";";
		ArrayList<ArrayList<String>> res = dumpResult(stmt.executeQuery(sqlQuery),1);
		if(res.size()==0)
		{
			return new String[]{"notexist",""+(findMaxAuthorID(stmt)+1)};
		}else if(res.size()==1)
		{
			return new String[]{"alreadyexist",res.get(0).get(0)};
		}else{
			return new String[]{"multiexist","-1"};
			//throw new SQLException("Cannot locate the author with given infomation, sql is "+sqlQuery);
		}
		//notexist alreadyexist multiexist
	}
}
