
Ext.onReady(function() {

			Ext.QuickTips.init();
			// turn on validation errors beside the field globally
			Ext.form.Field.prototype.msgTarget = 'side';

            var simple = new Ext.FormPanel({
						labelWidth : 75, // label settings here cascade
						// unless overridden
						id : 'form1',
						title : '�޸�����',
						bodyStyle : 'padding:5px 5px 0',
						width : 'auto',
						defaults : {
							width : 230
						},
						defaultType : 'textfield',
	    				buttonAlign:'center',

						items : [{
									name : 'userid',
									hidden : true,
									hideLabel : true,
									value: JrafSession.get('userid')
								}, {
									fieldLabel : '�û���',
									name : 'usercode',
									readOnly : true,
									value: JrafSession.get('usercode')
								}, {
									fieldLabel : '������',
									name : 'userpwd',
									inputType : 'password',
									allowBlank : false
								}, {
									xtype : 'fieldset',
									title : '������',
									checkboxToggle : false,
                                    collapsed: false,
                                    collapsible: false,
									autoHeight : true,
                                    autoWidth : false,
									defaults : {
										anchor : '-20'
                                        ,width:230
									},
									defaultType : 'textfield',
									items : [{
												fieldLabel : '������',
                                                title:'test1',
                                                id: 'userpwd1',
												name : 'userpwd1',
												inputType : 'password',
												allowBlank : false
											}, {
												fieldLabel : 'ȷ��������',
                                                title:'test2',
												name : 'userpwd2',
												inputType : 'password',
												allowBlank : false,
												vtype:'valEqual',
												valEqual:{field:'userpwd1'}
											}]

								}],
                        frame:true,
						buttons : [{
									text : '����',
									handler : function() {
										var jr = new JrafRequest('pcmc','userrole','changePwd');
                                        jr.setForm('form1');
                                        jr.postData();
									}
								}, {
									text : 'ȡ��',
									handler:function(){
										simple.getForm().reset();
									}
								}]
					});
			simple.render(document.body);
		});