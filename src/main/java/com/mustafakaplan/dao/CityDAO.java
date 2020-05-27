package com.mustafakaplan.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mustafakaplan.entity.Cities;

public class CityDAO 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public ArrayList<Cities> getAll()
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Cities order by id"); // tablo ad� de�il class ad�
		
		return (ArrayList<Cities>) query.getResultList();
	}
}
