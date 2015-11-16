<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>评论中心数据平台</title>
	<%@ include file="/inc/application.inc" %>
	
	<script type="text/javascript">
		var chart;

		var chartData = '${arr}';
		chartData = eval("(" + chartData + ")");

		AmCharts.ready(function() {
			// SERIAL CHART
			chart = new AmCharts.AmSerialChart();
			chart.dataProvider = chartData;
			chart.categoryField = "day";
			chart.startDuration = 0.5;
			chart.balloon.color = "#000000";

			// AXES
			// category
			var categoryAxis = chart.categoryAxis;
			categoryAxis.fillAlpha = 1;
			categoryAxis.fillColor = "#FAFAFA";
			categoryAxis.gridAlpha = 0;
			categoryAxis.axisAlpha = 0;
			categoryAxis.gridPosition = "start";
			// categoryAxis.position = "top";

			// value
			var valueAxis = new AmCharts.ValueAxis();
			valueAxis.title = "数量";
			valueAxis.dashLength = 5;
			valueAxis.axisAlpha = 0;
			valueAxis.minimum = 0;
			valueAxis.maximum = ${max};
			valueAxis.integersOnly = true;
			valueAxis.gridCount = 10;
			// valueAxis.reversed = true; // this line makes the value axis reversed
			chart.addValueAxis(valueAxis);

			// GRAPHS

			// Germany graph
			var graph = new AmCharts.AmGraph();
			graph.title = "count1";
			graph.valueField = "count1";
			graph.balloonText = "count1| [[category]]| [[value]]";
			graph.bullet = "round";
			chart.addGraph(graph);

			var graph = new AmCharts.AmGraph();
			graph.title = "count2";
			graph.valueField = "count2";
			graph.balloonText = "count2| [[category]]| [[value]]";
			graph.bullet = "round";
			chart.addGraph(graph);
			
			// LEGEND
			var legend = new AmCharts.AmLegend();
			legend.markerType = "circle";
			chart.addLegend(legend);

			// WRITE
			chart.write("chartdiv");
		});
	</script>
</head>
<body>
	<div class="container-fluid">
		<!-- 图表展示 -->
		<div class="row-fluid">
			<div class="span12">
				<div class="well well-small">
					<h4>评论中心数据查询</h4>
					<h5><a href="index">返回首页</a><h5>
					<form action="search1" method="get" class="form-horizontal">
						<div class="control-group">
							<label class="control-label">时间段(时:分:秒)(必填)</label>
							<div class="controls">
								<input  class="dayview"  type="text"  name="beginDate" value="${beginDate}"  data-date-format="hh:ii:ss"/>
								<input  class="dayview" type="text"  name="endDate" value="${endDate}" data-date-format="hh:ii:ss"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">对比日期1(必填)</label>
							<div class="controls">
									<input  class="monthview" type="text"  name="cDate1" value="${cDate1}"  data-date-format="yyyy-mm-dd"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label">对比日期2</label>
							<div class="controls">
									<input  class="monthview" type="text"  name="cDate2" value="${cDate2}"  data-date-format="yyyy-mm-dd"/>
							</div>
						</div>
						
						<div class="control-group">
				            <label class="control-label" >查询接口</label>
				            <div class="controls">
				              <select id="dropLang">
				              <option selected="selected" value="0"><Please Select></option>   
				                <option value="1">GET_ENTRY</option>
				                <option value="2">COMMENT_CACHE</option>
				                <option value="3">EXTERNAL_API</option>
				                <option value="4">COMMENT_API</option>
				                <option value="5">THREAD_QUEUE</option>
				              </select>
				              <select name="queryJiekou" id="dropFrame">
				              <option selected="selected" value="0"><Please Select></option>  
				                <option  parentid="1">get-photo</option>
				                <option  parentid="1">get-status</option>
				                <option  parentid="1">get-share</option>
				                <option  parentid="1">get-album</option>
				                <option  parentid="1">get-blog</option>
				                
				                <option  parentid="2">get-commentedUserList-from-cache</option>
				                <option  parentid="2">get-firstPage-from-cache</option>
				                <option  parentid="2">get-global-comment-from-cache</option>
				                <option  parentid="2">get-global-count-batch-from-cache</option>
				                 <option  parentid="2">get-count-batch-from-cache</option>
				                 <option  parentid="2">get-count-from-cache</option>
				                 
				                 <option  parentid="3">at-filter</option>
				                 <option  parentid="3">get-like-detail-batch</option>
				                 <option  parentid="3">at-format</option>
				                 <option  parentid="3">get-short-url-original-content-batch</option>
				                 
				                 
				                 <option parentid="4">get-multi-comments</option>
				                 <option  parentid="4">get-count-batch</option>
				                 <option parentid="4">get-comment-list</option>
				                 <option parentid="4">get-head-and-tail-comment-list</option>
				                 <option  parentid="4">create-comment</option>
				                 <option  parentid="4">get-comment</option>
				                 <option  parentid="4">remove-comment</option>
				                 <option  parentid="4">create-linked-comment</option>
				                 <option  parentid="4">get-global-comment-list</option>
				                 <option  parentid="4">get-count</option>
				                 
				                 <option parentid="5">asynJob</option>
				                 <option  parentid="5">asynGlobalJob</option>
				                 <option  parentid="5">asynPrintJob</option>
		                 
				              </select>

				            </div>
				          </div>
						
						<div class="control-group">
				            <label class="control-label" >查询指标</label>
				            <div class="controls">
				              <select name="queryZhibiao">
				              <option selected="selected" value="0"><Please Select></option>   
				                <option>count</option>
				                <option >maxTime</option>
				                <option >avgTime</option>
				                <option>missCount</option>
				                <option >timeoutCount</option>
				                <option >exceptionCount</option>
				              </select>
				              </div>
				              </div>
						
						<div class="control-group">
							<div class="controls">
								<input type="submit" class="btn" value="查询" />
							</div>
						</div>

					</form>
					<div id="chartdiv" style="width: 100%; height: 400px;"></div>
				</div>
			</div>

			
		</div>
	</div>
	<script>
	var date1 = new Date();
	$('.dayview').datetimepicker({
        language:  'en',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		startView: 1,
		minView: 0,
		maxView: 1,
		forceParse: 0,
		endDate:date1
    });
	$('.monthview').datetimepicker({
        language:  'en',
        weekStart: 1,
        todayBtn:  1,
		autoclose: 1,
		todayHighlight: 1,
		minView: 2,
		maxView: 1,
		forceParse: 0,
		endDate:date1
    });
	
	$(function(){  
	   $("#dropLang").unbind("change", eDropLangChange).bind("change", eDropLangChange);   
	   $("#dropFrame").unbind("change", eDropFrameChange).bind("change", eDropFrameChange);   
	});  
	var eDropLangChange = function(){  
	  var selectedValue = $(this).val();   
	  $("#dropFrame").children("span").each(function(){  
	    $(this).children().clone().replaceAll($(this));   
	  });  
	  if(parseInt(selectedValue) != 0){  
	    $("#dropFrame").children("option[parentid!='" + selectedValue + "'][value!='0']").each(function(){   
	      $(this).wrap("<span style='display:none'></span>");  
	    });   
	  }  
	};  
	var eDropFrameChange = function(){  
	  $("#dropLang").val($(this).children("option:selected").attr("parentid"));   
	};   

	</script>
</body>
</html>