PAYPAL.Checkout.Billing = {
	iPwd : null,
	iPhoneNum : null,
	cupEntryHeight : 0,
	passwordEntryHeight : 0,
	Anim : YAHOO.util.Anim,
	
	init: function(){
	//	var ua = YAHOO.env.ua();
	    
		// Run onContentUpdate to attach event listeners
		PAYPAL.Checkout.Billing.onContentUpdate();
		
		// Initialize animation for C2
		if(document.getElementById("FlagIsC2")) {
			YUD.replaceClass("cctype","radio cctype","cctype icon selected");
			YUD.addClass("cupJSDisabled","accessAid");
			YUD.addClass("submitBilling", "disabled");
			document.getElementById("submitBilling").disabled = true;
			YUD.addClass("fieldrowCCExpDate","accessAid");
			YUD.addClass("fieldrowCSC","accessAid");
			if(document.getElementById("fieldrowBankDOB")) {
				YUD.addClass("fieldrowBankDOB","accessAid");			
			}
			this.initAnimation();
			if(document.getElementById("cupOnlineTranseferEnabled").checked) {
			 	 YUD.removeClass("submitBilling", "disabled");
			 	 document.getElementById("submitBilling").removeAttribute('disabled');
			 	 YUD.addClass("cupOnlineServiceInfoBox", "accessAid");
			 	 PAYPAL.Checkout.Billing.toggleAnimation('cupEntry',PAYPAL.Checkout.Billing.cupEntryHeight);
			 }	 

		}	

		// Initialize Shipping Calculator
		PAYPAL.widget.ShippingCalculator.init();

		//Initialize Address Prefill
		PAYPAL.widget.Address.init();

		//add listener to credit card widget for resizing mini cart
		YUE.on("credit_card_type","change", PCU.resizeMiniCart);

		//For changing the Submit button Label - GlobalCredit
		if (document.getElementById('creditOfferSbmtBtnLabel')){
			if (document.getElementById("submitBilling")){
				sbmtbtnOriginal = document.getElementById("submitBilling").value;
			}
			if (document.getElementById('creditOfferSbmtBtnLabel')){
				offerButtonText=document.getElementById('creditOfferSbmtBtnLabel').value;
			}
		PAYPAL.Checkout.Billing.evtCreditMethodSelected();
		}
		
		//this addresses the IE tabindex issue around the hidden credit card radio inputs
		if (YAHOO.env.ua.ie) {
			this.ccTabListen();
		}
	},
	
	ccTabListen: function() {
		var ccField = document.getElementById('cc_number');
		var expDateField = document.getElementById('expdate_month');
        
		if (ccField && expDateField) {
			ccField.onkeydown = function(evt) {
				var myKey = evt || window.event;
				if (myKey.keyCode == 9) { //the tab key is 9
					
					//a race condition occurs that requires a tiny delay before setting the focus
					setTimeout(function() {
						expDateField.focus();
					}, 10);
				}
			};
		}
	},
		
	initAnimation: function () {
		var region = YAHOO.util.Dom.getRegion(document.getElementById('cupEntry'));
		this.cupEntryHeight = parseInt(region.bottom) - parseInt(region.top);
		this.toggleAnimation('cupEntry',0);

	},
	
	toggleAnimation: function(divId, divHeight) {
		
		var animation = new this.Anim(divId, {height: {to: divHeight } }, 1, YAHOO.util.Easing.easeNone);
		animation.onComplete.subscribe(PCU.resizeMiniCart);
		animation.animate();
	},
	
	showPasswordEntryFields: function() {		
		var oPasswordEntry = document.getElementById('passwordEntry');
		YUD.removeClass("passwordEntry", "hide");
		PAYPAL.Checkout.Billing.twisterButtonLabel("showPasswordEntryFields");
		if(document.getElementById("FlagIsC2")) {
			var region = YAHOO.util.Dom.getRegion(oPasswordEntry);	
			this.passwordEntryHeight  =  parseInt(region.bottom) - parseInt(region.top) + 20;
			var newHeight = PAYPAL.Checkout.Billing.cupEntryHeight + this.passwordEntryHeight;
			YAHOO.util.Dom.setStyle("cupEntry","height",String(newHeight) + "px");
		}		
		document.getElementById('createPasswordExpanded').value = "opened";		
	},
	
	hidePasswordEntryFields: function() {		
		PAYPAL.Checkout.Billing.twisterButtonLabel("hidePasswordEntryFields");
		if(document.getElementById("FlagIsC2")) {
			YAHOO.util.Dom.setStyle("cupEntry","height",String(PAYPAL.Checkout.Billing.cupEntryHeight) + "px");
		}
		if (!YUD.hasClass("passwordEntry", "opened")){
			YUD.addClass("passwordEntry", "hide");
		}
			document.getElementById('createPasswordExpanded').value = "closed";		
	},
	
	evtCreditMethodSelected: function(){
		 var l = document.getElementsByTagName("input");
		 var i = l.length;
		 var cc = document.getElementById("fieldsCC");
		 var csc = document.getElementById("fieldrowCSC")
		 var bml = false;
		 for (var c = 0; c < i; c++) {
		 	if (l.item(c).type == "radio" && l.item(c).name == "flag_show_ppc_signup_selected" && l.item(c).value == "BML" && l.item(c).checked){
				bml = true;
				break;
			}
		 }
		 if (cc) {
		 	if (bml) {
			 	YUD.addClass(cc, "accessAid");
				YUD.addClass(csc,"accessAid");
				// BML offer selected - For GlobalCredit
					YUD.replaceClass('creditOfferAcceptance', 'hide', 'show');
					if (document.getElementById('creditOfferSbmtBtnLabel')){
						 document.getElementById('submitBilling').value=offerButtonText;
					}
					else{
						if (document.getElementById("submitBilling")){
							document.getElementById('submitBilling').value=sbmtbtnOriginal;
						}
					}
			} else {
				YUD.removeClass(cc, "accessAid");
				YUD.removeClass(csc,"accessAid");
				// GlobalCredit not selected
				YUD.replaceClass('creditOfferAcceptance', 'show', 'hide');
				document.getElementById('submitBilling').value=sbmtbtnOriginal;
			}
		 }
		PCU.resizeMiniCart(true);
	},
	evtEnableHideShow: function (el,ns){
		var div = document.getElementById(el);
		switch (ns){
			case "TOS":
				if (this.iPwd.value.length > 0){
					YUD.replaceClass(div, "accessAid", "opened");
				} else {
					YUD.replaceClass(div, "opened", "accessAid");
				}
				break;
			case "GC" :
				if (this.iGC.checked && YUD.hasClass(div, "accessAid")){
					YUD.replaceClass(div, "accessAid", "opened");
					YUD.addClass("AOHideShowLink", "opened");	
					YUD.removeClass("passwordEntry","hide");
					if(document.getElementById("billingTwister")){
						PAYPAL.Checkout.Billing.twisterButtonLabel("GCchecked");
					}
				}
				if (!this.iGC.checked && YUD.hasClass(div, "opened")){
					YUD.replaceClass(div, "opened", "accessAid");
					YUD.removeClass("AOHideShowLink", "opened");
					if(document.getElementById("billingTwister")){
						PAYPAL.Checkout.Billing.twisterButtonLabel("GCnotchecked");
					}
				}
				break;
			case "WBB":	
				var hDiv = document.getElementById("billingInfo");
				if (div && hDiv) {
					YUD.replaceClass(div, "unavailable", "opened");
					YUD.replaceClass(hDiv, "opened", "unavailable");
				}
				break;
			case "WBC":		
				var hDiv = document.getElementById("contactInfo");
				if (div && hDiv) {
					YUD.replaceClass(div, "unavailable", "opened");
					YUD.replaceClass(hDiv, "opened", "unavailable");
				}
				break;
			}
		},
	
	formatPhoneUS: function(el){
		var div = document.getElementById(el);
		var num = div.value.replace(/[^0-9]/g, "");
		num = (num.length == 10) ? num.substring(0,3)+"-"+num.substring(3,6)+"-"+ num.substring(6,10) : div.value;
		this.iPhoneNum.value = num;
	},
	
	twisterButtonLabel: function(twst){
		switch (twst){
                        case "createPasswordExpanded":
                        case "showPasswordEntryFields":
                        case "GCchecked":
                        YUD.removeClass("billingTwister", "bgtwister");
                        YUD.addClass("saveInfoTwister", "bgtwister");
                        break;
                        
                        case "hidePasswordEntryFields":
                        case "GCnotchecked":
                        YUD.addClass("billingTwister", "bgtwister");
                        YUD.removeClass("saveInfoTwister", "bgtwister");
                        break;
                        
			default:
                        YUD.addClass("billingTwister", "bgtwister");
                        YUD.removeClass("saveInfoTwister", "bgtwister");
                        break;
                }
                var submitBilling;
		if(submitBilling = document.getElementById("submitBilling")){
			if(YUD.hasClass("saveInfoTwister", "bgtwister")){
                        	submitBilling.value = document.getElementById("signUpButtonLabelexpd")?document.getElementById("signUpButtonLabelexpd").value:submitBilling.value;
                	}
	                else{
        	                submitBilling.value = document.getElementById("signUpButtonLabelcol")? document.getElementById("signUpButtonLabelcol").value:submitBilling.value;
                	}
		}
        },
	
	onContentUpdate: function(){
	
		// Load shared methods for Login/Billing
		PAYPAL.Checkout.Shared.init();
		
		YUE.addListener("country_code", "change", function(){
			document.getElementById("r_country_code").value = 1;
			var button = document.getElementById("select_country");
			YUD.addClass(button, 'parentSubmit');
			PCU.newTarget = button;
			button.click();
		});
		this.iPhoneNum = document.getElementById("H_PhoneNumberUS");
		if (this.iPhoneNum){
		YUE.addListener(this.iPhoneNum, 'blur', function(){
				PAYPAL.Checkout.Billing.formatPhoneUS("H_PhoneNumberUS");
			});
		}
		
		// WAX booster
		var lnB = document.getElementById("changeBillingInfo");
		var lnC = document.getElementById("changeContactInfo");
		if (lnB){
			YUE.addListener(lnB, 'click', function(e){
				YUE.preventDefault(e);
				PAYPAL.Checkout.Billing.evtEnableHideShow("billingInfoEntry", "WBB");
			});
		}
		if (lnC){
			YUE.addListener(lnC, 'click', function(e){
				YUE.preventDefault(e);
				PAYPAL.Checkout.Billing.evtEnableHideShow("contactInfoEntry", "WBC");
			});
		}
		
		// AO twister related functions
		this.iGC = document.getElementById("gift-certificate");
		if (this.iGC){
			if (!this.iGC.checked && !YUD.hasClass("passwordEntry", "accessAid")){
				YUD.addClass("passwordEntry", "accessAid");
			}
			YUE.addListener(this.iGC, 'click', function(){
				PAYPAL.Checkout.Billing.evtEnableHideShow("passwordEntry", "GC");
			});
		}

		// Initialize CC elements
		YUE.onContentReady("cc_number", PAYPAL.CreditCardInput.init, PAYPAL.CreditCardInput, true);
		YUE.addListener(YUD.getElementsByClassName('mc_subtype'), 'click', function(){
			document.getElementById('cc_brand').value = this.id;
			
		});
		var l = document.getElementsByTagName("input");
		for (var c = 0; c < l.length; c++) {
			if (l.item(c).type == "radio" && l.item(c).name == "flag_show_ppc_signup_selected") {
				YUE.addListener(l.item(c), 'click', function(){ PAYPAL.Checkout.Billing.evtCreditMethodSelected(); });
			}
		}

		YUE.onContentReady("paymentMethods", PAYPAL.Checkout.ChoosePaymentMethod.init, PAYPAL.Checkout.ChoosePaymentMethod, true);
		YUE.onContentReady("usZip", function(){
			PAYPAL.widget.Address.init(this);
	});

	YUE.onContentReady("javascript_enabled", function(){
		document.getElementById("javascript_enabled").value="true";
	});
		
		//Initialize AO twister
		if (document.getElementById("createAccount")) {
			var toggleTrigger = document.getElementById("AOHideShowLink");
			var passwordEntryHideShow = new PAYPAL.widget.HideShow("passwordEntry", toggleTrigger);
			passwordEntryHideShow.onShow.subscribe(this.showPasswordEntryFields);
			passwordEntryHideShow.onHide.subscribe(this.hidePasswordEntryFields);			
			if(document.getElementById("createPasswordExpanded").value == "opened") {
                                PAYPAL.Checkout.Billing.twisterButtonLabel("createPasswordExpanded");
                        }
		}
		
		/* AN HR Street Types */
		/* for select data sources if you don't pass an input one will be created and assigned the same name as the select element */
		var myData = function (str, callback) {
			 var results = {};
			 var searchPattern = new RegExp('(^('+ escape(str) +')(.*))|((.*)\\((?='+ escape(str) +')(.*))$','i');
			 for(var key in streetTypes) {
					 if(key.match(searchPattern)) {
							 results[key] = streetTypes[key];
					 }
			  }
			 callback(results);
		}

		if(YUD.get('billing_streettype')){                            
			var autocomplete1 = new PAYPAL.widget.AutoComplete('billing_streettype', myData, {minLength: 1, maxDisplayed: 4, selectFirst: true, highlight: true, showSubmitValue: true});        
		}
		/* AN HR Street Types */
		
		/*s.scTrackPage(function() {
			s.pageName = document.getElementById('scPageName').value;	   
		});*/
	}
};
PAYPAL.Checkout.Billing.init();


