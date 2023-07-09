/**
 * 
 */
package projects.service;



import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import projects.exception.DbException;
import projects.dao.ProjectDao;
import projects.entity.Project;
/**
 * @author xr100
 *
 */

// pass through method to the project dao 
public class ProjectService {
private ProjectDao projectDao = new ProjectDao();

//pass through method to add a project
public Project addproject(Project project) {
	return projectDao.insertProject(project);
}

//pass through method to fetch all projects
public List<Project> fetchAllProjects() {
	// @formatter:off
	return projectDao.fetchAllProjects()
	.stream()
	.sorted((p1, p2) -> p1.getProjectId() - p2.getProjectId())
	.collect(Collectors.toList());
	// @formatter:on
}

//pass through method to fetch project by ID
public Project fetchProjectById(Integer projectId) {
	
	return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
			"Project with project ID=" + projectId + " does not exist." ));
}

//pass through method to modify project
public void modifyProjectDetails(Project project) {
	if(!projectDao.modifyProjectDetails(project)) {
		throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
	}
	
}
//pass through method to delete project
public void deleteProject(Integer projectId) {
	if(!projectDao.deleteProject(projectId)) {
		throw new DbException("project with ID=" + projectId + " does not exist.");
	}
}
}

	


