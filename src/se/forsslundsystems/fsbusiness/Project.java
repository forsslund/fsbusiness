package se.forsslundsystems.fsbusiness;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Project {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

	@Persistent
	private String name;
	
	@Persistent
    @Element(dependent = "true")
	private List<Invoice> generatedInvoices;
	
	@Persistent
    @Element(dependent = "true")
	private List<Customer> customers;

	public List<Customer> getCustomers() { return customers; }


	
	public List<Invoice> getGeneratedInvoices(){
		return generatedInvoices;
	}
	
	@Persistent
	private boolean active;
	
	@Persistent
	private boolean recurringInvocing;
	
	@Persistent
	private int daysOfInvocingPeriod;
	
	@Persistent
	private Date startDate;
	
	@Persistent
	private float standardInvoiceAmount;
	
	
	
//  This can be found in invoiced
//	@Persistent
//	private Date lastInvoiceDate;
	

	
	public int daysUntilNextInvoice(Date today){
		// if not recurring, invoice in 10 years
		if(!recurringInvocing)
			return 365*10;		
		
		// Figure out last date of invoicing
		Date lastInvoiceDate = getStartDate();
		if(!getGeneratedInvoices().isEmpty()){
			Invoice lastInvoice = getGeneratedInvoices().get(getGeneratedInvoices().size()-1);
			lastInvoiceDate = lastInvoice.getInvoiceDate();			
		}

		Date nextDate = new Date(lastInvoiceDate.getTime()+getDaysOfInvocingPeriod()*3600*1000*24L);
		return (int)((nextDate.getTime()-today.getTime())/(3600*1000*24L));		
	}
	
	/**
	 * @return Number of invoices created
	 */
	int checkDoInvocing(Company company, Date checkDate) {

		if(!isRecurringInvocing()){
			return 0;
		}
		
		if(daysUntilNextInvoice(checkDate) <= 0){
			// OK, time for invoice!
			int invoices=0;
			for(Customer c : getCustomers()){
				Invoice i = new Invoice(c.getKey(),getStandardInvoiceAmount(),checkDate,new Date(checkDate.getTime()+30*3600*1000*24L));
				this.fileInvoice(i);
				invoices++;
			}			
			
			return invoices;
		}
		
		return 0;
	}
	
	
	// Create new project
	public Project(String name, Date startDate){
		this.name = name;
		this.active = true;		
		this.recurringInvocing = false;
		this.daysOfInvocingPeriod = 0;
		this.startDate = startDate;
		this.customers = new ArrayList<Customer>();
		this.generatedInvoices = new ArrayList<Invoice>();
	}
	
	public void setRecurring(int daysOfInvocingPeriod){
		this.daysOfInvocingPeriod = daysOfInvocingPeriod;
		this.recurringInvocing = true;
	}
	
	public String getName(){ return name; }
	public boolean isActive() { return active; }
	public boolean isRecurringInvocing() { return recurringInvocing; }
	public Date getStartDate() { return startDate; }
	public int getDaysOfInvocingPeriod() { return daysOfInvocingPeriod; }
		
	public void addCustomer(Customer customer){
		customers.add(customer);
		// We need to make persistent..?
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		    pm.makePersistent(customer);
		} finally {
		    pm.close();
		}		
	}
	public void fileInvoice(Invoice invoice){
		generatedInvoices.add(invoice);
	}
	
	public float getStandardInvoiceAmount() {
		return standardInvoiceAmount;
	}
	public void setStandardInvoiceAmount(float standardInvoiceAmount) {
		this.standardInvoiceAmount = standardInvoiceAmount;
	}

}
