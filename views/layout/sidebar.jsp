<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
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
<link href="../css/examples.css" rel="stylesheet">
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
	<!--Thanh menu dọc bên trái-->
	<div class="sidebar sidebar-light sidebar-fixed" id="sidebar">
		<div class="sidebar-brand d-none d-md-flex">
			<svg class="sidebar-brand-full" width="118" height="46"
				alt="CoreUI Logo">
        <use
					xlink:href="${pageContext.request.contextPath}/admin/assets/brand/coreui.svg#full"></use>
      </svg>
			<svg class="sidebar-brand-narrow" width="46" height="46"
				alt="CoreUI Logo">
        <use
					xlink:href="${pageContext.request.contextPath}/admin/assets/brand/coreui.svg#signet"></use>
      </svg>
		</div>
		<ul class="sidebar-nav" data-coreui="navigation" data-simplebar="">
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/index"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-"></use>
          </svg> TRANG CHỦ
			</a> <!--  --> <!-- Học tập -->
			<li class="nav-title">HỌC TẬP</li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/learning/vocabulary">
					<svg class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-"></use>							
          </svg>Từ vựng
			</a><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/learning/grammar">
					<svg class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-"></use>							
          </svg>Ngữ pháp
			</a></li>

			<!--Quản lý sản phẩm-->
			<li class="nav-title">QUẢN LÝ SẢN PHẨM</li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/products"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-flower"></use>							
          </svg>Sản phẩm
			</a> <a class="nav-link"
				href="${pageContext.request.contextPath}/admin/storage"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/"></use>
          </svg>Kho lưu trữ
			</a><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/storage/document-type">
					<svg class="nav-icon">
            <use x
							link:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/"></use>
          </svg>Loại tài liệu
			</a></li>
			<!--Quản lý đơn hàng-->
			<li class="nav-title">QUẢN LÝ ĐƠN HÀNG</li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/orders"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-cart"></use>
          </svg>Tất cả đơn hàng
			</a></li>



			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/orders-unconfirm">
					<svg class="nav-icon">
	            <use xlink:href=""></use>
	          </svg>Chờ xác nhận ${count_unconfirm}
			</a></li>



			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/orders-delivery">
					<svg class="nav-icon">
            <use xlink:href=""></use>
          </svg>Đang giao hàng
			</a></li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/orders-completed">
					<svg class="nav-icon">
            <use xlink:href=""></use>
          </svg>Đã hoàn thành
			</a></li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/orders-cancel"> <svg
						class="nav-icon">
            <use xlink:href=""></use>
          </svg>Đã hủy
			</a></li>

			<li class="nav-title">QUẢN LÝ KHÁCH HÀNG</li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/customers"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-drop"></use>
          </svg> Khách hàng
			</a></li>

			<!-- Quản trị hệ thống-->
			<li class="nav-title">QUẢN TRỊ HỆ THỐNG</li>
			<!--Cài đặt-->
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/config"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-settings"></use>
          </svg> Cài đặt
			</a></li>
			<!--Nhật ký hệ thống-->
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/log"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/"></use>
          </svg> Nhật ký hệ thống
			</a></li>
			<!--Danh mục -->
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/category-type"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-justify-center"></use>
          </svg> Danh mục chung
			</a></li>
			<!-- Nhóm quyền -->
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/role"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#"></use>
          </svg> Nhóm quyền
			</a></li>
			<!--Tài khoản hệ thống-->
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/admin/account"> <svg
						class="nav-icon">
            <use
							xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-user"></use>
          </svg>Tài khoản hệ thống
			</a></li>
			<li class="nav-divider"></li>
		</ul>
		<!--End sidebar-->
		<!--NÃºt thu háº¹p cuá»i trang-->
		<button class="sidebar-toggler" type="button"
			data-coreui-toggle="unfoldable"></button>
	</div>
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
</body>
</html>