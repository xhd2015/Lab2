import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public class MessageResult implements Result{
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String message;
	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		ServletActionContext.getResponse().getWriter().println(getMessage());
	}

}
