$(function(){
  var userid = $('input[name="userid"]').val();
  var familyid = $('input[name="familyid"]').val();
  var baseUrl = $('input[name="baseUrl"]').val();
  var defaultChoose = "支出";
  switchClassifyData(baseUrl+"/api/getAccountClassifyList?userId="+userid+"&familyId="+familyid+"&type="+defaultChoose,defaultChoose);

  //初始化时间
  $('input[name="recordDate"]').datetimepicker({
    autoclose:true
  });

  var result = $('.ios-switch');
  $('.ios-switch').change(function(){
    console.log($('.ios-switch').is(':checked'))
    if($('.ios-switch').is(':checked')) {
      //收入
      defaultChoose="收入";
    } else {
      defaultChoose="支出";
    }
    $('#classifyInfo').text(defaultChoose);
    $('input[name="recordType"]').val(defaultChoose);
    switchClassifyData(baseUrl+"/api/getAccountClassifyList?userId="+userid+"&familyId="+familyid+"&type="+defaultChoose,defaultChoose);
  });

  function switchClassifyData(url) {
    var selectObj = $('.cf-select');
    //清空已有选项
    selectObj.empty();
    // 初始化分类选项
    httpAction(url,"get",{}).then(function(resp){
      console.log(resp);
      if(resp && resp.returnCode==0) {
        for(var i in resp.classifyList) {
          var item = resp.classifyList[i];
          selectObj.append('<option value="'+item.name+'">'+item.name+'</option>')
        }
      }
    },function(err){
      alert(err);
    });
  }

  //新增分类操作
  $('#addClassifyBtn').click(function(){
    $('#classifyPrompt').modal({
      relatedTarget: this,
      onConfirm:function(e) {
        console.log('你输入的是:>>>'+e.data);
        if(e.data.trim().length>0) {
          //添加账本分类
          httpAction(baseUrl+'/api/createClassify','post',{name:e.data,type:defaultChoose}).then(function(resp){
            if(resp && resp.returnCode==0) {
              showMsg('success','账本分类添加成功');
            } else {
              showMsg('error','网络异常');
            }
          },function(err) {
            showMsg('error','网络异常,'+JSON.stringify(err));
          })
        }
      },
      onCancel:function(e) {
      }
    });
  });

});