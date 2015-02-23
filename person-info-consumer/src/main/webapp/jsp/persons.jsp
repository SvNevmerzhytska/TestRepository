<%--
  Created by IntelliJ IDEA.
  User: s.nevmerzhytska
  Date: 2/13/2015
  Time: 1:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <link href="<s:url value="/css/app.css"/>" rel="stylesheet" type="text/css"/>
    <title>List all persons</title>
</head>
<body>
<h1>Persons</h1>
<table width=600 align=center>
    <tr><s:url id="insert" action="setUpForInsertOrUpdate"/>
        <td><s:a href="%{insert}">Click Here to Add New Person</s:a></td>
    </tr>
</table>
<br/>
<table align=center class="borderAll">
    <tr class="borderAll">
        <th>First Name</th>
        <th>Last Name</th>
        <th>Date of Birth</th>
        <th></th>
    </tr>
    <s:iterator value="persons" status="status">
        <tr class="<s:if test="#status.even">even</s:if><s:else>odd</s:else>">
            <td class="nowrap"><s:property value="firstName"/></td>
            <td class="nowrap"><s:property value="lastName"/></td>
            <td class="nowrap"><s:property value="birthDate"/></td>
            <td class="nowrap"><s:url id="update" action="setUpForInsertOrUpdate">
                <s:param name="person.id" value="id"/>
            </s:url> <s:a href="%{update}">Edit</s:a>

                <s:url id="delete" action="delete">
                    <s:param name="person.id" value="id"/>
                </s:url> <s:a href="%{delete}">Delete</s:a>
            </td>
        </tr>
    </s:iterator>
</table>
</body>
</html>
