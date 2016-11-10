<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生信息</title>
</head>
<%
	// 获取请求的上下文
	String context = request.getContextPath();
%>
<link href="../css/pagination.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="../js/jquery-1.11.3.js"></script>
<script type="text/javascript" src="../js/jquery.pagination.js"></script>
<script type="text/javascript">
// 第一步：页面渲染完之后，请求后台，获取学生数据，加载学生信息
// 第二步：完成学生查询功能
// 第三步：下拉加载更多数据的功能

var url = "<%=context%>/hibernate/HibernateDataServlet"; // 请求获取数据的url

//一共有多少页数据
var totalPage = 1;
//每页显示多少条数据
var pageSize = 30;

var isLoading = false;

// 页面渲染完之中执行的代码
$(function(){
	// 绑定事件，监听滚动条下拉的动作
	bindScrollEvent();
	
	// 请求后台，加载学生信息
	queryStudent(1);
});

function queryForm(){
	
	// 绑定事件，监听滚动条下拉的动作
	bindScrollEvent();
	
	// 清空学生的数据
	$("#studentDataBody").html("");
	
	// 清空学生为空的提示信息
	$("#emptyInfo").html("");
	
	// 查询学生
	queryStudent(1);
}


//请求后台，加载学生信息
function queryStudent(pageNum){	
	
	if(pageNum > totalPage){
		$("#emptyInfo").html("没有更多的学生数据了......");
		$(window).unbind("scroll");
	}
	if(isLoading){
		return;
	}else{
		isLoading = true; // 修改状态为正在加载数据
	}
	
	var stuName = $("#stu_name").val(); // 获取学生姓名
	var gender = $("#gender").val(); // 获取学生的性别

	$("#loading").show();
	
	// 进行post请求
	$.post(url,{"pageSize":pageSize,"pageNum":pageNum, "stuName":stuName, "gender":gender},function(data){
		// 加载学生信息
		totalPage = data.totalPage; // 一共有多少页数据
		currentPage = data.currentPage; // 获取当前第几页数据
		var stuentList = data.dataList; // 学生记录信息
		if(totalPage == 0){
			$("#emptyInfo").html("没有更多的学生数据了......");
		}
		//加载学生的数据
		showStudentData(stuentList);
		
		isLoading = false;
	},"json");
}

//加载学生的数据;
function showStudentData(stuentList){
	$("#loading").hide();
	var studentDataHtml = "";
	$.each(stuentList,function(idx, obj){
		studentDataHtml += "<tr>" ;
		studentDataHtml += "<td>" + obj.stuName + "</td>"; // 获取学生姓名的值
		if(obj.gender == 1){
			studentDataHtml += "<td>男</td>"; // 获取学生性别的值
		}else{
			studentDataHtml += "<td>女</td>"; // 获取学生性别的值
		}
		studentDataHtml += "<td>" + obj.age + "</td>"; // 获取学生年龄的值
		studentDataHtml += "<td>" + obj.address + "</td>"; // 获取学生住址的值
		studentDataHtml += "</tr>";
	});
	$("#studentDataBody").append(studentDataHtml);
}

function bindScrollEvent(){
	// 添加滚动监听事件
	$(window).scroll( function() {
			var docHeight = $(document).height(); // 获取整个页面的高度
			var winHeight = $(window).height(); // 获取当前窗体的高度
			var winScrollHeight = $(window).scrollTop(); // 获取滚动条滚动的距离
			if(docHeight -30 <= winHeight + winScrollHeight){
				// 加载更多的学生数据
				queryStudent(currentPage+1);
			}
			
	} );
}

</script>
<body>
	<div style="margin-left: 100px; margin-top: 100px;">
		<div>
			<font color="red">${errorMsg }</font>
		</div>
		<div>
			<form action="#" id="stuForm"  method="post" >
				姓名
				<input type="text" name="stuName" id="stu_name" style="width:120px" value="${stuName }">
				&nbsp;
				性别
				<select name="gender" id="gender" style="width:80px">
					<option value="0" selected="selected">全部</option>
					<option value="1">男</option>
					<option value="2">女</option>
				</select>
				&nbsp;&nbsp;
				<input type="button" value="查询"  onclick="queryForm()">
			</form>
		</div>		
		<br>
		学生信息列表：<br>
		<br>
		<!-- 后台返回结果不为空 -->
			<table border="1px" cellspacing="0px"
				style="border-collapse: collapse" id="studentTable">
				<thead>
					<tr height="30">
						<th width="130">姓名</th>
						<th width="130">性别</th>
						<th width="130">年龄</th>
						<th width="190">家庭地址</th>
					</tr>
				</thead>	
				<tbody id="studentDataBody">
				</tbody>
			</table>
				<img id="loading"  style="display:none;" alt="loading...." src="../images/loading.gif">
			<br>
	</div>
</body>
</html>