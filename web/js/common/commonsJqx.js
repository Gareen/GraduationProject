window.jqx = {
	date : {
		formatString : 'd',
		selectionMode : 'default',
		width : -1,
		height : 22,
		theme : 'arctic'	
	}
};


$(function (){
	var loader = $("<div id='loaderElement'></div>");
	$("body").append(loader);

	/*$("#loaderElement").jqxLoader({
		text: "", width: 60, height: 60
	});*/
});

var loader = {
	open : function (){
		$("#loaderElement").jqxLoader("open");
	},

	close : function (){
		$("#loaderElement").jqxLoader("close");
	}
};



function msg(text,callback){
	var windowId = "msgWindow";
	var closeId = "msgClosedBtn";
	
	$('#' + windowId).remove();
	
	var w =  	'<div id="' +windowId +'">'+
    					'<div class="fqs-jqx-window-header">消息框</div>' +
    					'<div>' +
    						'<div class="fqs-jqx-window-body">' + text + '</div>'+
    						'<div class="fqs-jqx-window-foot"><button id=' + closeId + '>确定</button></div>' +
    					'<div>' +
    				'</div>';
	$("body").append($(w));

	var height = getTrueLength(text) ;
	//alert(height);
	if(height > 90 && height < 160){
		height += 30;
	}


	buildButton(closeId,"240","",null,Global.theme);

    $('#' + windowId).jqxWindow({
        //maxHeight: 200,
        minHeight: 130,
        //maxWidth: 350,
        //minWidth: 150,
        height: height,
        width: 250,
        resizable: false, 
        isModal: true, 
        modalOpacity: 0.3,
		theme:Global.theme,
		closeAnimationDuration:250,
		showAnimationDuration:250,
        cancelButton: $('#' + closeId)
    }).on('close', function (event) {
    	if(isFunction(callback)){
    		callback();
    	}
    });



	$("#"+closeId).css("margin-left" , 0);
}



function buildDateInput(idOrClass,format,width,settings){
	var _settings = {
		width : width ? width : "100%",
		height : 25,
		culture: 'zh-CN',
		formatString: format ? format : DateFormat.yMd_Hms,
		showFooter:true,
		theme : Global.theme,
//		selectionMode : "range",
		todayString : "今天",
		clearString : "",
		allowNullDate: true,
		value : null
	};
	$.extend(_settings,settings);

	var item = $("#" + idOrClass);
	if(item.length == 0){
		item = $("." + idOrClass);
	}
	if(item.length == 0){
		return ;
	}

	item.jqxDateTimeInput(_settings);
}


function buildSelect(idOrClass,source,width,d){
	var remote = typeof (source) == "string";

	if(remote){
		$.ajax({
			url : source,
			dataType : 'json',
			async: false,
			success : function (rtn){
				source = rtn;
			}
		});
	}


	var p = {
		source: source,
		height : 25,
		width : width,
		displayMember : "lk_option",
		valueMember : "lk_value",
		theme:Global.theme,
		animationType:'none'
	};

	var c = $.extend({},p,d);
	var s = $("#" + idOrClass);
	if(s.length == 0){
		s = $("." + idOrClass);
	}
	if(s.length == 0){
		return ;
	}

	if( ! c.hasOwnProperty("selectedIndex")){
		c['selectedIndex'] = 0;
	}

	s.jqxDropDownList(c);
	return s;
}



function buildButton(id,width,template,callback,theme){
	var b = $("#" + id);
	b.css("margin-left","5px");
	var settings = {
		width : width ? width : "100%",
		template : template
	};

	$.extend(settings,theme ? {theme : theme} : {theme : Global.theme});

	b.jqxButton(settings);


	if(isFunction(callback)){
		b.on("click",function (){
			callback();
		});
	}
}


function buildInput(id,maxlength,width,theme){
	var input = $("#" + id);
	if(input.length == 0){
		input = $("." + id);
	}
	if(input.length == 0){
		return;
	}

	input.jqxInput({
		height: 25,
		width: width ? width : "100%",
		maxLength: maxlength ? maxlength : 50,
		theme:theme ? theme : Global.theme
	});
}

