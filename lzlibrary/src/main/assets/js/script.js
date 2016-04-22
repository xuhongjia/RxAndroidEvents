  function removeLogisticsTitle()
  {
  var   header = document.getElementsByClassName('ui-header'),
  		footer = document.getElementsByClassName('ui-footer'),
  		content = document.getElementsByClassName('ui-content'),
  		result = document.getElementById('result'),
  		comname = document.getElementById('result-comname'),
        kuaidinum = document.getElementById('result-kuaidinum');

  		for(var i=0;i<header.length;i++)header[i].parentNode.removeChild(header[i]);
  		for(var i=0;i<footer.length;i++)footer[i].parentNode.removeChild(footer[i]);
  		for(var i=0;i<content.length;i++)content[i].style.margin = '0';
  		result.style.padding = '0';

  		comname.parentNode.removeChild(comname);
        kuaidinum.parentNode.removeChild(kuaidinum);
    }
