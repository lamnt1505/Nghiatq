var email_regex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
var string_regex = /^[a-zA-Z]+$/;
var number_regex = /^([0-9])+$/;
var regex_pass = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
var phone_regex = /(84|0[3|5|7|8|9])+([0-9]{8})\b/;
function clear() {
	document.getElementById("ten").innerHTML = "";
	document.getElementById("matkhau").innerHTML = "";
	document.getElementById("nhaplaimatkhau").innerHTML = "";
	document.getElementById("sdt").innerHTML = "";
	document.getElementById("ngaysinh").innerHTML = "";
}

//trang query kiểm tra trang đăng ký

function checkempty(form) {
	clear();
	if (form.fullname.value.trim() == "") {
		document.getElementById("ten").innerHTML = "Không để trống tên!";
		return false;
	} 
	else if (form.password.value.trim() == "") {
		document.getElementById("matkhau").innerHTML = "Không để trống mật khẩu!";
		return false;
	} 
	else if (form.repass.value.trim() == "") {
		document.getElementById("nhaplaimatkhau").innerHTML = "Không để trống nhập lại mật khẩu!";
		return false;
	} 
	else if (form.phone.value.trim() == "") {
		document.getElementById("sdt").innerHTML = "Không để trống số điện thoại!";
		return false;
	} 
	else if (form.birthday.value.trim() == "") {
		document.getElementById("ngaysinh").innerHTML = "Không để trống ngày sinh!";
		return false;
	} 

	else if (!(form.password.value.match(regex_pass))) {
		document.getElementById("matkhau").innerHTML = "Mật khẩu từ 6 đến 20 ký tự có chứa ít nhất một chữ số, một chữ hoa và một chữ cái viết thường!";
		return false;
	}
	else if(form.password.value != (form.repass.value)){
		document.getElementById("nhaplaimatkhau").innerHTML = "Xác nhận mật khẩu không chính xác!";
		return false;
	}
	else if (!(form.phone.value.match(phone_regex))) {
		document.getElementById("sdt").innerHTML = "Số điện thoại phải đủ 10 số và bắt đầu từ số 0!";
		return false;
	}
	else {
		return true;
	}

}