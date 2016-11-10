<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

// 点击分页按钮以后触发的动作
function handlePaginationClick(new_page_index, pagination_container) {
    $("#stuForm").attr("action", "<%=context %>/jdbcSql/JdbcSqlServlet?pageNum=" + (new_page_index+1));
    $("#stuForm").submit();
    return false;
}

$(function(){
	$("#News-Pagination").pagination(${result.totalRecord}, {
        items_per_page:${result.pageSize}, // 每页显示多少条记录
        current_page:${result.currentPage} - 1, // 当前显示第几页数据
        num_display_entries:8, // 分页显示的条目数
        next_text:"下一页",
        prev_text:"上一页",
        num_edge_entries:2, // 连接分页主体，显示的条目数
        callback:handlePaginationClick
	});
	
	// 设置学生默认性别
	$("#gender").val("${gender}");
});
</script>
<body>
	<div style="margin-left: 100px; margin-top: 100px;">
		<div>
			<font color="red">${errorMsg }</font>
		</div>
		<div>
			<form action="<%=context %>/jdbcSql/JdbcSqlServlet"   id="stuForm"  method="post">
				姓名
				<input type="text" name="stuName" id="stu_name" style="width:120px" value="${stuName }">
				&nbsp;
				性别
				<select name="gender" id="gender" style="width:80px">
					<option value="0">全部</option>
					<option value="1">男</option>
					<option value="2">女</option>
				</select>
				&nbsp;&nbsp;
				<input type="submit" value="查询">
			</form>
		</div>		
		<br>
		学生信息列表：<br>
		<br>
		<!-- 后台返回结果为空 -->
		<c:if test="${fn:length(result.dataList) eq 0 }">
			<span>查询的结果不存在</span>
		</c:if>
		
		<!-- 后台返回结果不为空 -->
		<c:if test="${fn:length(result.dataList) gt 0 }">
			<table border="1px" cellspacing="0px"
				style="border-collapse: collapse">
				<thead>
					<tr height="30">
						<th width="130">姓名</th>
						<th width="130">性别</th>
						<th width="130">年龄</th>
						<th width="190">家庭地址</th>
					</tr>
				</thead>
					<c:forEach items="${result.dataList }" var="student">
						<tr>
							<td><c:out value="${student.stuName }"></c:out></td>
							<td>
								<c:if test="${ student.gender eq 1}">男</c:if>
								<c:if test="${ student.gender eq 2}">女</c:if>
							</td>
							<td><c:out value="${student.age }"></c:out></td>
							<td><c:out value="${student.address }"></c:out></td>
						</tr>
					</c:forEach>
			</table>
			<br> 
			<div id="News-Pagination"></div>
		</c:if>
	</div>
</body>
</html>