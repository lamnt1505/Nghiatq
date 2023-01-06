// @ts-ignore
$(document).ready(function() {
			// @ts-ignore
			var quantitiy = 0;
			
			// @ts-ignore
			$(".quantity-right-plus").click(function(e) {
				// Stop acting like a button
				e.preventDefault();
				// Get the field name
				// @ts-ignore
				var quantity = parseInt($("#quantity").val());
				// If is not undefined
				// @ts-ignore
				$("#quantity").val(quantity + 1);
				// Increment
			});
			
			// @ts-ignore
			$('.quantityinput').change(function() {
				// @ts-ignore
				var quantity = $(this).val();
				// @ts-ignore
				var price = $(this).parents('tr').find('.price').text();
				// @ts-ignore
				$(this).parents('tr').find('.total').text(quantity * price);
			});
							
			// @ts-ignore
			$(".updateclick").click(function(e) {
				e.preventDefault();
				// @ts-ignore
				var quantity = $(this).parents('tr').find('.quantityinput').val();
				// @ts-ignore
				var idproduct = $(this).data('productid');
				var product = {
					idproduct : idproduct,
					quantity : quantity
			};
				
			//console.log(product)
			// @ts-ignore
			$.post("/updatequantities",product).done(function(data,status) {
				if (data == "0") {
					// @ts-ignore
					Swal.fire({
						  icon: 'success',
						  title: 'Có lỗi hoặc số lượng đang nhỏ hơn 0! Vui lòng thử lại...'
						})
				} else if (data == "1") {
					// @ts-ignore
					Swal.fire({
						  icon: 'success',
						  title: 'Đã cập nhật thành công !'
						})
				} else if (data == "2") {
					// @ts-ignore
					Swal.fire({
						  icon: 'error',
						  title: 'Sản phẩm đã loại khỏi giỏ hàng !'
						})
						setTimeout(function(){
							window.location.reload();
						}, 1500);
					} else {
						alert("Có gì đó sai sai...^^ !");
					}
				});
			});
			
			// @ts-ignore
			$("#submitorders").click(function(e) {
				console.log('---start---');
			// @ts-ignore
			$.post("/orders").done(function(data,status) {
				if (data == "0") {
					// @ts-ignore
					Swal.fire({
						  icon: 'error',
						  title: 'Bạn phải đăng nhập!'
						})
						setTimeout(function(){
							// @ts-ignore
							window.location = "/login";
						}, 1000);
				} else if (data == "-1") {
					// @ts-ignore
					Swal.fire({
						  icon: 'error',
						  title: 'Chưa chọn sản phẩm vào giỏ hàng!'
						})
				} else if (data == "1") {
					// @ts-ignore
					Swal.fire({
						  icon: 'success',
						  title: 'Đặt hàng thành công!'
						})
						setTimeout(function(){
							window.location.reload();
						}, 3000);
					} else {
						alert("Có gì đó sai sai...^^ !");
					}
				});
			});

			console.log('---end---');
			
			// @ts-ignore
			$(".product-remove").click(function(e) {
				e.preventDefault();
				var quantity = 0;
				// @ts-ignore
				var idproduct = $(this).data('productid');
				var product = {
						idproduct : idproduct,
						quantity : quantity
				};
				
			//console.log(product)
			// @ts-ignore
			$.post("/updatequantities",product).done(function(data,status) {
				if (data == "0") {
					// @ts-ignore
					Swal.fire({
						  icon: 'success',
						  title: 'Có lỗi hoặc số lượng đang nhỏ hơn 0! Vui lòng thử lại...'
						})
				} else if (data == "1") {
					// @ts-ignore
					Swal.fire({
						  icon: 'success',
						  title: 'Đã cập nhật thành công !'
						})
				} else if (data == "2") {
					// @ts-ignore
					Swal.fire({
						  icon: 'error',
						  title: 'Sản phẩm đã loại khỏi giỏ hàng !'
						})
						setTimeout(function(){
							window.location.reload();
						}, 1500);
					} else {
						alert("Có gì đó sai sai...^^ !");
					}
				});
			});
			
			// @ts-ignore
			$(".quantity-left-minus").click(function(e) {
				// Stop acting like a button
				e.preventDefault();
				// Get the field name
				// @ts-ignore
				var quantity = parseInt($("#quantity").val());
				// Increment
				if (quantity > 0) {
					// @ts-ignore
					$("#quantity").val(quantity - 1);
					}
			});
		});