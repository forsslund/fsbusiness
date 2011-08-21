package se.forsslundsystems.fsbusiness;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

// The main company class
@PersistenceCapable
public class Company {
	

	@PrimaryKey
	@Persistent
	private String name;
	
	@Persistent
    //@Element(dependent = "true") 
	private List<Project> projects;
	
	public Company(String name){
		this.name=name;
		projects = new ArrayList<Project>();
	}
	
	public String getName() { return name; }

	@Persistent
	private int nextInvoiceId = 101;
	public int getNextInvoiceIdAndIncrement() {
		nextInvoiceId++;
		return nextInvoiceId-1; 
	}
	
	
	public void addProject(Project p){
		projects.add(p);
	}
	
	public List<Project> getProjectList(){
		return projects;
	}
	
	/**
	 * 
	 * @param today
	 * @return number of invoices created (all projects)
	 */
	public int checkAndDoInvoicing(Date today){
		int invoices=0;
		for(Project p : projects){
			invoices += p.checkDoInvocing(this, today);
		}
		return invoices;
	}
	
	
	

}
