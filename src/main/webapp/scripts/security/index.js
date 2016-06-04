
$(document).ready(function(){
	$("#btnSetSession").click(function(){
	    $.ajax({
	        type: "POST",
	        headers: {
	            Accept: "application/json"
	        },
	        url: contextPath+'/login/checkLogin',
	        data :{
	        	userName:$("#userName").val()
	        },
	        complete: function(xhr) {

	        },
	        async: false
	    });
	});

	$("#btnGetSession").click(function(){
	    $.ajax({
	        type: "GET",
	        headers: {
	            Accept: "application/json"
	        },
	        url: contextPath+'/login/getUserName',
	        complete: function(xhr) {
	        	alert(xhr.responseText);
	        },
	        async: false
	    });
	});

	$("#btnGetSessionGeneral").click(function(){
	    $.ajax({
	        type: "GET",
	        headers: {
	            Accept: "application/json"
	        },
	        url: contextPath+'/login/setSessionLogin',
	        data :{
	        	userName:$("#userName").val()
	        },
	        complete: function(xhr) {

	        },
	        async: false
	    });
	});

})