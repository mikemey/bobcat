package uk.mm.bobcat.service.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.mm.bobcat.domain.NameCandidate;

public interface NameCandidateRepository extends MongoRepository<NameCandidate, String> {

	public NameCandidate findByIndex(long index);
}