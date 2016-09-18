var ua = navigator.userAgent.toLowerCase(),
    iecheck = function(r){return r.test(ua);};
var isIE = !iecheck(/opera/) && iecheck(/msie/);
function createXMLDoc(){
    var xmlDoc;
    if (isIE) {
        xmlDoc = new ActiveXObject("MSXML2.DOMDocument.3.0");
        xmlDoc.async = false;
        while (xmlDoc.readyState != 4) {};
    }
    else{
    	xmlDoc = document.implementation.createDocument("", "", null);
    }
    return xmlDoc;
}
function getElementValue(item){
    if(isIE){
        return item.text;
    }else{
        return item.childNodes[0].nodeValue;
    }
}
if ( ! isIE){
      XMLDocument.prototype.loadXML = function(xmlString)
     {
        var childNodes = this.childNodes;
        for (var i = childNodes.length - 1; i >= 0; i--)
            this.removeChild(childNodes[i]);

        var dp = new DOMParser();
        var newDOM = dp.parseFromString(xmlString, "text/xml");
        var newElt = this.importNode(newDOM.documentElement, true);
        this.appendChild(newElt);
     };

    // check for XPath implementation
    if( document.implementation.hasFeature("XPath", "3.0"))
     {
       // prototying the XMLDocument
        XMLDocument.prototype.selectNodes = function(cXPathString, xNode)
        {
          if( !xNode ) { xNode = this; }
          var oNSResolver = this.createNSResolver(this.documentElement)
          var aItems = this.evaluate(cXPathString, xNode, oNSResolver,
                        XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null)
          var aResult = [];
          aResult.length = aItems.snapshotLength;
          for( var i = 0; i < aItems.snapshotLength; i++)
           {
              aResult[i] =   aItems.snapshotItem(i);
           }
           aResult.item=function(index){
            return aResult[index]
           }
          return aResult;
        }

       // prototying the Element
        Element.prototype.selectNodes = function(cXPathString)
        {
          if(this.ownerDocument.selectNodes)
           {
             return this.ownerDocument.selectNodes(cXPathString, this);
           }
          else{throw "For XML Elements Only";}
        }
        // prototying the XMLDocument
        XMLDocument.prototype.selectSingleNode = function(cXPathString, xNode)
        {
          if( !xNode ) { xNode = this; }
          var xItems = this.selectNodes(cXPathString, xNode);
          if( xItems.length > 0 )
           {
             return xItems[0];
           }
          else
           {
             return null;
           }
        }

       // prototying the Element
        Element.prototype.selectSingleNode = function(cXPathString)
        {
          if(this.ownerDocument.selectSingleNode)
           {
             return this.ownerDocument.selectSingleNode(cXPathString, this);
           }
          else{throw "For XML Elements Only";}
        }
     }
}