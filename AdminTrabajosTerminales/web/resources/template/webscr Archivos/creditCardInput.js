function showElement(element){
	YUD.replaceClass(element, 'accessAid', 'opened');
}

function hideElement(element){
	YUD.replaceClass(element, 'opened', 'accessAid');
}

function enableFields(elementArray){
for (var i=0; i<elementArray.length; i++) {
	if ( document.getElementById(elementArray[i]) ) {
		var fieldContainer = document.getElementById(elementArray[i]);
		var inputElements = fieldContainer.getElementsByTagName("input");
		for (var x = 0; x<inputElements.length; x++) {
			inputElements[x].removeAttribute('disabled');
		}
		showElement(fieldContainer);
	}
}
return;
}

function disableFields(elementArray){
for (var i=0; i<elementArray.length; i++) {
	if ( document.getElementById(elementArray[i]) ) {
		var fieldContainer = document.getElementById(elementArray[i]);
		var inputElements = fieldContainer.getElementsByTagName("input");
		for (var x = 0; x<inputElements.length; x++) {
			inputElements[x].setAttribute('disabled','true');
		}
		hideElement(fieldContainer);
	}
}
return;
}

PAYPAL.CreditCardInput = {
	cc_number : null,
	visa : null,
	electron : null,
	bancomer: null,
	mastercard : null,
	discover : null,
	amex : null,
	jcb : null,
	bank : null,
	cb:null,
	prepaid: null,
	postepay: null,
	cetelem: null,
	cofinoga: null,
	cofidis: null,
	cup:null,
	isCUP:0,


selCountry : "",
enableHighlight : null,
cardsDisabled: false,
init : function(){
	this.cc_number = document.getElementById("cc_number");
	 if(this.cc_number){
        this.cardsDisabled = this.cc_number.disabled;
        }

	if(document.getElementById('country_code')){
		var country = document.getElementById('country_code');
		if (country.options) {
			this.selCountry = country.options[country.selectedIndex].value;
		} else {
			this.selCountry = country.value;
		}
	}
	else
        {
         if(document.getElementById('cc_country_code')){
         var country = document.getElementById('cc_country_code');
         this.selCountry = country.value;
	        }
        }
	this.enableHighlight = (document.getElementById('cc_disable_highlighting')) ? false : true;
	if(!this.enableHighlight && !document.getElementById("radioCC")){
		this.selectDefaultRadioButton();	
		this.displayFields();
		YUE.addListener(YUD.getElementsByClassName('radio', 'label', 'cctype'), 'click', function(){
			this.displayFields();
		}, this, true);
		return false;
	}
	if(document.getElementById("FlagIsC2")){
		YUD.removeClass("cctype","radio");
		this.displayFields();
	}

	YUE.onContentReady("cctype", this.initRadioSelection, this, true);
	YUE.onContentReady("credit_card_type", this.initDropdownSelection, this, true);
	YUE.onContentReady("radioBank", this.initRadioToggle, this, true);                                                               
	return this;
},
initDropdownSelection : function(){
	var ccDropdown = document.getElementById("credit_card_type");
	//Do nothing if there is no pulldown
	if(ccDropdown && ccDropdown.options){
		if(!document.getElementById("radioBank")){
			this.displayFields();
		}
		
		YUE.addListener(ccDropdown, "change", function(){this.displayFields();}, this, true);
	}
},
cards: {},
initRadioSelection : function(){
	var cctype = document.getElementById("cctype");

	if (!YUD.hasClass(cctype, 'radio')) {
		YUD.addClass(cctype.getElementsByTagName('input'), 'accessAid');
	}
			if (YUD.hasClass(cctype, 'ccIcon')) {
				YUD.addClass(cctype, 'icon');
				YUD.removeClass(cctype, 'radio');
			}
	YUD.removeClass(['radioBank','radioCC'], 'accessAid');
	YUD.removeClass(YUD.getElementsByClassName('radioPayment'), 'accessAid');

	if (this.enableHighlight){
		YUD.addClass(cctype, 'selected');
	}
	this.cup = document.getElementById("pm-cup");
	this.cards.V = this.visa = document.getElementById("pm-visa");
	this.electron = document.getElementById("pm-electron");
	this.cards.M = this.mastercard = document.getElementById("pm-mastercard");
	this.discover = document.getElementById("pm-discover");
	this.cards.A = this.amex = document.getElementById("pm-amex");
	this.jcb = document.getElementById("pm-jcb");
	this.bank = document.getElementById("pm-bank");
	this.cb = document.getElementById("pm-cb");
	this.cetelem = document.getElementById("pm-cetelem");
	this.cofidis = document.getElementById("pm-cofidis");
	this.cofinoga = document.getElementById("pm-cofinoga");
	this.prepaid = document.getElementById("pm-prepaid");
	this.postepay = document.getElementById("pm-postepay");
	this.bancomer = document.getElementById("pm-bancomer");

	var uCCValue = this.cc_number.value.toUpperCase();
	var inputArr = document.getElementById("cctype").getElementsByTagName("input");
	var cardSelected = false;
	for(i=0;i<inputArr.length;i++){
		if(inputArr[i].checked){
			this.cardOn(inputArr[i].parentNode);
			cardSelected = true;
		}
        	if(this.enableHighlight){
                YUD.replaceClass('hlpHilite','hide','accessAid');                
	            inputArr[i].tabIndex=-1;
        	}else{
                YUD.replaceClass('hlpHilite','accessAid','hide');
                inputArr[i].tabIndex=0;
            }
            
	}

	if(uCCValue.indexOf("X") == -1 && !cardSelected){
		this.resetCards();
	}
	if((this.cc_number.value > 1) && (uCCValue.indexOf("X") == -1)){
		if(document.getElementById("FlagIsC2")){
			this.selectC2CCType();
		}else {	
			this.selectCCType();
		}	
	} else {
		if(!cardSelected){
			this.resetCards();
		}
	}
	if (this.enableHighlight) {
		if ( 'BR' == this.selCountry ) {
			var that = this;
			/*retrive bin from server*/
			PAYPAL.util.lazyLoad('/js/lib/yui/json.js', function() {
				var callback = {
					success: function(o){
						var data = YAHOO.lang.JSON.parse(o.responseText).indices;
						for ( var i = 0; i < data.length; i++ ) {
							data[i].index = new RegExp('^('+data[i].index.join('|')+')');
						}
						that.serverBinHighLightData = data;
						that.enableServerBinHighlight();
					},
					failure: function(o){
						that.addHighlightEvt();
					}
				};
				YAHOO.util.Connect.asyncRequest('GET', '/cgi-bin/webscr?cmd=_get-cc-bin-numbers&country='+that.selCountry, callback);
			});
		}
		else {
			this.addHighlightEvt();
		}
	}
},
serverBinHighLightData: {},
enableServerBinHighlight: function() {
	YUE.addListener('cc_number', 'keyup', this.serverBinSelect, this, true);
	YUE.addListener('cc_number', 'change', this.serverBinSelect, this, true);
	YUE.addListener('cc_number', 'blur', this.serverBinSelect, this, true);
},
serverBinSelect: function(e) {
	var el = YUE.getTarget(e);
	if ( el.value.length == 0 ) {
		this.resetCards();
		return;
	}
	var data = this.serverBinHighLightData;
	this.clearCards();
	for ( var i = 0; i < data.length; i++ ) {
		if ( data[i].index.test(el.value) ) {
			for ( var j = 0; j < data[i].type.length; j++ ) {
				this.cardOn(this.cards[data[i].type[j]]);
			}
			return;
		}
	}
},
addHighlightEvt: function() {
	if(document.getElementById("FlagIsC2")){
		YAHOO.util.Event.addListener("cc_number", "keyup", this.selectC2CCType, this, true);
		YAHOO.util.Event.addListener("cc_number", "change", this.selectC2CCType, this, true);
		YAHOO.util.Event.addListener("cc_number", "blur", this.selectC2CCType, this, true);
		
	} else{
		YAHOO.util.Event.addListener("cc_number", "keyup", this.selectCCType, this, true);
		YAHOO.util.Event.addListener("cc_number", "change", this.selectCCType, this, true);
		YAHOO.util.Event.addListener("cc_number", "blur", this.selectCCType, this, true);
	}
},
initRadioToggle : function(){
	this.togglePaymentFields();
	YUE.addListener("radioBank", "click", this.togglePaymentFields, this, true);
	YUE.addListener("radioCC", "click", this.togglePaymentFields, this, true);
},

cardOn : function(cardObj){
	if (cardObj){
		var cardRadioEl;

		cardRadioEl = cardObj.getElementsByTagName("input")[0];

		cardRadioEl.checked = true;

		if(!document.getElementById("radioBank") && !document.getElementById("FlagIsC2")){
			this.displayFields(cardRadioEl.value);
		}
		if (this.enableHighlight) {
			YUD.addClass(cardObj, 'hilite');
		}
	}
},

cardOff : function(cardObj){
	if (cardObj && this.enableHighlight) {
		YUD.removeClass(cardObj, 'hilite');
		var cardRadioEl = cardObj.getElementsByTagName("input")[0];
		cardRadioEl.checked = false;
	}
},

clearCards : function(){
	this.cardOff(this.visa);
	this.cardOff(this.electron);
	this.cardOff(this.mastercard);
	this.cardOff(this.discover);
	this.cardOff(this.amex);
	this.cardOff(this.jcb);
	this.cardOff(this.bank);
	this.cardOff(this.cb);
	this.cardOff(this.cetelem);
	this.cardOff(this.cofinoga);
	this.cardOff(this.cofidis);
	this.cardOff(this.prepaid);
	this.cardOff(this.postepay);
	this.cardOff(this.cup);
	this.cardOff(this.bancomer);
},

resetC2Cards :  function(){
	
	if(document.getElementById("cupOnlineTranseferNotEnabled")){
      document.getElementById("cupOnlineTranseferNotEnabled").checked = true;
   }
	if(document.getElementById('billingModule')){
		PAYPAL.Checkout.Billing.toggleAnimation('cupEntry',0);
		YUD.addClass("submitBilling","disabled");
		document.getElementById("submitBilling").setAttribute('disabled','true');
		YUD.addClass("fieldrowBankDOB","accessAid");
	}else if(document.getElementById('CCBlock')){
		YUD.addClass("add", "disabled");
      document.getElementById("add").setAttribute('disabled','true');
      YUD.addClass("cupOnlineServiceInfoBox", "accessAid");
      YUD.addClass("cupEntry", "accessAid");
      YUD.addClass("billingAddress", "accessAid");
	}
	
	if(YUD.hasClass('cupcheck','accessAid')) {
  		YUD.removeClass("cupcheck","accessAid");
		document.getElementById("cupOnlineTranseferEnabled").removeAttribute('disabled');
		document.getElementById("cupOnlineTranseferNotEnabled").removeAttribute('disabled');
	}
		
	YUD.addClass("cupEnabled","unavailable");
	YUD.addClass("fieldrowCCExpDate","accessAid");
	YUD.addClass("fieldrowCSC","accessAid");	
	
},

resetCards : function(){
	this.cardOn(this.visa);
	this.cardOn(this.electron);
	this.cardOn(this.mastercard);
	this.cardOn(this.discover);
	this.cardOn(this.amex);
	this.cardOn(this.jcb);
	this.cardOn(this.bank);
	this.cardOn(this.cb);
	this.cardOn(this.cetelem);
	this.cardOn(this.cofinoga);
	this.cardOn(this.cofidis);
	this.cardOn(this.prepaid);
	this.cardOn(this.postepay);
	this.cardOn(this.cup);
	this.cardOn(this.bancomer);
	var radioButtons = document.getElementById("cctype").getElementsByTagName("input");
	radioButtons[radioButtons.length - 1].checked = false;
	
	if(document.getElementById("FlagIsC2")){
		this.resetC2Cards();	
	}
	this.isCUP = 0;
},

toggleInputFields : function(radioElement){
	var selection = radioElement.id.substring(5);
	var fieldsEl = document.getElementById("fields" + selection);
	if(radioElement.checked == true){
		showElement(fieldsEl);
		if(selection == "Bank"){
			this.cc_number.value = "";
			this.clearCards();
			document.getElementById("bank").checked = true;
			this.cardOn(this.bank);
		} else {
			if(this.cc_number.value == ""){
				this.resetCards();
			}
			this.cardOff(this.bank);
			this.clearInputFields("fieldrowBankDOB");
		}
	}else{
		hideElement(fieldsEl);
	}
},

togglePaymentFields : function(){
	var radioBankInput = document.getElementById("radioBank");
	var radioCCInput = document.getElementById("radioCC");
	this.toggleInputFields(radioBankInput);
	this.toggleInputFields(radioCCInput);
},

clearInputFields : function(elId){
	var inputArray = document.getElementById(elId).getElementsByTagName("input");
	for(i=0;i<inputArray.length;i++){
		if(inputArray[i].type != 'hidden'){
			inputArray[i].value="";
		}
	}
},


selectCCType : function(){
	var bound = 1;
	if (!this.enableHighlight || YUD.hasClass('fieldsCC', 'accessAid') || (this.cc_number.value.toUpperCase()).indexOf("X") !=-1) return;
	var ccNumber = this.cc_number.value;
	if (ccNumber.length >= bound) {
		this.clearCards();
		switch (ccNumber.substring(0,1)) {
			case '3':
				//51 = 3 = Diners (Discover) (36) or Amex (34 or 37)  (7=55)
				if (ccNumber.substring(1,2) == '6' && this.discover ) {
					this.cardOn(this.discover);
				} else if (ccNumber.substring(1,2) == '5' && this.jcb ) {
					this.cardOn(this.jcb);
				} else {
					this.cardOn(this.amex);
				}
				
				break;
			case '4':
				//52 = 4 = visa
				var isElectron = false;
				var isBancomer = false;
				//check for visa electron if available
				if ( this.electron ) {
					var regex = /^4917|4913|4508|4844|417500/
					isElectron = regex.test(ccNumber);
				}
				//check for bancomer if available
				if ( this.bancomer ) {
					var regex = /^410180|410181|418073|418075|418080|418090|441311|444085|455500|455501|455503|455504|455505|455508|455513|455525|455526|455527|455529|455540|455545|493160|493161|493162|494398/
					isBancomer = regex.test(ccNumber);
				}
				if (isElectron) {
					this.cardOn(this.electron);
				} else if ( isBancomer ) {
					this.cardOn(this.bancomer);
				}
				else {
					this.cardOn(this.visa);
				}

				break;
			case '5':
				//53 = 5 = mastercard
				var isBancomer = false;
				var isCetelem = false;
				if ( this.cetelem ) {
					var regex = /^501765|501766/;
					isCetelem = regex.test(ccNumber);
				}
				if ( this.bancomer ) {
					var regex = /^522498|542010|542977|544053|544551|547077|547086|547095|547155|547156/;
					isBancomer = regex.test(ccNumber);
				}
				if ( isBancomer ) {
					this.cardOn(this.bancomer);
				}
				else if ( isCetelem ) {
					this.cardOn(this.cetelem);
				}
				else {
					this.cardOn(this.mastercard);
				}
				break;
			case '6':
				//54 = 6 = discover
				this.cardOn(this.discover);
				break;
		}
	} else {
		this.resetCards();
		this.cardOff(this.bank);
	}
},

displayCUPFields : function(){
	var ccNumber = this.cc_number.value;
	
	if(this.isCUP == 0) {
		this.resetC2Cards();
	}

	this.clearCards();
	this.cardOn(this.cup);
	if(ccNumber.length >= 11){
		YUD.removeClass("cupEnabled","unavailable");
		if(document.getElementById('CCBlock')){
			YUD.addClass("billingAddress","accessAid");
		}
		if(document.getElementById("cupOnlineTranseferNotEnabled").checked){
			YUD.removeClass("cupOnlineServiceInfoBox","accessAid");
		}	
		if(PCU){
			PCU.resizeMiniCart();
		}	
	}
	YUD.addClass("fieldrowCCExpDate","accessAid");
	YUD.addClass("fieldrowCSC","accessAid");
	
},

displayNonCUPFields : function(){
	var ccNumber = this.cc_number.value;
	
	if(this.isCUP == 1) {
		this.resetC2Cards();
	}
	YUD.addClass("cupEnabled","unavailable");
	if(ccNumber.length >= 11 && document.getElementById('billingModule')){
		PAYPAL.Checkout.Billing.toggleAnimation('cupEntry',PAYPAL.Checkout.Billing.cupEntryHeight);
		if(document.getElementById("fieldrowBankDOB")) {
			YUD.removeClass("fieldrowBankDOB","accessAid");			
		}
		YUD.removeClass('submitBilling','disabled');
		document.getElementById("submitBilling").removeAttribute('disabled');
	}else if(ccNumber.length >= 11 && document.getElementById('CCBlock')){
		YUD.removeClass('add','disabled');
      document.getElementById("add").removeAttribute('disabled');
      YUD.removeClass("billingAddress","accessAid");
	}
	
	enableFields(["fieldrowCSC","fieldrowCCExpDate","add"]);
	
},

selectC2CCType : function(){
	var bound = 6;
	this.isCUP = 0;
	if (!this.enableHighlight || YUD.hasClass('fieldsCC', 'accessAid') || (this.cc_number.value.toUpperCase()).indexOf("X") !=-1) return;
	var ccNumber = this.cc_number.value;
	if (ccNumber.length >= bound) {
		this.clearCards();
		switch (ccNumber.substring(0,1)) {
			case '1':
				//1 = CUP
				this.displayCUPFields();
				this.isCUP = 1;			
				
				break;
			case '3':
				//51 = 3 = Diners (Discover) (36) or Amex (34 or 37)  (7=55)
				if (ccNumber.substring(1,2) == '6') {
					this.clearCards();
					this.cardOn(this.discover);
				} else if (ccNumber.substring(1,2) == '5') {
					this.clearCards();
					this.cardOn(this.jcb);
				} else {
					this.clearCards();
					this.cardOn(this.amex);
				}
				
				this.displayNonCUPFields();
				
				break;
			case '4':
				//52 = 4 = visa

				if(document.getElementById("FlagIsC2")) {
					var cupPrefixShort4 = [45806,49102,49104];
					for (var i=0; i<cupPrefixShort4.length; i++) {
						if (ccNumber.substring(0,5) == cupPrefixShort4[i]) {
							this.displayCUPFields();
							this.isCUP = 1;
						}
					}
					
					var cupPrefix4 = [400360,404119,404120,405512,406252,406254,409669,409670,412962,421349,427018,433666,433667,436718,436728,436738,436742,438588,434061,434062,436728,436742,451804,451810,451811,453242,456351,458071,486493,486494,491031,491032,491035,491037,491038];
					for (var i=0; i<cupPrefix4.length; i++) {
						if (ccNumber.substring(0,bound) == cupPrefix4[i]) {
							this.displayCUPFields();
							this.isCUP = 1;
						}
					}
						
				
				}
				
				if(this.isCUP == 0){
					this.clearCards();
					this.cardOn(this.visa);
					this.cardOn(this.electron);
					this.cardOn(this.bancomer);
					//check for visa electron
					var isElectron = false;
					var isBancomer = false;
					if (ccNumber.length >= 4) {
						var electronPrefixArray4 = [4917,4913,4508,4844];
						for (i=0; i<electronPrefixArray4.length; i++) {
							if (ccNumber.substring(0,4) == electronPrefixArray4[i]) {
								isElectron = true;
							}
						}
						if (ccNumber.length >= 6 && !isElectron) {
							var electronPrefixArray6 = [417500];
							var bancomerPrefix = [410180,410181,418073,418075,418080,418090,441311,444085,455500,455501,455503,455504,455505,455508,455513,455525,455526,455527,455529,455540,455545,493160,493161,493162,494398];
							for (i=0; i<electronPrefixArray6.length; i++) {
								if (ccNumber.substring(0,6) == electronPrefixArray6[i]) {
									isElectron = true;
								}
							}
							for (i=0; i<bancomerPrefix.length; i++) {
								if ( ccNumber.substring(0,6) == bancomerPrefix[i] ) {
									isBancomer = true;
								}
							}
						}
					}
					if (isElectron) {
						this.cardOn(this.electron);
						this.cardOff(this.visa);
						this.cardOff(this.bancomer);
					} else if ( isBancomer ) {
						this.cardOn(this.bancomer);
						this.cardOff(this.electron);
						this.cardOff(this.visa);
					}
					else {
						this.cardOn(this.visa);
						this.cardOff(this.electron);
						this.cardOff(this.bancomer);
					}
					
					this.displayNonCUPFields();
				}
				break;
			case '5':
				//53 = 5 = mastercard
				if(document.getElementById("FlagIsC2")) {
					var cupPrefixShort5 = [53098,53242,53243,53591,53783];
					for (var i=0; i<cupPrefixShort5.length; i++) {
						if (ccNumber.substring(0,5) == cupPrefixShort5[i]) {
							this.displayCUPFields();
							this.isCUP = 1;
						}
					}
					var cupPrefix5 = [514957,514958,518364,518377,518378,518379,518474,518475,518476,519412,520082,524090,524091,524094,525498,526410,544033,547766,552245,552853,553242,558868];
					for (var i=0; i<cupPrefix5.length; i++) {
						if (ccNumber.substring(0,bound) == cupPrefix5[i]) {
							this.displayCUPFields();
							this.isCUP = 1;
						}
					}

				}
				
				if(this.isCUP == 0){
					this.clearCards();
					this.cardOn(this.mastercard);
					this.cardOn(this.bancomer);
					this.cardOn(this.cetelem);
					var isBancomer = false;
					var isCetelem = false;
					if (ccNumber.length >= 6) {
						var prefixArray = [501765,501766];
						for (i=0; i<prefixArray.length; i++) {
							if (ccNumber.substring(0,6) == prefixArray[i]) {
								isCetelem = true;
								break;
							}
						}
					}
					if ( ccNumber.length >= 6 ) {
						var bancomerPrefix = [522498,542010,542977,544053,544551,547077,547086,547095,547155,547156];
						for (i=0; i<bancomerPrefix.length; i++) {
							if (ccNumber.substring(0,6) == bancomerPrefix[i]) {
								isBancomer = true;
								break;
							}
						}
					}
					if ( isBancomer ) {
						this.cardOff(this.mastercard);
						this.cardOff(this.cetelem);
						this.cardOn(this.bancomer);
					}
					else if ( isCetelem ) {
						this.cardOff(this.bancomer);
						this.cardOff(this.mastercard);
						this.cardOn(this.cetelem);
					}
					else {
						this.cardOff(this.bancomer);
						this.cardOff(this.cetelem);
						this.cardOn(this.mastercard);
					}
					
					this.displayNonCUPFields();
				}
				break;
			case '6':
				//6 = CUP
			case '9':
				//9 = CUP
				this.displayCUPFields();
				this.isCUP = 1;
				break;
			default:
				this.clearCards();
		}
	} else {
		this.resetCards();
		this.cardOff(this.bank);
	}
},

displayFields : function(cardType){
	var selectedCCType = "";
	if (cardType) {
		selectedCCType = cardType;
	}else{
		var ccDropdown = document.getElementById("credit_card_type");
		if(ccDropdown && ccDropdown.options){
			selectedCCType = ccDropdown.options[ccDropdown.selectedIndex].value;
			if(typeof PCU != 'undefined') {
                        	if(selectedCCType != "") {
	                                PCU.resizeMiniCart();
					}
			}
		}else{
			var ccRadioArray = document.getElementById('cctype').getElementsByTagName('input');
			for(i=0;i<ccRadioArray.length;i++){
				if(ccRadioArray[i].checked==true){
					selectedCCType = ccRadioArray[i].value;
				}
			}
		}
	}
	if(this.cardsDisabled && !ccDropdown) return;
	disableFields(["fieldrowCCExpDate","fieldrowCSC","fieldrowCCDOB","fieldsBank","cardIssueInformation","cuplink","cupoptional","cupcheck","billingAddress"]);
	switch (selectedCCType){
		case "V": case "D": case "M": case "C": case "A": case "J": case "b": case "p" : case "t" :
			enableFields(["fieldrowCCNumber","fieldrowCCExpDate","fieldrowCSC","billingAddress"]);
			break;
		case "O": case "S":
			enableFields(["fieldrowCCNumber","cardIssueInformation","fieldrowCCExpDate","fieldrowCSC","billingAddress"]);
			break;
		case "N": case "Q":
			enableFields(["fieldrowCCNumber","fieldrowCCExpDate","fieldrowCCDOB","billingAddress"]);

			if (selectedCCType=="N" && (this.selCountry == 'IT' || this.selCountry == 'ES')){
				enableFields(["fieldrowCSC"]);
			}
			break;
		case "L":
			enableFields(["fieldrowCCNumber","fieldrowCCDOB","billingAddress"]);
			break;
		case "c":
			enableFields(["fieldrowCCNumber","cuplink","cupoptional","cupcheck"]);
			break;
		default:
			disableFields(["fieldrowCCNumber"]);
			enableFields(["billingAddress"]);
			if (selectedCCType != ""){
				enableFields(["fieldsBank","fieldrowCCDOB"]);
			}
			
			if( document.getElementById( 'cc_number_update_mode' )){
				enableFields(["fieldrowCCNumber","fieldrowCCDOB"]);
			}
			if(document.getElementById("FlagIsC2")) {
				enableFields(["fieldrowCCNumber","fieldrowCCExpDate","fieldrowCSC","cuplink","cupoptional","cupcheck"]);
				if(document.getElementById('CCBlock')){
					YUD.addClass("billingAddress","accessAid");
				}
			}
		}

	if(document.getElementById('country_code') && document.getElementById('country_code').value == 'AU'){
		enableFields(["fieldrowCCDOB"]);
	}

	if(document.getElementById("cup")) {
		enableFields("cup");
	}
	
	if(document.getElementById('DisableAllFields') && document.getElementById("FlagIsC2")){
		document.getElementById("cc_number").setAttribute('disabled','true');
	}

	var cupOnlineTransferIds = ["cupOnlineTranseferEnabled", "cupOnlineTranseferNotEnabled"];
	YAHOO.util.Event.addListener(cupOnlineTransferIds, "click", this.toggleCupInfo);
	
	
		
	},

toggleCupInfo : function(){
		//Yes Case
		if(this.value == '1' || document.getElementById("cupOnlineTranseferEnabled").checked)
			 {
			 	 if(document.getElementById('billingModule')){
			 	 	 YUD.removeClass("submitBilling", "disabled");
					 document.getElementById("submitBilling").removeAttribute('disabled');
			 	 	 YUD.addClass("cupOnlineServiceInfoBox", "accessAid");
			 	 	 PAYPAL.Checkout.Billing.toggleAnimation('cupEntry',PAYPAL.Checkout.Billing.cupEntryHeight);
			 	 	 if(document.getElementById("fieldrowBankDOB")) {
			 	 	 	YUD.removeClass("fieldrowBankDOB","accessAid");			
			 	 	 }
			 	 }else if(document.getElementById('CCBlock')){
			 	 	YUD.removeClass("add", "disabled"); 
					document.getElementById("add").removeAttribute('disabled');
			 	 	YUD.addClass("cupOnlineServiceInfoBox", "accessAid");
			 	 }		
				 
			 }
			 //No Case
			 else
			 {
			 	 if(document.getElementById('billingModule')) {
			 	 	 YUD.addClass("submitBilling", "disabled");
					 document.getElementById("submitBilling").setAttribute('disabled','true');
			 	 	 YUD.removeClass("cupOnlineServiceInfoBox", "accessAid");
			 	 	 PAYPAL.Checkout.Billing.toggleAnimation('cupEntry',0);
			 	 	 if(document.getElementById("fieldrowBankDOB")) {
			 	 	 	YUD.addClass("fieldrowBankDOB","accessAid");			
			 	 	 }
			 	 }else if(document.getElementById('CCBlock')){
			 	 	YUD.addClass("add", "disabled");
					document.getElementById("add").removeAttribute('disabled');
			 	 	YUD.removeClass("cupOnlineServiceInfoBox", "accessAid");
			 	 }
 
			 }
			 if(PCU){
			 	 PCU.resizeMiniCart();
			 }
		},
		
selectDefaultRadioButton : function(){
	var inputArr = document.getElementById("cctype").getElementsByTagName("input");
	var inputArrLength = inputArr.length;
		if(inputArrLength > 0){
			var cardSelected = false;
			for(i = 0; i < inputArrLength; i++){
				if(inputArr[i].checked){
					cardSelected = true;
					inputArrLength = 0;
				}
			}
			if(!cardSelected){
				inputArr[0].checked = true;
			}
		}
	}	
		
}

if(document.getElementById("FlagIsC2")) {
	YUE.onDOMReady("cc_number", PAYPAL.CreditCardInput.init, PAYPAL.CreditCardInput, true);
}else {
	YUE.onContentReady("cc_number", PAYPAL.CreditCardInput.init, PAYPAL.CreditCardInput, true);
}

YUE.addListener(YUD.getElementsByClassName('mc_subtype'), 'click', function() {
			document.getElementById('cc_brand').value=this.id;
		});
