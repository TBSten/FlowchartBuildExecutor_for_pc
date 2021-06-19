
function init(){
	$("#main").empty() ;
	$("#main").removeAttr("class");
	$("#main").addClass("baseDesign");
	if(exeType === "msgBox"){
		//none
	}else if(exeType === "log"){
		$("#main").append(`<ul id="log"></ul>`);
		$("#main").addClass("terminalDesign");
	}else if(exeType === "table"){
		$("#main").append(`<table border="1" id="log"></table>`);
	}else {
		alert("実行タイプに対応していません。");
	}
}
function output(...arg){
	console.log(arg);
	if(exeType === "msgBox"){
		alert(arg[0]);
	}else if(exeType === "log"){
		$("#log").append(`<li>${arg[0]}</li>`);
	}else if(exeType === "table"){
		let ans = "" ;
		arg.forEach((ele)=>{
			ans += `<td>${ele}</td>` ;
		});
		$("#log").append(`<tr>${ans}</tr>`);
	}
	return arg ;
}
function input(arg){
	return prompt(arg);
}


//##startPoint##

init() ;
