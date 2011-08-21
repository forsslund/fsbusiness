package se.forsslundsystems.fsbusiness;

import java.util.Date;

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
	private Customer customer;
	
	@Persistent
	private float amount;
	
	@Persistent 
	private Date invoiceDate;
	
	@Persistent
	private Date paymentDate;
	
	
	public Invoice(Company company, Customer customer, float amount, Date invoiceDate, Date paymentDate){
		this.customer =  customer;
		this.amount = amount;
		this.invoiceDate = invoiceDate;
		this.paymentDate = paymentDate;
		this.id = company.getNextInvoiceIdAndIncrement();
	}


	public Customer getCustomer() {
		return customer;
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
