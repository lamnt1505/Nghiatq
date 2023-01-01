//quản lý sự kiện Jquery
// @ts-ignore
$(document).ready(function() {

    var orderid =0;
    //biến chỉnh sửa status
    // @ts-ignore
    $(".editmodal").click(function(e) {
        e.preventDefault();//phương thức sẽ ngăn không cho liên kết link

        // @ts-ignore
        var status = $(this).parents('tr').find('.status').text();
        // @ts-ignore
        orderid = $(this).data('orderid');

        console.log(status);//show ra giá trị của biến
        
        // @ts-ignore
        $(".optionorder").val(status).change();//biến chỉnh sửa trạng thái của đơn hàng
    });

    // @ts-ignore
    $(".submitstatus").click(function(e) {
        e.preventDefault();//ngăn sự kiện xảy ra

        // @ts-ignore
        var newstatus = $(".optionorder").val();
        console.log(newstatus);//show ra giá trị của biến
        console.log(orderid);//show ra giá trị của biến
        // @ts-ignore
        //đường dẫn URL dẫn đến pt post trong HomeRestController
        $.post("/updatestatus",{
            "orderid":orderid,
            "status":newstatus}
            )
                // @ts-ignore
                .done(function(data,status) {
                    //sử dụng var tham chiếu đến số lượng
                    var quantity = parseInt(data);
                    //sd parseInt để trả về 1 số nguyên
                    console.log(quantity);
                    //show ra giá trị của biến (số lượng)
                    if (quantity==0) {//nếu sl = 0 đưa ra tb
                        // @ts-ignore
                        Swal.fire({//sử dụng hộp thoại SweetAlert đưa ra tb 
                              icon: 'error',
                              title: 'Có lỗi ! Vui lòng thử lại...'
                            })
                    }
                    else if (quantity==-1) {//nếu số lượng -1 đưa ra tb
                        // @ts-ignore
                        Swal.fire({
                              icon: 'error',
                              title: 'Số lượng trong kho không đủ !'
                            })
                    }
                    else if (quantity==-2) {//nếu số lượng -2 đưa ra tb
                        // @ts-ignore
                        Swal.fire({
                              icon: 'error',
                              title: 'Sản phẩm chưa được cập nhật trong kho !'
                            })
                    }
                    else if (quantity==1) {//số lượng = 1 đưa ra tb 
                        // @ts-ignore
                        Swal.fire({
                              icon: 'success',
                              title: 'Thêm thành công !'
                            })
                    }
                    else if (quantity<0) {//nhỏ hơn 0 
                        alert("Có gì đó sai sai...!");
                    }
                    setTimeout(function(){//thiết lập thời gian 2 giây tải lại trang
                        window.location.reload();// tải lại url hiện tại
                    }, 2000);
                });
    });
});