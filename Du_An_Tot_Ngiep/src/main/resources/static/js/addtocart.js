$(document).ready(function() {//quản lý sự kiện

	$('.procart').click(function(e) {//sự kiện click
		e.preventDefault();//hủy bỏ event
		console.log("---start function--");
		//in ra tb bắt đầu function
		var amount = 1;
		var idproduct = $(this).data('productid');
		$.post("/insertproduct}", {//pt post thêm 1 product
			idproduct : idproduct,
			amount : amount
		}, function(data, status) {
			console.log(data);//show ra giá trị của biến
			if (data == "1") { // them thanh cong. san pham chua nam trong gio
								// hang
//				alert("Thêm thành công !");
				Swal.fire({
					  icon: 'success',
					  title: 'Thêm sản phẩm thành công'
					})
				var so_luong = parseInt($("#so_luong_in_cart").html());
				so_luong++;
				$("#so_luong_in_cart").html(so_luong);
			} else if (data == "5") { // them thanh cong, san pham da nam
										// trong gio hang
				Swal.fire({
					  icon: 'success',
					  title: 'Đã cộng thêm một sản phẩm vào giỏ hàng'
					})
			} else if (data == "2") {
				Swal.fire({
					  icon: 'error',
					  title: 'Thêm sản phẩm thất bại'
					})
			} else if (data == "3") {
				Swal.fire({
					  icon: 'error',
					  title: 'Lỗi'
					})
			} else if (data == "4") {
				Swal.fire({
					  icon: 'success',
					  title: 'Đã cộng thêm một sản phẩm vào giỏ hàng'
					})
			} else
				alert("error:" + data)
		});
	});
});