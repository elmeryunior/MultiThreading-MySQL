package com.multithreadingmysql.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.multithreadingmysql.entity.Usuario;
import com.multithreadingmysql.repository.UsuarioRepo;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepo repository;
	
	Object target;
	Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Async
	public CompletableFuture<List<Usuario>> saveUsers(MultipartFile file) throws Exception {

		long start = System.currentTimeMillis();
		List<Usuario> users = parseCSVFile(file);
		logger.info("saving list of users of size {}", users.size(), "" + Thread.currentThread().getName());
		users = repository.saveAll(users);
		long end = System.currentTimeMillis();
		logger.info("Total time {}", (end - start));
		return CompletableFuture.completedFuture(users);
	}

	@Async
	public CompletableFuture<List<Usuario>> findAllUsers() {
		logger.info("get list of user by ", Thread.currentThread().getName());
		List<Usuario> users = repository.findAll();
		return CompletableFuture.completedFuture(users);
	}

	private List<Usuario> parseCSVFile(final MultipartFile file) throws Exception {

		final List<Usuario> users = new ArrayList<>();
		try {
			try (final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					final String[] data = line.split(",");
					final Usuario user = new Usuario();
					user.setName(data[0]);
					user.setEmail(data[1]);
					user.setGender(data[2]);
					users.add(user);
				}
				return users;
			}
		} catch (final IOException e) {
			logger.error("Failed to parse CSV file {}", e);
			throw new Exception("Failed to parse CSV file {}", e);
		}
	}
}
