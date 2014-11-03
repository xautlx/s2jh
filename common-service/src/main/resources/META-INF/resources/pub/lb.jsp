<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
    Long count = (Long) session.getAttribute("count");
    if (count == null) {
        count = 0L;
    }
    count += 1;
    session.setAttribute("count", count);
    String ip = request.getRemoteAddr();
    int port = request.getLocalPort();
    java.util.Date time = new java.util.Date();
    String sid = session.getId();
    String message = "[" + sid + "][" + ip + ":" + port + "][" + time
            + "] Loading balance testing, session count=" + count;
    System.out.println(message);
    
    Map<String, Object> data = new LinkedHashMap<String, Object>();

    Map<String, Object> keyData = new LinkedHashMap<String, Object>();
    data.put("Key Data", keyData);
    keyData.put("Time", time.toString());
    keyData.put("SessionID", sid);
    keyData.put("Count", String.valueOf(count));
    keyData.put("LocalHostAddress", java.net.InetAddress.getLocalHost().getHostAddress());

    Map<String, Object> requestData = new LinkedHashMap<String, Object>();
    data.put("Request Data", requestData);
    requestData.put("ContextPath", request.getContextPath());
    requestData.put("RemoteHost", request.getRemoteHost());
    requestData.put("RemotePort", request.getRemotePort());
    requestData.put("RemoteUser", request.getRemoteUser());
    requestData.put("LocalAddr", request.getLocalAddr());
    requestData.put("LocalName", request.getLocalName());
    requestData.put("LocalPort", request.getLocalPort());
    requestData.put("ServerName", request.getServerName());
    requestData.put("ServerPort", request.getServerPort());

    Map<String, Object> hearderData = new LinkedHashMap<String, Object>();
    data.put("Header Data", hearderData);

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String header = headerNames.nextElement();
        hearderData.put(header, request.getHeader(header));
    }
%>
<html>
<head>
<title>Loading balance test page</title>
</head>
<body>
	<div class="row-fluid">
		<input type="button" value="Reload Page" class="btn" onclick="self.location.reload()" />
	</div>
	<%
	    for (Map.Entry<String, Object> me : data.entrySet()) {
	%>
	<fieldset>
		<legend><%=me.getKey()%></legend>
		<div class="alert alert-info">
			<ul>
				<%
				    Object val = me.getValue();
				        if (val instanceof Map) {
				            Map<String, Object> childData = (Map<String, Object>) val;
				            for (Map.Entry<String, Object> child : childData.entrySet()) {
				%>
				<li><%=child.getKey()%> : <%=child.getValue()%></li>
				<%
				    }
				        } else {
				%>
				<li><%=val%></li>
				<%
				    }
				%>
			</ul>
		</div>
	</fieldset>
	<%
	    }
	%>

</body>
</html>
