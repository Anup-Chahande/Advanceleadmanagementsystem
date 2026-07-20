package com.app.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Notifications;
import com.app.Entity.USER;

public interface Notifcationsrepo extends JpaRepository<Notifications, Long>{
	List<Notifications> findByUserOrderByIdDesc(USER user);
}
