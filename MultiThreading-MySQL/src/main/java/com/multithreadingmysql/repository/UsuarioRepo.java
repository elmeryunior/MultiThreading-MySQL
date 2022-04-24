package com.multithreadingmysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multithreadingmysql.entity.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer>{

}
