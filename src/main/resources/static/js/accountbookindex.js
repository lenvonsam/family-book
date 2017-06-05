$(function(){
  var baseUrl = $('input[name="baseUrl"]').val();
  var userid = $('input[name="userid"]').val();
  var familyid = $('input[name="familyid"]').val();
  var currentPage = 0;
  var pageSize = 1;
  var currentPagnate = 1;
  var type = "";
  var word = "";
  //初始化数据
  generateAccountData(familyid,userid,currentPage);

  //下拉账本分类
  $('#abSelect').change(function() {
    var val = $(this).val();
    if(val == 'all') {
      type=""
    } else {
      type = val
    }
    currentPage=0
    currentPagnate=1
    generateAccountData(familyid,userid,currentPage,type);
  });

  //搜索
  $('#abSearch').keyup(function() {
    console.log($(this).val());
    var searchVal = $(this).val().trim();
    currentPage = 0;
    currentPagnate = 1;
    if(searchVal.length>0) {
      word = searchVal;
      generateAccountData(familyid,userid,currentPage,type,word);
    } else {
      generateAccountData(familyid,userid,currentPage,type);
    }
  });

  function generateAccountData(familyid,userId,currentPage,type,word) {
    var basicUrl = baseUrl+'/api/getAccountbookList?familyId='+familyid+'&userId='+userid+'&currentPage='+currentPage;
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
      if(res.abList.length>0) {
        for(var i in res.abList) {
          var item = res.abList[i];
          var recordTime = moment(item.recordTime).format("YYYY-MM-DD HH:mm:ss");
          var actionPart = '<div class="tpl-table-black-operation"><a class="mr-5" href="'+baseUrl+'/backend/accountBook/edit/'+item.id+'"><i class="am-icon-pencil mr-5"></i>编辑</a><a class="tpl-table-black-operation-detail"><i class="am-icon-eye mr-5"></i>详情</a><a class="tpl-table-black-operation-del ml-5"><i class="am-icon-trash mr-5"></i>删除</a></div>';
          $('.am-table tbody').append('<tr><td>'+item.recordType+'</td><td>'+item.classifyName+'</td><td>'+recordTime+'</td><td>'+item.price+'</td><td>'+item.owerName+'</td><td>'+actionPart+'</td></tr>');
        }
      } else {
        $('.am-table tbody').append('<tr><td colspan="6" style="text-align:center">非常抱歉未找到您要的信息</td></tr>')
      }
    }

   },function(err){
    alert('error');
  });
  }
});

function goToEditPage(id,baseUrl) {
  console.log('1xx')
  location.href=baseUrl+'/backend/accountBook/edit/'+id;
}