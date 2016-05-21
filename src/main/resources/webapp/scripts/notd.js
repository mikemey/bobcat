function findNewName() {
	$.get('randomName' + stupidIE(), function(data) {
		var previousNameHtml = $('#randomNameCell').html();
		$('#randomNameCell').html(data);
		var newRow = $('<tr><td></td><td>' + previousNameHtml + '</td></tr>');
		$('#randomNameRow').after(newRow);
	});
	return false;
}

function addName(elmt, name) {
	postNewName(elmt, name);
}

function modifyName(elmt, name) {
	var retval = window.prompt('Add name to competition:', name);
	if (retval) {
		postNewName(elmt, retval);
	}
}
function postNewName(elmt, newname) {
	$.post('/ranking/add_name', {
		name : newname
	}, function() {
		var ulElement = $(elmt.parentNode);
		ulElement.fadeOut(200);
		var nameSpan = ulElement.prev();
		nameSpan.addClass('plussign');
	});
}