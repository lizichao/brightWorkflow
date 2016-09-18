 Ext.namespace('Ext.ux.ManagedIFrame');

Ext.ux.ManagedIFrame.Element = Ext.extend(Ext.Element, {         
    src     : null,                      
    constructor : function(element, forceNew, doc ){
        var d = doc || document,
        	dom = element;//Ext.getDom(element, false, d);
        
        if(!dom || !(/^(iframe|frame)/i).test(dom.tagName)) { // invalid id/element
            return null;
        }
        var id = Ext.id(dom);                
        this.dom = dom;                
        this.id = id ;                
        this.dom.name || (this.dom.name = this.id);                 
        if(Ext.isIE){
             document.frames && (document.frames[this.dom.name] || (document.frames[this.dom.name] = this.dom));
         }                 
        this.dom.ownerCt = this;
    },
    destructor   :  function () {
        this.removeAllListeners();
        this.reset(); 
        this.dom.ownerCt = null;
    },      
    remove  : function(){
        this.destructor.apply(this, arguments);
        Ext.ux.ManagedIFrame.Element.superclass.remove.apply(this,arguments);
    },            
    getDocument : function(){ return this.dom ? this.dom.ownerDocument : document;},            
    resetUrl : (function(){
        return Ext.isIE && Ext.isSecure ? Ext.SSL_SECURE_URL : 'about:blank';
    })(),
    setSrc : function(url, discardUrl, callback, scope) {
        this.src = url;
        this.dom.src = url;
        return this;
    },
    setLocation : function(url, discardUrl, callback, scope) {
        this.getWindow().location.replace(s);
        return this;
    },
    reset : function(src, callback, scope) {                
        var s = src, 
            win = this.getWindow();
            
        if(win){
            Ext.isFunction(s) && ( s = src());
            s = this._targetURI = Ext.isEmpty(s, true)? this.resetUrl: s;
            win.location&&(win.location.href = s);
        }
        
        return this;
    },
    get : function(el) {
        var doc = this.getFrameDocument();
        return doc? Ext.get(el, doc) : doc=null;
    },
    fly : function(el, named) {
        var doc = this.getFrameDocument();
        return doc ? Ext.fly(el, named, doc) : null;
    },
    getDom : function(el) {
        var d;
        if (!el || !(d = this.getFrameDocument())) {
            return (d=null);
        }
        return Ext.getDom(el, d);
    },
    getFrameDocument : function() {
        var win = this.getWindow(), doc = null;
        try {
            doc = (Ext.isIE && win ? win.document : null)
                    || this.dom.contentDocument
                    || window.frames[this.dom.name].document || null;
        } catch (gdEx) {
            return false;
        }                
        return doc;
    },
    getDoc : function() {
        return this.getFrameDocument(); 
    },            
    getBody : function() {
        var d;
        return (d = this.getFrameDocument()) ? (d.body || d.documentElement) : null;
    },
    getDocumentURI : function() {
        var URI, d;
        try {
            URI = this.src && (d = this.getFrameDocument()) ? d.location.href: null;
        } catch (ex) {
        }
        return URI || (Ext.isFunction(this.src) ? this.src() : this.src);
    },
    getWindowURI : function() {
        var URI, w;
        try {
            URI = (w = this.getWindow()) ? w.location.href : null;
        } catch (ex) {
        }
        return URI || (Ext.isFunction(this.src) ? this.src() : this.src);
    },
    getWindow : function() {
        var dom = this.dom, win = null;
        try {
            win = dom.contentWindow || window.frames[dom.name] || null;
        } catch (gwEx) {}
        return win;
    },
    removeAllListeners : function(){
        Ext.EventManager.removeAll(this.dom);
        return this;
    }
});

