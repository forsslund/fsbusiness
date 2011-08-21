<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="se.forsslundsystems.fsbusiness.*" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.TimeZone" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.Query" %>
<html>
  <body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
    }
%>

<%
    
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
		
		%> Hello: <%= company.checkAndDoInvoicing(new Date(today.getTime())) %> <BR/> <%
	}
%><BR/><BR/><%


	PersistenceManager pm = PMF.get().getPersistenceManager();
	
	try {


	   	//@SuppressWarnings("unchecked")
		//Collection<Company> companiesToDelete = (Collection<Company>) pm.newQuery("select from "+Company.class.getName()).execute();
		//pm.newQuery(Company.class).deletePersistentAll();

		
	    pm.makePersistent(company);
	} finally {
	    pm.close();
	}
	
	// list all invoices
	for(Project prj : company.getProjectList()){
		for(Invoice inv : prj.getGeneratedInvoices()){
			String s = "Invoice " + inv.getId() + " " + inv.getCustomer().getCompanyName() + " Invoice date: " + inv.getInvoiceDate();
			%> An invoice: <%= s %><BR/> <%
		}
	}

%>

  </body>
</html>
