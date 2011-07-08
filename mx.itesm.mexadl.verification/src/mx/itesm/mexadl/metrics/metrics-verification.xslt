<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text"/>
   <xsl:template match="/">
<html>                                                                  
<head>  

&lt;script&gt;
	var invalidValues = new Array();
	var notFoundValues = new Array();

	function showContent(id) {
		if(id.children.length > 0) {
			if(id.children[0].style.display == 'none') {
				id.children[0].style.display = 'block';
			} else {
				id.children[0].style.display = 'none';
			}
		}
	}
&lt;/script&gt;


</head>
<body>

&lt;div id="summary"&gt;
&lt;/div&gt;

&lt;h4&gt;Details:&lt;/h4&gt;

<xsl:for-each select="log/record">
<xsl:choose>
    <!-- Class level -->
    <xsl:when test="contains(message, '** Beginning')">
    &lt;div id="${substring-before(substring-after(message, 'for:'), '**')}" onclick="showContent(this)" style="display:block; margin-top:5px; margin-bottom:5px;"&gt;
<xsl:value-of select="substring-before(substring-after(message, 'for:'), '**')"/>
&lt;div style="display:none"&gt;
    </xsl:when>
    
    <!-- Group level -->
    <xsl:when test="contains(message, '---- Beginning')">
    &lt;div style="margin-top:10px; margin-bottom:10px;"&gt;
<xsl:value-of select="substring-before(substring-after(message, 'Beginning '), 'check')"/>
    </xsl:when>
    
    <xsl:when test="contains(message, '** Ending')">
&lt;/div&gt;
&lt;/div&gt;
    </xsl:when>
    
    <xsl:when test="contains(message, '---- Ending')">
&lt;/div&gt;
    </xsl:when>

    <!-- Metric -->
    <xsl:otherwise>
    &lt;div id="<xsl:value-of select="sequence"/>"&gt;
<xsl:value-of select="message"/>
    <xsl:if test="contains(message, 'Invalid')">
    	&lt;script&gt;
    		invalidValues.push('<xsl:value-of select="sequence"/>');
    	&lt;/script&gt;
    </xsl:if>

    <xsl:if test="contains(message, 'No real value found')">
    	&lt;script&gt;
    		notFoundValues.push('<xsl:value-of select="sequence"/>');
    	&lt;/script&gt;
    </xsl:if>

&lt;/div&gt;
    </xsl:otherwise>
</xsl:choose>
</xsl:for-each>

		&lt;script&gt;
		    document.getElementById("summary").innerHTML = invalidValues.length + " invalid values found &lt;br/&gt;" + notFoundValues.length + " values not found";
		
			for(var i=0; i &lt; invalidValues.length; i++) {
				document.getElementById(invalidValues[i]).style.background = "#FF9999";
				document.getElementById(invalidValues[i]).parentElement.parentElement.parentElement.style.background = "#FFFF99";
			}
			
			for(var i=0; i &lt; notFoundValues.length; i++) {
				document.getElementById(notFoundValues[i]).style.background = "#00FFFF";
				document.getElementById(notFoundValues[i]).parentElement.parentElement.parentElement.style.background = "#FFFF99";
			}
    	&lt;/script&gt;

    </body>                                                                 
 </html>
</xsl:template>
</xsl:stylesheet>