package com.carlintelligence.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.carlintelligence.ppmtool.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{

	
}