function disableButton(id){
	$("#" + id ).jqxButton({disabled: true });
}
function enableButton(id){
	$("#" + id ).jqxButton({disabled: false });
}





/**
 * 颜色选择器
 * @param color
 * @returns
 */
function buildColorPicker(textId,pickerId,settings,onchange){
	var _settings = {
			defaultColor : "000000",
			width : 150,
			height : 22
	};
	$.extend(_settings,settings);
    $("#" + pickerId).on('colorchange', function (event) {
        $("#" + textId).jqxDropDownButton('setContent', getTextElementByColor(event.args.color));
        if(isFunction(onchange)){
        	onchange(event.args.color.hex);
        }
    });
    $("#" + pickerId).jqxColorPicker({ color: _settings.defaultColor, colorMode: 'hue', width: 220, height: 220});
    $("#" + textId).jqxDropDownButton({ width: _settings.width, height: _settings.height});
    $("#" + textId).jqxDropDownButton('setContent', getTextElementByColor(new $.jqx.color({ hex: _settings.defaultColor })));
}


function getTextElementByColor(color) {
    if (color == 'transparent' || color.hex == "") {
        return $("<div style='text-shadow: none; position: relative; padding-bottom: 2px; margin-top: 2px;'>transparent</div>");
    }
    var element = $("<div style='text-shadow: none; position: relative; padding-bottom: 2px; margin-top: 2px;'>#" + color.hex + "</div>");
    var nThreshold = 105;
    var bgDelta = (color.r * 0.299) + (color.g * 0.587) + (color.b * 0.114);
    var foreColor = (255 - bgDelta < nThreshold) ? 'Black' : 'White';
    element.css('color', foreColor);
    element.css('background', "#" + color.hex);
    element.addClass('jqx-rc-all');
    return element;
}


function exportToExcel(gridId,fileName,header){
	header = header ? true : false;
	var url = getRoot() +"/share/gridPrinter/excel";
	$("#"+gridId).jqxGrid('exportdata', 'xls', fileName,header, null, false, url);
}

function destroyGrid(gridId,css){
	var g = $("#" + gridId);
	var parent = g.parent();
	g.jqxGrid('clearfilters');
	g.jqxGrid("destroy");
	g.jqxDataTable('destroy');

	g = $("<div id=" + gridId + "></div>");

	parent.append(g);
	if(css){
		g.css(css);
	}
}

function getDropdownContext(str){
	return '<div style="position: relative; margin-left: 3px; margin-top: 5px;">' + str + '</div>';
}




