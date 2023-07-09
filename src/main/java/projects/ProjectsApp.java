package projects;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.math.BigDecimal;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

//menu scanner to take user inputs

public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	
	// list of options for users to chose from
	// @formatter:off
	private List<String> operations = List.of(
		
		"1) Add a project",	
		"2) list projects",
		"3) Select a project",
		"4) Update project details",
		"5) Delete a project"
	);
	// @formatter:on

	//method to process the users selection
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();

	}

	private void processUserSelections() {
		boolean done = false;

		while (!done) {
			
			try {
			int selection = getUserSelection();

			
				switch (selection) {
				case -1:
					done = exitMenu();
					break;

				case 1:
					createProject();
					break;
					
				case 2:
					listProjects();
					break;
					
				case 3:
					selectProject();
					break;
					
				case 4:
					updateProjectDetails();
					break;
					
				case 5:
					deleteProject();
					break;
					
				default:
					System.out.println("\n" + selection + " is not a valid selection. Try again.");
					break;
					
					
				}
			}
			catch (Exception e) {
				System.out.println("\n Error:  " + e + " Try again.");
			}
		}

	}

	// selection 5 delete a project and the outputs the user would see
	private void deleteProject() {
		Integer projectId = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was deleted successfully.");
		
		if(Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			curProject = null;
		}
		
	}
	
   //selection 4 update project and the outputs the user would see
	private void updateProjectDetails() {
		if(Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project.");
			return;
		}
		String projectName =
		 getStringInput("Enter the project name[" + curProject.getProjectName() + "]");
		
		BigDecimal estimatedHours =
		 getDecimalInput("Enter the estimated hours[" + curProject.getEstimatedHours() + "]");
		
		BigDecimal actualHours =
		 getDecimalInput("Enter the actual hours +[" + curProject.getActualHours() + "]");
		
		Integer difficulty =
		 getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		
		project.setEstimatedHours(
		 Objects.isNull(projectName) ? curProject.getEstimatedHours() : estimatedHours);
		
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
				
		 		
	}
  // selection 3 select a project and the outputs the user would see
	private void selectProject() {
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
				
		curProject = null;
		
		curProject =projectService.fetchProjectById(projectId);
		
	}
 
	//selection 2 list all projects to user
	private void listProjects() {
     List<Project> projects = projectService.fetchAllProjects();
     
     System.out.println("\nProjects");
     
     projects.forEach(project -> System.out
    		 .println(" " + project.getProjectId()
    		 + ": " + project.getProjectName()));

	}
 
	// selection 1 create  a new project and the outputs the user would see
	private void createProject() {
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String Notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
				
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(Notes);
		
		Project dbProject = projectService.addproject(project);
		System.out.println("You have successfully created project: " + dbProject);
	}
	
	private BigDecimal getDecimalInput(String prompt) {
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
			
		}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
	

	//exit the program and the exit message
	private boolean exitMenu() {
		System.out.println("Exiting the menu.");
		return true;
	}

	//the method to decifer the input from the user
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter new menu selection");

		return Objects.isNull(input) ? -1 : input;
	}
	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
			return null;
		}
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String input =scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
		}
	
	
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the enter key to quit:");
		

		operations.forEach(line -> System.out.println("  " + line));
		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

	
}