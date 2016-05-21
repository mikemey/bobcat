package uk.mm.bobcat.service.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.mm.bobcat.domain.BobcatUser;

public interface UserRepository extends MongoRepository<BobcatUser, String> {

	BobcatUser findByNameAndPassword(String username, String password);

	BobcatUser findByName(String username);
}
