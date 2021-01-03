<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../common/header.jsp"%>
<div class="easyui-layout" data-options="fit:true">
    <!-- Begin of toolbar -->
    <div id="wu-toolbar">
        <div class="wu-toolbar-button">
            <%@include file="../common/menus.jsp"%>
        </div>
        <div class="wu-toolbar-search">
            <label>菜名:</label><input id="search-name" class="wu-text" style="width:100px">
           
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>
    <!-- End of toolbar -->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#wu-toolbar"></table>
</div>
<!-- Begin of easyui-dialog -->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:420px; padding:10px;">
	<form id="add-form" method="post">
        <table>
            <tr>
                <td width="60" align="right">菜品预览:</td>
                <td valign="middle">
                	<img id="preview-photo" style="float:left;" src="/quanqian_hotel/resources/admin/easyui/images/user_photo.jpg" width="100px">
                	<a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">现场图片:</td>
                <td><input type="text" id="add-photo" name="photo" value="/quanqian_hotel/resources/admin/easyui/images/user_photo.jpg" readonly="readonly" class="wu-text " /></td>
            </tr>
          
            <tr>
                <td width="60" align="right">娱乐项目名称:</td>
                <td><input type="text" name="ename" class="wu-text easyui-validatebox" /></td>
            </tr>
            <tr>
                <td width="60" align="right">状态:</td>
                <td>
                	<select name="state" class="easyui-combobox" panelHeight="auto" style="width:268px">
		                <option value="0">不可用</option>
            			<option value="1">可用</option>
            			<option value="2">维护中</option>
		            </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<!-- 修改窗口 -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="edit-form" method="post">
        <input type="hidden" name="id" id="edit-id">
        <table>
            <tr>
                <td width="60" align="right">现场图片预览:</td>
                <td valign="middle">
                	<img id="edit-preview-photo" style="float:left;" src="/quanqian_hotel/resources/admin/easyui/images/user_photo.jpg" width="100px">
                	<a style="float:left;margin-top:40px;" href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-upload" onclick="uploadPhoto()" plain="true">上传图片</a>
                </td>
            </tr>
            <tr>
                <td width="60" align="right">现场图片:</td>
                <td><input type="text" id="edit-photo" name="photo" value="/quanqian_hotel/resources/admin/easyui/images/user_photo.jpg" readonly="readonly" class="wu-text " /></td>
            </tr>
          
            <tr>
                <td width="60" align="right">娱乐项目名称:</td>
                <td><input type="text" id="edit-ename" name="ename" class="wu-text easyui-validatebox" /></td>
            </tr>
            <tr>
                <td width="60" align="right">状态:</td>
                <td>
                	<select name="state" class="easyui-combobox" panelHeight="auto" style="width:268px">
		                <option value="0">不可用</option>
            			<option value="1">可用</option>
            			<option value="2">维护中</option>
		            </select>
                </td>
            </tr>
            
        </table>
    </form>
</div>
<div id="process-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-upload',title:'正在上传图片'" style="width:450px; padding:10px;">
<div id="p" class="easyui-progressbar" style="width:400px;" data-options="text:'正在上传中...'"></div>
</div>
<input type="file" id="photo-file" style="display:none;" onchange="upload()">
<%@include file="../common/footer.jsp"%>

