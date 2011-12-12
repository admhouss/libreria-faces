PAYPAL.namespace("Checkout");

/**
* Asynchronous communication (AJAX).
*
* @argument {string} id
*
* @requires	YAHOO.util.CustomEvent
* @requires	PAYPAL.Checkout.XhrRequest
* @requires	PAYPAL.Checkout.XhrResponse
*
* @author	Robert King
* @version	2.2
*/
PAYPAL.Checkout.Xhr = function(id) {
	/**
	* @property	Retry index.
	* @type	{integer}
	*/
	this.attempt = 0;

	/**
	* @property	Automatically update page
	* @type	{boolean}
	*/
	this.autoUpdate = true;

	/**
	* @property	Error object, set when there is a script exception.
	* @type	{object}
	*/
	this.err = null;

	/**
	* @property	Instance identifier
	* @type	{string}
	this.id = id.toString() || Math.random().toString().replace(/0\./,"");

	/**
	* @property	An array of the requests sent. 
	* @type	{PAYPAL.Checkout.XhrRequest[]}
	*/
	this.request = new Array();

	/**
	* @property	An array of the responses received.
	* @type	{PAYPAL.Checkout.XhrResponse[]}
	*/
	this.response = new Array();

	/**
	* @property	The number of retries to make automatically.
	* @type	{integer}
	*/
	this.retries = null;

	/**
	* @property	The number of seconds to wait for a response.
	* @type	{float}
	*/
	this.timeout = null;

	/**
	* @method	Returns the request represented by the index. If index is null or invalid, it returns the last request or null if there are no requests.
	* @returns	{PAYPAL.Checkout.XhrRequest}
	*
	* @argument	{integer} index
	*/
	this.getRequest = function(index) {
		if (me.request.length == 0) {
			return;
		} else if (!index || index < 0 || index > me.request.length) {
			return me.request[me.request.length - 1];
		} else {
			return me.request[index];
		}
	};

	/**
	* @method	Returns the response represented by the index. If index is null or invalid, it returns the last response or null if there are no responses.
	* @returns	{PAYPAL.Checkout.XhrResponse}
	*
	* @argument	{integer} index
	*/
	this.getResponse = function(index) {
		if (me.response.length == 0) {
			return;
		} else if (!index || index < 0 || index > me.response.length) {
			return me.response[me.response.length - 1];
		} else {
			return me.response[index];
		}
	};

	/**
	* @method	Returns the state
	* @returns	{PAYPAL.Checkout.Xhr.states}
	*/
	this.getState = function() {
		return _mode;
	};

	/**
	* @method	Sends the last request. Passing true into the function overrides AJAX processing and forces a full-page submit.
	* @returns	{void}
	*
	* @argument {boolean} force
	*/
	this.retry = function(force) {
		me.send(me.getRequest(), new Array({name:"retry", type:"hidden", value:me.attempt}), force);
		return;
	};

	/**
	* @method	Starts the communication. Returns false if there is an error, true otherwise.
	* @returns	{boolean}
	*
 	* @argument	{object|node|string} resource	The resource (an XhrRequest object, a string representing the DOM node id, or the DOM node -- the element should be a URL, HTML form, or HTML anchor) to be used to generate the request.
	* @argument	{object[]} cgi	An array of objects containing name, type, and value properties (like Input objects), e.g. new Array({name: 'foo', type:'hidden', value: 1}, {name: 'bar', type:'hidden', value: 2});
	* @argument {boolean} force	Override AJAX and force a full-page submit
	* @argument	{boolean} async	Send asynchronously (default is true)
	* @argument	{string} username	Username used to access the resource
	* @argument	{string} password	Password used to access the resource
	*/
	this.send = function(resource, cgi, force, async, username, password) {
		force = force ? force : false;
		if (_mode == PAYPAL.Checkout.Xhr.states.WAITING) {
			return false;
		}
		try {
			// Set the state to waiting
			_state(PAYPAL.Checkout.Xhr.states.WAITING);
			// Get a PAYPAL.Checkout.XhrRequest object if the resource passed in is not one
			var xhrRequest = (!resource.form) ? _formatRequest(resource, cgi, async, username, password) : resource;
			// If we have a connection and we're not being asked to force a full-page submit, open an XmlHttpRequest
			if (_connection && !force && me.getState() != PAYPAL.Checkout.Xhr.states.UNAVAILABLE) {
				_connection.open(xhrRequest.form.method, (xhrRequest.form.method.toUpperCase() == "GET" ? xhrRequest.form.action + "&view_requested=MiniPage" : xhrRequest.form.action), xhrRequest.async, xhrRequest.username, xhrRequest.password);
				_connection.setRequestHeader("Cache-Control", "no-store, no-cache, must-revalidate");
				if (xhrRequest.form.method.toUpperCase() == "POST") {
					// Set the request header content type so that the POST works correctly
					_connection.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
					if (_connection.overrideMimeType) { _connection.overrideMimeType("text/xml"); }
				}
				// Start the interval to watch for a response
				_watchRequest(_connection, me);
				// Set the data
				_connection.send(xhrRequest.form.method.toUpperCase() == "GET" ? xhrRequest.data : xhrRequest.data + "&view_requested=MiniPage");
			} else if (xhrRequest.form) {
				// Get the form (note that this is from the PAYPAL.Checkout.XhrRequest)
				var frm = xhrRequest.form;
				// Append it to the document (it's not already there)
				document.body.appendChild(frm);
				// Call the submit method
				frm.submit();
			}
		} catch (err) {
			_handleException(err);
			return false;
		}
		return true;
	};

	/**
	* @event	Event triggered if the response is a PAYPAL ABORT.
	*/
	this.abort = new YAHOO.util.CustomEvent(this.id + "_ERROR_ABORT");

	/**
	* @event	Event triggered if the response is a PAYPAL ERROR.
	*/
	this.error = new YAHOO.util.CustomEvent(this.id + "_ERROR_GENERAL");

	/**
	* @event	Event triggered if there is a script error.
	*/
	this.exception = new YAHOO.util.CustomEvent(this.id + "_SCRIPT_EXCEPTION");

	/**
	* @event	Event triggered when the HTTP status code is not PAYPAL.Checkout.Xhr.status.OK.
	*/
	this.http = new YAHOO.util.CustomEvent(this.id + "_ERROR_NETWORK");

	/**
	* @event	Event triggered when the response is loading.
	*/
	this.loading = new YAHOO.util.CustomEvent(this.id + "_LOADING");

	/**
	* @event	Event triggered when the response is ready for parsing.
	*/
	this.ready = new YAHOO.util.CustomEvent(this.id + "_READY");

	/**
	* @event	Event triggered when the timeout expires. This is only triggered if the timeout property is not null.
	*/
	this.timedout = new YAHOO.util.CustomEvent(this.id + "_ERROR_TIMEOUT");

	/**
	* @event	Event triggered if there are updates in the updates property and they have all been completed. 
	*/
	this.updated = new YAHOO.util.CustomEvent(this.id + "_UPDATED");

	/**
	* @event	Event triggered when the response string is malformed.
	*/
	this.violation = new YAHOO.util.CustomEvent(this.id + "_ERROR_RESPONSE")

	/**
	* @event	Event triggered when the XmlHttpRequest object sends a GET/POST request.
	*/
	this.waiting = new YAHOO.util.CustomEvent(this.id + "_WAITING");

	/**
	* @private
	* @method	Formats the request, returning true if it was able to be parsed, false if not.
	* @returns	{XhrRequest}
	*
 	* @argument	{node|string} resource
	* @argument	{object[]} elements
	* @argument	{boolean} async
	* @argument	{string} username
	* @argument	{string} password
	*/
	function _formatRequest(resource, elements, async, username, password) {
		var request = new PAYPAL.Checkout.XhrRequest(resource, elements, async, username, password);
		me.request.push(request);
		return request;
	}

	/**
	* @private
	* @method	Handle the script bomb.
	* @returns	{void}
	*
	* @argument	{error} err
	*/
	function _handleException(err) {
		me.err = err;
		if (_debug) {
			if (console.log) {
				console.log(err.fileName + " " + err.lineNumber + ": " + err.message);
				console.log("Stack trace: " + err.stack);
			} else {
				var msg = "";
				for (var prop in err) {
					msg += prop.toString() + ": " + err[prop].toString() + "\n";
				}
				alert(msg);
			}
		}
		if (me.exception) { me.exception.fire(); }
	}

	/**
	* @private
	* @method	Response handling.
	* @returns	{void}
	*
	* @argument	{XmlHttpRequest} xhr
	*/
	function _parseResponse(xhr) {
		if (xhr) {
			if (xhr.readyState == PAYPAL.Checkout.Xhr.readyStates.RESPONSE_DONE) {
				try {
					_state(PAYPAL.Checkout.Xhr.states.LOADING);
					_t.c();	// Clear the timers
					me.attempt = 0;	// Set the attempt counter since we got a good (HTTP status) reponse

					var xhrResponse = new PAYPAL.Checkout.XhrResponse(xhr);	// Create a response object
					xhrResponse.loaded.subscribe(function() { _state(PAYPAL.Checkout.Xhr.states.UPDATED); }, me, true);	// Subscribe to the loaded event
					xhrResponse.exception.subscribe(function() { _handleException(xhrResponse.err); }, me, true);	// Subscribe to the exception event for JS exceptions
					me.response.push(xhrResponse);	// Add the response to the collection
					if (xhrResponse.status != PAYPAL.Checkout.Xhr.status.OK) {	// If there was an HTTP error
						_state(PAYPAL.Checkout.Xhr.states.HTTP_ERROR);		// Report it
					} else if (xhrResponse.responseType == PAYPAL.Checkout.XhrResponse.types.HTML) {	// If we got a full-page response (not a JSON object)
						me.retry(true);	// Retry so that we get the response directly in the user-agent
					} else if (xhrResponse.responseType == PAYPAL.Checkout.XhrResponse.types.JSON) {	// If we got a JSON object
						switch (xhrResponse.type) {	// Look at the response type
							case "abort":		// If it's a PayPal abort
								_state(PAYPAL.Checkout.Xhr.states.ABORTED);	// Report it
								return;
							case "auto-submit":	// If PayPal says we need to auto-submit
								me.send(xhrResponse.autoSubmit, null, true);	// Request it
								return;
							case "error":	// If it's a PayPal error
								_state(PAYPAL.Checkout.Xhr.states.ERROR);	// Report it
								return;
							case "redirect":	// If we're redirecting to another URL
								document.location.href = xhrResponse.url;	// Request it
								return;
						}
						if (me.autoUpdate) {	// If we need to handle updates automatically
							xhrResponse.load();	// Load the response
						}
						if (xhrResponse.autoSubmit) {	// If the web developer says to auto-submit
							me.send(xhrResponse.autoSubmit, null, true);	// Request it
						}
						_state(PAYPAL.Checkout.Xhr.states.READY);	// If we get here, set everything to 'ready'
					} else {	// If we didn't get an appropriate HTML or JSON response
						_state(PAYPAL.Checkout.Xhr.states.RESPONSE_CONTENT_ERROR);	// Report it
					}
				} catch (err) {
					_handleException(err);
				}
			}
		}
		return;
	}

	/**
	* @private
	* @method	If the state attempts to change to an unrecognized state, the change is disallowed.
	* @returns	{void}
	*
	* @argument	{PAYPAL.Checkout.Xhr.states} newState
	*/
	function _state(newState) {
		if (_mode == PAYPAL.Checkout.Xhr.states.UNAVAILABLE && newState != PAYPAL.Checkout.Xhr.states.INITIALIZED) { return; }
		switch (newState) {
			case PAYPAL.Checkout.Xhr.states.ABORTED:
				_mode = newState;
				if (me.abort) { me.abort.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.ERROR:
				_mode = newState;
				if (me.error) { me.error.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.EXPIRED:
				_mode = newState;
				if (me.timedout) { me.timedout.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.HTTP_ERROR:
				_mode = newState;
				if (me.http) { me.http.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.INITIALIZED:
				_mode = newState;
				break;
			case PAYPAL.Checkout.Xhr.states.LOADING:
				_mode = newState;
				if (me.loading) { me.loading.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.READY:
				_mode = newState + (_mode == PAYPAL.Checkout.Xhr.states.UPDATED ? PAYPAL.Checkout.Xhr.states.UPDATED : 0);
				if (me.ready) { me.ready.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.RESPONSE_CONTENT_ERROR:
				_mode = newState;
				if (me.violation) { me.violation.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.UNAVAILABLE:
				_mode = newState;
				break;
			case PAYPAL.Checkout.Xhr.states.UPDATED:
				_mode = newState + (_mode == PAYPAL.Checkout.Xhr.states.READY ? PAYPAL.Checkout.Xhr.states.READY : 0);
				if (me.updated) { me.updated.fire(); }
				break;
			case PAYPAL.Checkout.Xhr.states.WAITING:
				_mode = newState;
				if (me.waiting) { me.waiting.fire(); }
				break;
			default:
				break;
		}
		return;
	}

	/**
	* @private
	* @method	Executed when the timeout expires.
	* @returns	{void}
	*
	* @argument	{XmlHttpRequest} xhr
	*/
	function _timeoutExpired(xhr) {
//		_t.c();
//		xhr.abort();
		_state(PAYPAL.Checkout.Xhr.states.EXPIRED);
//		if (me.retries && me.attempt < me.retries) {
//			me.retry();
//			me.attempt++;
//		}
		return;
	}

	/**
	* @private
	* @method	Creates a timer that watches the XmlHttpRequest object every 20 milliseconds and optionally, a timeout with a duration set by the user.
	* @returns	{void}
	*
	* @argument	{XmlHttpRequest} xhr
	*/
	function _watchRequest(xhr) {
		var s = this;
		_t.p = window.setInterval(function() { _parseResponse.apply(s, [xhr]); }, 20);
		pp_xo_xhr_timers.push(_t.p);
		if (me.timeout) { _t.t = window.setTimeout(function() { _timeoutExpired.apply(s, [xhr]); }, (me.timeout * 1000)); }
		return;
	}

	/**
	* Constructor
	*
	* _connection  {XmlHttpRequest}  The AJAX connection object. Created during initialization.
	* _debug       {boolean}         Show debug messages - true for paypal.com subdomains.
	* _id          {string}          Instance identifier
	* _mode        {integer}         One of the PAYPAL.Checkout.Xhr.states enumerated states.
	* _t           {object}          Timer object created by _watchRequest and consumed by _timeoutExpired and _parseResponse.
	*/
	var me = this;
	try {
		var _connection = null, _debug = (/www\..{1,}\.paypal.com/i).test(document.URL), _id = null, _mode = null;
		var _t = {
			/**
			* @property	The timer id of the watch interval
			* @type	{integer}
			*/
			p: null,

			/**
			* @property	The timer id of the timeout timer
			* @type	{integer}
			*/
			t: null,

			/**
			* @method	Destroys the timers
			* @returns	{void}
			*/
			c: function() {
				var i;
				if (this.p) {
					for (i = 0; i < pp_xo_xhr_timers.length; i++) {
						if (pp_xo_xhr_timers[i] == this.p) {
							pp_xo_xhr_timers.splice(i, 1);
						}
					}
					this.p = window.clearInterval(this.p);
				} else {
					for (i = 0; i < pp_xo_xhr_timers.length; i++) {
						window.clearInterval(pp_xo_xhr_timers[i]);
					}
					pp_xo_xhr_timers = [];
				}
				this.t = window.clearTimeout(this.t);
				return;
			}
		};

		try {
			if (window.XMLHttpRequest) {	// If we can create an XmlHttpRequest object, attempt to
				_connection = new XMLHttpRequest();
				_state(PAYPAL.Checkout.Xhr.states.INITIALIZED);
			} else if (typeof ActiveXObject != "undefined") {	// Otherwise, if we can create an ActiveX object, try to create one to handle the XmlHttpRequest
				// Create a list of all the MS XMLHTTP libraries
				var libIndex, libs = new Array("Microsoft.XMLHTTP","MSXML2.XMLHTTP.3.0","MSXML2.XMLHTTP");
				// Loop through the list to try to create the object
				for (libIndex = 0; libIndex < libs.length; ++libIndex) {
					try {
						_connection = new ActiveXObject(libs[libIndex]);
						_state(PAYPAL.Checkout.Xhr.states.INITIALIZED);
						break;
					} catch (liberr) {
						_state(PAYPAL.Checkout.Xhr.states.UNAVAILABLE);
					}
				}
			}
		} catch (err) {
			_state(PAYPAL.Checkout.Xhr.states.UNAVAILABLE);
		}

		// Declare a global array to hold the interval ids
		window.pp_xo_xhr_timers = (!window.pp_xo_xhr_timers) ? new Array() : window.pp_xo_xhr_timers;
	} catch (err) {
		_handleException(err);
	}
};
/**
* Enumerated states for the Xhr object.
*
* @author	Robert King
* @version	1.0
*/
PAYPAL.Checkout.Xhr.states = {
	HTTP_ERROR: -64,
	RESPONSE_CONTENT_ERROR: -32,
	UNAVAILABLE: -16,
	EXPIRED: -4,
	ABORTED: -2,
	ERROR: -1,
	INITIALIZED: 0,
	READY: 1,
	WAITING: 2,
	LOADING: 4,
	UPDATED: 16
};
/**
* Enumerated states for the XmlHttpRequest object.
*
* @author	Robert King
* @version	1.0
*/
PAYPAL.Checkout.Xhr.readyStates = {
	REQUEST_NOT_SENT: 0,
	OPENED: 1,
	RESPONSE_HEADERS_RECEIVED: 2,
	RESPONSE_LOADING: 3,
	RESPONSE_DONE: 4
};
/**
* Enumerated codes for HTTP status.
*
* @author	Robert King
* @version	1.0
*/
PAYPAL.Checkout.Xhr.status = {
	OK: 200
};

/**
* Asynchronous communication (AJAX).
*
* @argument	{node|string} resource	The resource (a string representing the DOM node id or the DOM node -- the element should be a URL, HTML form, or HTML anchor) to be used to generate the request.
* @argument	{object[]} elements	An array of objects containing name, type, and value properties (like Input objects), e.g. new Array({name: 'foo', type:'hidden', value: 1}, {name: 'bar', type:'hidden', value: 2});
* @argument	{boolean} async	Send asynchronously
* @argument	{string} username	Username
* @argument	{string} password	Password
*
* @author	Robert King
* @version	2.0
*/
PAYPAL.Checkout.XhrRequest = function(resource, elements, async, username, password) {
	/**
	* @property	Use asynchronous communication
	* @type	{boolean}
	* @default	true
	*/
	this.async = (async === false) ? false : true;

	/**
	* @property	Data to be sent in the request
	* @type	{string}
	*/
	this.data = null;

	/**
	* @property	The form to be used to generate the request
	* @type	{Form}
	*/
	this.form = null;

	/**
	* @property	The password to be presented in the request
	* @type	{string}
	*/
	this.password = password;

	/**
	* @property	The username to be presented in the request
	* @type	{string}
	*/
	this.username = username;

	/**
	* @private
	* @method	Exception handler.
	* @returns	{void}
	*
	* @argument	{error} err
	*/
	function _raiseError(err) {
		me.err = err;
		if (_debug) {
			if (console.log) {
				console.log(err.fileName + " " + err.lineNumber + ": " + err.message);
				console.log("Stack trace: " + err.stack);
			} else {
				var msg = "";
				for (var prop in err) {
					msg += prop.toString() + ": " + err[prop].toString() + "\n";
				}
				alert(msg);
			}
		}
		me.exception.fire();
	}

	/**
	* @private
	* @method	Normalizes an anchor to a form or processes a form (URL encodes the name(s) and value(s) of the form elements)
	* @returns	{form}
	* 
	* @argument	{node|string} a
	*/
	function _urlToForm(a) {
		try {
			var frm;
			if (typeof(a) == "string" && (/^https?\:\/\//).test(resource)) {	// We have a URL
				frm = document.createElement("form");
				frm.action = a;
			} else if (typeof(a) == "string") {	// We think we were given an ID
				var node = document.getElementById(a);
				if (node) {
					frm = _urlToForm(node);
				}
			} else if (a.nodeName && a.nodeName.toLowerCase() == "a") {	// We have a node that is an ANCHOR
				frm = document.createElement("form");
				frm.action = a.href;
			} else if (a.nodeName && a.nodeName.toLowerCase() == "form") {	// We have a node that is a FORM
				frm = document.createElement("form");
				frm.action = a.action;
				frm.method = a.method;
				frm.style.display = "none";
				frm.onsubmit = "";
				var elem, elemIndex, node;
				for (elemIndex = 0; elemIndex < a.elements.length; elemIndex++) {
					//Don't add checkbox/radio element to the newly-created form
            	var addToFrm = true;
					elem = a.elements[elemIndex];
					if (elem.disabled || typeof(elem.type) == "undefined") {
						continue;
					} else if (elem.name && (/cache\_buster/i).test(elem.name)) {
						node = document.createElement("input");
						node.type = "hidden";
						node.name = encodeURIComponent(elem.name);
						node.value = Math.random().toString().replace(/0\./, "");
						frm.appendChild(node);
					} else if (elem.name && (/view\_requested/i).test(elem.name)) {
						// Skip
					} else if (elem.name) {
						node = document.createElement("input");	// Create a new input
						node.type = "hidden";	// Make it hidden
						node.name = encodeURIComponent(elem.name || elem.id);	// Set the name
						switch (elem.type.toLowerCase()) {	// Check the type and set the value of the new node
							case "radio":
							case "checkbox":
								if (elem.checked) {
									node.value = encodeURIComponent(elem.value || "on");
								}
								//Don't add checkbox/radio element to the newly-created form
								else {
									addToFrm = false;
								}
								break;
							case "select-one":
							case "select-multiple":
								var options = elem.options, optionsIndex, values = new Array();
								for (optionsIndex = 0; optionsIndex < options.length; optionsIndex++) {
									if (options[optionsIndex].selected) {
										values.push(encodeURIComponent(options[optionsIndex].value));
									}
								}
								node.value = values.join(",");
								break;
							case "color":
							case "date":
							case "datetime-local":
							case "datetime":
							case "email":
							case "file":
							case "hidden":
							case "month":
							case "number":
							case "password":
							case "range":
							case "search":
							case "tel":
							case "text":
							case "textarea":
							case "time":
							case "url":
							case "week":
								node.value = encodeURIComponent(elem.value);
								break;
							default:
								addToFrm = false;
								break;
						}
						//Don't add checkbox/radio element to the newly-created form
						if(addToFrm){
							frm.appendChild(node);	// Add the element to the newly-created form
						}
					}
				}
			}
			frm.method = ((/POST/i).test(frm.method)) ? "POST" : "GET";	// Default the newly-created form HTTP verb
			return frm;
		} catch (err) {
			_raiseError(err);
		}
	}

	/**
	* Constructor
	*
	* _debug       {boolean}         Show debug messages - true for paypal.com subdomains.
	*/
	var me = this;
	try {
		var _debug = (/www\..{1,}\.paypal.com/i).test(document.URL), frm, elem, elementIndex, elementName, existingElement, queryString = new Array();

		// Normalize the constructor so that we have either an anchor or a form
		frm = _urlToForm(resource);
		// Do a sanity check
		if (!frm || !frm.action) { return; }
		// If we have additional CGI variables, append them to the form
		if (elements) {
			for (elementIndex = 0; elementIndex < elements.length; elementIndex++) {
				if (elements[elementIndex].name != "" && elements[elementIndex].value != "") {
					existingElement = resource.elements[elements[elementIndex].name];
					if (!existingElement || elements[elementIndex].type.toLowerCase() == "submit" || elements[elementIndex].type.toLowerCase() == "image") {
						elem = document.createElement("input");
						elem.type = "hidden";
						elem.name = encodeURIComponent(elements[elementIndex].name);
						elem.value = encodeURIComponent(elements[elementIndex].value);
						frm.appendChild(elem);
					}
				}
			}
		}
		// Create a query string
		for (elementIndex = 0; elementIndex < frm.elements.length; elementIndex++) {
			elem = frm.elements[elementIndex];
			queryString.push(elem.name + "=" + elem.value);
		}
		// If the form uses GET, append all the CGI variables to the URL in the query string and null out the form
		if ((/GET/i).test(frm.method)) {
			if (frm.action.indexOf("?")) {
				frm.action += "?";
			}
			frm.action += queryString.join("&");
			while (frm.firstChild) {
				frm.removeChild(frm.firstChild);
			}
			queryString = null;
		}
		// Create a data set using the query string array - if the verb is GET, this will be null because the data set is in the URL
		this.data = queryString ? queryString.join("&") : null;
		this.form = frm;
		if (!(/GET/i).test(this.form.method) && queryString) {
			var cgiIndex, cgiVar;
			for (cgiIndex = 0; cgiIndex < queryString.length; cgiIndex++) {
				cgiVar = queryString[cgiIndex].split("=");
				if (cgiVar) {
					elem = document.createElement("input");
					elem.type = "hidden";
					elem.name = cgiVar[0];
					elem.value = cgiVar[1];
					this.form.appendChild(elem);
				}
			}
		}
	} catch (err) {
		_raiseError(err);
	}
};

/**
* Asynchronous communication (AJAX).
*
* @argument	{XmlHttpRequest} xhr
*
* @author	Robert King
* @version	2.0
*/
PAYPAL.Checkout.XhrResponse = function(xhr) {
	/**
	* @property	The form to be submitted automatically
	* @type	{node}
	*/
	this.autoSubmit = null;

	/**
	* @property	Error object populated when an exception is raised.
	* @type	{error}
	*/
	this.err = null;

	/**
	* @property	The post-processed html property of the JSON object in the response
	* @type	{string}
	*/
	this.html = null;

	/**
	* @property	Script instructions contained in the response
	* @type	{object}
	*/
	this.instructions = {
		/**
		* @property	Error object. Populated when an exception is fired
		* @type	{error}
		*/
		err: null,

		/**
		* @method	Adds an item to the queue
		* @returns	{void}
		*
		* @argument	{object} item
		*/
		add: function(item) {
			this._instructionQueue.push(item);
		},

		/**
		* @method	Executes all items in the queue.
		* @returns	{void}
		*/
		load: function() {
			try {
				var index;	// Declare the loop index outside of the loop
				for (index = 0; index < this._instructionQueue.length; index++) {	// Loop through all the items in the instructionQueue array
					eval("window." + this._instructionQueue[index]);	// Execute the instruction using eval
				}
			} catch (err) {
				this.err = err;	// Set the error so we can perform any additional handling needed
				if (this.exception) { this.exception.fire(); }	// Fire the exception event
			}
		},

		/**
		* @event	Event triggered if there's a script error.
		*/
		exception: new YAHOO.util.CustomEvent("XHRRESPONSE_" + Math.random().toString().replace(/0\./, "") + "_INSTRUCTIONS_EXCEPTION"),

		/**
		* @private
		* @property	Queue of commands to execute.
		* @type	{string[]}
		*/
		_instructionQueue: new Array()
	};

	/**
	* @property	The readyState of the XmlHttpRequest object
	* @type	{integer}
	*/
	this.readyState = xhr.readyState;

	/**
	* @property	Resources (script and style tags) contained in the response
	* @type	{object}
	*/
	this.resources = {
		/**
		* @property	Error object. Populated when an exception is fired.
		* @type	{error}
		*/
		err: null,

		/**
		* @method	Adds an item to the queue
		* @returns	{void}
		*
		* @argument	{object} item
		*/
		add: function(item) {
			this._resourceQueue.push(item);
		},

		/**
		* @method	Loads all items in the queue.
		* @returns	{void}
		*/
		load: function() {
			try {
				var index, resourceElement, documentHead = document.getElementsByTagName("head").item(0), index2;	// Declare the variables we need
				for (index = 0; index < this._resourceQueue.length; index++) {	// Loop through all the resources in the queue
					resourceElement = document.createElement(this._resourceQueue[index].nodeName);	// Create the appropriate element
					for (index2 = 0; index2 < this._resourceQueue[index].attributes.length; index2++) {	// Loop through all the attributes in the element in the queue
						if (this._resourceQueue[index].attributes[index2].specified) {	// If the attribute originally had a developer-specified value
							resourceElement.setAttribute(this._resourceQueue[index].attributes[index2].nodeName, this._resourceQueue[index].attributes[index2].nodeValue);	// Set the value of the corresponding attribute on the created resource to the value of the developer-specified value
						}
					}
					// Note: In IE you can't dynamically add a script tag to the DOM using innerHTML it will throw an error you must use text
              	resourceElement.text = this._resourceQueue[index].innerHTML;     // Copy anything contained within the provided element to the created element
					documentHead.appendChild(resourceElement);	// Append the created element to the document head
				}
			} catch (err) {
				this.err = err;	// Set the error so we can perform any additional handling needed
				if (this.exception) { this.exception.fire(); }	// Fire the exception event
			}
		},

		/**
		* @event	Event triggered if there's a script error.
		*/
		exception: new YAHOO.util.CustomEvent("XHRRESPONSE_" + Math.random().toString().replace(/0\./, "") + "_RESOURCES_EXCEPTION"),

		/**
		* @private
		* @property	Queue of resources to load from the response.
		* @type	{string[]}
		*/
		_resourceQueue: new Array()
	};

	/**
	* @property	The pre-processed html property of the JSON object in the response
	* @type	{string}
	*/
	this.responseHtml = null;

	/**
	* @property	The text response entity body returned by the request
	* @type	{string}
	*/
	this.responseText = xhr.responseText;

	/**
	* @property	The type of response returned. A null responseType indicates a malformed response was received.
	* @type	{PAYPAL.Checkout.XhrResponse.types}
	*/
	this.responseType = null;

	/**
	* @property	The document response entity body returned by the request
	* @type	{XmlDocument}
	*/
	this.responseXml = xhr.responseXml;

	/**
	* @property	The HTTP status code returned by the request (established by W3C)
	* @type	{integer}
	*/
	this.status = xhr.status;

	/**
	* @property	The HTTP status text returned by the request
	* @type	{string}
	*/
	this.statusText = xhr.statusText;

	/**
	* @property	The name of the template used to generate the response
	* @type	{string}
	*/
	this.template = null;

	/**
	* @property	The type of response returned
	* @type	{string}
	*/
	this.type = null;

	/**
	* @property	Content updates handled automatically.
	* @type	{object}
	*/
	this.updates = {
		/**
		* @property	Error object. Populated when an exception is fired.
		* @type	{error}
		*/
		err: null,

		/**
		* @property	Array of ids updated.
		* @type	{string[]}
		*/
		list: new Array(),

		/**
		* @method	Adds an item to the queue
		* @returns	{void}
		*
		* @argument	{object} item
		*/
		add: function(item) {
			this._updateQueue.push(item);
		},

		/**
		* @method	Updates all items in the queue.
		* @returns	{void}
		*/
		load: function() {
			try {
				var index, domNode, updateObject;
				for (index = 0; index < this._updateQueue.length; index++) {	// Loop through all the items in the update queue
					updateObject = this._updateQueue[index];	// Get the next update item from the queue
					if (updateObject && updateObject.id && updateObject.id != "") {	// If it's a valid update object
						domNode = document.getElementById(updateObject.id);	// Get the corresponding dom node
						if (domNode) {	// If we got a valid dom node
							this.list.push(updateObject.id);	// Update the list of nodes updated
							if (updateObject.object) {	// If it's an object, insert it in the corresponding location and remove the original
								domNode.parentNode.insertBefore(updateObject.object, domNode.nextSibling);
								domNode.parentNode.removeChild(domNode);
							} else if (updateObject.html) {	// If it's an HTML update, do the update
								domNode.innerHTML = updateObject.html;
							}
						}
					}
				}
				var frms, skip, re_error, addClassFlag, fieldNodeTypeIndex, fieldElements, fieldElementIndex;
				frms = document.getElementsByTagName("form");	// Get all the forms in the document
				fieldNodeTypes = new Array("p", "fieldset");	// Create a list of element types to ignore
				re_error = /\berror\b/;	// Set the Regular Expression for the error class
				for (index = 0; index < frms.length; index++) {	// Loop through all the forms
					addClassFlag = false;	// Reset the addClass flag
					for (fieldNodeTypeIndex = 0; fieldNodeTypeIndex < fieldNodeTypes.length; fieldNodeTypeIndex++) {	// Loop through all the elements to check
						fieldElements = frms.item(index).getElementsByTagName(fieldNodeTypes[fieldNodeTypeIndex]);	// Get a collection of fields
						for (fieldElementIndex = 0; fieldElementIndex < fieldElements.length; fieldElementIndex++) {	// Loop through all the field elements to check for an error
							if (re_error.test(fieldElements.item(fieldElementIndex).className)) {	// If the field element has the error class
								addClassFlag = true;	// Set the class flag to true;
								break;	// Don't bother checking any more elements of that type
							}
						}
						if (addClassFlag) { break; }	// If we have at least one error, don't bother checking further
					}
					if (addClassFlag) {	// If we have at least one error
						if (!re_error.test(frms.item(index).className)) {
							frms.item(index).className += " error";	// Add the 'error' class to the form
						}
					} else {	// Otherwise, we don't have any errors
						frms.item(index).className.replace(re_error, "");	// Remove the 'error' class from the form
					}
				}
			} catch (err) {
				this.err = err;	// Set the error so we can perform any additional handling needed
				if (this.exception) { this.exception.fire(); }	// Fire the exception event
			}
		},

		/**
		* @event	Event triggered if there's a script error.
		*/
		exception: new YAHOO.util.CustomEvent("XHRRESPONSE_" + Math.random().toString().replace(/0\./, "") + "_UPDATES_EXCEPTION"),

		/**
		* @private
		* @property	Queue of content to update from the response.
		* @type	{object[]}
		*/
		_updateQueue: new Array()
	};

	/**
	* @property	The redirection URL contained in the response
	* @type	{string}
	*/
	this.url = null;

	/**
	* @method	Loads updates and resources and executes instructions
	* @returns	{void}
	*/
	this.load = function() {
		me.updates.load();
		me.resources.load();
		me.instructions.load();
		me.loaded.fire();
	},

	/**
	* @event	Fired when an exception is caught.
	*/
	this.exception = new YAHOO.util.CustomEvent("XHRRESPONSE_" + Math.random().toString().replace(/0\./, "") + "_EXCEPTION");

	/**
	* @event	Event triggered when updates are completed
	*/
	this.loaded = new YAHOO.util.CustomEvent("XHRRESPONSE_" + Math.random().toString().replace(/0\./, "") + "_LOADED");

	/**
	* @private
	* @method	Removes the specified node from the source string and returns the modified string
	* @returns	{string}
	*
	* @argument	{node} node
	* @argument	{string} sourceString
	*/
	function _deleteNode(node, sourceString) {
		if (node && node.innerHTML) {	// If the argument is not an empty element
			// The extra effort involved in replacing CR/LF and \s{2,} is because IE handles such things differently than other
			// browsers. If these extra steps are not taken, the indexOf call will fail to find a match.
			var startingPosition, endingPosition, stringToDelete = node.innerHTML;	// Set the string to delete to a string
			sourceString = sourceString.toString().replace(/\t/g, " ").replace(/\r?\n/g, " ").replace(/\s{2,}/g, " ");	// Replace all CR/LF and double-space with single
			stringToDelete = stringToDelete.replace(/\t/g, " ").replace(/\r?\n/g, " ").replace(/\s{2,}/g, " ");	// Replace all CR/LF and double-space with single
			startingPosition = sourceString.indexOf(stringToDelete);	// Find the starting position by looking for the content
			if (startingPosition < 0) {	// If the node is not found in the source
				return sourceString;	// Return the source
			}
			endingPosition = startingPosition;	// Initialize the ending position
			while (startingPosition > -1 && sourceString.substr(startingPosition, node.nodeName.length + 1).toLowerCase() != "<" + node.nodeName.toLowerCase()) { // While we haven't found the element declaration
				startingPosition--;	// Reduce the starting position by 1
			}
			stringToDelete = sourceString.substring(startingPosition, endingPosition) + stringToDelete + "</" + node.nodeName.toLowerCase() + ">";	// Set the string to delete by using the starting and ending positions
			return sourceString.replace(stringToDelete, "");
		} else if (node) {	// If the argument is an empty element
			var nodeIndex = 0, container = document.createElement("div"), attributeIndex, nodes, nodeMatchFlag;
			container.innerHTML = sourceString;	// Create an element using the provided HTML
			nodes = container.getElementsByTagName(node.nodeName);	// Get a list of all the nodes that are the same type as the provided node
			while (nodes.length > nodeIndex) {	// Loop through all the nodes
				nodeMatchFlag = true;	// Reset the node match flag
				for (attributeIndex = 0; attributeIndex < node.attributes.length; attributeIndex++) {	// Loop through all the attributes of the node
					if (node.attributes.item(attributeIndex).specified) {	// If the value of the attribute was specified by the developer
						var a = node.attributes.item(attributeIndex).nodeValue;	// Get the value of the attribute that was set by the developer
						var b = nodes.item(nodeIndex).getAttribute(node.attributes.item(attributeIndex).nodeName);	// Get the value of the node being examined
						if (a != b) {	// If the values do not match
							nodeMatchFlag = false;	// Set the 'not a match' flag
							break;	// Stop evaluating
						}
					}
				}
				if (nodeMatchFlag) {	// If the node matches
					nodes.item(nodeIndex).parentNode.removeChild(nodes.item(nodeIndex));	// Delete the node
				} else {
					nodeIndex++;	// Increment the node index
				}
			}
			return container.innerHTML;	// return the modified html of the container
		} else {
			return sourceString;	// nothing to delete
		}
	}

	/**
	* @private
	* @method	Parses the provided string, removing the script commands to be executed and adding them to the instructions queue before removing them from the string and returning it.
	* @returns	{string}
	*
	* @argument	{string} html
	*/
	function _parseInstructions(html) {
		var nodeList, container = document.createElement("div");
		container.innerHTML = html;	// Create a node
		nodeList = container.getElementsByTagName("ol");	// Get a list of all the OL elements
		if (nodeList.length > 0) {	// If we have OL elements
			var nodeIndex, childNodeIndex, childNodeList;
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the OL elements
				if ((/\bexec\b/).test(nodeList.item(nodeIndex).className)) {	// If it's an executable list
					childNodeList = nodeList.item(nodeIndex).childNodes;	// Get a list of all the child nodes
					for (childNodeIndex = 0; childNodeIndex < childNodeList.length; childNodeIndex++) {	// Loop through all the child nodes
						if (childNodeList.item(childNodeIndex).nodeType == 1) {	// If the child node is an element
							me.instructions.add(childNodeList.item(childNodeIndex).innerHTML.replace(/^window\./, ""));	// Add the item to the instructions queue
						}
					}
					html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
				}
			}
		}
		return html;	// Return the updated string
	}

	/**
	* @private
	* @method	Parses the provided string, removing the resources and adding them to the resource queue before removing them from the string and returning it.
	* @returns	{string}
	*
	* @argument	{string} html
	*/
	function _parseResources(html) {
		var nodeIndex, nodeList, container = document.createElement("div");
		container.innerHTML = html;	// Create an element to use
		nodeList = container.getElementsByTagName("ul");	// Get a list of all the UL nodes
		if (nodeList.length > 0) {	// If we have UL nodes
			var childNodeList, childNodeIndex, scriptElement;
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the UL nodes
				if ((/\blazyloadjs\b/).test(nodeList.item(nodeIndex).className)) {	// If it's a lazyload UL
					childNodeList = nodeList.item(nodeIndex).childNodes;	// Get all the child nodes
					for (childNodeIndex = 0; childNodeIndex < childNodeList.length; childNodeIndex++) {	// Loop through all the child nodes
						if (childNodeList.item(childNodeIndex).nodeType == 1) {	// If the child node is an element
							scriptElement = document.createElement("script");	// Create a script node
							scriptElement.id = childNodeList.item(childNodeIndex).innerHTML.replace(/\./, "_dot_") + "_source";	// Set the id
							scriptElement.src = childNodeList.item(childNodeIndex).innerHTML;	// Set the source to the contents of the element
							scriptElement.type = "text/javascript";	// Set the type
							me.resources.add(scriptElement);	// Add the resource to the queue
						}
					}
					html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
				}
			}
		}
		container.innerHTML = html;	// Update the container
		nodeList = container.getElementsByTagName("script");	// Get all the script elements
		if (nodeList.length > 0) {	// If we have script elements
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the script elements
				me.resources.add(nodeList.item(nodeIndex));	// Add the resource
				html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
			}
		}
		container.innerHTML = html;	// Update the container
		nodeList = container.getElementsByTagName("style");	// Get all the style elements
		if (nodeList.length > 0) {	// If we have style elements
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the style elements
				me.resources.add(nodeList.item(nodeIndex));	// Add the resource to the queue
				html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
			}
		}
		container.innerHTML = html;	// Update the container
		nodeList = container.getElementsByTagName("link");	// Get all the link elements
		if (nodeList.length > 0) {	// If we have link elements
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the link elements
				me.resources.add(nodeList.item(nodeIndex));	// Add the resource to the queue
				html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
			}
		}
		return container.innerHTML;	// Return the updated string
	}

	/**
	* @private
	* @method	Parses the provided string, removing the items to be updated and adding them to the updates queue before removing them from the string and returning it.
	* @returns	{string}
	*
	* @argument	{string} html
	*/
	function _parseUpdates(html) {
		var nodeList, container = document.createElement("div");
		container.innerHTML = html;	// Set the container
		nodeList = container.getElementsByTagName("div");	// Get all the div elements
		if (nodeList.length > 0) {
			var childNodeList, childNodeIndex, documentElement, nodeIndex, sourceElement;
			for (nodeIndex = 0; nodeIndex < nodeList.length; nodeIndex++) {	// Loop through all the div elements
				if ((/\bcontent\-update\b/).test(nodeList.item(nodeIndex).className)) {	// If it's one of many updates
					me.updates.add({id: nodeList.item(nodeIndex).id, html: null, object: nodeList.item(nodeIndex)});	// Add the object to the updates queue
					html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the item
				} else if ((/\bcontent\-update\b/).test(nodeList.item(nodeIndex).id)) {	// If it's a single update
					childNodeList = nodeList.item(nodeIndex).childNodes;	// Get all the child nodes
					for (childNodeIndex = 0; childNodeIndex < childNodeList.length; childNodeIndex++) {	// Loop through all the child nodes
						sourceElement = childNodeList.item(childNodeIndex);	// Set the source element
						if (sourceElement.id && sourceElement.id != "") {	// If the source element is updatable
							documentElement = document.getElementById(sourceElement.id);	// Get the document node to update
							if (documentElement) {	// If the document node exists
								me.updates.add({id: sourceElement.id, html: sourceElement.innerHTML, object: null});	// Add the update to the queue
							}
						}
					}
					html = _deleteNode(nodeList.item(nodeIndex), html);	// Delete the parent item
				}
			}
		}
		return html;	// Return the updated string
	}

	/**
	* @private
	* @method	Exception handler.
	* @returns	{void}
	*
	* @argument	{error} err
	*/
	function _raiseError(err) {
		me.err = err;
		if (_debug) {
			if (console.log) {
				console.log(err.fileName + " " + err.lineNumber + ": " + err.message);
				console.log("Stack trace: " + err.stack);
			} else {
				var msg = "";
				for (var prop in err) {
					msg += prop.toString() + ": " + err[prop].toString() + "\n";
				}
				alert(msg);
			}
		}
		me.exception.fire();
	}

	/**
	* @private
	* @method	Sets the autoSubmit property if the form is found and either the force argument is true or the class of the form contained in the html is auto-submit.
	* @returns	{string}
	*
	* @argument {string} html
	* @argument	{boolean} force
	*/
	function _setType(html, force) {
		var container = document.createElement("div"), frm;
		container.innerHTML = html;	// Set the container
		frm = container.getElementsByTagName("form").item(0);	// Get the first form
		if (frm) {	// If we get a form
			if ((/\bauto-submit\b/i).test(frm.className) || force) {	// If the class on the form contains 'auto-submit' or if we're forcing an automatic submit
				me.autoSubmit = frm;	// Set the autoSubmit value to the form
			}
		}
		return html;
	}

	/**
	* Constructor
	*
	* _debug       {boolean}         Show debug messages - true for paypal.com subdomains.
	*/
	var me = this;
	try {
		this.instructions.exception.subscribe(function() { _raiseError(me.instructions.err); }, this, true);
		this.resources.exception.subscribe(function() { _raiseError(me.resources.err); }, this, true);
		this.updates.exception.subscribe(function() { _raiseError(me.updates.err); }, this, true);

		var _debug = (/www\..{1,}\.paypal.com/i).test(document.URL), responseObject, responseString = (/\s*while\s\(1\)\;\s*(\{.{1,}\})/).exec(this.responseText);
		if (responseString) {
			try {
				responseObject = eval("(" + responseString[1] + ")");
			} catch (evalError) {
				_raiseError(evalError);
				return;
			}
			if (responseObject.html || responseObject.template || responseObject.type || responseObject.url) {	// If we got a valid MiniPage response
				this.html = responseObject.html;
				this.template = (/\/(\w+)\.MiniPage/).test(responseObject.template || "") ? (/\/(\w+)\.MiniPage/).exec(responseObject.template || "")[1] : responseObject.template;
				this.type = (/\bAbort\b/).test(responseObject.template || "") ? "abort" : responseObject.type || "error";
				this.url = responseObject.url;
				this.responseType = PAYPAL.Checkout.XhrResponse.types.JSON;
				this.responseHtml = this.html;

				this.html = _parseResources(this.html);
				this.html = _parseInstructions(this.html);
				this.html = _setType(this.html, (this.type == "auto-submit"));
				this.html = _parseUpdates(this.html);
			}
		} else if ((/^\s*\<\!DOCTYPE\b/).test(this.responseText)) {
			this.responseType = PAYPAL.Checkout.XhrResponse.types.HTML;
			this.responseHtml = this.responseText;
		}
	} catch (err) {
		this.responseType = null;
		_raiseError(err);
	}
};
/**
* Enumerated response types
*
* @author	Robert King
* @version	1.0
*/
PAYPAL.Checkout.XhrResponse.types = {
	HTML: 1,
	JSON: 2
};

/**
* Asynchronous multipage subflow.
* @argument {boolean} autosize
* @argument {string} id
*
* @requires	YAHOO.util.CustomEvent
* @requires	YAHOO.util.Dom
* @requires	YAHOO.util.Event
* @requires	YAHOO.widget.Module
* @requires	YAHOO.widget.Panel
* @requires	PAYPAL.Checkout.Xhr
*
* @author	Robert King
* @version	4.0
*/
PAYPAL.Checkout.Subflow = function(autosize, id) {
	this._i(autosize, id);
};

PAYPAL.Checkout.Subflow.prototype = {
	/**
	* @property	The XHR connection object
	* @type	{PAYPAL.Checkout.Xhr}
	*/
	connection: null,

	/**
	* @property	Display "secure payments" notice.
	* @type	{boolean}
	* @default	true
	*/
	isSecure: true,

	/**
	* @property	Function to call when the close button on the modal window is clicked. The default submits the form contained in the modal window with the CGI variable "cancel.x"
	* @type	{function}
	*/
	onCancel: function() {
		this.connection.send(this._u.form, new Array({name:"cancel.x", value:"cancel.x"}));
		return;
	},

	/**
	* @property	Function to call when the modal window is destroyed. This is a place-holder only, the function should be set by the user.
	* @type	{function}
	*/
	onEnd: function() { return; },

	/**
	* @method	Calls the onCancel function and destroys the panel.
	* @returns	{void}
	*/
	cancel: function() {
		this.onCancel.apply(this, []);
		this._c();
		this.cancelled.fire();
		this.end();
		return;
	},

	/**
	* @method	Destroys the modal window.
	* @returns	{void}
	*/
	end: function() {
		this._c();
		this.onEnd.call();
		if (this.completed && this._u.complete) { this.completed.fire(); }
		this.ended.fire();
		this._u.destroy();
		return;
	},

	/**
	* @method	Gets the number of milliseconds the XmlHttpRequest object will wait for a response.
	* @returns	{number}
	*/
	getTimeout: function() {
		return this.connection.timeout;
	},

	/**
	* @method	Sets the connection to the specified PAYPAL.Checkout.Xhr object
	* @returns	{void}
	* @argument	{PAYPAL.Checkout.Xhr} x
	*/
	open: function(x) {
		this.connection = x;
		if (this.connection.aborted && this.connection.aborted.subscribe) { this.connection.aborted.subscribe(this.cancel, this, true); }
		if (this.connection.loading && this.connection.loading.subscribe) { this.connection.loading.subscribe(this._u._w.stop, this, true); }
		if (this.connection.ready && this.connection.ready.subscribe) { this.connection.ready.subscribe(this._p, this, true); }
		if (this.connection.waiting && this.connection.waiting.subscribe) { this.connection.waiting.subscribe(this._u._w.start, this, true); }
		return;
	},

	/**
	* @method	Sets the number of milliseconds the XmlHttpRequest object will wait for a response.
	* @returns	{void}
	* @argument	{number} m
	*/
	setTimeout: function(m) {
		this.connection.timeout = m;
		return;
	},

	/**
	* @method	Starts the sub-flow using the specified form and starts listening for the hide event of the Panel object and the destroy event of the Panel object. When the Panel object is hidden the onCancel function is called, when the Panel is destroyed the onEnd function is called. Returns true if the subflow handler is started.
	* @returns	{boolean}
 	* @argument	{node|string} f
	* @argument	{object[]} e
	*/
	start: function(f, e) {
		f = (typeof f == "string") ? (f != "") ? document.getElementById(f) : null : f;
		if (!f) { return false; }
		if (this._a(f) || !this.connection.send(f, e)) {
			return false;
		}
		return true;
	},

	/**
	* @event	Fired when the process is cancelled.
	*/
	cancelled: null,

	/**
	* @event	Fired when the process is ended and the last page was identified
	*/
	completed: null,

	/**
	* @event	Fired when the process is ended.
	*/
	ended: null,

	/**
	* @event	Fired when the lightbox is shown
	*/
	shown: null,

	/**
	* @private
	* @method	Checks the specified form to determine whether or not the subflow should be aborted.
	* @returns	{boolean}
	* @argument	{node} f
	*/
	_a: function(f) {
		if (f.getElementById && f.getElementById("abort-subflow")) {
			return true;
		}
		return false;
	},

	/**
	* @private
	* @method	Clears all the event listeners
	* @returns	{void}
	*/
	_c: function() {
		this.connection.aborted.unsubscribeAll();
		this.connection.error.unsubscribeAll();
		this.connection.loading.unsubscribeAll();
		this.connection.ready.unsubscribeAll();
		this.connection.updated.unsubscribeAll();
		this.connection.waiting.unsubscribeAll();
		return;
	},

	/**
	* @private
	* @method	Initializes the modal window and returns true if the initislization is successfull.
	* @returns	{boolean}
	* @argument	{boolean} a
	* @argument	{string} i
	*/
	_i: function(a, i) {
		var s = Math.random().toString().replace(/0\./,"");
		this.cancelled = new YAHOO.util.CustomEvent(s + "_cancelled");
		this.completed = new YAHOO.util.CustomEvent(s + "_completed");
		this.ended = new YAHOO.util.CustomEvent(s + "_ended");
		this.error = new YAHOO.util.CustomEvent(s + "_error");
		this.shown = new YAHOO.util.CustomEvent(s + "_shown");
		this._u._n = (typeof a == "string") ? a : i;
		this._u._a = (typeof a == "boolean") ? a : null;
		this.open(new PAYPAL.Checkout.Xhr(s));
		this._u.canceled.subscribe(this.cancel, this, true);
		this._u.closed.subscribe(this.end, this, true);
		this._u.submitted.subscribe(this._s, this, true);
		this._u.loaded.subscribe(function() { this._u.show(); }, this, true);
		this._u.shown.subscribe(function() { this.shown.fire(); }, this, true);
		return true;
	},

	/**
	* @private
	* @method	Parses the content that is not handled directly.
	* @returns	{void}
	*/
	_p: function() {
		var d = this.connection.getResponse();
		if (d && d.html && d.html != "") {
			if (!this._u.rendered()) { this._u._i(); }
			var b, c, d = d.html, h, o, n;
			o = document.createElement("div");
			o.innerHTML = d;
			n = o.getElementsByTagName("div");
			for (c = 0; c < n.length; c++) {
				if (n.item(c).id == "processingMessage") {
					n = n.item(c);
					var hdr = YAHOO.util.Dom.getElementsByClassName("header", "div", n)[0];
					var bdy = YAHOO.util.Dom.getElementsByClassName("body", "div", n)[0];
					this._u._w.setContent((hdr ? hdr.innerHTML : ""), (bdy ? bdy.innerHTML : ""));
					n.parentNode.removeChild(n);
					break;
				}
			}
			n = YAHOO.util.Dom.getElementsByClassName("jump-back-into-flow", "div", o)[0];
			if (n) {
				this.connection.send();
				return;
			}
			n = YAHOO.util.Dom.getElementsByClassName("messagebox", "div", o)[0];
			if (n) { 
				this._u.setError(n.innerHTML);
			}
			n = YAHOO.util.Dom.getElementsByClassName("header", "div", o)[0];
			if (n) {
				this._u.setHeader(n.innerHTML);
				h = true;
			}
			n = YAHOO.util.Dom.getElementsByClassName("footer", "div", o)[0];
			if (n) {
				this._u.setFooter(n.innerHTML);
			}
			n = YAHOO.util.Dom.getElementsByClassName("body", "div", o)[0];
			if (n) {
				this._u.setBody(n);
				b = true;
			}
			if (h || b) {
				this._u._p.render(document.body);
			}
		}
		return;
	},

	/**
	* @private
	* @method	Event handler to process a form submit in the modal window. This appends the object used to trigger the submit, which is a button in the modal window.
	* @returns	{void}
	*/
	_s: function() {
		var ele = { name: "", value: "" }, btn = this._u.clicked, frm = this._u.form;
		if (btn && btn.name && btn.value) {
			ele.name = btn.name;
			ele.value = btn.name;
			ele.type = ele.type || ((/\bsubmit\b/i).test(btn.className)) ? "submit" : "hidden";
		}
		if ((/\blastpage\b/i).test(frm.className) || !this.connection) {
			this.connection.send(frm, new Array(ele), true);
		} else {
			this.connection.send(frm, new Array(ele));
		}
		return;
	},

	/**
	* @private
	* @property	The user interface
	* @type	{object}
	*/
	_u: {
		/**
		* @property	The button clicked when the clicked event is fired
		* @type	{node}
		*/
		clicked: null,

		/**
		* @property	Whether or not the last page has been reached
		* @type	{boolean}
		*/
		complete: false,

		/**
		* @property	The id of the form to be processed. Set each time a new form is loaded into the modal window.
		* @type	{node}
		*/
		form: null,

		/**
		* @property	The original height of the modal window.
		* @type	{integer}
		*/
		h: null,

		/**
		* @property	The original width of the modal window
		* @type	{integer}
		*/
		w: null,

		/**
		* @method	Destroys the user interface
		* @returns	{void}
		*/
		destroy: function() {
			this.canceled.unsubscribeAll();
			this.closed.unsubscribeAll();
			this.submitted.unsubscribeAll();
			var n = document.getElementById(this._n + "_c");
			if (n && n.parentNode) {
				n.parentNode.removeChild(n);
			}
			var n = document.getElementById(this._n + "_mask");
			if (n && n.parentNode) {
				n.parentNode.removeChild(n);
			}
			return;
		},

		/**
		* @method	Disables the primary button.
		* @returns	{void}
		*/
		disablePrimary: function() {
			var p = this._d();
			if (p) { p.disabled = true; }
			return;
		},

		/**
		* @method	Enables the primary button
		* @returns	{void}
		*/
		enablePrimary: function() {
			var p = this._d();
			if (p) { p.disabled = false; }
			return;
		},

		/**
		* @method	Returns true if the panel is available
		* @returns	{boolean}
		*/
		rendered: function() {
			this._n = this._n || "";
			var o = typeof(this._n) == "string" ? (this._n != "") ? document.getElementById(this._n) : null : this._n;
			return (o ? true : false);
		},

		/**
		* @method	Set body HTML.
		* @returns	{void}
		* @argument	{string|node} html
		*/
		setBody: function(html) {
			if (!html || html == "") {
				this._p.setBody("");
				return;
			} else if (typeof(html) == "string") {
				var e = document.createElement("div");
				e.innerHTML = html;
				html = e;
			}
			var b = document.createElement("div"), c = 0, d = new Array(), i, ix, k, n, s, scope = this;
			this._f = null;
			this._l = null;
			this.complete = this.complete || (/\blastpage\b/i).test(html.className);
			this._c._c = !(/\bhideClose\b/).test(html.className);
			this._c._f = !(/\bhideFooter\b/).test(html.className);

			b.className = "buttons";
			this.form = html.getElementsByTagName("form").item(0);
			var m = YAHOO.util.Dom.getElementsByClassName("messagebox", "div", html)[0];
			if (m) {
				m.parentNode.removeChild(m);
			}
			n = new Array();
			for (c = 0; c < this.form.elements.length; c++) {
				i = this.form.elements[c]
				if (i.type == "button" || i.type == "reset" || i.type == "submit") {
					k = ((/\bdummy\b/i).test(i.id) || (/\bdummy\b/i).test(i.name) || (/\bnoscript\b/i).test(i.className));
					if (!k) {
						k = i.cloneNode(true);
						b.appendChild(k);
						YAHOO.util.Event.addListener(k, "click", function(e) { scope._b(e); }, this, true);
					}
					n.push(i);
				}
			}
			for (c = 0; c < n.length; c++) {
				n[c].parentNode.removeChild(n[c]);
			}
			this._p.setBody(html);
			this._p.body.appendChild(b);
			k = this._p.body.getElementsByTagName("iframe");
			if (k.length > 0) {
				this._h();
			}
			k = this._p.body.getElementsByTagName("a");
			for (c = 0; c < k.length; c++) {
				n = k.item(c);
				if ((/\bsubmit\b/i).test(n.className)) {
					n.href = "javascript:void(0)";
					n.onclick = "return false;"
					YAHOO.util.Dom.removeClass(n, "hide");
					YAHOO.util.Event.addListener(n, "click", function(e) { scope._b(e); }, this, true);
				}
			}
			if (this.form) {
				this.complete = this.complete || (/\blastpage\b/i).test(this.form.className);
				for (c = 0; c < this.form.elements.length; c++) {
					n = this.form.elements[c];
					switch (n.tagName.toLowerCase()) {
						case "fieldset":
						case "legend":
						case "button":		break;
						case "select":		if (n.style.visibility != "hidden") {
										if (!this._f) { this._f = n; }
										this._l = n;
									}
									break;
						case "input":		if (n.type == "text") {
										n.defaultValue = n.value;
										if (n.defaultValue && n.defaultValue != "") { YAHOO.util.Dom.addClass(n, "is-default-value"); }
										YAHOO.util.Event.addListener(n, "blur", function() { if (this.defaultValue && this.defaultValue == this.value) { YAHOO.util.Dom.addClass(this, "is-default-value"); } else { YAHOO.util.Dom.removeClass(this, "is-default-value"); }}, n, true);
										YAHOO.util.Event.addListener(n, "focus", function() { YAHOO.util.Dom.removeClass(this, "is-default-value"); if ((/\bclearOnFocus\b/i).test(this.className)) { this.value = ""; } else { this.select(); } }, n, true);
									}
									if (n.type != "hidden" && n.style.visibility != "hidden") {
										if (!this._f) { this._f = n; }
										this._l = n;
									}
									break;
						case "textarea":	n.defaultValue = n.value;
									if (n.defaultValue && n.defaultValue != "") { YAHOO.util.Dom.addClass(n, "is-default-value"); }
									YAHOO.util.Event.addListener(n, "blur", function() { if (this.defaultValue && this.defaultValue == this.value) { YAHOO.util.Dom.addClass(this, "is-default-value"); } else { YAHOO.util.Dom.removeClass(this, "is-default-value"); }}, n, true);
									YAHOO.util.Event.addListener(n, "focus", function() { YAHOO.util.Dom.removeClass(this, "is-default-value"); if ((/\bclearOnFocus\b/i).test(this.className)) { this.value = ""; } else { this.select(); } }, n, true);
									if (n.style.visibility != "hidden") {
										if (!this._f) { this._f = n; }
										this._l = n;
									}
									break;
					}
				}
			}
			YAHOO.util.Dom.addClass(this._p.body, "body");
			if (this._e.t.length == 0) {
				this.loaded.fire();
			}
			return;
		},

		/**
		* @method	Sets the error message.
		* @returns	{void}
		* @argument	{string} html
		*/
		setError: function(html) {
			var o = document.createElement("div");
			o.className = "messageBox error";
			o.innerHTML = typeof(html) == "string" ? html : html.innerHTML ? html.innerHTML : "";
			this._p.body.insertBefore(o, this._p.body.firstChild);
			return;
		},

		/**
		* @method	Set footer HTML
		* @returns	{void}
		* @argument	{string} html
		*/
		setFooter: function(html) {
			var default_msg = new Array('<div class="paypal"></div>');
			if (this.isSecure) {
				default_msg.push('<div class="secure"></div>') ;
				var oSecureText = document.getElementById("secure-payment-notice");
				default_msg.push(oSecureText ? oSecureText.innerHTML : "");
			}
			html = html || default_msg.join(" ");
			html = typeof(html) == "string" ? html : html.innerHTML ? html.innerHTML : "";
			this._p.setFooter(html);
			YAHOO.util.Dom.addClass(this._p.footer, "footer");
			return;
		},

		/**
		* @method	Set header HTML
		* @returns	{void}
		* @argument	{string} html
		*/
		setHeader: function(html) {
			html = typeof(html) == "string" ? html : html.innerHTML ? html.innerHTML : "";
			this._p.setHeader(html);
			YAHOO.util.Dom.addClass(this._p.header, "header");
			return;
		},

		/**
		* @method	Show the modal window on the page
		* @returns	{void}
		*/
		show: function() {
			this._m();
			this._p.cfg.setProperty("zindex", 99);
			this._p.show();

			if (this._p.browser == "ie") {
				if (this._s == null) {
					this._s = [];
				}
				var sel = document.getElementsByTagName("select");
				for (var i = 0; i < sel.length; i++) {
					if (!YAHOO.util.Dom.isAncestor(this._n, sel[i]) && !YAHOO.util.Dom.hasClass(sel[i], "accessAid")) {
						YAHOO.util.Dom.addClass(sel[i], "accessAid");
						this._s[this._s.length] = sel[i];
					}
				}
			}

			var buttons = YAHOO.util.Dom.getElementsByClassName("buttons", "div", this._p.body)[0]
			this._p.close.style.visibility = (this._c._c) ? "visible" : "hidden";
			this._p.footer.style.visibility = (this._c._f) ? "visible" : "hidden";
			this._f = this._f ? this._f : (buttons.hasChildNodes) ? buttons.firstChild : YAHOO.util.Dom.getElementsByClassName("header", "div", this._p.element)[0];
			this._f.focus();
			this._l = (buttons.hasChildNodes) ? buttons.lastChild : this._l;
			if (this._f && this._l && this._p.setTabLoop) {
				this._p.setTabLoop(this._f, this._l);
			}
			this.shown.fire();
			return;
		},

		/**
		* @event	Fired when a cancel button is clicked.
		*/
		canceled: new YAHOO.util.CustomEvent("panelcanceled"),

		/**
		* @event	Fired when the closer button is clicked.
		*/
		closed: new YAHOO.util.CustomEvent("panelclosed"),

		/**
		* @event	Fired when the UI is completely loaded
		*/
		loaded: new YAHOO.util.CustomEvent("panelloaded"),

		/**
		* @event	Fired when the UI is shown
		*/
		shown: new YAHOO.util.CustomEvent("panelshown"),

		/**
		* @event	Fired when the closer button is clicked.
		*/
		submitted: new YAHOO.util.CustomEvent("panelsubmitted"),

		/**
		* @private
		* @property	Automatically resize the body of the panel and auto-scroll to handle small viewports. Can be set to true by the _i method.
		* @type	{boolean}
		*/
		_a: false,

		/**
		* @private
		* @method	Button click handler
		* @returns	{void}
		* @argument	{event} e
		*/
		_b: function(e) {
			this.clicked = YAHOO.util.Event.getTarget(e, true);
			this.form = (this.form.id != "") ? document.getElementById(this.form.id) : this.form;
			if (this.clicked.tagName.toLowerCase() == "a") {
				this.clicked.value = this.clicked.innerHTML;
				this.clicked.type = (/\bsubmit\b/i).test(this.clicked.className) ? "submit" : "cancel";
			}
			if (this.clicked.type != "submit" || (/\bcancel\b/i).test(this.clicked.className)) {
				this.canceled.fire();
			} else {
				this.submitted.fire();
			}
			return;
		},

		/**
		* @private
		* @property	Configuration settings of which elements to display
		* @type	{object}
		*/
		_c: {
			/**
			* @private
			* @property	Whether or not to show the close button
			* @type	{boolean}
			*/
			_c: true,

			/**
			* @private
			* @property	Whether or not to show the footer
			* @type	{boolean}
			*/
			_f: true
		},

		/**
		* @private
		* @method	Returns the default button
		* @returns	{node}
		*/
		_d: function() {
			if (!this._p || !this._p.body) { return; }
			var c, d = this._p.body.getElementsByTagName("div"), n;
			for (c = 0; c < d.length; c++) {
				if ((/\bbuttons\b/).test(d.item(c).className)) {
					var p, b = d.item(c);
					for (n = 0; n < b.childNodes.length; n++) {
						p = (/\bprimary\b/).test(b.childNodes[n].className) ? b.childNodes[n] : p ? p : b.childNodes[n];
					}
					return p;
				}
			}
			return;
		},

		/**
		* @private
		* @property	IFrame loading monitor
		* @type	{object}
		*/
		_e: {
			/**
			* @method	Kill the timer
			* @returns	{void}
			* @argument	{integer} i
			*/
			k: function(i) {
				window.clearInterval(this.t[i]);
				this.t.splice(i);
			},

			/**
			* @property	IFrame interval timers
			* @type	{integer}
			*/
			t: []
		},

		/**
		* @private
		* @property	First element
		* @type	{node}
		*/
		_f: null,

		/**
		* @private
		* @method	IFrame status update
		* @returns	{void}
		* @argument	{integer} i
		*/
		_g: function(i) {
			var b = true, c;
			this._e.t[i] = true;
			for (c = 0; c < this._e.t.length; c++) {
				b = !this._e.t[c] ? false : b;
			}
			if (b) {
				this.loaded.fire();
			}
			return
		},

		/**
		* @private
		* @method	Handle the iframe loading
		* @returns	{void}
		*/
		_h: function() {
			var c, i, k, s;
			k = this._p.body.getElementsByTagName("iframe");
			for (c = 0; c < k.length; c++) {
				i = c;
				this._e.t[i] = false;
				s = this;
				if (k.item(c).addEventListener) {
					k.item(c).addEventListener("load", function() { s._g.apply(s, [i]); }, false);
				} else if (k.item(c).attachEvent) {
					k.item(c).attachEvent("onload", function() { s._g.apply(s, [i]); });
				}
			}
			return;
		},

		/**
		* @private
		* @method	Initialize
		* @returns	{void}
		*/
		_i: function() {
			this._n = this._n || "mw_" + Math.random().toString().replace(/0\./, "");
			YAHOO.widget.Module.CSS_HEADER = "header";
			YAHOO.widget.Module.CSS_BODY = "body";
			YAHOO.widget.Module.CSS_FOOTER = "footer";
			this._p = new YAHOO.widget.Panel(this._n, { modal: true, close: true, draggable: false, fixedcenter: false, postmethod: "form" });
			this._p.changeContentEvent.subscribe(this._m, this, true);
			var KEY_ESC = new YAHOO.util.KeyListener(document, { keys:27 }, {fn: function() { this.canceled.fire(); }, scope: this, correctScope: true}, "keyup");
			var KEY_ENTER = new YAHOO.util.KeyListener(document, { keys:13 }, {fn: function() { this.submitted.fire(); }, scope: this, correctScope: true}, "keyup");
			this._p.cfg.queueProperty("keylisteners", [KEY_ESC, KEY_ENTER]);
			this._p.render(document.body);
			this._p.setHeader("");
			this._p.setBody("");
			this._p.setFooter("");
			this._p.element.className = this._p.element.className + (!(/\blightbox\b/i).test(this._p.element.className) ? " lightbox" : "");
			YAHOO.util.Event.addListener(this._p.close, "click", function() { this.closed.fire(); }, this, true);
			this._p.hide();
			this._w._i(this._p);
			this._v._i();

			this.canceled.subscribe(function() { this.destroy(); }, this, true);
			this.closed.subscribe(function() { this.destroy(); }, this, true);
			return;
		},

		/**
		* @private
		* @property	Last element
		* @type	{node}
		*/
		_l: null,

		/**
		* @private
		* @method	Resizes and aligns the modal window. Called when changeContentEvent occurs and by the _show method.
		* @returns	{void}
		*/
		_m: function() {
			if (this._a) {
				if (this._p.element && this._p.body) {
					this._p.body.style.overflowY = "auto";
					this._p.body.style.overflowX = "hidden";
					if (this._p.element.offsetHeight >= this._v.h) {
						this._p.body.style.height = Math.floor(this._v.h * .7) + "px";
					} else if (this._p.element.offsetHeight < this._v.h) {
						this._p.element.style.height = this._p.element.offsetHeight + "px";
						this._p.body.style.height = "auto";
					} else {
						this._p.body.style.height = "auto";
					}
					if (this._p.element.offsetWidth >= this._v.w) {
						this._p.element.style.width = Math.floor(this._v.w * .7) + "px";
						this._p.body.style.overflowX = "scroll";
					} else if (this._p.element.offsetWidth < this._v.w) {
						this._p.element.style.width = this._p.element.offsetWidth + "px";
					} else {
						this._p.element.style.width = "auto";
					}
					this.h = (this._p.element) ? this._p.element.offsetHeight : 500;
					this.w = (this._p.element) ? this._p.element.offsetWidth : 440;				}
			} else {
				this.h = (this._p.element) ? this._p.element.offsetHeight : 500;
				this.w = (this._p.element) ? this._p.element.offsetWidth : 440;
			}
			this._p.sizeUnderlay();
			this._p.element.style.left = Math.max(Math.floor((this._v.w - this.w)/2), 2).toString() + "px";
			this._p.element.style.top = Math.max(Math.floor((this._v.h - this.h)/2), 2).toString() + "px";
			return;
		},

		/**
		* @private
		* @property	The DOM Node that is the modal window.
		* @type	{node}
		*/
		_n: null,

		/**
		* @private
		* @property	The YAHOO panel widget that serves as the modal window.
		* @type	{YAHOO.widget.Panel}
		*/
		_p: null,

		/**
		* @private
		* @property	For IE6 and below, <select> elements have to be hidden.
		* @type	{node[]}
		*/
		_s: null,

		/**
		* @private
		* @property	The user's viewport
		* @type	{object}
		*/
		_v: {
			/**
			* @property	Height of the viewport
			* @type	{int}
			*/
			h: null,

			/**
			* @property	Width of the viewport
			* @type	{int}
			*/
			w: null,

			/**
			* @private
			* @method	Initializes the size of the viewport
			* @returns	{void}
			*/
			_i: function() {
				this.w = parseFloat(window.innerWidth ? window.innerWidth : (document.documentElement.clientWidth ? document.documentElement.clientWidth : document.getElementsByTagName('body').item(0).clientWidth));
				this.h = parseFloat(window.innerHeight ? window.innerHeight : (document.documentElement.clientHeight ? document.documentElement.clientHeight : document.getElementsByTagName('body').item(0).clientHeight));
				return;
			}
		},

		/**
		* @private
		* @property	Wait object
		* @type	{object}
		*/
		_w: {
			/**
			* @method	Sets the content.
			* @returns	{void}
			* @argument	{string} head
			* @argument	{string} body
			*/
			setContent: function(head, body) {
				this._h = (head && head != "" ? head : null);
				this._b = (body && body != "" ? body : null);
				return;
			},

			/**
			* @method	Starts the wait process.
			* @returns	{void}
			*/
			start: function() {
				var scope = (this._w) ? this._w : this;
				document.body.style.cursor = "wait";
				if (scope._h || scope._b) {
					if (!scope._h) {
						scope._h = "";
					}
					scope._p.close.style.visibility = "hidden";
					scope._p.footer.style.visibility = "hidden";
					scope._p.setHeader(scope._h);
					scope._p.setBody(scope._b);
					scope._p.header.setAttribute("tabindex", "-1");
					scope._p.header.focus();
					scope._p.center();
					scope._p.show();
				}
				return;
			},

			/**
			* @method	Stops the wait process.
			* @returns	{void}
			*/
			stop: function() {
				document.body.style.cursor = "default";
				return;
			},

			/**
			* @property	The HTML for the body.
			* @type	{string}
			*/
			_b: null,

			/**
			* @property	The HTML for the head.
			* @type	{string}
			*/
			_h: null,

			/**
			* @property	The YAHOO.widget.Panel object used for the wait window.
			* @type	{YAHOO.widget.Panel}
			*/
			_p: null,

			/**
			* @method	Initializes the wait panel.
			* @returns	{boolean}
			* @argument	{YAHOO.widget.Panel} panel
			*/
			_i: function(panel) {
				try {
					this._b = null;
					this._h = null;
					this._p = panel;
				} catch(err) {
					return false;
				}
				return true;
			}
		}
	}
};

