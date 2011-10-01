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
			var aggregatedMetrics = new Object();
		&lt;/script&gt;
	</head>
	<body>
		<!-- Begin metrics definitions -->
		
		&lt;script&gt;
			var metrics = ['linesOfCode', 'cyclomaticComplexityPerUnit', 'duplicatedBlocks', 'unitTestCoverage', 'depthInInheritanceTree', 'numberOfChildren', 'responseForClass', 'lackOfCohesionOfMethods', 'weightedMethodComplexity', 'couplingBetweenObjects', 'numberOfPublicMethods', 'afferentCoupling'];
			function showDescription(text) {
				for(metric in metrics) {
					if(text.indexOf(metrics[metric]) != -1) {
						$("#" + metrics[metric]).dialog();
						break;
					}
				}
			}
		&lt;/script&gt;
		
		&lt;div id="linesOfCode" title="Lines of code" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This quality metric is intended to measure the volume of a software system, considering that it provides a good comparison base for systems developed using the same programming language. &lt;/br&gt;&lt;/br&gt; 
			It was one of the first metrics to be used in software engineering, and despite its simplicity it is still widely used nowadays, mainly because in can serve as an indication of the complexity that someone in charge of maintaining the code base will face.&lt;/br&gt;&lt;/br&gt; 
			For the purpose of the study, we will consider this metric in regard to non-commenting source statements.
			
			&lt;script&gt;
				aggregatedMetrics['linesOfCode'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Cyclomatic complexity per unit" id="cyclomaticComplexityPerUnit" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This quality metric refers to the degree of internal intricacy of the source code units from which a system is composed.&lt;/br&gt;&lt;/br&gt; 
			It was first proposed in the mid 1970s, and since then it has been one of the most used quality metrics in software engineering.&lt;/br&gt;&lt;/br&gt;
			It calculates the independent paths of execution within the control flow graph of a program, in order to measure its associated structural complexity.&lt;/br&gt;&lt;/br&gt; 
			A program with a high complexity tends to be error prone, and requires a great deal of effort to properly define test cases for each of the possible program execution paths.&lt;/br&gt;&lt;/br&gt; 
			In regard to analyzability, a program with a high cyclomatic complexity will be harder to analyze as part of maintenance processes.
			
			&lt;script&gt;
				aggregatedMetrics['cyclomaticComplexityPerUnit'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;

		&lt;div title="Duplicated blocks" id="duplicatedBlocks" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric is defined as the number of code blocks that occur more than once in the source code for the system.&lt;/br&gt;&lt;/br&gt; 
			The rationale behind this metric is to help identify architectural or design problems, avoiding cut and paste techniques.&lt;/br&gt;&lt;/br&gt;
			Duplicated blocks lead to maintenance problems because during the system evolution they are usually not updated to reflect the same set of changes, which may cause unexpected side effects in the system execution.
			
			&lt;script&gt;
				aggregatedMetrics['duplicatedBlocks'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Unit test coverage" id="unitTestCoverage" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric reflects the percentage of the source code effectively executed during the execution of the test cases defined for the system.&lt;/br&gt;&lt;/br&gt; 
			While it does not provide information on the validity or accuracy of the executed test cases, it can be used to identify components, or regions of components, that are not being considered as part of the unit test procedures.&lt;/br&gt;&lt;/br&gt; 
			This is the only metric defined for the study that requires the execution of the system components, that is, it performs a dynamic analysis instead of a static one.
			
			&lt;script&gt;
				aggregatedMetrics['unitTestCoverage'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Depth in the inheritance tree" id="depthInInheritanceTree" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This quality metric is defined as the number of ancestors that a particular class is associated to.&lt;/br&gt;&lt;/br&gt; 
			The rationale behind its use is to help identify the set of classes that would be affected if a particular base class were to suffer a change.&lt;/br&gt;&lt;/br&gt; 
			A high depth value usually leads to complex classes because at each step in the inheritance tree, there is a chance to inherit methods and properties. &lt;/br&gt;&lt;/br&gt;
			Still, when used properly, inheritance is a powerful object oriented technique that promotes reuse.
			
			&lt;script&gt;
				aggregatedMetrics['depthInInheritanceTree'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Number of children" id="numberOfChildren" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			 This metric identifies the number of immediate sub-classes associated to a particular class.&lt;/br&gt;&lt;/br&gt; 
			 It serves to indicate the impact that a base class would have on other ones if it were to suffer a change.&lt;/br&gt;&lt;/br&gt; 
			 We can appreciate that this metric is closely related to the &lt;em&gt;Depth in the inheritance tree&lt;/em&gt; metric, but instead of measuring depth it concerns with the breadth of a class hierarchy.&lt;/br&gt;&lt;/br&gt; 
			 A class with a high number of children indicates a high reuse by means of inheritance, and it may lead to an improper use of the base class if it does not contain the appropriate level of abstraction. In such cases, additional inheritance levels can be considered to reduce the breadth of the class hierarchy.
			 
			&lt;script&gt;
				aggregatedMetrics['numberOfChildren'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Response for class" id="responseForClass" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This quality metric identifies the number of methods of a class that are available for invocation.&lt;/br&gt;&lt;/br&gt; 
			The intent of this metric is to indicate the level of communication associated to a particular class. &lt;/br&gt;&lt;/br&gt;
			It can serve to estimate the effort and time that will be required to diagnose failures or to identify components that require modification.
			
			&lt;script&gt;
				aggregatedMetrics['responseForClass'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Lack of cohesion of methods" id="lackOfCohesionOfMethods" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric identifies the number of unrelated regions inside a class. &lt;/br&gt;&lt;/br&gt;
			It is defined as the number of pairs of methods that do not share a common class field, minus the number of pairs of methods that do share common fields.&lt;/br&gt;&lt;/br&gt; 
			A high number of unrelated regions inside a class may indicate a violation to the single responsibility principle, if these regions are mapped to functionalities offered by that particular class. In this case, a redesign should be considered to group related regions into one or more classes, and to promote the reuse of common behaviors among them.
			
			&lt;script&gt;
				aggregatedMetrics['lackOfCohesionOfMethods'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Weighted method per class" id="weightedMethodComplexity" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric is defined as the sum of the static complexity values for all the methods defined in a class.&lt;/br&gt;&lt;/br&gt; 
			This metric can serve to estimate how much time and effort will be required to maintain a set of classes. This is because the more methods a class has, the greater added complexity it will acquire. &lt;/br&gt;&lt;/br&gt;
			This complexity will directly affect the efforts required for the maintenance of that particular class.&lt;/br&gt;&lt;/br&gt; 
			It should also be noted that if a base class has a high weighted method per class value, this is likely to affect its descendants, since they will also inherit the complexity associate to the base class methods.
			
			&lt;script&gt;
				aggregatedMetrics['weightedMethodComplexity'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Coupling between objects" id="couplingBetweenObjects" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric identifies the number of classes to which a specific class is coupled, that is, the degree to which methods declared in a class use methods or instances of another one.&lt;/br&gt;&lt;/br&gt; 
			This metric favors encapsulation within the system components. &lt;/br&gt;&lt;/br&gt;
			A high coupling between system components prevents reuse and may indicate a defective modular design. This is because components with fewer dependencies have more chances to be reused as part other systems, and also because a component with a high number of dependencies is more sensitive to changes made during maintenance efforts.
			
			&lt;script&gt;
				aggregatedMetrics['couplingBetweenObjects'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Number of public methods" id="numberOfPublicMethods" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			This metric measures the size of the public interface provided by a specific class.&lt;/br&gt;&lt;/br&gt; 
			It can help identify classes that need to be broken up to favor testing procedures, considering that for each public method associated to a class there should be at least one test case that ensures that the expected functionality is really provided.&lt;/br&gt;&lt;/br&gt; 
			It should be noted that a proper definition of test cases is not trivial, and that their update process adds complexity to the overall maintenance process of the system components.
			
			&lt;script&gt;
				aggregatedMetrics['numberOfPublicMethods'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		
		&lt;div title="Afferent coupling" id="afferentCoupling" style="display:none; text-align: justify; text-justify: newspaper"&gt;
			At the class level, this quality metric refers to the number of classes that directly depend on another class.&lt;/br&gt;&lt;/br&gt; 
			By taking this metric into account we can help identify the effects that a change in a particular class can have over other classes that belong to the same system.&lt;/br&gt;&lt;/br&gt; 
			A high afferent coupling value can be a sign of a violation to the single responsibility principle. In this case, a class with many associated responsibilities can cause unexpected effects over a set of dependent classes.
			
			&lt;script&gt;
				aggregatedMetrics['afferentCoupling'] = new Array();
			&lt;/script&gt;	
		&lt;/div&gt;
		<!-- End metrics definitions -->

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
				    		<xsl:if test="not(contains(message, 'An error ocurred'))">
						   		&lt;div id="<xsl:value-of select="sequence"/>"&gt;
								<xsl:value-of select="message"/> &lt;a style="cursor: help" onclick='showDescription("<xsl:value-of select='message'/>")'&gt;&lt;em style="font-size:10px"&gt;(Description)&lt;/em&gt;&lt;/a&gt;
					    	
						    	<!-- Aggregate metrics -->
						    	<xsl:if test="not(contains(message, 'No real value found'))">
						    		&lt;script&gt;
										aggregatedMetrics['<xsl:value-of select="substring-before(substring-after(message, 'for '), '.')"/>'].push(<xsl:value-of select="substring-before(substring-after(message, 'real: '), ')')"/>);
									&lt;/script&gt;
								</xsl:if>
					    	
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
						</xsl:if>
				    </xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		&lt;/div&gt;
	
		&lt;script&gt;
		    document.getElementById("summary").innerHTML = invalidValues.length + " invalid values &lt;br/&gt;" + notFoundValues.length + " values not found";
		
			// Mark invalid values
			var componentTypesInvalidCount = new Object();
			for(var i=0; i &lt; invalidValues.length; i++) {
				document.getElementById(invalidValues[i]).style.background = "#FF9999";
				groupMessages(document.getElementById(invalidValues[i]), componentTypesInvalidCount);
			}
			
			// Mark unknown values
			var componentTypesNotFoundCount = new Object();
			for(var i=0; i &lt; notFoundValues.length; i++) {
				document.getElementById(notFoundValues[i]).style.background = "#FFFF99";
				groupMessages(document.getElementById(notFoundValues[i]), componentTypesNotFoundCount);
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
		
					newLi.innerHTML = "&lt;a href='#" + property + "'&gt;&lt;b&gt;" + property + "&lt;/b&gt; (" + count  + " classes, " + componentTypesInvalidCount["'" + property + "'"] + " invalid values, " + componentTypesNotFoundCount["'" + property + "'"] + " values not found)&lt;/a&gt;";
					tabsList.appendChild(newLi);
		
					contentDiv.setAttribute('id', property + "_content");
					newDiv.setAttribute('id', property);
					newDiv.setAttribute('style', 'margin-top: 50px; margin-bottom: 50px;');
					newHeader.innerHTML = "&lt;a onclick='showDiv(\"" + property + "_content\")' style='cursor: pointer'&gt;" + property + " (" + count  + " classes, " + componentTypesInvalidCount["'" + property + "'"] + " invalid values, " + componentTypesNotFoundCount["'" + property + "'"] + " values not found)&lt;/a&gt;";
					
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
			
			// helper functions
			function groupMessages(element, storeObject) {
				var firstElement = element.parentNode;
				while(firstElement.previousSibling != null) {
					firstElement = firstElement.previousSibling;
				}
				
				if(firstElement.nextSibling.title.length &gt; 0) {
					if(storeObject["'" + firstElement.nextSibling.title + "'"]  == null) {
						storeObject["'" + firstElement.nextSibling.title + "'"] = 1;
					} else {
						storeObject["'" + firstElement.nextSibling.title + "'"] = storeObject["'" + firstElement.nextSibling.title + "'"]  + 1;
					}
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