var DEBUG_MODE = true;
/*for PPSCR01247748 to avoid scrollToTop fuction*/
var avoidscrollToTop;
/* disable continue button first, then enable after scrolling down to iframe bottom */
var bmlToSViewed = false, bmlToSPollInterval,dps;
function attachScrollListener(iframeObj) {
	var i = YUD.getElementsByClassName("primary", "input", "bml_signup_lightbox")[0], f = iframeObj.contentWindow;
	if (document.getElementById("CreditApplicationWrapper")) {
		i = YUD.getElementsByClassName("primary", "input", "CreditApplicationWrapper")[0];
		if (document.getElementById("dob_month").disabled == false) {
			document.getElementById("dob_month").focus();
		}
	}
	if (i && f) {
		dps = document.getElementById("bmlUX");
		if(dps){
			document.getElementById("tandc_reviewed").value = false;
		}
		var b = YUE.addListener(f, "scroll", function() {
				var docBody = f.document.body;
				var doc = f.document.documentElement;
				var x = doc.scrollTop || docBody.scrollTop;
                                         
				if ((doc.scrollHeight - doc.clientHeight - x) <= 1) {
					bmlToSViewed = true;
					if(dps){
						document.getElementById("tandc_reviewed").value = true;
					}
					else{
						i.disabled = false;
						YUD.removeClass(i, "disabled");
					}
				}
			});
			if (b && !(dps)) {
				i.disabled = true;
				YUD.addClass(i, "disabled");
			}
	}
}
function bmlToSView() {
	var myFrame = YUD.getElementsByClassName("bmlscrolltandc")[0];
	if(myFrame) {
		attachScrollListener(myFrame);
	}
}


/**
* @property	An HTML element that was the selected element to begin a subflow action. This is used to return focus to the original location for accessibility purposes
* @type (HTMLElement)
*/
var previousSelectedElement = null;

/**
 * Main JS for 1X Checkout flow
 */
PAYPAL.namespace("Checkout");

/**
 * Global shortcut variables
 */
var YUD = YAHOO.util.Dom;
var YUE = YAHOO.util.Event;
var xhr = new PAYPAL.Checkout.Xhr();
var isIE6 = /msie|MSIE 6/.test(navigator.userAgent);
var listenersLoaded = false;
var bmlDone = false;
var ANIMATION_DURATION = 0; //The approximate duration of the slide-out animation (in seconds)

YAHOO.widget.Module.CSS_HEADER = "header";
YAHOO.widget.Module.CSS_BODY = "body";
YAHOO.widget.Module.CSS_FOOTER = "footer";

// v Code added for error event logging - requires Xhr 2.x
var regex_url = /https:\/\/.{1,}paypal.com\/([^\/]*)\/.{1,}/;
var countryCode = regex_url.exec(document.URL)[1];
switch (countryCode.toLowerCase()) {
	case "au":
	case "cn":
	case "c2":
		xhr.timeout = 14;
		break;
	case "de":
		xhr.timeout = 12;
		break;
	case "uk":
		xhr.timeout = 10;
		break;
	default:
		xhr.timeout = 8;
		break;
}
// ^ Code added for error event logging

/**
 * Checkout-specific utility functions
 */
PAYPAL.Checkout.Util = {
	/**
	* @property
	* @type	{}
	*/
	lastHeight: null,

	/**
	* @property
	* @type	{}
	*/
	newTarget: false,

	/**
	* @method	Turns strings into elements and can take a "default value" for the potentially missing element
	* @returns	{node}
	* @argument	{string|node} el
	* @argument	{string|node} def
	*/
	resolveElement: function(el, def) {
		el = (el === undefined && def !== undefined) ? def : el;
		return (typeof el == "string") ? document.getElementById(el) : el;
	},

	/**
	* @method
	* @returns	{boolean}
	* @argument	{variant} needle
	* @argument	{variant[]} haystack
	*/
	inArray: function(needle, haystack) {
		return (haystack.toString().indexOf(needle) !== -1);
	},

	/**
	* @method	Resize MiniCart height based on main content height and remove scrolling from the item list as appropriate
	* @returns	{void}
	* @argument	{boolean} force
	*/
	resizeMiniCart: function(force) {
		force = (force === undefined || force === false) ? false : true;

		var p = YUD.getRegion(YUD.getElementsByClassName("col last")[0]);
		var c = document.getElementById("miniCartContent");
		if ((p && c) && (force || !PAYPAL.Checkout.Util.lastHeight || PAYPAL.Checkout.Util.lastHeight != p.height)) {
			var h = Math.max(parseInt(YUD.getStyle(c, "min-height"), 10), parseInt(p.height - 43, 10));
			var anim = new YAHOO.util.Anim(c, {height: { to:h }}, 0.25, YAHOO.util.Easing.easeIn);
			if(avoidscrollToTop){
				avoidscrollToTop = null;
				}
				else{
				PCU.scrollToTop();
				}
			anim.animate();
		}
		PAYPAL.Checkout.Util.lastHeight = p.height;
	},

	/**
	* @method	Method for scrolling to the top of the page, which occurs between page transitions and form errors.
	* @returns	{void}
	*/
	scrollToTop: function() {
		window.scrollTo(0, 0);
	},

	/**
	* @method
	* @returns	{void}
	*/
	highlightElements: function(el) {
		if (YUD.hasClass(el, "cartUpdated")) {
			var c = YUD.getElementsByClassName("highlight", null, el);
			var l = c.length, i;
			for (i=0; i < l; i++) {
				PAYPAL.widget.AnimatedHighlight(c[i]);
			}
		}
	},

	/**
	* @method
	* @returns	{void}
	*/
	isCartUpdated: function() {
		PAYPAL.Checkout.Util.highlightElements("miniCart");
		PAYPAL.Checkout.Util.highlightElements("main");
	},

	/**
	* @method
	* @returns	{void}
	* @argument	{} el
	*/
	highlightInputs: function(el) {
		YUD.addClass(this, "focused");
	},

	/**
	* @method
	* @returns	{void}
	* @argument	{} el
	*/
	blurInputs: function(el) {
		YUD.removeClass(this, "focused");
	},

	/**
	* @method
	* @returns	{void}
	* @argument	{} el
	* @argument	{} nspace
	*/
	noScriptAid: function(el, nspace) {
		if (nspace == "on") {
			YUD.addClass(el, "hide");
		} else {
			YUD.removeClass(el, "accessAid");
			YUD.removeClass(el, "hide");
		}
	},

	/**
	* @method
	* @returns	{void}
	*/
	hideAllNoScript: function() {
		PAYPAL.Checkout.Util.noScriptAid(YUD.getElementsByClassName("noScript"), "on");
	},

	/**
	* @method
	* @returns	{void}
	* @argument	{string|node} el	Element to be counted
	* @argument	{string|node} target	id of element to be updated with the count
	* @argument	{int} maxChars	Maximum number of characters allowed in field
	*/
	charCounter: function(el, target, maxChars) {
		el = (typeof(el) == "string") ? document.getElementById(el) : el;
		target = (typeof(target) == "string") ? document.getElementById(target) : target;
		
		if (el.value.length > maxChars) {
			el.value = el.value.substr(0, maxChars);
		}
		if (target.value) {
			target.value = maxChars - el.value.length;
		} else {
			target.innerHTML = maxChars - el.value.length;
		}
	},

	/**
	* @method	Gets the parent form
	* @returns	{node}
	*/
	getEntryForm: function() {
		return document.getElementById("parentForm");
	},

	/**
	* @method	buttonToLink should be deprecated after all button links are refactored to use <c:ButtonLink> instead of <c:FormFieldSumit>
	* @returns	{node|boolean}
	* @argument	{string|node} el
	*/
	buttonToLink: function(el) {
		el = (typeof(el) == "string") ? document.getElementById(el) : el;
		if (!el) { return false; }
		if (!YUD.get(el.name)) {
			var a = document.createElement("a");
			a.setAttribute("href", "#" + el.name);
			a.setAttribute("id", el.name);
			a.appendChild(document.createTextNode(el.value));
			el.parentNode.insertBefore(a, el);
			return a;
		}
	},

	/**
	* @method	Add click event to form submit button, overriding any previously clicked AllPurposeTextSubmit which may have redirected the form to a popup
	* @returns	{void}
	*/
	unPopupForm: function(e) {
		var frm = YUE.getTarget(e).form, txt = document.getElementById("myAllTextSubmitID");
		if (!frm || !txt) { return; }
		frm.target = "";
		txt.name = "myAllTextSubmitID";
		txt.value = "";
	},

	/**
	* @method	Gets the submit buttons on the form
	* @returns	{node[]}
	*/
	getSubmitButton: function(oForm) {
		var s = [];
		if (oForm && oForm.elements) {
			for (i = 0; i < oForm.elements.length; i++) {
				if (oForm.elements[i].type && oForm.elements[i].type == "submit") {
					s.push(oForm.elements[i]);
				}
			}
		}
		return s;
	},
	
	ToolTipHandler: function(autoToolTip,container){
		var autoToolTip = YUD.getElementsByClassName(autoToolTip, "a", container);
		if (autoToolTip) {
			for(var i=0; i<autoToolTip.length; i++) {
				YUE.addListener(autoToolTip[i], "click", function(e){YUE.preventDefault(e);});
			}
		}
	}	
};
var PCU = PAYPAL.Checkout.Util;

