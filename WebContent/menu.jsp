<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- メニュー -->
<a href="/shopping/ShowItemServlet?action=top">ようこそ</a>|
<!-- 検索機能の追加 -->
<form action="/shopping/SearchServlet" method="post">
		 <input type="text" name="search" size="20">
		<input type="submit" value="検索">
	</form>


<c:forEach items="${categories}" var="category">
<a href="/shopping/ShowItemServlet?action=list&code=${category.code}">${category.name}</a>|
</c:forEach>

<a href="/shopping/CartServlet?action=show">カートを見る</a>

