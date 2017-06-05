$(function(){
  var baseUrl = $('input[name="baseUrl"]').val();
  var userid = $('input[name="userid"]').val();
  var currentPage = 0;
  var pageSize = 1;
  var currentPagnate = 1;
  var type = "";
  var word = "";
  //初始化数据
  generateClassifyData(userid,currentPage,type,word);

  //下拉账本分类
  $('#cSelect').change(function() {
    var val = $(this).val();
    if(val == 'all') {
      type=""
    } else {
      type = val
    }
    currentPage=0
    currentPagnate=1
    generateClassifyData(userid,currentPage,type);
  });

  //搜索
  $('#cSearch').keyup(function() {
    console.log($(this).val());
    var searchVal = $(this).val().trim();
    currentPage = 0;
    currentPagnate = 1;
    if(searchVal.length>0) {
      word = searchVal;
      generateClassifyData(userid,currentPage,type,word);
    } else {
      generateClassifyData(userid,currentPage,type);
    }
  });

  function generateClassifyData(userId,currentPage,type,word) {
    var basicUrl = baseUrl+'/api/getClassifyList?userId='+userid+'&currentPage='+currentPage;
    if(type != undefined && type != "") {
      basicUrl+='&type='+type;
    }
    if(word != undefined && word != "") {
      basicUrl+='&word='+word;
    }
    httpAction(basicUrl,'get',{}).then(function(res){
    if(res && res.returnCode==0) {
      $('.am-table tbody').empty();
      pageSize = res.totalPage;
      $('#totalPage').text(pageSize);
      $('.pagIndex').text(currentPagnate);
      if(res.cList.length>0) {
        for(var i in res.cList) {
          var item = res.cList[i];
          var updateTime = moment(item.updateTime).format("YYYY-MM-DD HH:mm:ss");
          var actionPart = '<div class="tpl-table-black-operation"><a class="mr-5 classify-update-btn" data-id="'+item.id+'" data-name="'+item.name+'"><i class="am-icon-pencil mr-5"></i>编辑</a></div>';
          $('.am-table tbody').append('<tr><td>'+item.name+'</td><td>'+item.type+'</td><td>'+updateTime+'</td><td>'+actionPart+'</td></tr>');
        }
        bindModifyAction();
      } else {
        $('.am-table tbody').append('<tr><td colspan="6" style="text-align:center">非常抱歉未找到您要的信息</td></tr>')
      }
    }

   },function(err){
    alert('error');
  });
  }

  //分类修改
  function bindModifyAction() {
    $('.classify-update-btn').click(function() {
      console.log($(this).data("id"));
      var id = $(this).data("id");
      var name = $(this).data("name");
      $('input.am-modal-prompt-input').val(name);
      $('#classifyPrompt').modal({
        relatedTarget: this,
        onConfirm: function(e) {
          alert('你输入的是：' + e.data || '')
          if(e.data.trim().length>0) {
            httpAction(baseUrl+"/api/updateClassify",'post',{id:id,name:e.data}).then(function(res){
              if(res.returnCode == 0) {
                showMsg('success','账本分类更新成功');
                generateClassifyData(userid,currentPage,type,word);
              } else {
                showMsg('error','账本分类更新失败');
              }
            },function(err){
              alert('网络异常:>>>'+JSON.stringify(err));
            });
          } else {
            showMsg('error','内容不能为空');
          }
        },
        onCancel: function(e) {
        }
      });
    });
  }



});