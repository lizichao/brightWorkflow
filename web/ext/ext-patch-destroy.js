// ******************************* Memory Release *****************************
Ext.override(Ext.chart.Chart, {
    onDestroy: function(){
        this.bindStore(null);
        var tip = this.tipFnName;
        if(!Ext.isEmpty(tip)){
            delete window[tip];
        }
        Ext.chart.Chart.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.Component,{
    onDestroy   :   function(){
        if(this.plugins){
            Ext.destroy(this.plugins);
        }
        Ext.destroy(this.el);
    }
});

Ext.override(Ext.Panel,{
    onDestroy : function(){
        Ext.destroy(
            this.header,
            this.tbar,
            this.bbar,
            this.footer,
            this.body,
            this.bwrap,
            this.dd
        );
        Ext.Panel.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.dd.DragDrop,{
    onDestroy   :   Ext.emptyFn,
    destroy : function(){
        this.onDestroy();
        this.unreg();
    }
});

Ext.override(Ext.dd.DragSource,{
    onDestroy   :   function(){
        Ext.destroy(this.proxy);
        Ext.dd.DragSource.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.grid.GridDragZone,{
    onDestroy   :   function(){
        Ext.destroy(this.ddel);
        Ext.grid.GridDragZone.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.dd.StatusProxy,{
    onDestroy   :   Ext.emptyFn,
    destroy : function(){
        this.onDestroy();
        Ext.destroy(this.anim,this.el,this.ghost);
    }
});

Ext.override(Ext.grid.GridView,{
    destroy : function(){
        if(this.colMenu){
            Ext.menu.MenuMgr.unregister(this.colMenu);
            this.colMenu.destroy();
        }
        if(this.hmenu){
            Ext.menu.MenuMgr.unregister(this.hmenu);
            this.hmenu.destroy();
        }
        this.initData(null, null);
        this.purgeListeners();

        if(this.grid.enableColumnMove){
            delete Ext.dd.DDM.locationCache[this.columnDrag.id];
            Ext.destroy(this.columnDrag,this.columnDrop);
        }

        Ext.fly(this.innerHd).removeAllListeners();
        Ext.removeNode(this.innerHd);

        Ext.destroy(
            this.el,
            this.mainWrap,
            this.mainHd,
            this.scroller,
            this.mainBody,
            this.focusEl,
            this.resizeMarker,
            this.resizeProxy,
            this.activeHdBtn,
            this.dragZone,
            this.splitZone,
            this._flyweight
        );
        Ext.EventManager.removeResizeListener(this.onWindowResize, this);
    }
});

Ext.override(Ext.tree.TreePanel,{
    onDestroy : function(){
        if(this.rendered){
            this.body.removeAllListeners();
            Ext.dd.ScrollManager.unregister(this.body);
            Ext.destroy(this.dropZone,this.dragZone,this.innerCt);
        }
        Ext.destroy(this.root);
        Ext.tree.TreePanel.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.grid.HeaderDropZone,{
    onDestroy   :   function(){
        Ext.destroy(this.proxyTop,this.proxyBottom);
        Ext.grid.HeaderDropZone.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.menu.Menu,{
    onDestroy : function(){
        Ext.destroy(this.el);
        Ext.menu.MenuMgr.unregister(this);
        Ext.EventManager.removeResizeListener(this.hide, this);
        if(this.keyNav) {
            this.keyNav.disable();
        }
        var s = this.scroller;
        if(s){
            Ext.destroy(s.topRepeater, s.bottomRepeater, s.top, s.bottom);
        }
        this.purgeListeners();
        Ext.menu.Menu.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.TabPanel,{ // Ext.Panel
    onDestroy   :   function(){
        Ext.destroy(
            this.stack,
            this.stripWrap,
            this.stripSpacer,
            this.strip,
            this.edge,
            this.leftRepeater,
            this.rightRepeater
        );
        Ext.TabPanel.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.layout.ColumnLayout,{
    destroy :   function(){
        Ext.destroy(this.innerCt);
    }
});

Ext.override(Ext.form.Field,{
    onDestroy   :   function(){
        Ext.destroy(this.errorEl,this.errorIcon);
        Ext.form.Field.superclass.onDestroy.call(this);
    }
});

Ext.override(Ext.form.TextField,{
    onDestroy   :   function(){
        Ext.destroy(this.metrics);
        if(this.validationTask){
            this.validationTask.cancel();
            this.validationTask = null;
        }
        Ext.form.TextField.superclass.onDestroy.call(this);
    }
});