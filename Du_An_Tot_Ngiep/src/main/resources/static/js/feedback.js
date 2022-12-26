/**
 * 
 */
function addFeedBack() {
	var dataFeeBack = {
		"name": $('#nameAddFeedBack').val(),
		"email": $('#emailAddFeedBack').val(),
		"subjects": $('#subjectsAddFeedBack').val(),
		"content": $('#contentAddFeedBack').val()
	};
	$.ajax({
		type: "POST",//pt post
		contentType: "application/json; charset=utf-8",//pt json tv 
		dataType: 'json',
		url: "/manager/addFeedBackAjax",//đường dẫn
		data: JSON.stringify(dataFeeBack),
		success: function(result) {
			alert("cảm ơn đã góp ý");//đưa ra tb gửi thành công
			$('#nameAddFeedBack').val('');
			$('#emailAddFeedBack').val('');
			$('#subjectsAddFeedBack').val('');
			$('#contentAddFeedBack').val('');
		},
		error: function(result) {
			alert('failedd');
		}
	});
}