package com.carlintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlintelligence.ppmtool.domain.Backlog;
import com.carlintelligence.ppmtool.domain.Project;
import com.carlintelligence.ppmtool.exceptions.ProjectIDException;
import com.carlintelligence.ppmtool.repositories.BacklogRepository;
import com.carlintelligence.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	
	public Project saveOrUpdateProject(Project project) {
		
		String projectIdentifierUpperCase = project.getProjectIdentifier().toUpperCase();
		
		try {
			project.setProjectIdentifier(projectIdentifierUpperCase);
			
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(projectIdentifierUpperCase);
			}
			
			if(project.getId() != null) {
				project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifierUpperCase));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIDException("Project ID '" + projectIdentifierUpperCase +"' already exists");
		}
	}
	
	public Project findProjectByIdentifier(String projectId) {
		
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if (project == null) {
			throw new ProjectIDException("Project does not exists");
		}
			
		return project;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIDException("Cannot Delete Project with '" + projectId + "'. This project does not exists");
		}
		
		projectRepository.delete(project);
	}
}
