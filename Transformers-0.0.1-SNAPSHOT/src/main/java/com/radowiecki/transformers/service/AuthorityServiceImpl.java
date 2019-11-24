package com.radowiecki.transformers.service;

import com.radowiecki.transformers.dao.AuthorityDao;
import com.radowiecki.transformers.model.Authority;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	private AuthorityDao authorityDao;

	public Authority getByName(String authorityName) {
		List<Authority> allAuthorities = this.authorityDao.getAll();
		List<Authority> filteredAuthorities = (List) allAuthorities.stream().filter((a) -> {
			return a.getAuthority().equals(authorityName);
		}).collect(Collectors.toList());
		return filteredAuthorities.size() > 0
				? (Authority) filteredAuthorities.get(0)
				: (Authority) filteredAuthorities.get(1);
	}
}