package com.amdocs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amdocs.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUserId(long userId);

}
