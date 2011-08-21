package se.forsslundsystems.fsbusiness;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Invoice {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
	private int id;
	
	public int getId() { return id; }
	
	@Persistent
	private Key customer; // Not type "Customer"!! 
	
	@Persistent
	private float amount;
	
	@Persistent 
	private Date invoiceDate;
	
	@Persistent
	private Date paymentDate;
	
	
	public Invoice(Key customerKey, float amount, Date invoiceDate, Date paymentDate){
		this.customer =  customerKey;
		this.amount = amount;
		this.invoiceDate = invoiceDate;
		this.paymentDate = paymentDate;
		this.id = 17;//company.getNextInvoiceIdAndIncrement();
	}


	public Customer getCustomer() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
	    return pm.getObjectById(Customer.class, customer);
	}
	
	public float getAmount() {
		return amount;
	}
	
	public Date getInvoiceDate(){
		return invoiceDate;
	}
	
	public Date getPaymentDate(){
		return paymentDate;
	}


	
	

}
