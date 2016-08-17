package lfkimura.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import lfkimura.model.Trail;;

/**
 * class reposible for access data in MongoDB
 * @author luis
 *
 */
public interface RouteRepository extends MongoRepository<Trail, String> {

	Page<Trail> findAll(Pageable pageable);

	List<Trail> findByMapAndOriginAllIgnoringCase(String map, String origin);

}
