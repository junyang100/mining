layui.use(['table','form','element'], function(){
        //初始化表格工具
        var table = layui.table, form  = layui.form, element = layui.element;
        var updateindex;
        var addindex;
        table.render({
            elem: '#demo'
            ,size:'sm'
            ,even:true
            ,height:'full-228'
            ,method:'post'
            ,url: '/config/list' //数据接口
            ,page: true //开启分页
            ,limit:30
            ,loading:true//开启加载条
            ,cols:  [[ //标题栏
                {field: 'application', title: '服务名称', width:160, fixed: 'left'}
                ,{field: 'key', title: 'Key', width:300, fixed: 'left'}
                ,{field: 'value', title: 'Value', width: 400}
                ,{field: 'profile',title:'环境',width:100, templet: function(d){
                    if(d.profile_desc == null || d.profile_desc == '') {
                        return d.profile
                    }else {
                        return d.profile + '<i class="layui-icon" title="' + d.profile_desc + '">&#xe60a;</i>'
                    }
                }}
                ,{field: 'label', title: '版本号', width:100, templet: function(d){
                    if(d.label_desc == null || d.label_desc == '') {
                        return d.label
                    }else {
                        return d.label + '<i class="layui-icon" title="' + d.label_desc + '">&#xe60a;</i>'
                    }
                }}
                ,{field: 'configGroup',title:'配置类型',width:100}
                ,{field: 'description', title: '配置说明', width: 600}
                ,{field: 'score', title: '操作', width: 125, toolbar: '#barDemo', fixed: 'right'}
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
            var application1 = $('#application1');
            var key1 = $('#key1');
            var config_group1 = $('#config_group1');
            var profile1 = $('#profile1');
            //执行重载
            table.reload('demoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    application12: application1.val(),
                    key12:key1.val(),
                    config_group12:config_group1.val(),
                    profile12:profile1.val()
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
    form.on('select(aihao)', function(data){
        active['reload'].call(this);
    });

    table.on('tool(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
        ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'del'){
            layer.confirm('确认删除Key\"'+data.key+'\"么？', function(index){
                $.post('/config/delete',{id:data.id},function(data){
                        if(data.status == 200){
                            layer.msg('删除成功！');
                            obj.del(); //删除对应行（tr）的DOM结构
                            layer.close(index);
                        }else{
                            layer.msg('删除失败');
                        }
                });

            });
        }else if(layEvent === 'edit'){
            document.getElementById("UpdateConfigDiv").reset();
            updateindex = layer.open({
                title: '修改配置'
                ,type:1
                ,anim:5
                ,btnAlign: 'c' //按钮居中
                ,closeBtn:2
                ,area:['600px','700px']
                ,content: $('#UpdateConfigDiv')//捕获的元素
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
            //赋值
            $("#application").val(data.application);
            $("#profile").val(data.profile);
            $("#label").val(data.label);
            $("#config_group").val(data.configGroup);
            $("#key").val(data.key);
            $("#value").val(data.value);
            $("#description").val(data.description);
            $("#profile_desc").val(data.profileDesc);
            $("#label_desc").val(data.labelDesc);
            $("#id").val(data.id);
            $("#update_div").show();
            $("#refershDes").show();
        }else if(layEvent === 'copy'){
            document.getElementById("UpdateConfigDiv").reset();
            addindex = layer.open({
                title: '复制配置'
                ,type:1
                ,anim:5
                ,btnAlign: 'c' //按钮居中
                ,closeBtn:2
                ,area:['600px','650px']
                ,content: $('#UpdateConfigDiv')//捕获的元素
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
            $("#update_div").hide();
            $("#refershDes").hide();
            //赋值
            $("#id").val("");
            $("#application").val(data.application);
            $("#profile").val(data.profile);
            $("#label").val(data.label);
            $("#config_group").val(data.configGroup);
            $("#key").val(data.key);
            $("#value").val(data.value);
            $("#description").val(data.description);
            $("#profile_desc").val(data.profileDesc);
            $("#label_desc").val(data.labelDesc);

        }
    });

    $("#AddConfig").click(function(data){
        $("#UpdateConfigDiv")[0].reset();
        $("#id").val("");
        $("#update_div").hide();
        $("#refershDes").hide();
        addindex = layer.open({
            title: '增加配置'
            ,type:1
            ,anim:5
            ,btnAlign: 'r' //按钮居中
            ,closeBtn:2
            ,area:['600px','650px']
            ,content: $('#UpdateConfigDiv')//捕获的元素
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
        if(data.field.id){
            $.post('/config/update',{
                'id':data.field.id,
                'application':data.field.application,
                'profile':data.field.profile,
                'profile_desc':data.field.profile_desc,
                'label':data.field.label,
                'label_desc':data.field.label_desc,
                'key':data.field.key,
                'value':data.field.value,
                'description':data.field.description,
                'flag':data.field.flag,
                'config_group':data.field.config_group,
                'modifyDescription':data.field.modifyDescription
                },function(result){
                if(result.status == 200){
                    layer.close(updateindex);
                    layer.msg('配置信息修改成功！');
                    active['reload'].call(this);
                }else{
                    layer.msg(result.msg);
                }
            });
        }else{
            $.post('/config/add',{
                'application':data.field.application,
                'profile':data.field.profile,
                'label':data.field.label,
                'label_desc':data.field.label_desc,
                'key':data.field.key,
                'value':data.field.value,
                'config_group':data.field.config_group,
                'profile_desc':data.field.profile_desc,
                'description':data.field.description
                },function(result){
                if(result.status == 200){
                    layer.close(addindex);
                    layer.msg('配置信息添加成功！');
                    active['reload'].call(this);
                }else{
                    layer.msg(result.msg);
                }
            });
        }
        return false;
    });

    //监听指定开关
    form.on('switch(switchTest)', function(data){
        if(this.checked){
            $("#refershDes").show();
        }else{
            $("#refershDes").hide();
        }

    });


    //为下拉列表增加选项
    $.post('/platform/listPlatformAndApp',{
        'page' : 1,
        'limit' : 100
    },function(result){
        if(result.status == 200){
            document.getElementById("UpdateConfigDiv").reset();//加上这个，列表页的下来菜单才能将平台初始化进去
            $.each(result.data, function() {
                $("#application").append("<optgroup label='" + this.key + "'>");
                $("#application1").append("<optgroup label='" + this.key + "'>");
                $.each(this.value, function() {
                    $("#application").append("<option value='" + this.application_id + "'>" + this.application_name + "(" + this.application_id + ")" + "</option>");
                    $("#application1").append("<option value='" + this.application_id + "'>" + this.application_name + "(" + this.application_id + ")" + "</option>");
                });
                $("#application").append("</optgroup>");
                $("#application1").append("</optgroup>");
            });
        }else{

        }
    });
});

$.get("common/header.html",function(data){
    $("#header").html(data);
});

$.get("common/footer.html",function(data){
    $("#footer").html(data);
});

