<!DOCTYPE html>
<html lang="en">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<head>
<base href="./../">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
<meta name="description"
	content="CoreUI - Open Source Bootstrap Admin Template">
<meta name="author" content="Łukasz Holeczek">
<meta name="keyword"
	content="Bootstrap,Admin,Template,Open,Source,jQuery,CSS,HTML,RWD,Dashboard">
<title>Hệ thống quản lý</title>
<link rel="manifest" href="assets/favicon/manifest.json">
<meta name="msapplication-TileColor" content="#ffffff">
<meta name="msapplication-TileImage"
	content="assets/favicon/ms-icon-144x144.png">
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
<link rel="canonical" href="https://coreui.io/docs/content/tables/">
<style>
img {
	width: 150px;
	height: 200px;
}
</style>
</head>

<body>
	<c:if test="${empty name}">
		<c:redirect url="/admin/login"></c:redirect>
	</c:if>

	<div class="wrapper d-flex flex-column min-vh-100">

		<!--Header-->
		<jsp:include page="header.jsp" />

		<!--Nội dung page-->
		<div class="body flex-grow-1 px-3">
			<div class="container-fluid">
				<div class="row text-center"
					style="margin-top: 150px; margin-left: 150px; margin-right: 150px;">
					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/admin/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/quan-tri-he-thong.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>QUẢN TRỊ HỆ THỐNG</strong>
							</figcaption></a>
					</figure>

					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/sales/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/quan-ly-kinh-doanh.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>QUẢN LÝ BÁN HÀNG</strong>
							</figcaption>
						</a>
					</figure>

					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/spend/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/quan-ly-chi-tieu.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>QUẢN LÝ CHI TIÊU</strong>
							</figcaption>
						</a>
					</figure>

					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/task/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/quan-ly-cong-viec.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>QUẢN LÝ CÔNG VIỆC</strong>
							</figcaption>
						</a>
					</figure>

					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/storage/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/kho-du-lieu.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>KHO LƯU TRỮ</strong>
							</figcaption>
						</a>
					</figure>

					<figure class="figure col">
						<a class="nav-link"
							href="${pageContext.request.contextPath}/learning/home"> <img
							style="width: 80px"
							src="${pageContext.request.contextPath}/admin/assets/img/module/hoc-tap.svg"
							class="figure-img img-fluid rounded" alt="...">

							<figcaption class="figure-caption">
								<strong>QUẢN LÝ HỌC TẬP</strong>
							</figcaption>
						</a>
					</figure>
				</div>

			</div>
		</div>

		<!--Nội dung footer-->
		<jsp:include page="layout/footer.jsp" />
	</div>
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
</body>
</html>