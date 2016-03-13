var request = null;
function sendPostRequestJSON(url, id) {
	request = new XMLHttpRequest();
	request.onreadystatechange = doReadyStateChange;
	request.open("POST", url, true);
	request.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
	
	var messageBody = new Object();
	messageBody.action = "jsonJson";
	messageBody.id = id;
	request.send(JSON.stringify(messageBody));
}
function sendGetRequest(url, id, action) {
	console.log("QQ");
	var queryString = "action="+action+"&id="+id+"&dummy="+new Date().getTime();
	request = new XMLHttpRequest();
	request.onreadystatechange = doReadyStateChange;
	request.open("GET", url+"?"+queryString, true);
	request.send(null);
}
function sendPostRequest(url, id, action) {
	var queryString = "action="+action+"&id="+id;
	request = new XMLHttpRequest();
	request.onreadystatechange = doReadyStateChange;
	request.open("POST", url, true);
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.send(queryString);
}
function processJSON(data) {
	var json = JSON.parse(data);
	var showTextNode = document.createTextNode(json[0].text);
	var spanElement = document.getElementsByTagName("span")[0];
	spanElement.appendChild(showTextNode);
	if(json[0].hasMoreData) {
		document.forms[0].id.value = json[1].id;
		document.forms[0].name.value = json[1].name;
		document.forms[0].price.value = json[1].price;
		document.forms[0].make.value = json[1].make;
		document.forms[0].expire.value = json[1].expire;
	}
}
function doReadyStateChange() {
	if(request.readyState==4) {
		if(request.status==200) {
			document.getElementsByTagName("img")[0].style.display = "none";
//			processText(request.responseText);
//			processXML(request.responseXML);
			processJSON(request.responseText);
		} else {
			console.log("Error Code:"+request.status+", "+request.statusText);
		}
	}
}
function processText(data) {
	var show = data;
	var index = data.indexOf(":");
	if(index!=-1) {
		show = data.substring(0, index);
		var temp = data.substring(index+1);
		var array = temp.split(",");		
		document.forms[0].id.value = array[0];
		document.forms[0].name.value = array[1];
		document.forms[0].price.value = array[2];
		document.forms[0].make.value = array[3];
		document.forms[0].expire.value = array[4];
	}
	var showTextNode = document.createTextNode(show);
	var spanElement = document.getElementsByTagName("span")[0];
	spanElement.appendChild(showTextNode);
}
function processXML(dom) {
	var show = dom.getElementsByTagName("text")[0].firstChild.nodeValue;
	var showTextNode = document.createTextNode(show);
	var spanElement = document.getElementsByTagName("span")[0];
	spanElement.appendChild(showTextNode);
	
	var hasMoreData = dom.getElementsByTagName("hasMoreData")[0].firstChild.nodeValue;
	if(hasMoreData=="true") {
		document.forms[0].id.value =
			dom.getElementsByTagName("id")[0].firstChild.nodeValue;
		document.forms[0].name.value =
			dom.getElementsByTagName("name")[0].firstChild.nodeValue;
		document.forms[0].price.value =
			dom.getElementsByTagName("price")[0].firstChild.nodeValue;
		document.forms[0].make.value =
			dom.getElementsByTagName("make")[0].firstChild.nodeValue;
		document.forms[0].expire.value =
			dom.getElementsByTagName("expire")[0].firstChild.nodeValue;
	}
}