<!-- End of easyui-dialog -->
<script type="text/javascript">
	//上传图片
	function start(){
			var value = $('#p').progressbar('getValue');
			if (value < 100){
				value += Math.floor(Math.random() * 10);
				$('#p').progressbar('setValue', value);
			}else{
				$('#p').progressbar('setValue',0)
			}
	};
	function upload(){
		if($("#photo-file").val() == '')return;
		var formData = new FormData();
		formData.append('photo',document.getElementById('photo-file').files[0]);
		$("#process-dialog").dialog('open');
		var interval = setInterval(start,200);
		$.ajax({
			url:'upload_photo',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data){
				clearInterval(interval);
				$("#process-dialog").dialog('close');
				if(data.type == 'success'){
					$("#preview-photo").attr('src',data.filepath);
					$("#add-photo").val(data.filepath);
					$("#edit-preview-photo").attr('src',data.filepath);
					$("#edit-photo").val(data.filepath);
				}else{
					$.messager.alert("消息提醒",data.msg,"warning");
				}
			},
			error:function(data){
				clearInterval(interval);
				$("#process-dialog").dialog('close');
				$.messager.alert("消息提醒","上传失败!","warning");
			}
		});
	}
	
	function uploadPhoto(){
		$("#photo-file").click();
		
	}
	
	
	/**
	*  添加记录
	*/
	function add(){
		var validate = $("#add-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		var data = $("#add-form").serialize();
		$.ajax({
			url:'add',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert('信息提示','添加成功！','info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
				}else{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	/**
	* Name 修改记录
	*/
	function edit(){
		var validate = $("#edit-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		var data = $("#edit-form").serialize();
		$.ajax({
			url:'edit',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert('信息提示','修改成功！','info');
					$('#edit-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
				}else{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}
	
	/**
	* 删除记录
	*/
	function remove(){
		$.messager.confirm('信息提示','确定要删除该记录？', function(result){
			if(result){
				var item = $('#data-datagrid').datagrid('getSelections');
				if(item == null || item.length == 0){
					$.messager.alert('信息提示','请选择要删除的数据！','info');
					return;
				}
				var ids = '';
				for(var i=0;i<item.length;i++){
					ids += item[i].id + ',';
				}
				$.ajax({
					url:'delete',
					dataType:'json',
					type:'post',
					data:{ids:ids},
					success:function(data){
						if(data.type == 'success'){
							$.messager.alert('信息提示','删除成功！','info');
							$('#data-datagrid').datagrid('reload');
						}else{
							$.messager.alert('信息提示',data.msg,'warning');
						}
					}
				});
			}	
		});
	}
	
	/**
	* Name 打开添加窗口
	*/
	function openAdd(){
		//$('#add-form').form('clear');
		$('#add-dialog').dialog({
			closed: false,
			modal:true,
            title: "添加用户信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            	//$("#add-form input").val('');
            }
        });
	}
	
	/**
	* 打开修改窗口
	*/
	function openEdit(){
		//$('#edit-form').form('clear');
		var item = $('#data-datagrid').datagrid('getSelections');
		if(item == null || item.length == 0){
			$.messager.alert('信息提示','请选择要修改的数据！','info');
			return;
		}
		if(item.length > 1){
			$.messager.alert('信息提示','请选择一条数据进行修改！','info');
			return;
		}
		item = item[0];
		$('#edit-dialog').dialog({
			closed: false,
			modal:true,
            title: "修改用户信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');                    
                }
            }],
            onBeforeOpen:function(){
            	$("#edit-id").val(item.id);
            	$("#edit-preview-photo").attr('src',item.photo);
				$("#edit-photo").val(item.photo);
            	$("#edit-ename").val(item.ename);
            	$("#edit-state").val(item.state);
            }
        });
	}	
	
	
	//搜索按钮监听
	$("#search-btn").click(function(){
		
		var option = {ename:$("#search-name").val()};
		$('#data-datagrid').datagrid('reload',option);
	});
	
	/** 
	* 载入数据
	*/
	$('#data-datagrid').datagrid({
		url:'entertainment_list',
		rownumbers:true,
		singleSelect:false,
		pageSize:10,           
		pagination:true,
		multiSort:true,
		fitColumns:true,
		idField:'id',
	    treeField:'name',
		fit:true,
		columns:[[
			{ field:'chk',checkbox:true},
			{ field:'photo',title:'图片',width:100,align:'center',formatter:function(value,row,index){
				var img = '<img src="'+value+'" width="50px" />';
				return img;
			}},
			{ field:'ename',title:'菜名',width:100,sortable:true},
			{ field:'state',title:'状态',width:100,formatter:function(value,row,index){
				switch(value){
					case '0':{
						return '不可用';
					}
					case '1':{
						return '可用';
					}
					case '2':{
						return '维护中';
					}
				}
				return value;
			}}
		]],
		onLoadSuccess:function(data){  
			$('.authority-edit').linkbutton({text:'编辑权限',plain:true,iconCls:'icon-edit'});  
		 }  
	});
</script>