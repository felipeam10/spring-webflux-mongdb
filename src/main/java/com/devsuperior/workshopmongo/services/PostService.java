package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.dto.PostDTO;
import com.devsuperior.workshopmongo.repositories.PostRepository;
import com.devsuperior.workshopmongo.services.exceptioons.ResourceNotFoundException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class PostService {

	@Autowired
	private PostRepository repository;

	public Mono<PostDTO> findById(String id) {
		return repository.findById(id)
				.map(existPost -> new PostDTO(existPost))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado:")));
	}

	public Flux<PostDTO> findByTitle(String text) {
		return repository.searchTitle(text)
				.map(x -> new PostDTO(x));

	}

	public Flux<PostDTO> fullSearch(String text, Instant minDate, Instant maxDate) {
		maxDate = maxDate.plusSeconds(86400); // 24 * 60 * 60
		return repository.fullSearch(text, minDate, maxDate).map(x -> new PostDTO(x));
	}

	public Flux<PostDTO> findByUser(String id) {
		return repository.findByUser(new ObjectId(id)).map(x -> new PostDTO(x));
	}

}
