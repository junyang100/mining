layui.use(['table','form','element'], function(){
        //初始化表格工具
        var table = layui.table
        table.render({
             elem: '#configChangePush'
            ,size:'sm'
            ,even:true
            ,height:'full-228'
            ,page: true //开启分页
            ,loading:true//开启加载条
            ,cols:  [[ //标题栏
                 {field: 'application', title: '服务名称', width:"10%", fixed: 'left'}
                ,{field: 'applicationIp', title: '应用IP', width:"10%", fixed: 'left'}
                ,{field: 'applicationPort', title: '应用端口号', width: "5%"}
                ,{field: 'pushStatus',title:'推送状态',width:"5%", templet: function(d){
                        if(d.pushStatus == 1) {
                            return "推送中"
                        }else if(d.pushStatus == 2){
                            return "推送成功"
                        }else if(d.pushStatus == 3){
                            return "推送失败"
                        }
                }}
                ,{field: 'pushTime', title: '推送时间', width:"10%"}
                ,{field: 'pushFinishTime', title: '推送完成时间', width: "10%"}
                ,{field: 'pushUserName', title: '操作人', width: "10%"}
                ,{field: 'pushDescription', title: '描述', width: "40%"}
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
            var pushStatus = $('#pushStatus');
            var beginTime =  $('#dateBegin');
            var endTime =   $('#dateEnd');
            //执行重载
            table.reload('demoReload', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,page: true //开启分页
                ,url: '/configChangePush/queryConfigChangePushInfo'//数据接口
                ,method:'post'
                ,where: {
                    application: application.val(),
                    pushStatus:pushStatus.val(),
                    beginTimeStr:beginTime.val(),
                    endTimeStr :endTime.val()
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

