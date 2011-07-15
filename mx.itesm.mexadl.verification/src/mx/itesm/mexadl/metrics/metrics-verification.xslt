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
			var classValues = new Array();
			var componentTypes = new Object();
		&lt;/script&gt;
	</head>
	<body>

		&lt;h4&gt;Summary:&lt;/h4&gt;
		&lt;div id="summary"&gt;
		&lt;/div&gt;
		
		&lt;hr/&gt;
		&lt;h4&gt;Details:&lt;/h4&gt;
		&lt;div style="margin-top:10px; margin-bottom:20px;"&gt;
		&lt;table&gt;
			&lt;tbody&gt;
				&lt;tr&gt;
					&lt;td style="height:20px"&gt;Color notation:&lt;/td&gt;
					&lt;td style="background: #FF9999; height:20px"&gt;Invalid value&lt;/td&gt;
					&lt;td style="background: #FFFF99; height:20px"&gt;Value not found&lt;/td&gt;
				&lt;/tr&gt;
			&lt;/tbody&gt;
		&lt;/table&gt;
		&lt;/div&gt;
		
		&lt;div id="details"&gt;
			<xsl:for-each select="log/record">
				<xsl:choose>
				    <!-- Beginning class level -->
				    <xsl:when test="contains(message, '** Beginning')">
				    	&lt;div&gt;
					    &lt;h3 id="<xsl:value-of select='sequence'/>"&gt; &lt;a href="#section<xsl:value-of select='sequence'/>"&gt;
							<xsl:value-of select="substring-before(substring-after(message, 'for:'), '**')"/>
						&lt;/a&gt;&lt;/h3&gt;
						&lt;div&gt;
							&lt;div title='[<xsl:value-of select="translate(substring-before(substring-after(message, 'for:'), '**'), '\.', '\/')"/>]'&gt;
								&lt;h4&gt;Type: [<xsl:value-of select="translate(substring-before(substring-after(message, 'for:'), '**'), '\.', '\/')"/>]&lt;/h4&gt;
				    			&lt;script&gt;
				    				componentTypes['[<xsl:value-of select="translate(substring-before(substring-after(message, 'for:'), '**'), '\.', '\/')"/>]'] = '1';
				    				classValues.push('<xsl:value-of select="sequence"/>');
				    			&lt;/script&gt;
							&lt;/div&gt;
				    </xsl:when>
				    
				    <!-- Beggining group level -->
				    <xsl:when test="contains(message, '---- Beginning')">
				    	&lt;div&gt;
							&lt;h4&gt;<xsl:value-of select="substring-before(substring-after(message, 'Beginning '), 'check')"/>&lt;/h4&gt;
				    </xsl:when>
				    
				    <!-- Ending class level -->
				    <xsl:when test="contains(message, '** Ending')">
						&lt;/div&gt;
						&lt;/div&gt;
				    </xsl:when>
				    
				    <!-- Ending group level -->
				    <xsl:when test="contains(message, '---- Ending')">
						&lt;/div&gt;
				    </xsl:when>
				
				    <!-- Metric level -->
				    <xsl:otherwise>
				    	<xsl:if test="not(contains(message, 'null'))">
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
						</xsl:if>
				    </xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		&lt;/div&gt;
	
		&lt;script&gt;
		    document.getElementById("summary").innerHTML = invalidValues.length + " invalid values &lt;br/&gt;" + notFoundValues.length + " values not found";
		
			// Mark invalid values
			for(var i=0; i &lt; invalidValues.length; i++) {
				document.getElementById(invalidValues[i]).style.background = "#FF9999";
			}
			
			// Mark unknown values
			for(var i=0; i &lt; notFoundValues.length; i++) {
				document.getElementById(notFoundValues[i]).style.background = "#FFFF99";
			}
			
			// Remove classes without data
			var removedElements = new Array();
			for(var i=0; i &lt; classValues.length; i++) {
				if(document.getElementById(classValues[i]).nextSibling.nextSibling.children.length &lt;= 1) {
					document.getElementById(classValues[i]).style.display = "none";
					removedElements.push(classValues[i]);
				}
			}
			
			
			// Group classes according to their component type 
			var tabsDiv = document.createElement("div");
			var tabsList = document.createElement('ul');

			tabsDiv.setAttribute('id', 'tabsDiv');
			document.body.appendChild(tabsDiv);
			tabsDiv.appendChild(tabsList);
			
			for(var property in componentTypes) {
				if(componentTypes.hasOwnProperty(property)) {
					var newLi = document.createElement('li'); 
					var newDiv = document.createElement('div'); 
					var newHeader = document.createElement('h4'); 
					var contentDiv = document.createElement('div'); 
		
					var count = $('div[title|=' + property + ']').size();
					$('div[title|=' + property + ']').each(function(index, Element) {
						if(jQuery.inArray(Element.parentNode.previousSibling.previousSibling.id, removedElements) != -1) {
							count = count-1;
						}
					});
		
					newLi.innerHTML = "&lt;a href='#" + property + "'&gt;" + property + " (" + count  + " classes)&lt;/a&gt;";
					tabsList.appendChild(newLi);
		
					contentDiv.setAttribute('id', property + "_content");
					newDiv.setAttribute('id', property);
					newDiv.setAttribute('style', 'margin-top: 50px; margin-bottom: 50px;');
					newHeader.innerHTML = "&lt;a href='#' onclick='showDiv(\"" + property + "_content\")'&gt;" + property + " (" + count  + " classes)&lt;/a&gt;";
					
					newDiv.appendChild(document.createElement('hr'));
					newDiv.appendChild(newHeader);
					newDiv.appendChild(contentDiv);
					tabsDiv.appendChild(newDiv);
					
					$('div[title|=' + property + ']').each(function(index, Element) {
						var parent = Element.parentNode.parentNode;
						while(parent.firstChild != null) {
							contentDiv.appendChild(parent.firstChild);
						}
					});
						
					$( "#" + property + "_content").accordion({
						autoHeight: false,
						collapsible: true,
						animated:false
					});
				}
			}
			
			function showDiv(id) {
				if(document.getElementById(id).style.display != 'none') {
					document.getElementById(id).style.display = 'none';
				} else {
					document.getElementById(id).style.display = 'block';
				}
			}
			
		&lt;/script&gt;

    </body>                                                                 
 </html>
</xsl:template>
</xsl:stylesheet>