/**
 * Create object for Shipping Calculator widget
 */
PAYPAL.widget.ShippingCalculator = {
	/**
	* @property
	* @type	{string}
	*/
	prevZipValue: "",

	/**
	* @method
	* @returns	{void}
	*/
	init: function() {
		var cl = YUD.getElementsByClassName("cancelWidget");
		if (document.getElementById("shippingHandling")) {
			YUD.removeClass(cl, "accessAid");
			YUE.addListener(cl, "click", PAYPAL.widget.ShippingCalculator.toggleWidget);
		}
		YUE.addListener("flipToIntlShipping", "click", PAYPAL.widget.ShippingCalculator.flipShipTo);
		YUE.addListener("flipToUSShipping", "click", PAYPAL.widget.ShippingCalculator.flipShipTo);
		YUE.addListener("shipping_zip", "focus", PAYPAL.widget.ShippingCalculator.focusZip);
		YUE.addListener("shipping_zip", "blur", PAYPAL.widget.ShippingCalculator.blurZip);
		YUE.onAvailable("toggleWidget", function() { YUE.removeListener("toggleWidget", "click", PAYPAL.widget.ShippingCalculator.toggleWidget); YUE.addListener("toggleWidget", "click", PAYPAL.widget.ShippingCalculator.toggleWidget); });
		this.initAutoCompleteCountry();
	},

	/**
	* @method	Toggles the ship to international/us block
	* @returns	{void}
	*/
	flipShipTo: function(e) {
		YUE.preventDefault(e);
		var intl = document.getElementById("shippingIntlRow");
		var us = document.getElementById("shippingZipRow");
		if (YUD.hasClass(us, "accessAid")) {
			YUD.addClass(intl, "accessAid");
			YUD.removeClass(us, "accessAid");
		} else if (YUD.hasClass(intl, "accessAid")) {
			YUD.addClass(us, "accessAid");
			YUD.removeClass(intl, "accessAid");
		}
	},

	/**
	* @method	Event handler for the onfocus event
	* @returns	{void}
	*/
	focusZip: function(e) {
		PCU.newTarget = document.getElementById("calcZipButton");
		if (YUD.hasClass(this, "hintText")) {
			PAYPAL.widget.ShippingCalculator.prevZipValue = this.value;
			this.value = "";
			YUD.removeClass(this, "hintText");
		}
	},

	/**
	* @method	Event handler for the onblur event
	* @returns	{void}
	*/
	blurZip: function(e) {
		PCU.newTarget = false;
		if (this.value === "" && PAYPAL.widget.ShippingCalculator.prevZipValue !== "") {
			this.value = PAYPAL.widget.ShippingCalculator.prevZipValue;
			YUD.addClass(this, "hintText");
		}
	},

	/**
	* @method	Toggles the widget to show/hide
	* @returns	{void}
	*/
	toggleWidget: function(e) {
		YUE.preventDefault(e);
		var widget = document.getElementById("shippingWidget");
		var link = document.getElementById("toggleWidget");
		if (YUD.hasClass(widget, "accessAid")) {
			YUD.removeClass(widget, "accessAid");
		} else {
			YUD.addClass(widget, "accessAid");
		}
		if (YUD.hasClass(link, "accessAid")) {
			YUD.removeClass(link, "accessAid");
		} else {
			YUD.addClass(link, "accessAid");
		}
	},

	/**
	* @method	Initializes the auto-complete "ship to" country
	* @returns	{void}
	*/
	initAutoCompleteCountry: function() {
		var cl = document.getElementById("exp_ship_to_country");
		if (cl) {
			var ac = new PAYPAL.widget.AutoComplete(null, cl, {maxResults:false, selectFirst:true, edge:"left", delay:0.01});
			var toggle = document.createElement("span");
			YUD.addClass(toggle, "acToggle");
			YUD.insertAfter(toggle, ac.input);
			YUE.on(toggle, "click", function(e) { ac.input.value="";YUE.stopEvent(e); ac.input.focus(); });
			var hintText = document.getElementById("ac_hint_text");
			YUD.addClass(ac.input, "hintText");
			ac.input.value = hintText.value;
			YUE.on(ac.input, "focus", function() { YUD.removeClass(this, "hintText"); if (this.value == hintText.value) { this.value = ""; } });
			YUE.on(ac.input, "blur", function() { if (this.value.length === 0) { YUD.addClass(this, "hintText"); this.value = hintText.value;} });
		}
	}
};

/**
 * Create object for PP Cart
 */
