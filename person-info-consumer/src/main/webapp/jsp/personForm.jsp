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
    <title>Create/edit person record</title>
</head>
<body>
<h1><s:if test="person==null || person.id == null">
    <s:label value="Add new person"/>
</s:if>
    <s:else>
        <s:label value="Edit existent person"/>
    </s:else></h1>

<table width=600 align=center>
    <tr><td><a href="getAllPersons.action">Click Here to View Persons</a></td>
    </tr>
</table>
<table>
    <tr><td align="left" style="color:#ff4730">
        <s:fielderror/>
        <s:actionerror/>
        <s:actionmessage/></td></tr>
</table>

<s:form>
    <table align="center" class="borderAll">

        <tr><td class="tdLabel"><s:label value="First Name"/></td>
            <td><s:textfield name="person.firstName" size="30"/></td>
        </tr>
        <tr>
            <td class="tdLabel"><s:label value="Last Name"/></td>
            <td><s:textfield name="person.lastName" size="30"/></td>
        </tr>
        <tr><td class="tdLabel"><s:label value="Date of Birth"/></td>
            <td><input type="date" value="birthDate"></td>
            <s:hidden name="person.id"/>
        </tr>
    </table>

    <table align="center">
        <tr>
            <td><s:submit action="insertOrUpdate" value="Submit" cssClass="butStnd"/></td>
            <td><s:reset value="Cancel" cssClass="butStnd"/></td>
        <tr>
    </table>
</s:form>
</body>
</html>
