//const define
var progress = $.AMUI.progress;
var Bluebird = Promise.noConflict()

function httpAction(url,type,params) {
  pageLoad();
  return new Bluebird(function(resolve,reject){
    $.ajax({
      url:url,
      type:type,
      dataType:'json',
      data:params,
      success:function(data){
        pageDone();
        resolve(data);
      },
      error:function(err) {
        pageDone();
        reject(err);
      }
    });
  });
}


function pageLoad() {
  progress.start();
}

function pageDone() {
  progress.done();
}

//消息错误提示
function msgTemp(type,content) {
  var temp = "";
  switch(type) {
    case 'error':
      temp='<div class="am-alert am-alert-danger s-alert am-animation-slide-right am-animation-delay-1">'+content+'</div>'
      break;
    case 'success':
      temp='<div class="am-alert am-alert-success s-alert am-animation-slide-right am-animation-delay-1">'+content+'</div>'
      break;
    default:
      temp="";
    break;
  }
  return temp;
}

function showMsg(type,content) {
  $('body>div').append(msgTemp(type,content));
  setTimeout(function(){
    if(type=='error') {
      $('.s-alert.am-alert-danger').addClass('am-animation-fade');
      setTimeout(function(){
        $('.s-alert.am-alert-danger').remove();
      },1000);
    } else {
      $('.s-alert.am-alert-'+type).addClass('am-animation-fade');
      setTimeout(function(){
        $('.s-alert.am-alert-'+type).remove();
      })
    }

  },5000);

}

// 侧边菜单开关
function autoLeftNav() {
  $('.tpl-header-switch-button').on('click', function() {
    if ($('.left-sidebar').is('.active')) {
      if ($(window).width() > 1024) {
          $('.tpl-content-wrapper').removeClass('active');
      }
      $('.left-sidebar').removeClass('active');
    } else {

      $('.left-sidebar').addClass('active');
      if ($(window).width() > 1024) {
          $('.tpl-content-wrapper').addClass('active');
      }
    }
  })

  if ($(window).width() < 1024) {
    $('.left-sidebar').addClass('active');
  } else {
    $('.left-sidebar').removeClass('active');
  }
}

// 侧边菜单
$('.sidebar-nav-sub-title').on('click', function() {
  $(this).siblings('.sidebar-nav-sub').slideToggle(80)
    .end()
    .find('.sidebar-nav-sub-ico').toggleClass('sidebar-nav-sub-ico-rotate');
});

function chooseFamily() {
  // httpAction()
  $('#rightFamilyList li').click(function() {
    var id = $(this).data("id");
    var baseCsrf = $('input[name="rootcsrf"]').val();
    var rootBase = $('input[name="rootbase"]').val();
    httpAction(rootBase+'/backend/family/chooseUserFamily','post',{familyId: id,_csrf: baseCsrf}).then(function(resp){
      if(resp && resp.returnCode == 0) {
        window.location.href=rootBase + "/backend";
      } else {
        showMsg('error',resp.errMsg);
      }
    })
  })
}

// 侧边栏高亮
function hightSideBar() {
  var path = window.location.pathname;
  if(path.substring(path.length-1,path.length) == '/') {
    path = path.substring(0,path.length-1)
  }
  var pathArr = [/^\/backend$/, /^\/backend\/accountBook[\/|\w]{0,}$/, /^\/backend\/classify[\/|\w]{0,}$/]
  console.log($('.sidebar-nav .item a'))
  $('.sidebar-nav .item a').removeClass('active');
  for(var i=0; i<pathArr.length; i++) {
    var item = pathArr[i]
    if(item.test(path)){
      $('.sidebar-nav .item a').eq(i).addClass('active');
      if(i == 2) {
        $('.sidebar-nav-sub-title').click()
      }
      break;
    }
  }
}


$(function() {
  autoLeftNav();
  $(window).resize(function() {
      autoLeftNav();
  });
  hightSideBar();
  console.log(window.location.pathname);
  var baseCsrf = $('input[name="rootcsrf"]').val();
  var rootBase = $('input[name="rootbase"]').val();
  var familyArray = [];
  // 创建家庭
  $('#rightSideFamilyCreate').click(function() {
    $('#familyPrompt').modal({
      relatedTarget: this,
      onConfirm: function(e) {
        console.log(e.data)
        var familyName = e.data || '';
        if(familyName.trim().length==0) {
          showMsg('error', '家庭名不能为空');
        } else {
          httpAction(rootBase+'/backend/family/create','post', {_csrf:baseCsrf,name: familyName}).then(function(resp){
            console.log(resp);
            if(resp.returnCode == 0) {
              showMsg('success', '家庭创建');
              $('#familyLists').offCanvas('close');
              $()
            } else {
              showMsg('error', resp.errMsg);
            }
          },function(err){
            console.log(err);
            showMsg('error',JSON.stringify(err));
          });
        }
      }
    })
  });
  // 打开右侧边栏
  $('#openRightBtn').click(function() {
    var toggle = $(this).data('ref');
    if(toggle == 'open') {
      httpAction(rootBase+'/backend/family/list','get', {}).then(function(resp){
        console.log(resp);
        if (resp.returnCode == 0 && resp.list.length > 0) {
          $('#rightFamilyList').empty()
          for(var k in resp.list) {
            var temp = resp.list[k]
            if(temp[5] == 1) {
              $('#rightFamilyList').append('<li class="right-item" data-id="'+temp[3]+'">'+temp[8]+'<span class="am-fr am-icon-check am-success am-icon-md" style="color:#5eb95e"></span></li>')
            } else {
              $('#rightFamilyList').append('<li class="right-item" data-id="'+temp[3]+'">'+temp[8]+'</li>')
            }
          }
          chooseFamily()
          $('#familyLists').offCanvas(toggle);
        }
      },function(err){
        showMsg('error', JSON.stringify(err))
      })
    } else {
      $('#familyLists').offCanvas(toggle);
    }
  });
  // 家庭列表选择侧边栏控制
  $('#familyLists').on('open.offcanvas.amui',function(){
    console.log('right sidebar open');
    $('#openRightBtn').data('ref','close');
  });
  $('#familyLists').on('close.offcanvas.amui',function(){
    $('#openRightBtn').data('ref','open');
  })
});