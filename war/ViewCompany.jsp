<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="se.forsslundsystems.fsbusiness.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TimeZone"%>
<%@ page import="javax.jdo.PersistenceManager"%>

<html>
<body>

	<%
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user != null) {
	%>
	<p>
		Hello,
		<%=user.getNickname()%>! (You can <a
			href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign
			out</a>.)
	</p>
	<%
		} else {
	%>
	<p>
		Hello! <a
			href="<%=userService.createLoginURL(request.getRequestURI())%>">Sign
			in</a> to include your name with greetings you post.
	</p>
	<%
		}
	%>

	<%
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Company.class.getName();
		List<Company> greetings = (List<Company>) pm.newQuery(query).execute();
		if (greetings.isEmpty()) {
	%>
	<p>There are no companies.</p>
	<%
		} else {
			for (Company company : greetings) {
	%><H1><%=company.getName()%></H1>
	<%
				// list all invoices
				for (Project prj : company.getProjectList()) {
	%><B>Project: <%=prj.getName()%></B><BR/>
    <%	

    				List<Customer> lc = prj.getCustomers();
    				List<Invoice> li = prj.getGeneratedInvoices();
    				
    				if(li==null){
    					%>li ar null<BR/><%
    				}
    				if(lc==null){
    					%>lc ar null<BR/><%
    				}
    				
    
    
    				for (Customer c : prj.getCustomers()){
	%>---Customers: <%=c.getCompanyName()%><BR/>
    <%	
    				}
    
    
    
				}
			}
		}
		pm.close();
	
     %>
</body>
</html>
