package com.carlintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carlintelligence.ppmtool.domain.Backlog;
import com.carlintelligence.ppmtool.domain.ProjectTask;
import com.carlintelligence.ppmtool.repositories.BacklogRepository;
import com.carlintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		
		//PT to be a specific project, project != null, BL exist
		Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		
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

}
