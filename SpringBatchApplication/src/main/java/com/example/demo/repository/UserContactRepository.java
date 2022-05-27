package com.example.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.dto.User;
import com.example.demo.dto.UserContact;

public interface UserContactRepository extends JpaRepository<UserContact, String> {
}
