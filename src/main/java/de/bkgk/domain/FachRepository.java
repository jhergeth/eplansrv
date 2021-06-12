package de.bkgk.domain;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository
public interface FachRepository extends CrudRepository<Fach, String> {
}
