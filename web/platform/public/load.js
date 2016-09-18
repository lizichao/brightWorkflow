function loadjs(src)
{
	var script=document.getElementsByTagName("SCRIPT");
	for(var i=0;i<script.length;i++)
	{
		var s=script[i].src.toLowerCase();
		var _id=script[i].id;
		if(s==src.toLowerCase()||s==_id){
			//alert(s);
			return;
		}
	}
	document.write('<script src="'+src+'"></script>');
};
function loadjstext(_response, _opts){		
	var s=_response.responseText;
    if(null!=s && ''!=s)
    {
    	var oHead = document.getElementsByTagName('HEAD').item(0); 
        var oScript = document.createElement( "SCRIPT" ); 
        oScript.language = "javascript"; 
        oScript.type = "text/javascript"; 
        oScript.defer = true;
        oScript.id = _opts.url;
        oScript.text = s;
        oHead.appendChild(oScript);
        //alert(window.eval);
        //window.eval(s);
    }
};
function loadjsErr(_res,_opt){}
function loadjs2(src)
{
	var script=document.getElementsByTagName("SCRIPT");
	for(var i=0;i<script.length;i++)
	{
		var s=script[i].src.toLowerCase();
		var d=script[i].id;
		//if(s==src.toLowerCase()||d==src){return;}
	}
	
	Ext.Ajax.request({
        url: src,
        method: 'GET',
        success: loadjstext,
        failure: loadjsErr,
        async: false
    });
};