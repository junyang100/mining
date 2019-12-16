layui.use(['table','form','element'], function(){
        //初始化表格工具
        var table = layui.table, form  = layui.form, element = layui.element;
        var addindex;
        table.render({
            elem: '#userTable'
            ,size:'sm'
            ,even:true
            ,height:'full-228'
            ,method:'post'
            ,url: '/user/queryUserList' //数据接口
            ,page: true //开启分页
            ,limit:30
            ,loading:true//开启加载条
            ,cols:  [[ //标题栏
                {field: 'userName', title: '用户名', width:'20%', fixed: 'left'}
                ,{field: 'nickName', title: '用户昵称', width:'20%', fixed: 'left'}
                ,{field: 'status',title:'用户状态',width:'10%', templet: function(d){
                    if(d.status == 1) {
                        return "正常"
                    }else {
                        return "停用"
                    }
                }}
                ,{field: 'createTime',title:'创建时间',width:'20%'}
                ,{field: 'score', title: '操作', width: '30%', toolbar: '#barDemo', fixed: 'right'}
                ]]
            ,id: 'demoReload'
            ,text: {
                none: '暂无匹配数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
            },response: {
                statusName: 'status' //规定数据状态的字段名称，默认：code
                ,statusCode: 200 //规定成功的状态码，默认：0
                ,msgName: 'msg' //规定状态信息的字段名称，默认：msg
                ,countName: 'totalCount' //规定数据总数的字段名称，默认：count
                ,dataName: 'data' //规定数据列表的字段名称，默认：data
            }
        });

    var active = {
        reload: function(){
            //执行重载
            table.reload('demoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    nickName:$("#nickName1").val(),
                    userName: $("#userName").val()

                }
            });
        }
    };

    $('#select').on('click', function(){
        var type = layui.$(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
    $('[lay-filter="auto_submit"]').on('keyup', function(){
        active['reload'].call(this);
    });

    table.on('tool(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
        ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'del'){
            layer.confirm('确认删除['+data.nickName+']么？', function(index){
                $.post('/user/removeUser',{id:data.id},function(data){
                        if(data.status == 200){
                            layer.msg('删除成功！');
                            obj.del(); //删除对应行（tr）的DOM结构
                            layer.close(index);
                        }else{
                            layer.msg('删除失败');
                        }
                });

            });
        }else if(layEvent === 'menu'){
            var xtreeMenu = new layuiXtree({
                elem: 'xtreeNenu',
                form: form,
                data: '/menu/queryMenuTree?userId='+data.id,
                 isopen: true,
                 ckall: true,
                 ckallback: function () {},
                 color: {
                    open: "#EE9A00"
                    , close: "#EEC591"
                    , end: "#828282"
                }
            });
            layer.open({
                title: '菜单授权'
                ,type:1
                ,anim:5
                ,btnAlign: 'r' //按钮居中
                ,closeBtn:2
                ,area:['500px','600px']
                ,content: $('#assignMenuDiv')//捕获的元素
                ,scrollbar: false
                ,shadeClose: true
                ,btnAlign: 'r'
                ,btn: ['立即提交', '关闭']
                ,yes: function(index, layero){
                    var menuDataArray = [];
                    var checkedArrays = xtreeMenu.GetChecked();
                    if(checkedArrays){
                        for(var i = 0;i < checkedArrays.length;i++){
                             var menuData = {};
                             var menuDataParent = {};
                             menuData.menuId = checkedArrays[i].value;
                            var parent = xtreeMenu.GetParent(checkedArrays[i].value);
                            if(parent){
                                menuDataParent.menuId = parent.value;
                            }else{
                                menuDataParent.menuId = checkedArrays[i].value;
                            }
                            addMenuIfAbsent(menuDataArray,menuData);
                            addMenuIfAbsent(menuDataArray,menuDataParent);
                        }
                    }
                    var userMenuJsonStr = "All";
                    if(menuDataArray.length > 0){
                        userMenuJsonStr = JSON.stringify(menuDataArray);
                    }
                    $.post('/user/assignUserMenu',{
                        'userMenuJsonStr':userMenuJsonStr,
                        'userId':data.id
                    },function(result){
                        if(result.status == 200){
                            layer.close(index);
                            layer.msg('菜单授权成功！');
                        }else{
                            layer.msg(result.msg);
                        }
                    });

                    return false;
                }
                ,btn2: function(index, layero){
                    //按钮【按钮二】的回调
                    //return false; //开启该代码可禁止点击该按钮关闭
                }
            });
        }else if(layEvent === 'privilege'){
            var xtreePrivilege = new layuiXtree({
                elem: 'xtreePrivilege',
                form: form,
                data: '/platform/queryApplicationTree?userId='+data.id,
                isopen: true,
                ckall: true,
                ckallback: function () {},
                color: {
                    open: "#EE9A00"
                    , close: "#EEC591"
                    , end: "#828282"
                }
            });
            layer.open({
                title: '数据授权'
                ,type:1
                ,anim:5
                ,btnAlign: 'r' //按钮居中
                ,closeBtn:2
                ,area:['500px','600px']
                ,content: $('#assignPrivilegeDiv')//捕获的元素
                ,scrollbar: false
                ,shadeClose: true
                ,btnAlign: 'r'
                ,btn: ['立即提交', '关闭']
                ,yes: function(index, layero){
                    var privilegeDataArray = [];
                    var checkedArrays = xtreePrivilege.GetChecked();
                    if(checkedArrays){
                        for(var i = 0;i < checkedArrays.length;i++){
                            var privilegeData = {};
                            var privilegeDataParent = {};
                            privilegeData.privilegeId = checkedArrays[i].value;
                            var parent = xtreePrivilege.GetParent(checkedArrays[i].value);
                            if(parent){
                                privilegeDataParent.privilegeId = parent.value;
                            }else{
                                privilegeDataParent.privilegeId = checkedArrays[i].value;
                            }
                            addPrivilegeIfAbsent(privilegeDataArray,privilegeData);
                            addPrivilegeIfAbsent(privilegeDataArray,privilegeDataParent);
                        }
                    }
                    var userPrivilegeJsonStr = "All";
                    if(privilegeDataArray.length > 0){
                        userPrivilegeJsonStr = JSON.stringify(privilegeDataArray);
                    }
                    $.post('/user/assignUserDataPrivilege',{
                        'userPrivilegeJsonStr':userPrivilegeJsonStr,
                        'userId':data.id
                    },function(result){
                        if(result.status == 200){
                            layer.close(index);
                            layer.msg('数据授权成功！');
                        }else{
                            layer.msg(result.msg);
                        }
                    });

                    return false;
                }
                ,btn2: function(index, layero){
                    //按钮【按钮二】的回调
                    //return false; //开启该代码可禁止点击该按钮关闭
                }
            });
        }
    });

    $("#addUserBtn").click(function(data){
        $("#addUserDiv")[0].reset();
        $("#id").val("");
        addindex = layer.open({
            title: '添加用户'
            ,type:1
            ,anim:5
            ,btnAlign: 'r' //按钮居中
            ,closeBtn:2
            ,area:['500px','400px']
            ,content: $('#addUserDiv')//捕获的元素
            ,scrollbar: false
            ,shadeClose: true
            ,btnAlign: 'r'
            ,btn: ['立即提交', '关闭']
            ,yes: function(index, layero){
                $("#btn_submit").click();
                return false;
            }
            ,btn2: function(index, layero){
                //按钮【按钮二】的回调
                //return false; //开启该代码可禁止点击该按钮关闭
            }
        });

    });

    form.on('submit(add)', function(data){
            if($('#password1').val() != $('#passwordConfirm').val()) {
                layer.msg('两次输入密码不一致!');
                return false;
            }
            $.post('/user/addUser',{
                'userName':data.field.userNameAdd,
                'passwordSha1':sha1(data.field.password1),
                'nickName':data.field.nickNameAdd
               },function(result){
                if(result.status == 200){
                    layer.close(addindex);
                    layer.msg('用户添加成功！');
                    active['reload'].call(this);
                }else{
                    layer.msg(result.msg);
                }
            });

        return false;
    });
});

function addMenuIfAbsent(array,menuData){
    var found = false;
    for(var i = 0;i < array.length;i++){
          if(array[i].menuId == menuData.menuId){
              found = true;
              break;
          }
    }
    if(!found){
        array.push(menuData);
    }

}

function addPrivilegeIfAbsent(array,privilegeData){
    var found = false;
    for(var i = 0;i < array.length;i++){
        if(array[i].privilegeId == privilegeData.privilegeId){
            found = true;
            break;
        }
    }
    if(!found){
        array.push(privilegeData);
    }

}

$.get("common/header.html",function(data){
    $("#header").html(data);
});

$.get("common/footer.html",function(data){
    $("#footer").html(data);
});

