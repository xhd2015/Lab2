function addColumn(which)
		{
			var $parent=$(which).parent().parent();
			var $template=$parent.find(".adding-column-example");
			var $last=$parent.children().last();
			
			var $copied=$template.clone().attr("class","adding-column").show();
			$last.before($copied);
		}
function removeColumn(which)
{
	$(which).parent().remove();
}
//搜索,返回图书信息
function getSearchResult(argBtn)
{
	var argHolder=$(argBtn).parent();
	var type=argHolder.find(".input-type").val();
	var text=argHolder.find(".input-text").val();
	if(!text || !type || type=="---Select Input Type---")
	{
		$("#searchres").find(".prompt").css("display","").html("invalid input type or text.");
	}else{
		$("#searchres").find(".prompt").css("display","none");
		$("#searchres").find(".holder").find(".item-show").remove();
		$.get("/WebDemo/booksearch",{"type":mapping_class[type],"text":text},function(resp){
			
			/*
			*resp=
			[
			{"ISBN":"9787111075660","authorID":1,
				"authorName":"W.Richard Stevens",
				"price":0,"publishDate":"",
				"publisher":"",
				"title":"TCP/IP详解 卷1:协议"}
			]
			**/
		resp=JSON.parse(""+resp);
		
		var $baseitem=$("#searchres").find(".holder").find(".example-show");
		var $item=$baseitem.clone().css("display","").attr("class","item-show");
		var $basestart=$baseitem.next();
		
		
		if(resp.length==0)
		{
			$("#searchres").find(".prompt").css("display","").html("no results for \""+text+"\".");
		}
		else
			for(var i=0;i<resp.length;i++)
			{
				var isbn=resp[i]["ISBN"];
				var authorid=resp[i]["authorID"];
				
				var $toadd=$item.clone().attr("isbn",""+isbn).attr("authorid",authorid);	

				$toadd.find(".book_name").html(resp[i]["title"]);
				$toadd.find(".author_name").html(resp[i]["authorName"]);
				$toadd.find(".publisher").html(resp[i]["publisher"]);
				$toadd.find(".publishyear").html(resp[i]["publishDate"]);
				$toadd.find(".price").html(resp[i]["price"]);
				$toadd.find(".isbn").html(resp[i]["ISBN"]);
	
				
				$basestart.after($toadd);
				$basestart=$toadd;
			}
		});
	}
}

//点击Add按钮的时候,将会提交图书信息,返回值是最终形成的图书信息
function getAddResult(argBtn)
{
	/*add分几种情形
	*前提:ISBN是唯一的,一次添加需要三个必要信息:ISBN Book-Name Author-Name
	*1.ISBN已经存在,失败
	*2.ISBN不存在,Author-Name唯一的存在则添加只图书信息
	*3.ISBN不存在,Author-Name不唯一,需要作者的所有信息来标志,首先添加图书信息,其次,检查作者是否已经存在,不存在则添加作者信息
	**/
	var data={};
	
	for(var i in mapping_class)
	{
		if(i!="---Select Input Type---")
			data[mapping_class[i]]="";
	}
	var $arrs=$(argBtn).parent().find(".adding-column").each(function(){
		var type=$(this).find(".input-type").val();
		
		if(type && type != "---Select Input Type---")
		{
			data[mapping_class[type]]=$(this).find(".input-text").val();	
		}

	});
	/*
	*
	"failed_DatabaseInserationFailed"
	"faild_AuthorNotSelectable"
	"success_ISBNAdded"
	"success_ISBNAuthorAdded"
	"failed_ISBNAlreadyExist"
	**/
	//定义服务/WebDemo/bookadd
	var mapping_resp={
			"failed_DatabaseInserationFailed"		:"internal errorr,try again later.",
			"faild_AuthorNotSelectable"				:"cannot bind a author to the given book,please give more infomation.",
			"success_ISBNAdded"						:"your request is accepted.",
			"success_ISBNAuthorAdded"				:"your request is accepted and a new author is added.",
			"failed_ISBNAlreadyExist"				:"is your ISBN correct?There already exists one."
	};
	$.post("/WebDemo/bookadd",data,function(resp){
		$("#searchres").find(".prompt").html(mapping_resp[resp]).show();
	});
	
}

//点击modify按钮的时候会显示修改的类型
function changeToModify(argBtn)
{
	var $from=$(argBtn).parent();
	var $to=$from.parent().find(".example-modify").clone().removeClass("example-modify").addClass("item-modify");
	
	//change item type,assigne isbn,authorid
	$to.attr("authorid",$from.attr("authorid")).attr("isbn",$from.attr("isbn"));
	var keyarrs=["book_name","author_name","price","publisher","publishyear","isbn"];
	for(var i in keyarrs)
	{
		var s="."+keyarrs[i];
		$to.find(s).val($from.find(s).text());
	}
	$from.after($to);
	$from.hide();
	$to.show();
}
//"book_name","author_name","price","publisher","publishyear","isbn"
function changeToShow(argBtn,if_modified)
{
	var $original=$(argBtn).parent().prev();
	if(!if_modified)
	{
		$(argBtn).parent().remove();
		$original.show();
	}else{
		var keyarrs=["book_name","author_name","price","publisher","publishyear","isbn"];
		var data={};
		//获取输入框中的数据
		for(var i in keyarrs)
		{
			data[keyarrs[i]]=$(argBtn).parent().find("."+keyarrs[i]).val();
		}
		//数据获取完毕
		$.post("/WebDemo/bookupdate",data,function(resp){
		//	"success_newAuthorAdded"   "failed_authorInfoExistsNotOnly"  "success_bookInfoModified"
			var mapping_resp={
					"success_newAuthorAdded"			:"your request is accepted and the author has been also modified.",
					"failed_authorInfoExistsNotOnly"	:"there exists not only one author denoted by your input.",
					"success_bookInfoModified"			:"your request is accepted."
				}
			resp=JSON.parse(resp);
			var type=resp["type"];
			if(type=="failed_authorInfoExistsNotOnly")
				{
					$("#searchres").find(".prompt").html(mapping_resp[type]).show();
					//以没有修改过的逻辑处理
					return changeToShow(argBtn,false);
				}
			else{
				if(type=="success_newAuthorAdded")
				{
					var newid=resp["data"];
					$original.attr("authorid",newid);
				}
				for(var i in keyarrs)
					{
						$original.find("."+keyarrs[i]).html($(argBtn).parent().find("."+keyarrs[i]).val());
					}
				$(argBtn).parent().remove();//移除修改项, 显示show项
				$original.show();
			}
			
			
		});
	}
	
}
function getDeleteResult(argBtn)
{
	//定义/WebDemo/bookdelete?isbn=...
	var isbn=$(argBtn).parent().attr("isbn");
	$.get("/WebDemo/bookdelete",{"isbn":isbn},function(resp){
		resp=JSON.parse(resp);
		if(resp["isbn"]===isbn)
		{
			$("#searchres").find(".prompt").css("display","none");
			$("#searchres").find(".holder").find("[isbn="+isbn+"]").remove();
		}else{
			$("#searchres").find(".prompt").html("ISBN : "+isbn+" has not been deleted.").css("display","");
		}
	});
}