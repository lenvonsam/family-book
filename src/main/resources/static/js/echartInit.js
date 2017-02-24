$(function(){
  var echartsA = echarts.init(document.getElementById('indexTplEcharts'));

  option = {
      tooltip: {
          trigger: 'axis'
      },
      grid: {
          top: '3%',
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
      },
      xAxis: [{
          type: 'category',
          boundaryGap: false,
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      }],
      yAxis: [{
          type: 'value'
      }],
      textStyle: {
          color: '#838FA1'
      },
      series: [{
          name: '邮件营销',
          type: 'line',
          stack: '总量',
          areaStyle: { normal: {} },
          data: [120, 132, 101, 134, 90],
          itemStyle: {
              normal: {
                  color: '#1cabdb',
                  borderColor: '#1cabdb',
                  borderWidth: '2',
                  borderType: 'solid',
                  opacity: '1'
              },
              emphasis: {

              }
          }
      }]
  };

  echartsA.setOption(option);

  $('#btnModal').click(function(){
    alert('xxx');
    $('#familyChooseInfo').modal('open');
  });
});
