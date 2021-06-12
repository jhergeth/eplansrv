package de.bkgk.domain;

import io.micronaut.context.annotation.Executable;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository
public abstract class KlasseRepository implements CrudRepository<Klasse, String> {
}
