<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<link href="marginal-tax.css" rel="stylesheet" type="text/css"></link>
	</head>
	<body>
		<div class="body row">
			<div class="column1">

				<h2>Input</h2>
				<form:form id="input" method="get" modelAttribute="input">
					<label>Year:</label>
					<form:select path="year">
						<form:option value="2021" selected="selected">2021</form:option>
						<form:option value="2020">2020</form:option>
						<form:option value="2019">2019</form:option>
					</form:select>
					<br />
						<label>Status:</label>
						<form:select path="status">
							<form:option value="S" selected="selected">Single</form:option>
							<form:option value="MFJ">Married Filing Jointly</form:option>
							<form:option value="HH">Head of House Hold</form:option>
						</form:select>
						<br />
							<label>Salary:</label>
							<form:input path="salary" type="text" />
							<br />
							<input type="submit" value="calculate" />
				</form:form>

			</div>
			<div class="middle"></div>

			<div class="column2">
				<h2>Marginal Tax Rate Example</h2>
				<p>
					The table below shows the rates and income levels for three types
					of
					filer in
					<span class="replaceable">${year}:</span>
					single, married
					filing jointly, and heads of household
				</p>
				<table>
					<thead>
						<tr>
							<th>Rate</th>
							<th>For Singles With Taxable Income Over</th>
							<th>For Married Filing Jointly With Taxable Income Over</th>
							<th>For Heads of Household With Taxable Income Over</th>
						</tr>
					</thead>
					<tbody class="money">
					    <c:forEach items="${taxRule}" var="n">
					        <tr>
                                <td>${n.rate}</td>
                                <td>${n.sSalary}</td>
                                <td>${n.mfjSalary}</td>
                                <td>${n.hhSalary}</td>
                            </tr>
					    </c:forEach>

					</tbody>
				</table>
				<p>Individuals who make the lowest amount of income are placed
					into
					the lowest marginal tax rate bracket,while higher-earning
					individuals are placed into higher marginal tax brackets. However,
					the marginal tax bracket in which an individual falls does not
					determine how the entire income is taxed. Instead, income taxes are
					assessed progressively, with each bracket having a range of income
					values that are taxed at a particular rate.
				</p>

				<p>
					Under the current plan, if a
					<span class="replaceable">${status}
					</span>
					taxpayer earned
					<span class="replaceable">${salary}</span>
					in
					taxable income, they would owe the following income taxes for
					<span class="replaceable">${year}</span>
					(due in April
					<span class="replaceable">${year+1}</span>
					), as shown below:
				</p>
				<ul>
			        <c:forEach items="${taxBracket}" var="n">
			            <li>
                            <strong>${n.bracket}</strong>
                            ${n.computeDescription}
                        </li>
			        </c:forEach>
				</ul>
				<p>
					If you add up these amounts, the entire tax liability for this
					individual would be
					<span class="replaceable">${taxPaid}</span>
					, or
					an effective tax rate of
					<span class="replaceable">${totalRate}</span>
					<span class="replaceable">(${taxPaid} / ${salary})</span>
				</p>
			</div>
		</div>
	</body>
</html>