PAYPAL.Checkout.Cart = {
	/**
	* @method	Initializes the cart
	* @returns	{void}
	*/
	init: function() {
		PAYPAL.Checkout.Util.isCartUpdated();
		var cartTable = document.getElementById("cartTable");
		var highlightElements = YUD.getElementsByClassName("quantity", "input", cartTable);
		highlightElements.push(document.getElementById("shipping_zip"));
		YUE.addListener(highlightElements, "focus", PAYPAL.Checkout.Util.highlightInputs);
		YUE.addListener(highlightElements, "blur", PAYPAL.Checkout.Util.blurInputs);
		PAYPAL.widget.ShippingCalculator.init();
	},

	/**
	* @method	Changes the window location so the user can continue shopping
	* @returns	{void}
	* @argument	{string} shoppingURL	The url to use for shopping
	* @argument	{string} noWinAlert	Shown when there is no shopping url and we can't tell if the parent is closed
	* @argument	{string} closeWinAlert	Shown when there is no shopping url and the parent is closed
	*/
	continueShopping: function(shoppingURL, noWinAlert, closeWinAlert) {
		var p = window.opener;
		if (p && !p.closed)	{
			if (shoppingURL && shoppingURL != "") {
				try { // this is needed for same-origin security - some merchant use iframes with third party urls
					p.location.href = shoppingURL; // load the shopping url in the parent
				} catch (e) {
					// nothing to do here - just didn't want to refresh with the shopping URL
				}
			}
			window.close();
		} else if (shoppingURL && shoppingURL != "") {
			window.location.href = shoppingURL;
		} else {
			alert(p && p.closed ? closeWinAlert : noWinAlert);
		}
		return false;
	}
};

/**
* Create object for Payment Method features
*/
PAYPAL.Checkout.PaymentMethod = function(header, details) {
	this._create(header, details);
};

PAYPAL.Checkout.PaymentMethod.prototype = {
	/**
	* @method	Expands the detail block
	* @returns	{void}
	*/
	expandDetails: function() {
		YUD.addClass(this._h, "selected");
		YUD.replaceClass(this._d, "accessAid", "opened");
	},

	/**
	* @method	Collapses the detail block
	* @returns	{void}
	*/
	hideDetails: function() {
		YUD.removeClass(this._h, "selected");
		YUD.replaceClass(this._d, "opened", "accessAid");
	},

	/**
	* @method	Toggles the detail block
	* @returns	{void}
	*/
	toggleDetails: function() {
		if (this._r.checked === true) {
			this.expandDetails();
		} else {
			this.hideDetails();
		}
	},

	/**
	* @event
	*/
	clicked: null,

	/**
	* @private
	* @property	The node containing the option header
	* @type	{node}
	*/
	_h: null,

	/**
	* @private
	* @property	The node containing the option details
	* @type	{node}
	*/
	_d: null,

	/**
	* @private
	* @property	The node containing the option radio button
	* @type	{node}
	*/
	_r: null,

	/**
	* @private
	* @property	The node containing the option title
	* @type	{node}
	*/
	_t: null,

	/**
	* @private
	* @property	The node containing the option image
	* @type	{node}
	*/
	_i: null,

	/**
	* @private
	* @method	Initializes the header and detail block-level elements as well as the title element, radio button input, and the image associated with the option (if any)
	* @returns	{void}
	* @argument	{node} header
	* @argument	{node} detail
	*/
	_create: function(header, detail) {
		this._h = header;
		this._d = detail;
		this._r = this._h.getElementsByTagName("input").item(0);
		this._t = this._h.getElementsByTagName("h3").item(0);
		this._i = this._t ? this._t.getElementsByTagName("img").item(0) : null;
		var id = this._h.id != "" ? this._h.id : this._d.id != "" ? this._d.id : Math.random().toString().replace(/0\./,"");
		this.clicked = new YAHOO.util.CustomEvent("payment_" + id + "_clicked");
		YUE.addListener(this._r, "click", this.clicked.fire, this, true);
		YUE.addListener(this._t, "click", this._selected, this, true);
		if (this._i) {
			YUE.addListener(this._i, "click", this._selected, this, true);
		}
	},

	/**
	* @private
	* @method	Selects the method
	* @returns	{void}
	*/
	_selected: function() {
		this._r.checked = true;
		this.clicked.fire();
	}
};

/**
* Create object for Payment Method options
*/
PAYPAL.Checkout.ChoosePaymentMethod = {
	/**
	* @property
	* @type	{}
	*/
	paymentMethodArray: [],

	/**
	* @property
	* @type	{}
	*/
	subMethodArray: [],

	/**
	* @method	Initializes the payment methods
	* @returns	{void}
	*/
	init: function(e) {
		var i, paymentMethods = YUD.getElementsByClassName("sectionHead", "div", "paymentMethods"), paymentDetails = YUD.getElementsByClassName("sectionDetails", "div", "paymentMethods");
		var l = paymentMethods.length;
		for (i = 0; i < l; i++) {
			this.paymentMethodArray[i] = new PAYPAL.Checkout.PaymentMethod(paymentMethods[i], paymentDetails[i], i);
			this.paymentMethodArray[i].clicked.subscribe(this.showSelected, this, true);
		}
		var subMethods = YUD.getElementsByClassName("subOptions", "li", "paymentMethods"), subDetails = YUD.getElementsByClassName("subOptionDescription", "div", "paymentMethods");
		var l = subMethods.length;
		for (i = 0; i < l; i++) {
			this.subMethodArray[i] = new PAYPAL.Checkout.PaymentMethod(subMethods[i], subDetails[i]);
			this.subMethodArray[i].clicked.subscribe(this.showSelected, this, true);
		}
		this.showSelected();
		return this;
	},

	/**
	* @method	Toggles the payment method details to show the selected item
	* @returns	{void}
	*/
	showSelected: function() {
		var paymentMethodArrayLen = this.paymentMethodArray.length;
		for (var i=0; i < paymentMethodArrayLen; i++) {
			this.paymentMethodArray[i].toggleDetails();
		}
		var subMethodArrayLen = this.subMethodArray.length;
		for (i=0; i < subMethodArrayLen; i++) {
			this.subMethodArray[i].toggleDetails();
		}
	}
};

/**
 * Create object for shared functions
 */
