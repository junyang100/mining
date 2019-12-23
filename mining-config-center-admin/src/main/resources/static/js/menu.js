layui.use(['table','form','element'], function(){
    //初始化表格工具
    var table = layui.table, form  = layui.form, element = layui.element;
    var addindex;
    var upateindex;
    table.render({
        elem: '#menuTable'
        ,size:'sm'
        ,even:true
        ,height:'full-228'
        ,method:'post'
        ,url: '/menu/queryMenuList' //数据接口
        ,page: true //开启分页
        ,limit:30
        ,loading:true//开启加载条
        ,cols:  [[ //标题栏
            {field: 'menuDisplayname', title: '菜单名称', width:'20%', fixed: 'left'}
            ,{field: 'menuAction', title: '菜单URL', width:'20%', fixed: 'left'}
            ,{field: 'parentMenu',title:'父菜单名称',width:'20%', templet: function(d){
                    if(d.parentMenu) {
                        return d.parentMenu.menuDisplayname;
                    }else {
                        return "";
                    }
                }}
            ,{field: 'menuOrder',title:'菜单顺序',width:'10%', }
            ,{field: 'createTime',title:'创建时间',width:'20%'}
            ,{field: 'score', title: '操作', width: '10%', toolbar: '#barDemo', fixed: 'right'}
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
                    menuName:$("#menuName").val(),
                    menuAction: $("#menuAction").val()

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
            layer.confirm('确认删除['+data.menuDisplayname+']么？', function(index){
                $.post('/menu/removeMenu',{id:data.id},function(data){
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

            $("#addMenuOrUpdateDiv")[0].reset();
            //赋值
            $("#menuNameAdd").val(data.menuName);
            $("#menuDisplaynameAdd").val(data.menuDisplayname);
            $("#menuActionAdd").val(data.menuAction);
            $("#parentMenuId").val(data.menuParent);
            $("#menuOrderAdd").val(data.menuOrder);
            $("#id").val(data.id);
            upateindex = layer.open({
                title: '修改配置'
                ,type:1
                ,anim:5
                ,btnAlign: 'c' //按钮居中
                ,closeBtn:2
                ,area:['600px','500px']
                ,content: $('#addMenuOrUpdateDiv')//捕获的元素
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

        }
    });

    $("#addMenuBtn").click(function(data){
        $("#addMenuOrUpdateDiv")[0].reset();
        $("#id").val("");
        addindex = layer.open({
            title: '添加菜单'
            ,type:1
            ,anim:5
            ,btnAlign: 'r' //按钮居中
            ,closeBtn:2
            ,area:['600px','500px']
            ,content: $('#addMenuOrUpdateDiv')//捕获的元素
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

        if(data.field.id) {
            $.post('/menu/modifyMenu', {
                'id': data.field.id,
                'menuName':data.field.menuNameAdd,
                'menuDisplayname':data.field.menuDisplaynameAdd,
                'menuAction':data.field.menuActionAdd,
                'menuParent':data.field.parentMenuId,
                'menuOrder':data.field.menuOrderAdd
            }, function (result) {
                if (result.status == 200) {
                    layer.close(upateindex);
                    layer.msg('菜单修改成功！');
                    active['reload'].call(this);
                } else {
                    layer.msg(result.msg);
                }
            });
        }else{
            $.post('/menu/addMenu',{
                'menuName':data.field.menuNameAdd,
                'menuDisplayname':data.field.menuDisplaynameAdd,
                'menuAction':data.field.menuActionAdd,
                'menuParent':data.field.parentMenuId,
                'menuOrder':data.field.menuOrderAdd
            },function(result){
                if(result.status == 200){
                    layer.close(addindex);
                    layer.msg('菜单添加成功！');
                    active['reload'].call(this);
                }else{
                    layer.msg(result.msg);
                }
            });
        }
        return false;
    });

    initParentMenu();
});


function initParentMenu(){
    //为下拉列表增加选项
    $.post('/menu/queryParentMenu',{

    },function(result){
        if(result.status == 200){
            $("#parentMenuId").empty();
            $("#parentMenuId").append(" <option value=\"0\">请选父菜单</option>");
            $.each(result.data, function(index,element) {
                $("#parentMenuId").append("<option value='" + element.id + "'>" + element.menuDisplayname + "</option>");

            });
        }else{
            layer.msg("父菜单加载失败！")
        }
    });
}



$.get("common/header.html",function(data){
    $("#header").html(data);
});

$.get("common/footer.html",function(data){
    $("#footer").html(data);
});

