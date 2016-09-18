Ext.layout.ColumnFormLayout = Ext.extend(Ext.layout.FormLayout, {
	/*
	onLayout : function(ct, target){
		Ext.layout.AnchorLayout.superclass.onLayout.call(this, ct, target);
		var size = this.getAnchorViewSize(ct, target);
		var w = size.width, h = size.height;
		if(w < 20 && h < 20){
			return;
		}
		var aw, ah;
		if(ct.anchorSize){
			if(typeof ct.anchorSize == 'number'){
				aw = ct.anchorSize;
			}else{
				aw = ct.anchorSize.width;
				ah = ct.anchorSize.height;
			}
		}else{
			aw = ct.initialConfig.width;
			ah = ct.initialConfig.height;
		}
		var cs = ct.items.items, len = cs.length, i, c, a, cw, ch;
		for(i = 0; i < len; i++){
			c = cs[i];
			if(c.anchor){
				a = c.anchorSpec;
				if(!a){
					var vs = c.anchor.split(' ');
					c.anchorSpec = a = {
						right: this.parseAnchor(vs[0], c.initialConfig.width, aw),
						bottom: this.parseAnchor(vs[1], c.initialConfig.height, ah)
					};
				}
				cw = a.right ? this.adjustWidthAnchor(a.right(w), c) : undefined;
				ch = a.bottom ? this.adjustHeightAnchor(a.bottom(h), c) : undefined;
				if(cw || ch){
					c.setSize(cw || undefined, ch || undefined);
				}
			}
		}
	},
*/

	monitorResize:true,
	//extraCls: 'x-column',
	scrollOffset : 0,
	isValidParent : function(c, target){
		return (c.getPositionEl ? c.getPositionEl() : c.getEl()).dom.parentNode == this.innerCt.dom;
		//return target && this.container.getEl().contains(c.getDomPositionEl());
	},
	onLayout : function(ct, target){
		//Ext.layout.FormLayout.superclass.onLayout.call(this, ct, target);
		var cs = ct.items.items, len = cs.length, c, i;
		if(!this.innerCt){
			target.addClass('x-column-layout-ct');
			this.innerCt = target.createChild({cls:'x-column-inner'});
			this.innerCt.createChild({cls:'x-clear'});
		}
		this.renderAll(ct, this.innerCt);
		var size = Ext.isIE && target.dom != Ext.getBody().dom ? target.getStyleSize() : target.getViewSize();
		if(size.width < 1 && size.height < 1){
			return;
		}
		var w = size.width - target.getPadding('lr') - this.scrollOffset,
		h = size.height - target.getPadding('tb'),
		pw = w;
		this.innerCt.setWidth(w);
		for(i = 0; i < len; i++){
			c = cs[i];
			if(!c.columnWidth){
				pw -= (c.getSize().width + c.getEl().getMargins('lr'));
			}
		}
		pw = pw < 0 ? 0 : pw;
		for(i = 0; i < len; i++){
			c = cs[i];
			if(c.columnWidth){
				c.setSize(Math.floor(c.columnWidth*pw) - c.getEl().getMargins('lr'));
			}
		}
	},
	renderItem : function(c, position, target){
		if(c && (c.isFormField || c.fieldLabel) && c.inputType != 'hidden'){
			var args = this.getTemplateArgs(c);
			if(Ext.isNumber(position)){
				position = target.dom.childNodes[position] || null;
			}
			if(position){
				c.itemCt = this.fieldTpl.insertBefore(position, args, true);
			}else{
				c.itemCt = this.fieldTpl.append(target, args, true);
			}
			if(!c.rendered){
				c.render('x-form-el-' + c.id);
			}else if(!this.isValidParent(c, target)){
				Ext.fly('x-form-el-' + c.id).appendChild(c.getPositionEl());
			}
			if(!c.getItemCt){
				Ext.apply(c, {
					getItemCt: function(){
						return c.itemCt;
					},
					customItemCt: true
				});
			}
			c.label = c.getItemCt().child('label.x-form-item-label');
			if(this.trackLabels && !this.isHide(c)){
				if(c.hidden){
					this.onFieldHide(c);
				}
				c.on({
					scope: this,
					show: this.onFieldShow,
					hide: this.onFieldHide
				});
			}
			this.configureItem(c);
		}else {
			Ext.layout.FormLayout.superclass.renderItem.apply(this, arguments);
		}
	}
});
Ext.Container.LAYOUTS['columnform'] = Ext.layout.ColumnFormLayout;

Ext.override(Ext.form.ComboBox,{
	customProperties : false,

    getValue : function (){
    	var v;
    	if(this.valueField){
    		v=Ext.isDefined(this.value) ? this.value : '';
    	}else{
    		v=Ext.form.ComboBox.superclass.getValue.call(this);
    	}
    	if (typeof(v)=='undefined') v = '';
      	var r = this.getRawValue() || '';
      	if (!this.customProperties || typeof(v)!='string' ||
        	(typeof(v)=='string' && v !=''))
        	return v;
      	return r;
    }
});

Ext.override(Ext.form.CheckboxGroup,{
	isComposite : true
});