PAYPAL.Checkout.Shared = {
	/**
	* @method	
	* @returns	{void}
	*/
	init: function(e) {
		if (document.getElementById("pageDispatch")) {
			formActionHandler("parentForm", "pageDispatch");
		}
		function formActionHandler(formId, pageDispatchId) {
			var formAction = document.getElementById(formId);
			var currentPageServerName =  document.getElementById("pageServerName") ? document.getElementById("pageServerName").value : "merchantpaymentweb";
			formAction.action = formAction.action.replace(/(.*\/cgi-bin\/).*/, "$1" + currentPageServerName + "?dispatch=" + document.getElementById(pageDispatchId).value);
		};

		PAYPAL.Checkout.Util.isCartUpdated();
		PAYPAL.Checkout.Util.noScriptAid(YUD.getElementsByClassName("noScript"), "on");
		PAYPAL.Checkout.Util.noScriptAid(YUD.getElementsByClassName("showScript"), "off");
		var paymentOptions = YUD.getElementsByClassName("panel", "div");
		YUE.addListener(paymentOptions, "mouseover", function() { if (!YUD.hasClass(this, "active")) { YUD.addClass(this, "hover"); } });
		YUE.addListener(paymentOptions, "mouseout", function() { if (!YUD.hasClass(this, "active")) { YUD.removeClass(this, "hover"); } });
		if (!listenersLoaded) {	
			YUE.addListener("parentForm", "keypress", slider.enterKeyHandler);
			YUE.addListener("parentForm", "submit", function (e) { slider.submitHandler(e, this); });
			listenersLoaded = true;
		}
		YUE.addListener(document.getElementsByTagName("input"), "click", function (e) {PCU.explicitOrigTarget = YUE.getTarget(e);});
		setTimeout(PCU.resizeMiniCart, 100);
		YUE.addListener(document.getElementsByTagName("a"), "click", function(e) { avoidscrollToTop = true; setTimeout(PCU.resizeMiniCart, 50); });
		YUE.addListener("gift-certificate", "click", function(e) { avoidscrollToTop = true; setTimeout(PCU.resizeMiniCart, 50);});
		YUE.onAvailable("item_name", function() {
			YUE.addListener("item_name", "click", function() {
				if (!YUD.hasClass(this, "clicked")) {
					this.value = "";
					YUD.addClass(this, "clicked");
				}
			});
		});
		function historyStateChangeHandler(state) {
/* For some reason the first time the back button is pressed the input elements in the form 
 for history Session are lost. Need to add these input form fields manually if they are chopped off for 
 the back button functionality to work as expected. 
*/
			if(!document.getElementById("historySession") || !document.getElementById("historySubmit")) {
				var frm = document.getElementById("historyForm");
				var historySession_input = document.createElement('INPUT');
				historySession_input.type = 'hidden';
				historySession_input.name = 'SESSION';
				historySession_input.id = 'historySession';
				frm.appendChild(historySession_input);
				var historySubmit_input = document.createElement('INPUT');
				historySubmit_input.type = 'submit';
				historySubmit_input.className = "hide parentSubmit";
				historySubmit_input.name = 'historySubmit';
				historySubmit_input.id = 'historySubmit';
				frm.appendChild(historySubmit_input);
			}
			var pageState = YAHOO.util.History.getCurrentState("pageState");
			var thisSession = YAHOO.util.History.getCurrentState("pageSession");
			var thisDispatch = YAHOO.util.History.getCurrentState("pageDispatch");
			var bookmarkedSession = true;
			var bookmarkedDispatch = true;
			if (YAHOO.env.ua.ie >= 8 && !YAHOO.util.History.getBookmarkedState("pageSession")) {
				bookmarkedSession = false;
				var queryString = window.location.search;
				var sessionStart = queryString.indexOf("SESSION=") + 8;
				var sessionEnd = queryString.indexOf("&dispatch=");
				document.getElementById("historySession").value = queryString.slice(sessionStart, sessionEnd);
			}
			if (pageState != document.getElementById("pageState").value || !bookmarkedSession) {
				if (!document.getElementById("babySlider").style.marginLeft.match(/-/)) {
					slider.slideOut("babySlider");
				}
				if (bookmarkedSession) {
					thisSession = YAHOO.util.History.getCurrentState("pageSession");
					document.getElementById("historySession").value=thisSession;
				}
				if (bookmarkedDispatch) {                                        
					thisDispatch = YAHOO.util.History.getCurrentState("pageDispatch");
					document.getElementById("historyDispatch").value=thisDispatch;
				}
				YUE.addListener("historyForm", "submit", function (e) { slider.submitHandler(e, this); });
				if(document.getElementById("loginBackButton") && pageState == "billing" && document.getElementById("pageState").value == "login"){
					return;
				}
				document.getElementById("historySubmit").click();
			}
		}
		var bookmarkedstate = YAHOO.util.History.getBookmarkedState("pageState");
		var currentState = document.getElementById("pageState").value;
		var initialstate = bookmarkedstate || currentState;
		var bookmarkedSession = YAHOO.util.History.getBookmarkedState("pageSession");
		var currentSession = document.getElementById("currentSession").value;
		var initialSession = bookmarkedSession || currentSession;
		var bookmarkedDispatch = YAHOO.util.History.getBookmarkedState("pageDispatch");		
		var currentDispatch = document.getElementById("pageDispatch").value;
		var initialDispatch = bookmarkedDispatch || currentDispatch;
		YAHOO.util.History.register("pageState", initialstate, function() {});
		YAHOO.util.History.register("pageDispatch", initialDispatch, function() {});
		YAHOO.util.History.register("pageSession", initialSession, historyStateChangeHandler);
		YAHOO.util.History.onReady(function() { 
			setTimeout(function(){
				var readyState = YAHOO.util.History.getCurrentState("pageState"); 
				if (readyState != document.getElementById("pageState").value) { 
					var readySession = YAHOO.util.History.getCurrentState("pageSession"); 
					historyStateChangeHandler(readySession); 
				}
			}, 900); 
		});
		YAHOO.util.History.initialize("yui-history-field", "yui-history-iframe");
		YUD.getElementsBy( function (el) {
				return (YUD.hasClass(el, "panel") && !YUD.hasClass(el, "active")) ? true : false;
			}, "div", "sliderWrapper", function (el) {
				var button = YUD.getElementsByClassName("parentSubmit", "input", el)[0];
				if (button) {
					el.style.cursor = "pointer";
					YUE.addListener(el, "click", function(e) {
						var target = (e.explicitOriginalTarget) ? e.explicitOriginalTarget : document.activeElement;
						if (target === document.body) { //hack for WebKit
							target = PCU.explicitOrigTarget;
						}
						if (target != button) {
							if(slider.isRunning === false){
								PCU.newTarget = button;
							}
							button.click();
						}
					});
				}
		});
		if (document.getElementById("scPageName")) {
			s.pageName = document.getElementById("scPageName").value;
			var fm = document.getElementById("parentForm"), el, j;
			for (j = 0; j < fm.elements.length; j++) {
				el = fm.elements[j];
				var t = el.type ? el.type.toUpperCase() : null;
				if (t) {
					var md = el.onmousedown, kd = el.onkeydown, fn = /\.fam\(/;
					var omd = md ? md.toString() : "", okd = kd ? kd.toString() : "";
					if (!fn.test(omd) && !fn.test(okd)) {
						el.s_famd = md;
						el.s_fakd = kd;
						el.onmousedown = s.fam;
						el.onkeydown = s.fam;
					}
				}
			}
		}
		PCU.ToolTipHandler("autoTooltip","miniCart");
		PCU.ToolTipHandler("balloonControl","passwordEntry");
		PAYPAL.Checkout.expCollapse.init();
		PAYPAL.Checkout.ElementBinder.finopsectionlinktoggle();
	}
};

PAYPAL.Checkout.Slider = {
	/**
	* @property	These templates are defined as parents. When the minipage response includes one of these as the template name, the code treats it as a "full-page" reload
	* @type	{string[]}
	*/
	parentTemplates: ["Review", "Billing", "Login", "Done", "PaymentOptions", "ManageCC", "OpenWax", "FinancialOptions", "ConfirmCCInfo", "EditBilling" ,"CreditApplication"],

	/**
	* @property	A flag to prevent multiple submits by the user
	* @type	{boolean}
	*/
	isRunning: false,

	/**
	* @property	A flag to indicate when display is right-to-left
	* @type	{boolean}
	*/
	isRTL: false,
	sliderContentReady: new YAHOO.util.CustomEvent('sliderContentReady'),
	doneRunning: new YAHOO.util.CustomEvent('doneRunning'),

	/**
	* @method	Returns true if BML is chosen
	* @returns	{void}
	*/
	bmlChosen: function(form) {
		var c, e;
		for (c = 0; c < form.elements.length; c++) {
			e = form.elements[c];
			if ((!document.getElementById("bmlUX")) && (/fundingsource|flag_show_ppc_signup_selected|creditOffer_form/).test(e.name) && (/BML/).test(e.value) && (e.checked || (/hidden/i).test(e.type))) {
				return true;
			}
		}
		return false;
	},

	/**
	* @method   Handles errors thrown by the Xhr object, allowing us to update tracking info
	* @returns  {void}
	* @argument {object} opt
	* @argument {integer} http
	*/
	handleError: function(opt, http) {
		var p = "parentSlider";
		slider.showPanelMask(false);
		if (opt.isBaby) {
			p = "babySlider";
			slider.slideOut(p);
		}
		if (!opt.noResize) {
			PCU.resizeMiniCart();
		}
		slider.toggleInputs(p, false);
		slider.slideIn(p);
		slider.clearMiniPagers();
		slider.isRunning = false;
		PCU.scrollToTop();
		if (s && s.scTrackPage) {
			s.scTrackPage(function() { s.pageType = http; });
		}
	},

	/**
	* @method	Event handler for the enter key
	* @returns	{void}
	*/
	enterKeyHandler: function(e) {
		var k = e.charCode || e.keyCode;
		if (k == 13) {
			var t = e.target || e.srcElement;	
			var ac = YUD.getElementsByClassName("acResults", "div");
			for(var i=0; i<ac.length; i++) {
				if(t.id == "billing_streettype" && !YUD.hasClass(ac[i], "accessAid")) return;			
			}
			if ((/INPUT/i).test(t.nodeName)) {			
				YUE.stopEvent(e);
				if ((/SUBMIT|IMAGE/i).test(t.type)) {
					slider.submitHandler(e, this, t);
				} else {
					slider.submitHandler(e, this, slider.getDefaultButton(this));
				}
			}
		}
	},

	/**
	* @method	Event handler for form submit
	* @returns	{void}
	*/
	submitHandler: function(e, form, button) {
		var itemName = document.getElementById("item_name");
		if (itemName != null && !YUD.hasClass(itemName, "clicked")) {
			itemName.value = "";
		}
		var isBml = this.bmlChosen(form);
		var et = button ? button : e.explicitOriginalTarget ? e.explicitOriginalTarget : document.activeElement;
		if (et === document.body) { //hack for WebKit
			et = PCU.explicitOrigTarget;
		}

		if (isBml && !bmlDone && ((et.type == "submit" && YUD.hasClass(et, "primary")) || et.nodeName.toLowerCase() == "body")) {
			YUE.preventDefault(e);
			var bml = new PAYPAL.Checkout.Subflow("bml_signup_lightbox");
			bml.shown.subscribe(function() { if (!bmlToSViewed && document.getElementById("termsagreechk")) { window.bmlToSView(); }});
			bml.completed.subscribe(function() { bmlDone = true; }, this, true);
			bml.ended.subscribe(function() { slider.showPanelMask(false); }, this, true);
			bml.start(form, new Array({name:"YesBMLLightbox", value:"Continue", type:"hidden"}, et));
			slider.slideOut("babySlider");
			slider.showPanelMask(false);
			return false;
		}
		if (slider.isRunning === false) {
			slider.isRunning = true;
			// PPSCR01368035 fix
			slider.showPanelMask(true);
			// End PPSCR01368035 fix
			var form = (typeof form != undefined) ? form : YUE.getTarget(e);
			if (!button) {
				var el = (e.explicitOriginalTarget) ? e.explicitOriginalTarget : document.activeElement;
				if (el === document.body) { //hack for WebKit
					el = PCU.explicitOrigTarget;
				}
			} else {
				var el = button;
			}
			el = (PCU.newTarget) ? PCU.newTarget : el;
			PCU.newTarget = false;
			var history = (form.getAttribute("id") == "historyForm");
			if ((!(/INPUT/i).test(el.nodeName) && !history) || ((/INPUT/i).test(el.nodeName) && !(/SUBMIT|IMAGE/i).test(el.type))) {
				return false;
			}
			opt = { "noMask": false, "noAnimation": false, "enableInputs": false, "isBaby": false, "noResize": false };
			if (YUD.hasClass(el, "noMask")) {
				opt.noMask = true;
			}
			if (YUD.hasClass(el, "noAnimation")) {
				opt.noAnimation = true;
			}
			if ( form.getAttribute('id') == 'parentForm' ) {
				previousSelectedElement = el;	
			}
			if (YUD.hasClass(el, "parentSubmit") || history) { // Submit page to reload the parent slider
				YUE.preventDefault(e);
				slider.init(el, form, opt);
			} else if (YUD.hasClass(el, "babyOpen") || (form.getAttribute("id") == "babySliderForm" && (!YUD.hasClass(el, "fullPageSubmit")))) { // Submit page to produce a baby slider from a button on the parent page
				YUE.preventDefault(e);
				opt.isBaby = true;
				slider.init(el, form, opt);
			} else if (YUD.hasClass(el, "updateSubmit")) { // Don't slide anything, just do minipage submit
				YUE.preventDefault(e);
				opt.noAnimation = true;
				opt.noResize = true;
				slider.init(el, form, opt);
			} else if (YUD.hasClass(el, "noSubmit")) { // Prevent a submit if this class is applied
				YUE.preventDefault(e);
				return false;
			} else { // Normal submit. Do not preventDefault. Let it nuke the page
				return true;
			}
		} else if(typeof ShippingHandling != undefined && ShippingHandling.objSubflow && ShippingHandling.objSubflow.getState() == PAYPAL.Checkout.Xhr.state.waiting) {
			YUE.preventDefault(e);
		}
	},

	/**
	* @method	This function is use to check if it is RTL, sometimes may need to be explicitly called. It is done already in the init function. Returns true if the page has class "rtl" in the "body" tag, else false
	* @returns	{boolean}
	*/
	checkIsRTL: function() {
		return (this.isRTL = YUD.hasClass(document.body, "rtl"));
	},

	/**
	* @method	Initializes using the provided options object. Object may have the following properties: noAnimation: @type {boolean}, @default: false, @description: When true, disables all slider animating; enableInputs: @type {boolean}, @default: false, @description: When true, does not disable all input during slider interaction; isBaby: @type {boolean}, @default: false, @description: When true, Slider treats the interaction as a baby slider
	* @returns	{void}
	*/
	init: function(button, form, opt) {
		form = PCU.resolveElement(form, "parentForm");
		button = PCU.resolveElement(button);
		this.checkIsRTL();
		var panelId = (!opt.isBaby) ? "parentSlider" : "babySlider";
		if (!opt.enableInputs && form.id != "historyForm") {
			slider.toggleInputs(form, true);
		}
		if (!opt.noMask) {
			slider.showPanelMask(true);
			var panels = YUD.getElementsByClassName("panel");
			YUE.removeListener(panels, "click");
			YUE.removeListener(panels, "mouseover");
			YUE.removeListener(panels, "mouseout");
		}
		slider.setMiniPager(button);
		xhr.abort.unsubscribeAll(); // If not using Xhr 2.x use error event
		xhr.abort.subscribe( function() { // If not using Xhr 2.x use error event
				slider.showPanelMask(false);
				if (opt.isBaby) {
					slider.slideOut("babySlider");
					if (DEBUG_MODE && xhr.getResponse().type == "abort") {
						var a = new PAYPAL.util.Lightbox("babySliderAbort");
						var d = document.createElement("div");
						d.innerHTML = xhr.getResponse().html;
						var l = d.getElementsByTagName("div");
						for (var c = 0; c < l.length; c++) {
							if (l.item(c).className == "header") {
								a.setHeader(l.item(c).innerHTML);
							} else if (l.item(c).className == "body") {
								a.setBody(l.item(c).innerHTML);
							}
						}
						a.show();
					}
				} else if (DEBUG_MODE && xhr.getResponse().type == "abort") {
					var m = document.getElementById("parentSlider");
					if (m) {
						while (m.firstChild) {
							m.removeChild(m.firstChild);
						}
						var d = document.createElement("div");
						d.innerHTML = xhr.getResponse().html;
						var l = d.getElementsByTagName("div");
						var h = document.createElement("h2");
						var b = document.createElement("div");
						b.className = "panel active";
						var top = document.createElement("div");
						top.className = "top";
						top.innerHTML = "<div> </div>";
						var bottom = document.createElement("div");
						bottom.className = "bottom";
						bottom.innerHTML = "<div> </div>";
						var content = document.createElement("div");
						content.className = "body clearfix";
						b.appendChild(top);
						b.appendChild(content);
						b.appendChild(bottom);
						for (var c = 0; c < l.length; c++) {
							if (l.item(c).className == "header") {
								h.innerHTML = l.item(c).innerHTML;
							} else if (l.item(c).className == "body") {
								var p = l.item(c).getElementsByTagName("p");
								for (var i = 0; i < p.length; i++) {
									content.appendChild(p.item(i));
								}
								content.appendChild(l.item(c).getElementsByTagName("form").item(0));
							}
						}
						m.appendChild(h);
						m.appendChild(b);
					}
				}
				if (!opt.noResize) {
					PCU.resizeMiniCart();
				}
				slider.toggleInputs(panelId, false);
				slider.slideIn(panelId);
				slider.clearMiniPagers();
				slider.isRunning = false;
				PCU.scrollToTop();
			}
		);
		// v Code added for error event logging - requires Xhr 2.x
		xhr.error.unsubscribeAll();
		xhr.error.subscribe(
			function() {
				slider.handleError(opt, 418);
			}
		);
		xhr.http.unsubscribeAll();
		xhr.http.subscribe(
			function() {
				slider.handleError(opt, xhr.getResponse().status);
			}
		);
		// ^ Code added for error event logging
		xhr.loading.unsubscribeAll();
		xhr.loading.subscribe(
			function() {
				slider.clearMiniPagers();
				slider.clearErrorMessages();
				if (!opt.noAnimation && !opt.isBaby) {
					slider.slideOut(panelId);
				}
			}
		);
		xhr.ready.unsubscribeAll();
		xhr.ready.subscribe(
			function() {
				slider.clearMiniPagers();
				//  PPSCR01368035 fix
				slider.isRunning = false;
				// End PPSCR01368035 fix
				setTimeout(function() {
				slider.resizePanelMask();
					if (!opt.isBaby) {
						slider.showPanelMask(false);
					}
					if (PCU.inArray(xhr.getResponse().template, slider.parentTemplates)) {
						if (!opt.noResize) {
							PCU.resizeMiniCart();
						}
						var currentState = document.getElementById("pageState") ? document.getElementById("pageState").value : "";
						var currentSession = document.getElementById("currentSession") ? document.getElementById("currentSession").value : "";
						var currentDispatch = document.getElementById("pageDispatch") ? document.getElementById("pageDispatch").value : "";
						if (form.id != "historyForm") {
							YAHOO.util.History.multiNavigate({"pageSession":currentSession, "pageState":currentState , "pageDispatch":currentDispatch});
						}
						var rosettaContainer = document.getElementById("html-rosetta-container");
						if (rosettaContainer) {
							var isHidden = YUD.hasClass(rosettaContainer, "hide");
							if (document.getElementById("loginModule") || document.getElementById("billingModule")) {
								if (isHidden) {
									YUD.removeClass(rosettaContainer, "hide");
								}
							} else {
								if (!isHidden) {
									YUD.addClass(rosettaContainer, "hide");
								}
							}
						}
					} else {
						slider.swapContent();
					}
					if (PCU.inArray(xhr.getResponse().template, slider.parentTemplates)) {
						slider.loadExternals("parentSlider");
					} else {
						slider.loadExternals(panelId);
					}
					slider.changePageTitle();
				}, 100);
				PAYPAL.widget.Balloons.init();
			}
		);
		xhr.ready.subscribe(
			function() {
				setTimeout(function() {				
					if((panelId == "babySlider") && (PCU.inArray(xhr.getResponse().template, slider.parentTemplates))){
						return;
					}
					slider.slideIn(panelId);
					var formReferences = new Array(form, "parentForm", document.login_form, document.billing_form, document.entry_form);
					if ((/^(Billing|Review|ConfirmCCInfo|ManageCC|PaymentOptions)\b/).test(xhr.getResponse().template)) {
						YUD.addClass(formReferences, "edit");
					} else if ((/\b(Login|Done)\b/).test(xhr.getResponse().template)) { 
						YUD.removeClass(formReferences, "edit");	
					}
				}, 1100);
			}
		);
		xhr.ready.subscribe(
			function() {
				setTimeout(function() {
					if ((xhr.getResponse().template == null) || (PCU.inArray(xhr.getResponse().template, slider.parentTemplates) && opt.isBaby)) {
						slider.slideOut("babySlider");
						slider.showPanelMask(false);						
					}
					if (form.id != "historyForm") {
						slider.toggleInputs(panelId, false);
					}
					if (!opt.noResize) {
						PCU.resizeMiniCart(true);
					}
					if (document.getElementById("CreditApplicationWrapper") && !bmlToSViewed) {
						bmlToSView();
					}
					slider.isRunning = false;
					slider.doneRunning.fire();
					PCU.scrollToTop();
					// update the site speed times (the Minipage response contains the updated TrackerInfo data track this as a unique page impression with all of the page data
					if (typeof TrackerInfo !== 'undefined' && typeof Tracker !== 'undefined') {
						Tracker.trackPage();
					}
					// update the site catalyst values
					if (s.scTrackPage) {
						s.scTrackPage();
					}
				}, 2200);
			}
		);
		// v Code added for error event logging - requires Xhr 2.x
		xhr.timedout.unsubscribeAll();
		xhr.timedout.subscribe( function() {
				slider.handleError(opt, 504);
			}
		);
		xhr.violation.unsubscribeAll();
		xhr.violation.subscribe( function() {
				slider.handleError(opt, 409);
			}
		);
		// ^ Code added for error event logging
		xhr.send(form);
	},

	/**
	* @method	Removes children from the error messages block
	* @returns	{void}
	*/
	clearErrorMessages: function() {
		var errorContainer = document.getElementById("messageBox");
		while(errorContainer.hasChildNodes()) {
			errorContainer.removeChild(errorContainer.firstChild);
			if (opt.isBaby) {
				slider.resizePanelMask();
			}
		}
	},

	/**
	* @method	Closes the baby slider
	* @returns	{void}
	*/
	closeBabySlider: function(e) {
		YUE.preventDefault(e);
		slider.slideOut("babySlider");
		slider.clearMiniPagers();
		setTimeout(function() {
			slider.toggleInputs("parentForm", false);
			slider.showPanelMask(false);
			if ( previousSelectedElement != null ) {
				slider.focusOnElement(previousSelectedElement);
			}
			slider.clearErrorMessages();
		}, 200);
	},

	/**
	* @method	
	* @returns	{void}
	*/
	swapContent: function() {
		var swap = document.getElementById("swapPanel");
		var panel = document.getElementById("babySlider");
		panel.innerHTML = swap.innerHTML;
		swap.innerHTML = "";
		slider.toggleInputs(panel, true);
		YUE.addListener("babySliderForm", "submit", function (e) { slider.resizePanelMask(); slider.submitHandler(e, this); });
		YUE.addListener("cancelBabySlider", "click", slider.closeBabySlider);
		YUE.addListener("babySliderForm", "keypress", slider.enterKeyHandler);
		slider.sliderContentReady.fire();
	},

	focusOnElement: function(el) {
		el = PCU.resolveElement(el);
		var exempt = ['A','AREA','BUTTON','INPUT','OBJECT','SELECT','TEXTAREA'];
		if ( !Boolean(window.chrome) && (!PCU.inArray(el.nodeName, exempt) || el.getAttribute('tabIndex') == null) ) {
			el.setAttribute('tabIndex', -1);
		}
		el.focus();
		/*
		if ( document.all ) { el.focus(); }
		temp_el = el;
		setTimeout("temp_el.focus()", 0);
		*/
	},

	/**
	* @method	Clears the baby slider
	* @returns	{void}
	*/
	clearContent: function(el) {
		var panel = PCU.resolveElement(el, "babySlider");
		panel.innerHTML = "";
	},

	/**
	* @method	Toggles submit inputs between disabled and enabled
	* @returns	{void}
	*/
	toggleInputs: function(form, flag) {
		form = PCU.resolveElement(form);
		var buttons = YUD.getElementsBy( function(el) { return (el.type == "submit" || el.type == "image"); }, "input", form);
		for (var i=0; i < buttons.length; i++) {
			buttons[i].disabled = (flag) ? "disabled" : false;
		}
	},
	
	/**
	* @method	DEPRECATED. Loads the scripts included in the response.
	* READDED for P1 issue PPSCR01331572
	* @returns	{void}
	*/
	loadExternals: function(panel) {
		panel = PCU.resolveElement(panel);
		var scripts = panel.getElementsByTagName("script");
		var head = document.getElementsByTagName("head")[0];
		for (var i=0; i < scripts.length; i++) {
			var newEl = document.createElement("script");
			newEl.type = "text/javascript";
			if (scripts[i].src && scripts[i].src !== "") {
				newEl.src = scripts[i].src;
			}
			if (scripts[i].text && scripts[i].text !== "") {
				newEl.text = scripts[i].text;
			}
			scripts[i].parentNode.replaceChild(newEl, scripts[i]);
		}
		//var styles = panel.getElementsByTagName("link");
		//for (i=0; i < styles.length; i++) {
		//	newEl = document.createElement("link");
		//	newEl.href = styles[i].href;
		//	newEl.type = styles[i].type;
		//	newEl.rel = styles[i].rel;
		//	newEl.media = styles[i].media;
		//	head.appendChild(newEl);
		//	styles[i].parentNode.removeChild(styles[i]);
		//}
	},

	/**
	* @method	Masks the panel (match the size of parentSlider)
	* @returns	{void}
	*/
	showPanelMask: function(flag) {
		var panelMask = document.getElementById("panelMask");
		if (flag) {
			slider.resizePanelMask();
			YUD.removeClass(panelMask, "accessAid");
		} else {
			YUD.addClass(panelMask, "accessAid");
		}
	},

	/**
	* @method	Resizes the panel mask
	* @returns	{void}
	*/
	resizePanelMask: function() {
		var panelMask = document.getElementById("panelMask");
		var activePanel = YUD.getElementsBy( function (el) { return (YUD.hasClass(el, "panel") && YUD.hasClass(el, "active")) ? true : false; }, "div", "sliderWrapper")[0];
		if (activePanel != null) {
			var bodyEl = YUD.getElementsByClassName("body", "div", "panelMask")[0];
			var r = YUD.getRegion(activePanel);
			if (YUD.hasClass(panelMask, "accessAid") || opt.isBaby) {
				YUD.setStyle(panelMask, "top", r.top+"px");
				YUD.setStyle(panelMask, "height", r.height+"px");
				YUD.setStyle(bodyEl, "height", (r.height-20)+"px");
			}
			var newTop = parseInt(r.height/2) - 42;
			if (newTop < 140) {
				YUD.setStyle("progressMeter", "top", newTop+"px");
			}
		}
	},

	/**
	* @method	Initializes the inputs based on the button clicked.
	* @returns	{void}
	* @argument	{node} button
	*/
	setMiniPager: function(button) {
		button = PCU.resolveElement(button);
		var i = document.getElementById("internalPager");
		if (i && button) {
			i.setAttribute("name", button.name);
			i.setAttribute("value", button.value);
		}
		var h = document.getElementById("miniPager");
		if (h && button) {
			h.setAttribute("name", button.name);
			h.setAttribute("value", button.value);
		}
	},

	/**
	* @method	Clears the inputs
	* @returns	{void}
	*/
	clearMiniPagers: function() {
		var i = document.getElementById("internalPager");
		if (i) {
			i.setAttribute("name", "");
			i.setAttribute("value", "");
		}
		var h = document.getElementById("miniPager");
		if (h) {
			h.setAttribute("name", "");
			h.setAttribute("value", "");
		}
	},

	/**
	* @method	Hides the panel (el) before it's updated.
	* @returns	{void}
	* @argument	{node} el
	*/
	slideOut: function(el) {
		el = PCU.resolveElement(el);
		var margin = (-el.offsetWidth);
		(this.isRTL) ? YUD.setStyle(el, "marginRight", margin+"px") : YUD.setStyle(el, "marginLeft", margin+"px");
		slider.clearContent(el);
		if (isIE6 && document.getElementById("country_code") != null) {
			YUD.setStyle("country_code", "visibility", "visible");
		}
	},

	/**
	* @method	Slides the panel in so that it's visible.
	* @returns	{void}
	* @argument	{node} el
	*/
	slideIn: function(el) {
		el = PCU.resolveElement(el);
		var miniCart = YUD.getRegion(YUD.getElementsByClassName("col first")[0]);
		var slideIn = null;
		YUE.addListener(document.getElementsByTagName("input"), "click", function (e) {PCU.explicitOrigTarget = YUE.getTarget(e);}); //hack for WebKit
		if (el.getAttribute("id") === "babySlider") {
			var formName = document.getElementById("babySliderForm");
			formName = formName ? formName.getAttribute("name") : null;
			if (formName && (formName == "editBillingForm" )) {
				YUD.setStyle("innerSlider", "height", (parseInt(YUD.getComputedStyle(document.getElementById("sliderWrapper"), "height"))-parseInt(YUD.getComputedStyle(el, "marginTop"))*2)+"px");
			} if (formName && (formName == 'ANForm' || formName == 'pepQuestionnaire')) { /* AN */
			YUD.setStyle('babySlider','marginTop','132px');					
			} if (formName && (formName == 'PepQuestionnaire')) {
				var billingHt = document.getElementById('miniCartContent').offsetHeight;
				if (billingHt<710) {
					YUD.setStyle("pepPage", "height", "420px");
					YUD.setStyle("pepPage", "overflow", "auto");
				}				
			} else if (formName && (formName == "choose_address_form")) {
				YUD.setStyle("scrollBuffer", "height", "270px");
			}			
			var switchB = YUD.getElementsByClassName("switchB","div").length ? 55 : 0;
			var animData = (( this.isRTL) ? {marginRight:{to:miniCart.width}} : {marginLeft:{to:miniCart.width+switchB}});
			slideIn = new YAHOO.util.Anim(el, animData, 0.5, YAHOO.util.Easing.easeIn);
			var fe = "innerSlider";
		} else {
			var animData = (( this.isRTL) ? {marginRight:{to:0}} : {marginLeft:{to:0}} );
			var fe = el;
		}
		slideIn = new YAHOO.util.Anim(el, animData, ANIMATION_DURATION, YAHOO.util.Easing.easeIn);
		slideIn.onComplete.subscribe(function() {slider.focusOnElement(fe)});
		if (isIE6 && document.getElementById("country_code") != null) {
			YUD.setStyle("country_code", "visibility", "hidden");
		}
		slideIn.onTween.subscribe(function() { el.style.zIndex = 10; YUD.removeClass(el, "accessAid"); });
		slideIn.animate();
	},

	/**
	* @method	Changes the page title for parent pages.
	* @returns	{void}
	*/
	changePageTitle: function() {
		if (PCU.inArray(xhr.getResponse().template, slider.parentTemplates) && document.getElementById("pageTitle")) {
			document.title = document.getElementById("pageTitle").value;
		}
	},

	/**
	* @method	Gets the default input for the specified container
	* @returns	{node}
	* @argument	{node} container
	*/
	getDefaultButton: function(container) {
		return YUD.getElementsByClassName("default", "input", PCU.resolveElement(container))[0];
	}
};

//TODO: add this javascript code to the standards "widget" library
/**
 * Enables add more functionality
 *
 * Usage: 
 * Requires a container element with an id attrubute
 * The container element must have at least one paragraph which contains one text field
 * The container element must have one button
 * This widget hides all paragraphs within the container element, and attaches the "addMore" function to the button
 *
 * @param {Object} el The ID of the container element within which the AddMore widget will render itself
 */

PAYPAL.widget.AddMore = function (el) {
	this.init(el);
};


PAYPAL.widget.AddMore.prototype = {
	/**
	* @property	Node to update
	* @type	{node}
	*/
	addMoreContainer: null,

	/**
	* @property	Input to trigger update
	* @type	{node}
	*/
	addMoreButton: null,

	/**
	* @property	Elements shown
	* @type	{node[]}
	*/
	shownNodeList: [],

	/**
	* @property	Elements hidden
	* @type	{node[]}
	*/
	hiddenNodeList: [],

	/**
	* @property
	* @type	{string}
	*/
	addMoreError: "The current DOM does not appear ready to support the PAYPAL.widget.AddMore object. Please read the AddMore documentation and try again.",

	/**
	* @method	Initializes the widget
	* @returns	{void}
	*/
	init: function (el) {
		this.addMoreContainer = document.getElementById(el);
		if (this.addMoreContainer === null) {
			throw new Error(this.addMoreError);
		}
		var fieldContainers = this.addMoreContainer.getElementsByTagName("p");
		var txtField = null;
		for (var i = 1; i < fieldContainers.length; i++) {
			txtField = fieldContainers[i].getElementsByTagName("input")[0];
			if (txtField.value == "") {
				YUD.addClass(fieldContainers[i], "hide");
				this.hiddenNodeList.push(fieldContainers[i]);
			}
		}
		this.addMoreButton = YUD.getElementsByClassName("add-more-button", "input", this.addMoreContainer);
		if (this.addMoreButton.length != 1 || this.hiddenNodeList.length === 0) {
			throw new Error(this.addMoreError);
		}
		YUE.on(this.addMoreButton, "click", this.addMore, this);
		YUD.removeClass(this.addMoreButton, "hide");
	},

	/**
	* @method	Remove the "hide" class from the first element of hiddenNodeList, focus the input that was just shown, and shift the first element from hiddenNodeList and push it onto the shownNodeList
	* @returns	{void}
	*/
	addMore:function (e, addMoreObj) {
		YUE.preventDefault(e);
		if (addMoreObj.hiddenNodeList.length == 1) {
			YUD.addClass(addMoreObj.addMoreButton, "hide");
		}
		YUD.removeClass(addMoreObj.hiddenNodeList[0], "hide");
		addMoreObj.hiddenNodeList[0].getElementsByTagName("input")[0].focus();
		addMoreObj.shownNodeList.push(addMoreObj.hiddenNodeList.shift());
	}
};

/* Expand/Collapse Seller Details*/
PAYPAL.Checkout.expCollapse = {
	/**
	* @method	Collapse Seller details on page load
	* @returns	{void}
	*/
	init:function() {
		var sellName = YUD.getElementsByClassName("expSell", "a", "miniCart"); 
		var sellItem = YUD.getElementsByClassName("selleritm", "li", "miniCart");
		for(var i=0; i<sellName.length; i++) {
			YUD.replaceClass(sellName[i], "expSell", "collSell");
			for(var j=0; j<sellItem.length; j++) {
				YUD.replaceClass(sellItem[j], "show", "hide");
			}			
			YUE.addListener(sellName[i], "click", PAYPAL.Checkout.expCollapse.showHideDet);
		}
	},

	/**
	* @method	Toggles the seller details
	* @returns	{void}
	*/
	showHideDet:function() {
		var item = this.parentNode.parentNode.getElementsByTagName("li");
		for(var i=0; i<item.length; i++) {
			if (item[i].id != "") {
				var sellerDet = item[i].id;
			}
		}
		if (YUD.hasClass(sellerDet, "hide")) {
			YUD.replaceClass(sellerDet, "hide", "show");
			YUD.replaceClass(this.id, "collSell", "expSell");
		} else {
			YUD.replaceClass(sellerDet, "show", "hide");
			YUD.replaceClass(this.id, "expSell", "collSell");
		}	
	}
};

function submitPopWindow(frm, width, height) {
	var features = "resizable=1, scrollbars=1, width=" + width + ", height=" + height;
	var frm = PCU.resolveElement(frm, "parentForm");
	frm.setAttribute("target", "popupWin");
	slider.setMiniPager("receipt.x");
	var win = window.open("", "popupWin", features);
	frm.submit();
	win.focus();
}

if (typeof mcSequenceDuration != "undefined" && mcSequenceDuration != null) {
	setTimeout(function() {
		PAYPAL.Checkout.Slider.checkIsRTL();
		PAYPAL.Checkout.Slider.slideIn("parentSlider");
	}, mcSequenceDuration);
}
var slider = PAYPAL.Checkout.Slider;
PAYPAL.Checkout.expCollapse.init();
PCU.resizeMiniCart(true);
/* Financial Options page Section Link toggle */
PAYPAL.Checkout.ElementBinder = {
	finopsectionlinktoggle: function() {
		setTimeout(function() {
			YUE.onAvailable('linknewcard',function(){document.getElementById('linknewcard').disabled=false;});
			YUE.onAvailable('backToUpdatecard',function(){document.getElementById('backToUpdatecard').disabled=false;});
		}, 2000);
	}
}

