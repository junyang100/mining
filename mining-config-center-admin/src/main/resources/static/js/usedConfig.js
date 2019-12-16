layui.use(['table','form','element'], function(){
        //初始化表格工具
        var table = layui.table
        table.render({
            elem: '#usedAppConfig'
            ,size:'sm'
            ,even:true
            ,height:'full-228',
            page:false
            ,loading:true//开启加载条
            ,cols:  [[ //标题栏
                {field: 'application', title: '服务名称', width:"10%", fixed: 'left'}
                ,{field: 'key', title: 'Key', width:"30%", fixed: 'left'}
                ,{field: 'value', title: 'Value', width: "30%"}
                ,{field: 'profile',title:'环境',width:"15%", templet: function(d){
                        return d.profile;
                }}
                ,{field: 'label', title: '版本号', width:"15%", templet: function(d){
                        return d.label;
                }}
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
            var application = $('#application');
            var profile = $('#profile');
            var label = $("#label");
            var key = $("#key");
            //执行重载
            table.reload('demoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                },
                url: '/config/listUsedConfigs'//数据接口
                ,method:'post'
                ,page: false //开启分页
                ,where: {
                    application: application.val(),
                    profile:profile.val(),
                    label:label.val(),
                    key : key.val()
                }
            });
        }
    };

    $('#queryBtnId').on('click', function(){
        var type = layui.$(this).data('type');
        active[type] ? active[type].call(this) : '';
        return false;
    });


    //为下拉列表增加选项
    $.post('/platform/listPlatformAndApp',{
        'page' : 1,
        'limit' : 100
    },function(result){
        if(result.status == 200){
           document.getElementById("queryConditionForm").reset();//加上这个，列表页的下来菜单才能将平台初始化进去
           for(var i = 0;i < result.data.length;i++){
               var outData = result.data[i];
               $("#application").append("<optgroup label='" + outData.key + "'>");
               for(var j = 0;j < outData.value.length;j++){
                   var innerData = outData.value[j];
                   $("#application").append("<option value='" + innerData.application_id + "'>" + innerData.application_name + "(" + innerData.application_id + ")" + "</option>");
               }
               $("#application").append("</optgroup>");
           }
        }else{
            layer.msg('加载服务列表失败');
        }
    });
});

$.get("common/header.html",function(data){
    $("#header").html(data);
});

$.get("common/footer.html",function(data){
    $("#footer").html(data);
});

