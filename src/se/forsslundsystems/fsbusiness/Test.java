package se.forsslundsystems.fsbusiness;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Test {
	
	public static void main(String[] args){

		Company company = new Company("Forsslund Systems AB");
		
		Customer c1 = new Customer("Johan Schiff consulting");
		Customer c2 = new Customer("Pierre Ingmanson consulting");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
		Date startDate = new Date();
		try {
			startDate = sdf.parse("2011-08-01");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Project p = new Project("ServerHosting",startDate);
		
		p.addCustomer(c1);
		p.addCustomer(c2);
		p.setStandardInvoiceAmount(250.0f);
		p.setRecurring(90);


		company.addProject(p);		
		
		Project p2 = new Project("Finland-website",startDate);
		p2.addCustomer(new Customer("Scandinvaiske kakirurgitjosan"));
		p2.setRecurring(365);
		p2.setStandardInvoiceAmount(3600.0f);
		
		
		
		company.addProject(p2);
		
		
	
		
		// Step day
		Date today = new Date();
		for(int i=0;i<800;i+=5){
			today.setTime(today.getTime()+3600*1000*24L*5);
			System.out.print("Days: " + (today.getTime()-startDate.getTime())/(3600*1000*24L));
			
			System.out.print(" " + company.checkAndDoInvoicing(new Date(today.getTime())) + "\n");
			System.out.print(" " + company.checkAndDoInvoicing(new Date(today.getTime())) + "\n");
		}

		// list all invoices
		for(Project prj : company.getProjectList()){
			for(Invoice inv : prj.getGeneratedInvoices()){
				String s = "Invoice " + inv.getId() + " " + inv.getCustomer().getCompanyName() + " Invoice date: " + inv.getInvoiceDate();
				System.out.println(s);
			}
		}
		
		
	}
	

}
