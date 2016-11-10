package com.imooc.page.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.imooc.page.Constant;
import com.imooc.page.HibernateSessionFactory;
import com.imooc.page.model.Pager;
import com.imooc.page.model.Student;
import com.imooc.page.util.JdbcUtil;

public class HibernateStudentDaoImpl implements StudentDao{

	@SuppressWarnings("unchecked")
	@Override
	public Pager<Student> findStudent(Student searchModel, int pageNum, int pageSize) {
		Pager<Student> result = null;
		
		//存放查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		String stuName = searchModel.getStuName();
		int gender = searchModel.getGender();
		
		StringBuilder hql = new StringBuilder(" from Student where 1=1");
		StringBuilder countHql = new StringBuilder("select count(id) from Student where 1=1");
		
		if(stuName != null && !stuName.equals("")){
			hql.append(" and stuName like :stuName");
			countHql.append(" and stuName like :stuName");
			paramMap.put("stuName","%" + stuName + "%");
		}
		
		if(gender == Constant.GENDER_MALE  || gender == Constant.GENDER_FEMALE){
			hql.append(" and gender = :gender");
			countHql.append(" and gender = :gender");
			paramMap.put("gender",gender);
		}
		
		//起始索引
		int fromIndex = pageSize * (pageNum - 1);
		
		List<Student> studentList = new ArrayList<Student>();
		
		Session session = null;
		
		try {

			session = HibernateSessionFactory.getSession();
			//获取query对象
			Query hqlQuery = session.createQuery(hql.toString());
			Query countHqlQuery = session.createQuery(countHql.toString());
			//设置查询参数
			setQueryParams(hqlQuery, paramMap);
			setQueryParams(countHqlQuery, paramMap);
			
			//从第几条记录开始查询
			hqlQuery.setFirstResult(fromIndex);
			//一共查询多少条记录
			hqlQuery.setMaxResults(pageSize);
			
			//获取查询的结果
			studentList = hqlQuery.list();
			//获取总计条数
			List<?> countResult = countHqlQuery.list();
			
			int totalRecord = ((Number)countResult.get(0)).intValue();
			//获取总页数
			int totalPage = totalRecord / pageSize;
			if (totalRecord % pageSize != 0) {
				totalPage += 1;
			}
			
			//组装pager
			result  = new Pager<>(pageSize, pageNum, totalRecord, totalPage, studentList);
			
			
		} catch (Exception e) {
			throw new RuntimeException("查询所有数据异常!", e);
		}finally {
			if (session != null) {
				HibernateSessionFactory.closeSession();
			}
		}
		
		return result;	
	}
	
	/**
	 * 设置查询参数
	 * @param query
	 * @param paramMap
	 * @return
	 */
	private Query setQueryParams(Query query, Map<String, Object> paramMap){
		if(paramMap != null && !paramMap.isEmpty()){
			for(Map.Entry<String, Object> param : paramMap.entrySet()){
				query.setParameter(param.getKey(), param.getValue());
			}
		}
		return query;
	}

}
