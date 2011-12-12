PAYPAL.namespace('tns');

/**
 * MID Read/drop
 * @author tilynn
 */

/**
 * Will append the flash to the page
 */
PAYPAL.tns.MIDinit = function() {	
	/* Check if Flash JS library was already loaded */
	if (typeof PAYPAL.util.Flash == 'object') {
		
		/* Location for the flash movie */
		PAYPAL.tns.flashDiv = document.createElement('div');
		document.body.appendChild(PAYPAL.tns.flashDiv);
		PAYPAL.tns.flashRef = PAYPAL.util.Flash.insertFlash(PAYPAL.tns.flashLocation,1,1,PAYPAL.tns.flashDiv,true,8,'midflash',true);
		setTimeout("PAYPAL.tns.flashInit()",500);
	}
}

/**
* Fires after the flash has finished loading
*/
PAYPAL.tns.flashInit = function() {
	 /* If token was passed to us, write it */
	 //alert("asdf");	 
 	if (PAYPAL.tns.token) {
 		setTimeout ( "PAYPAL.tns.flashRef.writeTokenValue(PAYPAL.tns.token)", 500);
 	} else {
 		/* Get token and append */
 		try 
 		{
 			var token = PAYPAL.tns.flashRef.getTokenValue();
 			if (token) {
 				PAYPAL.tns.appendField('fso',token);
 			} else {
 				PAYPAL.tns.appendField('fso_enabled',PAYPAL.util.Flash.getVersion());
 			}
 		} 
 		catch (e) { } 
 		/* Collect login button */
 		//PAYPAL.tns.appendField('login.x','Log in');
	 }
}
 
/**
 * Used to append the hidden form field to the forms on the page
 */
PAYPAL.tns.appendField = function(fieldName,value) {
	var formsCollection = document.getElementsByTagName('form');
	var field = document.createElement('input');
	field.setAttribute("type","hidden");
	field.setAttribute("name",fieldName);
	field.setAttribute("value",value);
	for (var i = 0; i < formsCollection.length ; i++) {
		formsCollection[i].appendChild(field.cloneNode(false));
	}
}

PAYPAL.tns.detectFsoBlock = function(resultOfFso) {	
	if(PAYPAL.tns.loginflow) {
		PAYPAL.tns.appendField('flow_name',PAYPAL.tns.loginflow);
	}
	if(!resultOfFso) {
		PAYPAL.tns.appendField('fso_blocked','true');
	}	
}


/* MG:  this function is engaged on the MiniPage to write a new FSO token to the client if it doesn't have one. The token string is passed via PPRD to javascript on the MiniPage.
/vobs/all/pexml/content/Risk/pages/default/en_US/general/FSOToken.MiniPage.aml
*/
PAYPAL.tns.FSODrop = function(fsotoken) {
		if (fsotoken != "") {
			PAYPAL.tns.flashRef.writeTokenValue(fsotoken);
		}
} 
/**
 * Start when the page has loaded
 */
YAHOO.util.Event.addListener(window,'load',PAYPAL.tns.MIDinit);