package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.dto.UserDTO;
import com.devsuperior.workshopmongo.entities.User;
import com.devsuperior.workshopmongo.repositories.UserRepository;
import com.devsuperior.workshopmongo.services.exceptioons.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;

	public Flux<UserDTO> findAll() {
		return repository.findAll().map(user -> new UserDTO(user));
	}

	public Mono<UserDTO> findById(String id) {
		return repository.findById(id)
				.map(existUser -> new UserDTO(existUser))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado:")));
	}

	public Mono<UserDTO> insert(UserDTO dto) {
		User entity = new User();
		copyToEntity(dto, entity);
		Mono<UserDTO> result = repository.save(entity)
				.map(user -> new UserDTO(user));
		return result;
	}

	private void copyToEntity(UserDTO dto, User entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
	}
}
