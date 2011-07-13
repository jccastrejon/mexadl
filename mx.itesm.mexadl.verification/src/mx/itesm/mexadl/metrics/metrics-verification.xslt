<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>
   <xsl:template match="/">
<html>                                                                  
	<head>  
		&lt;link rel="stylesheet" type="text/css" media="screen" href="MEXADL_HOME/reports/jquery-ui-1.8.14.custom/css/redmond/jquery-ui-1.8.14.custom.css" /&gt;
		&lt;style&gt;
		* {
			font-size:12px
		}
		&lt;/style&gt;
		
		&lt;script type="text/javascript" src="MEXADL_HOME/reports/jquery-1.6.2.min.js"&gt;&lt;/script&gt;
		&lt;script type="text/javascript" src="MEXADL_HOME/reports/jquery-ui-1.8.14.custom/js/jquery-ui-1.8.14.custom.min.js"&gt;&lt;/script&gt;
		&lt;script&gt;
			var invalidValues = new Array();
			var notFoundValues = new Array();
		
			$(function() {
				$( "#details" ).accordion({
					autoHeight: false,
					collapsible: true,
					animated:false
				});
			});
		&lt;/script&gt;
	</head>
	<body>

		&lt;div id="summary"&gt;
		&lt;/div&gt;
		
		&lt;h4&gt;Details:&lt;/h4&gt;
		&lt;hr/&gt;
		&lt;div style="margin-top:10px; margin-bottom:20px;"&gt;
		&lt;table&gt;
			&lt;tbody&gt;
				&lt;tr&gt;
					&lt;td style="height:20px"&gt;Color notation:&lt;/td&gt;
					&lt;td style="background: #FFFF99; height:20px"&gt;Warning&lt;/td&gt;
					&lt;td style="background: #FF9999; height:20px"&gt;Invalid value&lt;/td&gt;
					&lt;td style="background: #00FFFF; height:20px"&gt;Value not found&lt;/td&gt;
				&lt;/tr&gt;
			&lt;/tbody&gt;
		&lt;/table&gt;
		&lt;/div&gt;
		
		&lt;div id="details"&gt;
			<xsl:for-each select="log/record">
				<xsl:choose>
				    <!-- Beginning class level -->
				    <xsl:when test="contains(message, '** Beginning')">
					    &lt;h3&gt; &lt;a href="#section<xsl:value-of select='sequence'/>"&gt;
							<xsl:value-of select="substring-before(substring-after(message, 'for:'), '**')"/>
						&lt;/a&gt;&lt;/h3&gt;
						&lt;div&gt;
				    </xsl:when>
				    
				    <!-- Beggining group level -->
				    <xsl:when test="contains(message, '---- Beginning')">
				    	&lt;div&gt;
							&lt;h4&gt;<xsl:value-of select="substring-before(substring-after(message, 'Beginning '), 'check')"/>&lt;/h4&gt;
				    </xsl:when>
				    
				    <!-- Ending class level -->
				    <xsl:when test="contains(message, '** Ending')">
						&lt;/div&gt;
				    </xsl:when>
				    
				    <!-- Ending group level -->
				    <xsl:when test="contains(message, '---- Ending')">
						&lt;/div&gt;
				    </xsl:when>
				
				    <!-- Metric level -->
				    <xsl:otherwise>
				   		&lt;div id="<xsl:value-of select="sequence"/>"&gt;
							<xsl:value-of select="message"/>
				    	
				    	<!-- Mark this metric as invalid -->
				    	<xsl:if test="contains(message, 'Invalid')">
				    		&lt;script&gt;
				    			invalidValues.push('<xsl:value-of select="sequence"/>');
				    		&lt;/script&gt;
				    	</xsl:if>
				
						<!-- Mark this metric as unknown -->
				    	<xsl:if test="contains(message, 'No real value found')">
				    		&lt;script&gt;
					    		notFoundValues.push('<xsl:value-of select="sequence"/>');
				    		&lt;/script&gt;
				    	</xsl:if>
						
						&lt;/div&gt;
				    </xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		&lt;/div&gt;
	
		&lt;script&gt;
		    document.getElementById("summary").innerHTML = invalidValues.length + " invalid values found &lt;br/&gt;" + notFoundValues.length + " values not found";
		
			for(var i=0; i &lt; invalidValues.length; i++) {
				document.getElementById(invalidValues[i]).style.background = "#FF9999";
				document.getElementById(invalidValues[i]).parentElement.parentElement.previousSibling.previousSibling.style.background = "#FFFF99";
			}
			
			for(var i=0; i &lt; notFoundValues.length; i++) {
				document.getElementById(notFoundValues[i]).style.background = "#00FFFF";
				document.getElementById(notFoundValues[i]).parentElement.parentElement.previousSibling.previousSibling.style.background = "#FFFF99";
			}
		&lt;/script&gt;

    </body>                                                                 
 </html>
</xsl:template>
</xsl:stylesheet>