Ext.override(Ext.layout.ColumnLayout,{
	columnfit : false,

    onLayout : function(ct, target){
    	var cs = ct.items.items,
    		len = cs.length,
    		c,
    		i,
    		m,
    		margins = [];
    	this.renderAll(ct, target);
    	var size = this.getLayoutTargetSize();
    	if(size.width < 1 && size.height < 1){
    		return;
    	}
    	var w = size.width - this.scrollOffset,
    		h = size.height,
    		pw = w;
    		this.innerCt.setWidth(w);
    	for(i = 0; i < len; i++){
    		c = cs[i];
    		m = c.getPositionEl().getMargins('lr');
    		margins[i] = m;
    		if(!c.columnWidth){
    			pw -= (c.getWidth() + m);
    		}
    	}
    	pw = pw < 0 ? 0 : pw;
    	for(i = 0; i < len; i++){
    		c = cs[i];
    		m = margins[i];
    		if(c.columnWidth){
    			if(this.columnfit)
    				c.setSize(Math.floor(c.columnWidth * pw) - m,h);
    			else
    				c.setSize(Math.floor(c.columnWidth * pw) - m);
    		}
    		else if(this.columnfit){
    			c.setHeight(h);
    		}
    	}
    	if (Ext.isIE) {
    		if (i = target.getStyle('overflow') && i != 'hidden' && !this.adjustmentPass) {
    			var ts = this.getLayoutTargetSize();
    			if (ts.width != size.width){
    				this.adjustmentPass = true;
    				this.onLayout(ct, target);
    			}
    		}
    	}
    	delete this.adjustmentPass;
    }
});

Ext.override(Ext.data.Store,{
	save : function(){
		if (!this.writer) {
			throw new Ext.data.Store.Error('writer-undefined');
		}
		var queue = [],
			len,
			trans,
			batch,
			data = {};
		if(this.removed.length){
			queue.push(['destroy', this.removed]);
		}
		var rs = [].concat(this.getModifiedRecords());
		if(rs.length){
			var phantoms = [];
			var upts = [];
			for(var i=0; i<rs.length; i++){
				if(rs[i].isValid()){
					if(rs[i].phantom === true){
						phantoms.push(rs[i]);
					}
					else{
						upts.push(rs[i]);
					}
				}
			}
			if(phantoms.length){
				queue.push(['create', phantoms]);
			}
			if(upts.length){
				queue.push(['update', upts]);
			}
		}
		len = queue.length;
		if(len){
			batch = ++this.batchCounter;
			for(var i = 0; i < len; ++i){
				trans = queue[i];
				data[trans[0]] = trans[1];
			}
			if(this.fireEvent('beforesave', this, data) !== false){
				for(var i = 0; i < len; ++i){
					trans = queue[i];
					this.doTransaction(trans[0], trans[1], batch);
				}
				return batch;
			}
		}
		return -1;
	}
});
//Ext3.3.0 error
Ext.override(Ext.grid.CheckboxSelectionModel,{
    initEvents : function(){
        Ext.grid.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.innerHd).on('mousedown', this.onHdMouseDown, this);

        }, this);
    }
});

Ext.override(Ext.form.HtmlEditor,{
    syncValue : function(){
        if(this.initialized){
            var bd = this.getEditorBody();
            var html = bd.innerHTML;
            if(Ext.isWebKit){
                var bs = bd.getAttribute('style');
                var m = bs.match(/text-align:(.*?);/i);
                if(m && m[1]){
                    html = '<div style="'+m[0]+'">' + html + '</div>';
                }
            }
            html = this.cleanHtml(html);
            if(this.fireEvent('beforesync', this, html) !== false){
                this.el.dom.value = this.deValue(html);
                this.fireEvent('sync', this, html);
            }
        }
    },
    pushValue : function(){
        if(this.initialized){
            var v = this.el.dom.value;
            if(!this.activated && v.length < 1){
                v = this.defaultValue;
            }
            if(this.fireEvent('beforepush', this, v) !== false){
                this.getEditorBody().innerHTML = this.enValue(v);
                if(Ext.isGecko){

                    this.setDesignMode(false);
                    this.setDesignMode(true);
                }
                this.fireEvent('push', this, v);
            }

        }
    },
    enValue : function(v){
    	return v;
    },
    deValue : function(v){
    	return v;
    }
});
//必填项加前缀*
Ext.override(Ext.layout.FormLayout,{
    getTemplateArgs: function(field) {
        var noLabelSep = !field.fieldLabel || field.hideLabel;
        return {
            id            : field.id,
            label         : (field.allowBlank==false?"<font color='red'>*</font>":"")+(noLabelSep?"":field.fieldLabel),
            itemCls       : (field.itemCls || this.container.itemCls || '') + (field.hideLabel ? ' x-hide-label' : ''),
            clearCls      : field.clearCls || 'x-form-clear-left',
            labelStyle    : this.getLabelStyle(field.labelStyle),
            elementStyle  : this.elementStyle || '',
            labelSeparator: noLabelSep ? '' : (Ext.isDefined(field.labelSeparator) ? field.labelSeparator : this.labelSeparator)
        };
    }
});
//默认Grid 可复制
Ext.override(Ext.grid.GridView,{
    cellTpl: new Ext.Template(
        '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} {css}" style="{style}" tabIndex="0" {cellAttr}>',
	        '<div class="x-grid3-cell-inner x-grid3-col-{id}" unselectable="off" {attr}>{value}</div>',
	    '</td>'
    )
});