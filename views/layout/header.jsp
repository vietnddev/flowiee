<!DOCTYPE html>
<html lang="en">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
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
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

.container {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	display: flex;
	align-items: center;
	justify-content: center;
}

#file-input-avatar {
	display: none;
}

.preview {
	padding: 10px;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-direction: column;
	width: 100%;
	max-width: 350px;
	margin: auto;
	box-shadow: 0 0 20px rgba(170, 170, 170, 0.2);
}

.img-avatar {
	width: 100%;
	object-fit: cover;
	margin-bottom: 10px;
}

.label-avatar {
	font-size: 14px;
	font-weight: 600;
	cursor: pointer;
	color: #fff;
	border-radius: 8px;
	padding: 5px 10px;
	background-color: rgb(101, 101, 255);
}
</style>
<body>
	<!--Menu ngang phía trên (header)-->
	<header class="header header-sticky mb-4">
		<div class="container-fluid">
			<button class="header-toggler px-md-0 me-md-3" type="button"
				onclick="coreui.Sidebar.getInstance(document.querySelector('#sidebar')).toggle()">
				<svg class="icon icon-lg">
            <use
						xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-menu"></use>
          </svg>
			</button>
			<a class="header-brand d-md-none" href="#"> <svg width="118"
					height="46" alt="CoreUI Logo">
            <use
						xlink:href="${pageContext.request.contextPath}/admin/assets/brand/coreui.svg#full"></use>
          </svg>
			</a>
			<ul class="header-nav d-none d-md-flex">
				<li class="nav-item"><a class="nav-link" href="">Dashboard</a></li>
			</ul>
			<ul class="header-nav ms-auto">
				<li class="nav-item"><a 
					href="${pageContext.request.contextPath}/sales/checkout">
						<button type="button"
							class="btn btn-outline position-relative btn-sm">
							<svg class="icon icon-lg">
                <use
									xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-cart"></use>
              </svg>
							<span
								class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger badge-sm">
								${sizecart} </span>
						</button>
				</a></li>


				<!-- Chuông thông báo -->
				<li class="nav-item"><button type="button"
						class="btn btn-sm btn-outline position-relative"
						data-coreui-toggle="modal" data-coreui-target="#Notification">
						<svg class="icon icon-lg">
                <use
								xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-bell"></use>
              </svg>
						<span
							class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger badge-sm">
							${noti_unread} </span>
					</button></li>
			</ul>

			<ul class="header-nav ms-3">
				<li class="nav-item dropdown"><a class="nav-link py-0"
					data-coreui-toggle="dropdown" href="#" role="button"
					aria-haspopup="true" aria-expanded="false">
						<div class="avatar avatar-md">
							<img class="avatar-img"
								src="${pageContext.request.contextPath}/admin/assets/img/avatars/${sessionScope.profileid}.png"
								alt="#">
						</div> <strong style="font-size: 14px; margin-left: 10px">${sessionScope.name}</strong>
				</a>
					<div class="dropdown-menu dropdown-menu-end pt-0">
						<button type="button" class="dropdown-item btn btn-sm"
							data-coreui-toggle="modal"
							data-coreui-target="#staticBackdropProfile">Thông tin cá
							nhân</button>

						<button type="button" class="dropdown-item btn btn-sm"
							data-coreui-toggle="modal"
							data-coreui-target="#staticBackdropChangePassword">Đổi
							mật khẩu</button>

						<div class="dropdown-divider"></div>
						<a class="dropdown-item" style="font-size: 14px"
							href="${pageContext.request.contextPath}/admin/logout"> <svg
								class="icon me-2">
                  <use
									xlink:href="${pageContext.request.contextPath}/admin/vendors/@coreui/icons/svg/free.svg#cil-account-logout"></use>
                </svg> Đăng xuất
						</a>
					</div></li>
			</ul>
		</div>
		<div class="header-divider"></div>
		<div class="container-fluid">
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb my-0 ms-2">
					<li class="breadcrumb-item">
						<!-- if breadcrumb is single--> <a style="text-decoration: none"
						href="${pageContext.request.contextPath}/admin/index"><span
							class="text-secondary">Home</span></a>
					</li>
					<li class="breadcrumb-item"><a style="text-decoration: none"
						href="${pageContext.request.contextPath}/admin/${page}"><span
							class="text-secondary">${breadcrumb}</span></a></li>
				</ol>
			</nav>
		</div>
	</header>
	<!--End header-->

	<!-- Modal cập nhật thông tin cá nhân -->
	<div class="modal fade" id="staticBackdropProfile"
		data-coreui-backdrop="static" data-coreui-keyboard="false"
		tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<form enctype="multipart/form-data"
					action="${pageContext.request.contextPath}/admin/updateProfile"
					method="POST">
					<div class="modal-header">
						<h6 class="modal-title" id="staticBackdropLabel">Cập nhật
							thông tin cá nhân</h6>
						<button type="button" class="btn-close"
							data-coreui-dismiss="modal" aria-label="Close"></button>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-sm btn-secondary"
							data-coreui-dismiss="modal">Đóng</button>
						<button type="submit" class="btn btn-sm btn-primary">Lưu</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Modal đổi mật khẩu -->
	<div class="modal fade" id="staticBackdropChangePassword"
		data-coreui-backdrop="static" data-coreui-keyboard="false"
		tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered">
			<div class="modal-content">
				<form
					action="${pageContext.request.contextPath}/admin/change-password"
					method="POST">
					<div class="modal-header">
						<h6 class="modal-title" id="staticBackdropLabel">Đổi mật khẩu</h6>
						<button type="button" class="btn-close"
							data-coreui-dismiss="modal" aria-label="Close"></button>
					</div>
					<div class="modal-body">
						<div class="mb-3 row">
							<label for="password" class="col-sm-4 col-form-label">Mật
								khẩu cũ</label>
							<div class="col-sm-8 mb-3">
								<input type="password" class="form-control form-control-sm"
									name="password" />
							</div>

							<label for="password_new" class="col-sm-4 col-form-label">Mật
								khẩu</label>
							<div class="col-sm-8 mb-3">
								<input type="password" class="form-control form-control-sm"
									name="password_new" />
							</div>

							<label for="email" class="col-sm-4 col-form-label">Nhập
								lại mật khẩu</label>
							<div class="col-sm-8 mb-3">
								<input type="password" class="form-control form-control-sm"
									name="password_re" />
							</div>

						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-sm btn-secondary"
							data-coreui-dismiss="modal">Đóng</button>
						<button type="submit" class="btn btn-sm btn-primary">Lưu</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div class="modal fade" id="Notification" tabindex="-1"
		style="font-size: 14px" data-coreui-backdrop="static"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-scrollable">
			<div class="modal-content">
				<div class="modal-header text-center">
					<h6 class="modal-title" id="exampleModalLabel">Thông báo</h6>
					<button type="button" class="btn-close" data-coreui-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<c:forEach var="noti" items="${notification}">
						<form method="POST"
							action="${pageContext.request.contextPath}/admin/read-notification">
							<input type="hidden" name="ID" value="${noti.ID}" /> <input
								type="hidden" name="IDOrders" value="${noti.IDOrders}" />
							<c:choose>
								<c:when test="${noti.isReaded == false}">
									<div class="alert alert-info" role="alert">
										Thông báo hệ thống: <i> Bạn có đơn hàng số
											<button
												style="text-decoration: none; margin-left: -5px; margin-right: -5px"
												type="submit" class="btn btn-sm btn-link text-middle">${noti.message}</button>đang
											chờ bạn xác nhận!
										</i> <br> ${noti.created}
									</div>
								</c:when>
								<c:when test="${noti.isReaded == true}">
									<div class="alert alert-light" role="alert">
										Thông báo hệ thống: <i> Bạn có đơn hàng số
											<button
												style="text-decoration: none; margin-left: -5px; margin-right: -5px"
												type="submit" class="btn btn-sm btn-link text-middle">${noti.message}</button>đang
											chờ bạn xác nhận!
										</i> <br> ${noti.created}
									</div>
								</c:when>
							</c:choose>
						</form>
					</c:forEach>
				</div>
				<div class="modal-footer d-grid gap-2 mx-auto">
					<a class="btn btn-sm btn-primary"
						href="${pageContext.request.contextPath}/admin/notification">Xem
						tất cả thông báo</a>
				</div>
			</div>
		</div>
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
	<script type="text/javascript">
		/**/
		const input_avatar = document.getElementById('file-input-avatar');
		const image_avatar = document.getElementById('img-avatar');
		input_avatar.addEventListener('change', (e) => {
		    if (e.target.files.length) {
		        const src = URL.createObjectURL(e.target.files[0]);
		        image_avatar.src = src;
		    }
		})
	</script>
</body>
</html>