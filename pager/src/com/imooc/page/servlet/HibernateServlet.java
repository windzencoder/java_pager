package com.imooc.page.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.imooc.page.Constant;
import com.imooc.page.model.Pager;
import com.imooc.page.model.Student;
import com.imooc.page.service.HibernateStudentServiceImpl;
import com.imooc.page.service.JdbcSqlStudentServiceImpl;
import com.imooc.page.service.StudentService;
import com.imooc.page.service.SublistStudentServiceImpl;
import com.imooc.page.util.StringUtil;

public class HibernateServlet extends HttpServlet {
	
	private StudentService studentService = new HibernateStudentServiceImpl();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("hibernateStudent.jsp").forward(req, resp);
	}
	
}
