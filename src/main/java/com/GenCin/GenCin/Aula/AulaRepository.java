package com.GenCin.GenCin.Aula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;
@RepositoryRestResource(exported = false)
public interface AulaRepository extends JpaRepository<Aula, UUID> {

}
