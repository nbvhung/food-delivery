package com.nhom.fooddelivery.repository;
import com.nhom.fooddelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByPhone(String phone);
}