function toolbar(grid,toolBar,func){
	var g = $("#" + grid);

	// appends buttons to the status bar.
	var container = $("<div style='overflow: hidden; position: relative; height: 100%; width: 100%;'></div>");

	container.append($("<div style='float: right; padding: 5px; margin:6px 12px 6px 6px;font-weight: bold;'><span>&nbsp;&nbsp;</span><span id='" + grid + "TotalNumber' ></span></div>"));

	var buttonTemplate = "<div style='float: left; padding: 3px; margin: 2px;'><div style='margin: 4px; width: 16px; height: 16px;'></div></div>";
	var rowKey = null;

	if(func){
		var searchButton = $(buttonTemplate);
		var addButton = $(buttonTemplate);
		var editButton = $(buttonTemplate);
		var deleteButton = $(buttonTemplate);

		if(isFunction(func['search'])){
			container.append(addButton);
			addButton.jqxButton({cursor: "pointer", disabled: false,enableDefault: false, height: 25, width: 25 });
			addButton.find('div:first').addClass('jqx-icon-search');
			addButton.click(function () {
				if (!addButton.jqxButton('disabled')) {
					func['search'](rowKey);
				}
			});
		}


		if(isFunction(func['add'])){
			container.append(addButton);
			addButton.jqxButton({cursor: "pointer", disabled: false,enableDefault: false, height: 25, width: 25 });
			addButton.find('div:first').addClass('jqx-icon-plus');
			addButton.click(function () {
				if (!addButton.jqxButton('disabled')) {
					func['add'](rowKey);
				}
			});
		}

		if(isFunction(func['edit'])){
			container.append(editButton);
			editButton.jqxButton({ cursor: "pointer", disabled: true, enableDefault: false,  height: 25, width: 25 });
			editButton.find('div:first').addClass('jqx-icon-edit');
			editButton.click(function () {
				if (!editButton.jqxButton('disabled')) {
					func['edit'](rowKey);
				}
			});
		}

		if(isFunction(func['delete'])){
			container.append(deleteButton);
			deleteButton.jqxButton({ cursor: "pointer", disabled: true, enableDefault: false,  height: 25, width: 25 });
			deleteButton.find('div:first').addClass('jqx-icon-delete');
			deleteButton.click(function () {
				if (!deleteButton.jqxButton('disabled')) {
					func['delete'](rowKey);
				}
			});
		}


		var updateButtons = function (action) {
			switch (action) {
				case "Select":
					searchButton.jqxButton({ disabled: false });
					//addButton.jqxButton({ disabled: false });
					deleteButton.jqxButton({ disabled: false });
					editButton.jqxButton({ disabled: false });
					break;
				case "Unselect":
					searchButton.jqxButton({ disabled: true });
					//addButton.jqxButton({ disabled: true });
					deleteButton.jqxButton({ disabled: true });
					editButton.jqxButton({ disabled: true });
					break;
			}
		}



		g.on('rowSelect rowselect', function (event) {
			//此处由于在用户选择数据时\Grid会优先执行rowselect 再执行rowunselect事件
			//导致按钮再次被禁用\所以延迟该事件延迟100毫秒
			setTimeout(function (){
				rowKey = event.args.row.id;
				updateButtons('Select');
			},50);
		});

		g.on('rowUnselect rowunselect', function (event) {
			updateButtons('Unselect');
		});

	}

	toolBar.append(container);




	if( ! g.jqxTreeGrid("getRows")){
		g.on("init filter",function (){
			setGridTotalNumber(grid);
		}).trigger("init");
	}




}

function setGridTotalNumber(gridId,value){
	var g = $("#" + gridId);
	var box = $("#" + gridId + "TotalNumber");
	var total = Number(box.html());
	value = value == "+" ? ++total : ( value == "-" ? --total : (g.jqxGrid("getrows") || g.jqxDataTable("getRows") ).length ) ;
	box.html(value);
}




function createDataAdapter(source,ext){
	var dataAdapter = new $.jqx.dataAdapter(source, $.extend(
		{
//		自定义load方法 ,在这种情况下可返回自定义对象等 进行各种操作
			loadServerData: function (data,source,callback){

				$.ajax({
					url : source.url,
					type : source.type ? source.type : "GET",
					data : data,
					dataType : source.datatype,
					success : function (rtn){
						if(rtn){
							if(isArray(rtn)){
								callback({records:rtn});
							}else if(isObject(rtn)) {
								if(rtn.records){
									callback({records: rtn.records, totalCount: rtn.totalCount});
								}else{
									//兼容陆帅峰框架的返回值
									callback({records: rtn.data, totalCount: rtn.totalCount});
								}

							}else{
								callback({records : []});
							}
						}
//					注意 data 中必须有 records 属性、否则需要用以下方式调用参数
					}
				});
			},
//		该方法在请求数据前触发,
//		formatData: function (data) {
//			return data;
//		},

//     该方法在数据传输完成后触发但在使用自定义方法请求数据时无效、故不使用
//        downloadComplete: function (data, status, xhr) {
//        	source.totalRecords = data.totalCount;
//        },
//     该方法在load完成后触发、触发时间晚于downloadComplete
//      用于设置totalCount;
			loadComplete: function (data, status, xhr) {
				source.totalRecords = data.totalCount;
			}
		},
		ext ? ext : {}
	));
	return dataAdapter;

}