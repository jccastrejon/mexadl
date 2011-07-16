<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<html>                                                                  
	<head>   
		<link rel="stylesheet" type="text/css" media="screen" href="MEXADL_HOME/reports/jquery-ui-1.8.14.custom/css/redmond/jquery-ui-1.8.14.custom.css" />
		<link rel="stylesheet" type="text/css" media="screen" href="MEXADL_HOME/reports/jquery.jqGrid-4.1.1/css/ui.jqgrid.css" />
		<script type="text/javascript" src="MEXADL_HOME/reports/jquery-1.6.2.min.js"></script>          
		<script type="text/javascript" src="MEXADL_HOME/reports/jquery.jqGrid-4.1.1/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="MEXADL_HOME/reports/jquery.jqGrid-4.1.1/js/jquery.jqGrid.min.js"></script>
		<script type="text/javascript" src="MEXADL_HOME/reports/jquery-ui-1.8.14.custom/js/jquery-ui-1.8.14.custom.min.js"></script>

		<style>
			.ui-jqgrid tr.jqgrow td  {
 				white-space: normal !important;
 				height:auto;
 				vertical-align:text-top;
 				padding-top:2px;
			}

			* {
				font-size:12px
			}
		</style>

		<script type="text/javascript">
			jQuery.extend(jQuery.jgrid.defaults, { autowidth:true, shrinkToFit:true });
		</script>

		<script type="text/javascript"> 
 
  			var outputData = [
				<xsl:for-each select="log/record">
 					{
						className:"<xsl:value-of select="substring-before(substring-after(message, 'mexadl_tmp_src/'), '.java')"/>",
						type:"[ <xsl:value-of select="substring-before(substring-after(message, 'mexadl_tmp_src/'), '.java')"/> ]", 
						lineNumber:"<xsl:value-of select="substring-before(substring-after(message, '.java:'), ':')"/>", 
						message:<xsl:value-of select="substring-before(substring-after(message, '[message]'), '[/message]')"/>, 
						detail:"<xsl:value-of select="substring-before(substring-after(message, '[details]'), '[/details]')"/>"
					},
				</xsl:for-each>

			];
	
    		$(document).ready(function() {
    
		        // Load table data    
        		jQuery("#output").jqGrid({
		        data: outputData,
		        datatype: "local",
		        height: 'auto',
		        rowNum: 400,
		        colNames:['Class', 'Type', 'Line #','Message', 'Detail'],
		        colModel:[
        	    	{name:'className',index:'className'},
        	    	{name:'type',index:'type', width:40},   	
            		{name:'lineNumber',index:'lineNumber', width:20},
            		{name:'message',index:'message'},
            		{name:'detail',index:'detail'}
        		],
        		pager: "#pager",
        		viewrecords: true,
        		sortname: 'className',
        		grouping:true,
        		groupingView : {
            		groupField : ['className'],
            		groupColumnShow : [true],
            		groupText : ['<b>{0} - {1} Item(s)</b>'],
            		groupCollapse : true
        		},
        		caption: "<xsl:value-of select="count(log/record)"/> Invalid Interactions"
    		});
    
        	//Change table grouping
        	jQuery("#changeGroups").change(function(){
            	var group = $(this).val();
            	if(group) {
	                jQuery("#output").jqGrid('groupingGroupBy', group);
    	        }
	        	});
			});
 		</script>                                                               
	</head>                                                                 
	<body>          
 
 		<b>Valid Interactions:</b> 
 		<a href="#" title="Show Architecture" onclick="showArchitecture()">
 			<img src="../src_mexadl/mx/itesm/mexadl/Architecture.png" width="60px" height="60px"/>
 		</a><em>(Click to enlarge)</em>
		<hr/>
 
		Group By: <select id="changeGroups">
			<option value="className">Class name</option>
			<option value="type">Component type</option>
			<option value="message">Warning message</option>	
		</select>
		<br />
		<br />
		<table id="output"></table>
		<div id="pager"></div>

		<div id="dialog" title="Software Architecture" style="display:none">
			<img src="../src_mexadl/mx/itesm/mexadl/Architecture.png"/>
		</div>
		
		<script>
			function showArchitecture() {
	        	var architectureImg = new Image();
	        	architectureImg.src = "../src_mexadl/mx/itesm/mexadl/Architecture.png";
	        	$("#dialog").dialog({
	        		width: architectureImg.width+50, 
	        		height:  architectureImg.height+50});
			}
		</script>

 	</body>                                                                 
 </html>
</xsl:template>
</xsl:stylesheet>
