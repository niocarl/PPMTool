package com.carlintelligence.ppmtool.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlintelligence.ppmtool.domain.Backlog;
import com.carlintelligence.ppmtool.domain.Project;
import com.carlintelligence.ppmtool.domain.ProjectTask;
import com.carlintelligence.ppmtool.exceptions.ProjectNotFoundException;
import com.carlintelligence.ppmtool.repositories.BacklogRepository;
import com.carlintelligence.ppmtool.repositories.ProjectRepository;
import com.carlintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		//PT to be a specific project, project != null, BL exist
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
		if (backlog == null) {
			throw new ProjectNotFoundException("Project not found");
		}
		
		//set the BL to PT
		projectTask.setBacklog(backlog);
		
		//project sequence to be like this - IDPRO-1 IDPRO-2
		Integer BacklogSequence = backlog.getPTSequence();
		
		//Update the BL sequence
		BacklogSequence++;
		
		backlog.setPTSequence(BacklogSequence);
		
		//Add sequence to PT
		projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		//INIITAL priority when priority is null
		if(projectTask.getPriority() == null) { //In the future need projectTask.getPriority() == 0 to handle the form
			projectTask.setPriority(3);
		}
		
		//INITIAL status when status is null
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}
		
		return projectTaskRepository.save(projectTask);
	}

	public Iterable<ProjectTask> findBacklogById(String id) {
		
		Project project = projectRepository.findByProjectIdentifier(id);
		
		if (project == null) {
			throw new ProjectNotFoundException("Project with ID: '" + id +"' does not exist");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		
		//make sure searching on existing backlog
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		
		if (backlog == null) {
			throw new ProjectNotFoundException("Project with ID: '" + backlog_id +"' does not exist");
		}
		
		//make sure that task exist
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		
		if (projectTask == null) {
			throw new ProjectNotFoundException("Project Task with ID: '" + pt_id + "' not found");
		}
		
		//make sure that the backlog/project id in the path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task with ID: '" + pt_id + "' does not exist in project: '" + backlog_id);
		}
				
		return projectTask;
	}

}
