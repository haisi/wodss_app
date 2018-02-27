package ch.fhnw.wodss.betchampion.repository;

import ch.fhnw.wodss.betchampion.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
