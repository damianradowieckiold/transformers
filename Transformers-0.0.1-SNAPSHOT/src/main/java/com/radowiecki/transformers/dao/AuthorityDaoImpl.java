package com.radowiecki.transformers.dao;

import com.radowiecki.transformers.model.Authority;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthorityDaoImpl implements AuthorityDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public List<Authority> getAll() {
		List<Authority> authorities = this.sessionFactory.getCurrentSession().createQuery("from Authority").list();
		return authorities;
	}
}