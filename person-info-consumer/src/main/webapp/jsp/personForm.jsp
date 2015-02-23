<%--
  Created by IntelliJ IDEA.
  User: s.nevmerzhytska
  Date: 2/13/2015
  Time: 1:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<html>
<head>
    <link href="<s:url value="/css/app.css"/>" rel="stylesheet" type="text/css"/>
    <title>Create/edit person record</title>
    <sj:head />
</head>
<body>
<h1><s:if test="person==null || person.id == 0">
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

<s:form action="insertOrUpdate">
    <table align="center" class="borderAll">

        <tr>
            <td><s:textfield label="First Name" name="person.firstName" size="30" requiredLabel="true"/></td>
        </tr>
        <tr>
            <td><s:textfield label="Last Name" name="person.lastName" size="30" requiredLabel="true"/></td>
        </tr>
        <tr>
            <td>
                <sj:datepicker label="Date of Birth" displayFormat="yy-mm-dd" name="person.birthDate"/>
            </td>
            <s:hidden name="person.id"/>
        </tr>
    </table>

    <table align="center">
        <tr>
            <s:submit value="Submit" cssClass="butStnd"/>
            <s:reset value="Cancel" cssClass="butStnd"/>
        <tr>
    </table>
</s:form>
</body>
</html>
