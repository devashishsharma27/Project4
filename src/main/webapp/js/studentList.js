function orsDelete1(form){
	var orsArray = new Array();
	var isChecked = false ;
	var checkboxes = form.getElementsByTagName("input");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].type === 'checkbox') {
			if(checkboxes[i].checked){
				isChecked = true ;				
				orsArray.push(checkboxes[i].value);
				
			}
		}
	}
	
	if(isChecked){
	//	document.getElementsByName("selectedRecord").value = orsArray ;
		document.getElementById("selectedRecord").value = orsArray ;
	//	document.getElementById("studentForm").submit();
	}else{
		alert('Please select atleast one Record');
		
	}
	
}

