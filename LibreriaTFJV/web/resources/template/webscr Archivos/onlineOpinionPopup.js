/*
Spec #22956 OpinionLab
*/
var getOpinionLabURL = 0;
var ffX = 0;
var ffY = 0;

function OpinionLabOnCloseEvent(redirectTo){
	/* Auto popup of comment card when window closes. This is temporarily
	 * disabled as per PPSCR01204564 - tsullivan 10/06/2010 */
	return 0;

	if (navigator.userAgent.indexOf('Firefox/3')!== -1) {
		window.onbeforeunload = function() {
			if (ffX <=0 || ffY >= 0) {
				showpopup(redirectTo);
			}
		}
	} else {
		window.onunload = function () {
			if (navigator.appName=="Microsoft Internet Explorer") {
				var evt = window.event;
				if(navigator.userAgent.indexOf('MSIE 6.0')!== -1){
					if (evt.clientX < 0 && evt.clientY < 0) {
						showpopup(redirectTo);
					}
				} else {
					if (evt.clientX < 0 || evt.clientY < 0) {
						showpopup(redirectTo);
					}
				}
			} else {
				showpopup(redirectTo);
			}
		}
	}
	window.onmousemove = function(e) {
		e = window.event ? window.event : e;
		ffX = e.clientX;
		ffY = e.clientY;
	};
	
}
					
function showpopup(redirectTo){
	var mywin;	
	mywin = window.open('','', 'top=3000,left=3000,width=1,height=1,menubar=0,scrollbars=0,resizeable=1');
	if (mywin) {
		mywin.document.open;
		var myURL = ""
		HTML_txt = "<html><scr" + "ipt type='text/javascript' src='/js/opinionlab/oo_engine.js'></scr" + "ipt><scr" + "ipt language='javascript'>";
		HTML_txt = HTML_txt + "_hr='"+_hr_temp+"';";
		HTML_txt = HTML_txt + "_ht='"+_ht_temp+"';";
		HTML_txt = HTML_txt + "custom_var='"+custom_var_temp+"';";
			
		if ((typeof baseurl == 'undefined')){}else{HTML_txt = HTML_txt + "baseurl='"+baseurl+"';";}
		if ((typeof url_var == 'undefined')){}else{HTML_txt = HTML_txt + "url_var='"+url_var+"';";}
		if(document.all){getOpinionLabURL = 1;}else{getOpinionLabURL = 0;}
			
		if(redirectTo=='ppwebscr'){
			if(document.all){
				myURL = PP_O_LC();
			}
			HTML_txt = HTML_txt + "function connect(){try{if(document.all){if(window.opener.closed){window.location.replace('" + myURL + "');}else{self.close();}}else{if(opener == null){PP_O_LC();setTimeout('self.close()', 5000);}else{self.close();}}}catch(err){window.location.replace('" + myURL + "');}return 0;}</scr";
			HTML_txt = HTML_txt + "ipt><body><scr" + "ipt language='javascript'>setTimeout('connect()',1);</scr" + "ipt></body></html>";
		}else{
			if(document.all){myURL = O_LC();}
			HTML_txt = HTML_txt + "function connect(){try{if(document.all){if(window.opener.closed){window.location.replace('" + myURL + "');}else{self.close();}}else{if(opener == null){O_LC();setTimeout('self.close()', 5000);}else{self.close();}}}catch(err){window.location.replace('" + myURL + "');}return 0;}</scr";
			HTML_txt = HTML_txt + "ipt><body><scr" + "ipt language='javascript'>setTimeout('connect()',1);</scr" + "ipt></body></html>";
		}
		mywin.document.write(HTML_txt);
		mywin.document.close;
	}					
}

