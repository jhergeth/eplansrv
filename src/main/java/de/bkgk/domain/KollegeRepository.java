package de.bkgk.domain;

import edu.umd.cs.findbugs.annotations.CreatesObligation;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository(dialect = Dialect.H2)
public abstract class KollegeRepository implements CrudRepository<Kollege, String> {
    abstract List<Kollege> find();
}
