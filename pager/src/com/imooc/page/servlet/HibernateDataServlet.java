package com.imooc.page.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.imooc.page.Constant;
import com.imooc.page.model.Pager;
import com.imooc.page.model.Student;
import com.imooc.page.service.HibernateStudentServiceImpl;
import com.imooc.page.service.StudentService;
import com.imooc.page.util.StringUtil;

public class HibernateDataServlet extends HttpServlet {
	
	private StudentService studentService = new HibernateStudentServiceImpl();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 接收request里的参数
		String stuName = request.getParameter("stuName"); //学生姓名
		
		// 获取学生性别
		int gender = Constant.DEFAULT_GENDER;
		String genderStr = request.getParameter("gender");
		if(genderStr!=null && !"".equals(genderStr.trim())){
			gender = Integer.parseInt(genderStr);
		}
		
		// 校验pageNum参数输入合法性
		String pageNumStr = request.getParameter("pageNum"); 
		if(pageNumStr !=null && !StringUtil.isNum(pageNumStr)){
			request.setAttribute("errorMsg", "参数传输错误");
			request.getRequestDispatcher("jdbcSqlStudent.jsp").forward(request, response);
			return;
		}
		
		int pageNum = Constant.DEFAULT_PAGE_NUM; //显示第几页数据
		if(pageNumStr!=null && !"".equals(pageNumStr.trim())){
			pageNum = Integer.parseInt(pageNumStr);
		}
		
		int pageSize = Constant.DEFAULT_PAGE_SIZE;  // 每页显示多少条记录
		String pageSizeStr = request.getParameter("pageSize");
		if(pageSizeStr!=null && !"".equals(pageSizeStr.trim())){
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		// 组装查询条件
		Student searchModel = new Student(); 
		searchModel.setStuName(stuName);
		searchModel.setGender(gender);
		
		//调用service 获取查询结果
		Pager<Student> result = studentService.findStudent(searchModel, 
																pageNum, pageSize);
		//不使用缓存
		response.setHeader("Cache-Control", "no-cache");  
		response.setHeader("Pragma", "no-cache");  
		//设置超时时间为0
		response.setDateHeader("Expires", 0);  
		//设置编码格式为utf-8
		response.setContentType("text/html;charset=utf-8");
		
		//获取查询数据的json格式
		String responseStr = JSON.toJSONString(result);
		//写入数据到response
		Writer writer = response.getWriter();
		writer.write(responseStr);
		writer.flush();
	}
	
	public static void main(String[] args) {
		String s = String.format("%05d", 123);
		System.out.println(s);
	}

}



