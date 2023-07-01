/**
 * 
 */
package projects.service;



import projects.dao.ProjectDao;
import projects.entity.Project;
/**
 * @author xr100
 *
 */
public class ProjectService {
private ProjectDao projectDao = new ProjectDao();

public Project addproject(Project project) {
	return projectDao.insertProject(project);
}
}

	


