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
        ,url: '/platform/list' //数据接口
        ,page: true //开启分页
        ,limit:30
        ,loading:true//开启加载条
        ,cols:  [[ //标题栏
            {field: 'id', title: '平台编码', width: 200, fixed: 'left'}
            ,{field: 'platformName', title: '平台名称'}
            ,{field: 'contactName', title: '联系人'}
            ,{field: 'contactMobile',title:'联系人电话'}
            ,{field: 'platformDesc',title:'平台说明'}
            ,{field: 'updateTime',title:'上次修改时间'}
            ,{field: 'score', title: '操作', width: 100, toolbar: '#barDemo', fixed: 'right'}
        ]]
        ,id: 'demoReload'
        ,text: {
            none: '暂无匹配数据' //默认：无数据。注：该属性为 layui 2.2.5 开始新增
        }
    });

    var active = {
        reload: function(){
            var platform_id = $('#platform_id1');
            var platform_name = $('#platform_name1');
            var contact_name = $('#contact_name1');
            //执行重载
            table.reload('demoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    id : platform_id.val(),
                    platform_name : platform_name.val(),
                    contact_name : contact_name.val()
                }
            });
        }
    };
    $('#btn_refresh').on('click', function(){
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
            layer.confirm('确认删除平台\"'+data.platformName+'\"吗？', function(index){
                $.post('/platform/delete',{id:data.id},function(data){
                    if(data.status=='SUCCESS'){
                        layer.msg('删除成功！');
                        obj.del(); //删除对应行（tr）的DOM结构
                        layer.close(index);
                    }else{
                        layer.msg('删除失败');
                    }
                });

            });
        }else if(layEvent === 'edit'){
            document.getElementById("editDiv").reset();
            $("#flag").val("update");
            updateindex = layer.open({
                title: '修改平台'
                ,type:1
                ,anim:5
                ,btnAlign: 'c' //按钮居中
                ,closeBtn:2
                ,area:['600px','500px']
                ,content: $('#editDiv')//捕获的元素
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
            $("#platform_name").val(data.platformName);
            $("#contact_name").val(data.contactName);
            $("#contact_mobile").val(data.contactMobile);
            $("#platform_desc").val(data.platformDesc);
            $("#id").val(data.id);
        }
    });

    $("#btn_add").click(function(data){
        document.getElementById("editDiv").reset();
        $("#flag").val("add");
        addindex = layer.open({
            title: '增加平台'
            ,type:1
            ,anim:5
            ,btnAlign: 'r' //按钮居中
            ,closeBtn:2
            ,area:['600px','500px']
            ,content: $('#editDiv')//捕获的元素
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
        if($("#flag").val() == 'update'){
            $.post('/platform/update',{
                'id':data.field.id,
                'platform_name':data.field.platform_name,
                'contact_name':data.field.contact_name,
                'contact_mobile':data.field.contact_mobile,
                'platform_desc':data.field.platform_desc
            },function(result){
                if(result.status=='SUCCESS'){
                    layer.close(updateindex);
                    layer.msg('平台信息修改成功！');
                    active['reload'].call(this);
                }else{

                    layer.msg('平台信息修改失败！');
                }
            });
        }else{
            $.post('/platform/add',{
                'id':data.field.id,
                'platform_name':data.field.platform_name,
                'contact_name':data.field.contact_name,
                'contact_mobile':data.field.contact_mobile,
                'platform_desc':data.field.platform_desc
            },function(result){
                if(result.status=='SUCCESS'){
                    layer.close(addindex);
                    layer.msg('平台信息添加成功！');
                    active['reload'].call(this);
                }else{
                    layer.msg('平台信息添加失败！');
                }
            });
        }
        return false;
    });
});

$.get("common/header.html",function(data){
    $("#header").html(data);
});

$.get("common/footer.html",function(data){
    $("#footer").html(data);
});
