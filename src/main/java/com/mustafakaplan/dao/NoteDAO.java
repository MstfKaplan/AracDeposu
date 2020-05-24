package com.mustafakaplan.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mustafakaplan.entity.Notes;

public class NoteDAO 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	// CRUD
	
	public Long insert(Notes note)
	{
		return (Long) sessionFactory.getCurrentSession().save(note);
	}
	
	public void update(Notes note)
	{
		sessionFactory.getCurrentSession().update(note);
	}
	
	public void persist(Notes note) // Id de�eri tabloda varsa o kayd� g�nceller yoksa o kayd� ekler
	{
		sessionFactory.getCurrentSession().persist(note);
	}
	
	public void delete(Notes note)
	{
		sessionFactory.getCurrentSession().delete(note);
	}
	
	public ArrayList<Notes> getAll()
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Notes"); // Note -> tablo ad� de�il class ad�
		
		return (ArrayList<Notes>) query.getResultList();
	}
	
	public ArrayList<Notes> getAll(Long user_id)
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Notes WHERE user_id=:arananId order by id desc")
				.setLong("arananId", user_id);
		
		return (ArrayList<Notes>) query.getResultList();
	}
	
	public Notes getFindByNoteId(Long noteId)
	{
		Query query = sessionFactory.getCurrentSession().createQuery("FROM Notes WHERE id=:arananId")
				.setLong("arananId", noteId);
		
		return (Notes) query.getSingleResult();
	}
}
