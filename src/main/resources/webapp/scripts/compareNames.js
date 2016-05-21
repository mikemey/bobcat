function displayRandomNamePair() {
	$.get('competition/getnamepair' + stupidIE(), function(data) {
		var left = $('#left');
		var right = $('#right');
		left.children(0).text(data.first);
		left.show();
		left.css('background-color', '');
		right.children(0).text(data.second);
		right.show();
		right.css('background-color', '');
	});
}

function preferName(elmt) {
	var selectedName = $(elmt).children(0).text();
	var otherId = elmt.id === 'left' ? '#right' : '#left';
	$(elmt).animate({
		backgroundColor : '#7CBF24'
	});
	$(otherId).fadeOut(function() {
		displayRandomNamePair();
	});
	var otherName = $(otherId).children(0).text();
	$.post('competition/matchresult', {
		winner : selectedName,
		loser : otherName
	}, function(data) {
		$('#result').text(data);
	});
}