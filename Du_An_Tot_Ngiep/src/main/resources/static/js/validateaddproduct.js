function clear() {//pt làm mới
	document.getElementById("ten").innerHTML = "";
	document.getElementById("anh").innerHTML = "";
	document.getElementById("tien").innerHTML = "";
	document.getElementById("ngayhethang").innerHTML = "";
	document.getElementById("nguongoc").innerHTML = "";
	//document.getElementById("chitietsanpham").innerHTML = "";
}

function checkempty(formproduct) {//kiểm tra các box
	clear();//pt xóa
	var date1 = new Date().getTime(); // current date
	var date2 = new Date(formproduct.dateOfManufacture.value).getTime();
	//thay đổi phần tử nội dung html
	if (formproduct.name.value.trim() == "") {
		document.getElementById("ten").innerHTML = "Không để trống tên!";
		return false;
	} 
	else if (formproduct.image.value.trim() == "") {
		document.getElementById("anh").innerHTML = "Không để trống ảnh!";
		return false;
	} 
	else if (formproduct.price.value.trim() == "") {
		document.getElementById("tien").innerHTML = "Không để trống giá tiền!";
		return false;
	} 
	else if (formproduct.dateOfManufacture.value.trim() == "") {
		document.getElementById("ngayhethang").innerHTML = "Không để trống ngày hết hạng!";
		return false;
	} 
	else if (formproduct.origin.value.trim() == "") {
		document.getElementById("nguongoc").innerHTML = "Không để trống nguồn gốc!";
		return false;
	} 
	else if (formproduct.price.value.trim() <1000 || formproduct.price.value == 0 ) {
		document.getElementById("tien").innerHTML = "Giá phải lớn lơn 1000!";
		return false;
	} 
	else if(date2 < date1){
		document.getElementById("ngayhethang").innerHTML = "Ngày hết hạn phải lớn hơn ngày hiện tại!";
		return false;
	}
	else {
		return true;
	}

}