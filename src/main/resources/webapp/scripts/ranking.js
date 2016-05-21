function removeName(nameToRemove) {
	var confirm = window
			.confirm('Are you sure you want to remove the following name from the competition?\n\nName: '
					+ nameToRemove);
	if (confirm) {
		$.post('/ranking/remove_name', {
			name : nameToRemove
		}).success(function(data) {
			location.reload();
		});
	}
}

function modifyName(nameToModify) {
	var retval = window.prompt('Edit existing name:  ', nameToModify);
	if (retval) {
		$.post('/ranking/modify_name', {
			originalName : nameToModify,
			newName : retval
		}).success(function(data) {
			location.reload();
		});
	}
}