Ext.ux.ManagedIFrame.ComponentAdapter = function(){}; 
Ext.ux.ManagedIFrame.ComponentAdapter.prototype = {       
    defaultSrc : null,
    frameConfig  : null,
    frameEl : null,   
    autoScroll: true,        
    getId : function(){
         return this.id   || (this.id = "mif-comp-" + (++Ext.Component.AUTO_ID));
    },
    setAutoScroll : function(auto){
        var scroll = Ext.value(auto, this.autoScroll === true);
        this.rendered && this.getFrame() &&  
            this.frameEl.setOverflow( (this.autoScroll = scroll) ? 'auto':'hidden');
        return this;
    },
    getFrame : function(){
         if(this.rendered){
            if(this.frameEl){ return this.frameEl;}
            var f = this.items && this.items.first ? this.items.first() : null;
            f && (this.frameEl = f.frameEl);
            return this.frameEl;
         }
         return null;
    },        
    getFrameWindow : function() {
        return this.getFrame() ? this.frameEl.getWindow() : null;
    },
    getFrameDocument : function() {
        return this.getFrame() ? this.frameEl.getFrameDocument() : null;
    },
    getFrameDoc : function() {
        return this.getFrame() ? this.frameEl.getDoc() : null;
    },
    getFrameBody : function() {
        return this.getFrame() ? this.frameEl.getBody() : null;
    },        
    resetFrame : function() {
        this.getFrame() && this.frameEl.reset.apply(this.frameEl, arguments);
        return this;
    },
    getUpdater : function() {
        return this.getFrame() ? this.frameEl.getUpdater() : null;
    },        
    setSrc : function(url, discardUrl, callback, scope) {
        this.getFrame() && this.frameEl.setSrc.apply(this.frameEl, arguments);
        return this;
    },
    setLocation : function(url, discardUrl, callback, scope) {
       this.getFrame() && this.frameEl.setLocation.apply(this.frameEl, arguments);
       return this;
    }
};
   
Ext.ux.ManagedIFrame.Component = Ext.extend(Ext.BoxComponent , {             
    ctype     : "Ext.ux.ManagedIFrame.Component", 
    onRender : function(ct, position){                
        var frCfg = this.frameCfg || this.frameConfig || (this.relayTarget ? {name : this.relayTarget.id}: {}) || {};
        
        var frDOM = frCfg.autoCreate || frCfg;
        frDOM = Ext.apply({tag  : 'iframe', id: Ext.id()}, frDOM);
        
        var el = Ext.getDom(this.el);

        (el && el.tagName == 'iframe') || 
          (this.autoEl = Ext.apply({
                            name : frDOM.id,
                            frameborder : 0
                           }, frDOM ));
         
        Ext.ux.ManagedIFrame.Component.superclass.onRender.apply(this, arguments);
       
        var frame = this.el ;        
        var F;
        if( F = this.frameEl = (this.el ? new Ext.ux.ManagedIFrame.Element(this.el.dom, true): null)){            
            Ext.apply(F,{
                ownerCt          : this.relayTarget || this
            });
            F.ownerCt.frameEl = F;
            F.addClass('ux-mif');             
            delete this.contentEl;                    
         }
    },
    afterRender  : function(container) {
        Ext.ux.ManagedIFrame.Component.superclass.afterRender.apply(this,arguments);
        
        if (this.fitToParent && !this.ownerCt) {
            var pos = this.getPosition(), size = (Ext.get(this.fitToParent)
                    || this.getEl().parent()).getViewSize();
            this.setSize(size.width - pos[0], size.height - pos[1]);
        }

        this.getEl().setOverflow('hidden'); //disable competing scrollers
        this.setAutoScroll();
        var F;
        if(F = this.frameEl){
            if (this.defaultSrc) {
                    F.setSrc(this.defaultSrc, false);
            }
        }
    },    
    beforeDestroy : function() {
        var F;
        if(F = this.getFrame()){
            F.remove();
            this.frameEl = null;
        }
        this.relayTarget && (this.relayTarget.frameEl = null);
        Ext.ux.ManagedIFrame.Component.superclass.beforeDestroy.call(this);
    }
});
Ext.override(Ext.ux.ManagedIFrame.Component, Ext.ux.ManagedIFrame.ComponentAdapter.prototype);
Ext.reg('mif', Ext.ux.ManagedIFrame.Component);

function embed_MIF(config){    
	config || (config={});
	config.layout = 'fit';
	config.items = {
		xtype    : 'mif',
		ref    : 'mifChild',
		autoScroll : Ext.value(config.autoScroll , this.autoScroll),
		defaultSrc  : Ext.value(config.defaultSrc , this.defaultSrc),
		frameConfig : Ext.value(config.frameConfig || config.frameCfg , this.frameConfig),
		relayTarget : this
	};
	delete config.html;
	delete config.data;
	return config; 
};
    
Ext.ux.ManagedIFrame.Panel = Ext.extend( Ext.Panel , {
    ctype       : 'Ext.ux.ManagedIFrame.Panel',
    bodyCssClass: 'ux-mif-mask-target',
    constructor : function(config){
        Ext.ux.ManagedIFrame.Panel.superclass.constructor.call(this, embed_MIF.call(this, config));
    }
});
  
Ext.override(Ext.ux.ManagedIFrame.Panel, Ext.ux.ManagedIFrame.ComponentAdapter.prototype);
Ext.reg('iframepanel', Ext.ux.ManagedIFrame.Panel);