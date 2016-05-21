package uk.mm.bobcat.service.repos;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import uk.mm.bobcat.domain.Rating;

public interface RatingRepository extends MongoRepository<Rating, String> {

	public Rating findByName(String name);

	@Query("{ name: ?0, $or: [ { active: true }, { active: { $exists: false } } ] }")
	public Rating findByNameAndActive(String name);

	@Query("{ $or: [ { active: true }, { active: { $exists: false } } ] }")
	public List<Rating> findActiveRanking(Sort sort);
}