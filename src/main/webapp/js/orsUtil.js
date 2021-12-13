function selectAll(form) {
	var checkboxes = form.getElementsByTagName("input");
	for (var i = 0; i < checkboxes.length; i++) {
		if (checkboxes[i].type === 'checkbox') {
			if (document.getElementById("orsSelectAll").checked) {
				checkboxes[i].checked = true;
			} else {
				checkboxes[i].checked = false;
			}
		}
	}
}