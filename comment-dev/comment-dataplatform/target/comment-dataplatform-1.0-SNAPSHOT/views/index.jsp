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
			
			var graph = new AmCharts.AmGraph();
			graph.title = "count3";
			graph.valueField = "count3";
			graph.balloonText = "count3| [[category]]| [[value]]";
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
					<form action="search" method="get" class="form-horizontal">
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
							<label class="control-label">对比日期3</label>
							<div class="controls">
									<input  class="monthview" type="text"  name="cDate3" value="${cDate3}"  data-date-format="yyyy-mm-dd"/>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">查询接口(eg:create-linked-comment)</label>
							<div class="controls">
								<input type="text" id="src" name="queryJiekou"  value="${queryJiekou}" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">查询指标(eg:timeout-rate(%%))</label>
							<div class="controls">
								<input type="text" id="src" name="queryZhibiao"  value="${queryZhibiao}" />
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
	</script>
</body>
</html>