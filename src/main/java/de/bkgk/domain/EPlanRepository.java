package de.bkgk.domain;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@JdbcRepository
public interface EPlanRepository extends CrudRepository<EPlan, Long> {
    public List<EPlan> findBySchuleAndBereich(String schule, String bereich);
    public void deleteBySchuleLikeAndBereichLike(String schule, String bereich);

    public List<EPlan> findBySchuleAndKlasse(String schule, String klasse);
    public List<EPlan> findBySchuleAndLehrer(String schule, String lehrer);
    public List<EPlan> findBySchuleAndFach(String schule, String fach);
}
