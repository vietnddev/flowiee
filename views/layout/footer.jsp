<!DOCTYPE html>
<html lang="en">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<head>
<base href="./">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
<meta name="description"
	content="CoreUI - Open Source Bootstrap Admin Template">
<meta name="author" content="Åukasz Holeczek">
<meta name="keyword"
	content="Bootstrap,Admin,Template,Open,Source,jQuery,CSS,HTML,RWD,Dashboard">
<title>Flowiee</title>
<link rel="manifest"
	href="${pageContext.request.contextPath}/admin/assets/favicon/manifest.json">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage"
	content="${pageContext.request.contextPath}/admin/assets/favicon/ms-icon-144x144.png">
<meta name="theme-color" content="#ffffff">
<!-- Vendors styles-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/admin/vendors/simplebar/css/simplebar.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/admin/css/vendors/simplebar.css">
<!-- Main styles for this application-->
<link href="${pageContext.request.contextPath}/admin/css/style.css"
	rel="stylesheet">
<!-- We use those styles to show code examples, you should remove them in your application.-->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/prismjs@1.23.0/themes/prism.css">
<link href="${pageContext.request.contextPath}/admin/css/examples.css"
	rel="stylesheet">
<!-- Global site tag (gtag.js) - Google Analytics-->
<script async=""
	src="https://www.googletagmanager.com/gtag/js?id=UA-118965717-3"></script>
<script>
	window.dataLayer = window.dataLayer || [];
	function gtag() {
		dataLayer.push(arguments);
	}
	gtag('js', new Date());
	// Shared ID
	gtag('config', 'UA-118965717-3');
	// Bootstrap ID
	gtag('config', 'UA-118965717-5');
</script>
<link
	href="${pageContext.request.contextPath}/admin/vendors/@coreui/chartjs/css/coreui-chartjs.css"
	rel="stylesheet">
</head>
<body>
	<!-- Chân trang (footer) -->
	<footer class="footer" style="border: none">
		<div>Flowiee © official.</div>
		<div class="ms-auto">
			Thiết kế by&nbsp;ViệtNĐ</a>
		</div>
	</footer>
	<!-- CoreUI and necessary plugins-->
	<script
		src="${pageContext.request.contextPath}/admin/vendors/@coreui/coreui/js/coreui.bundle.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/admin/vendors/simplebar/js/simplebar.min.js"></script>
	<!-- Plugins and scripts required by this view-->
	<script
		src="${pageContext.request.contextPath}/admin/vendors/chart.js/js/chart.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/admin/vendors/@coreui/chartjs/js/coreui-chartjs.js"></script>
	<script
		src="${pageContext.request.contextPath}/admin/vendors/@coreui/utils/js/coreui-utils.js"></script>
	<script src="${pageContext.request.contextPath}/admin/js/main.js"></script>
	<script>
		
	</script>

</body>

</html>