package com.imooc.page.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imooc.page.Constant;
import com.imooc.page.model.Pager;
import com.imooc.page.model.Student;
import com.imooc.page.service.JdbcSqlStudentServiceImpl;
import com.imooc.page.service.StudentService;
import com.imooc.page.service.SublistStudentServiceImpl;
import com.imooc.page.util.StringUtil;

public class JdbcSqlServlet extends HttpServlet {
	
	private StudentService studentService = new JdbcSqlStudentServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//接收request参数
		String stuName = req.getParameter("stuName");
		
		//性别
		int gender = Constant.DEFAULT_GENDER;
		String genderStr = req.getParameter("gender");
		if (genderStr != null && !"".equals(genderStr.trim())) {
			gender = Integer.parseInt(genderStr);
		}
		
		
		//校验pageNum参数输入合法性
		String pageNumStr = req.getParameter("pageNum");
		if (pageNumStr != null && !StringUtil.isNum(pageNumStr)) {
			req.setAttribute("errorMsg", "参数错误");
			req.getRequestDispatcher("jdbcSqlStudent.jsp").forward(req, resp);
			return;
		}
		
		//显示第几页数据
		int pageNum = Constant.DEFAULT_PAGE_NUM;
		if (pageNumStr != null && !"".equals(pageNumStr.trim())) {
			pageNum = Integer.parseInt(pageNumStr);
		}
		
		//每页显示多少条数据
		int pageSize = Constant.DEFAULT_PAGE_SIZE;
		String pageSizeStr = req.getParameter("pageSize");
		if (pageSizeStr != null && !"".equals(pageSizeStr.trim())) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		
		//组装查询条件
		Student searchModel = new Student();
		searchModel.setStuName(stuName);
		searchModel.setGender(gender);
		//调用service获取查询结果
		Pager<Student> result = studentService.findStudent(searchModel, pageNum, pageSize);
		
		//返回结果给页面
		req.setAttribute("result", result);
		req.setAttribute("stuName", stuName);
		req.setAttribute("gender", gender);
		
		req.getRequestDispatcher("jdbcSqlStudent.jsp").forward(req, resp);
	}
	
}
