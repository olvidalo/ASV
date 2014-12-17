<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cmd="http://www.clarin.eu/cmd/"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="xs"
    version="2.0"
    xpath-default-namespace="http://www.clarin.eu/cmd/">
    
    <xsl:include href="cmdi2general.xsl"/>
    <xsl:include href="collection2html.xsl"/>
    <xsl:param name="htmlEnvelope" select="false()" />
    <xsl:param name="styleLocation" required="no" />
    
    <xsl:template name="content">
        <xsl:choose>
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1345561703620/xsd')">
                <xsl:call-template name="CMDI_COLLECTION_2_HTML"/>
            </xsl:when>
            <!--            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1345561703620/xsd')">
                <xsl:call-template name="COLLECTION2CORPUS" />
            </xsl:when>
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1361876010525/xsd')">
                <xsl:call-template name="DISCANPROJECT2CORPUS" />
            </xsl:when>            
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1361876010653/xsd')">
                <xsl:call-template name="DISCANTEXTCORPUS2CORPUS" />
            </xsl:when>            
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1366895758243/xsd')">
                <xsl:call-template name="DISCANCASE2IMDI" />
            </xsl:when> 
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1328259700928/xsd')">
                <xsl:call-template name="SOUNDBITES2IMDI" />
            </xsl:when>
            <xsl:when test="contains(/CMD/@xsi:schemaLocation, 'http://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/profiles/clarin.eu:cr1:p_1375880372947/xsd')">
                <xsl:call-template name="LESLLA2IMDI" />
            </xsl:when>-->
            
            
            <!-- Add new profile templates here -->
            <!--        
			<xsl:when test="exists(//Components/WHAT-EVER)">
                <xsl:call-template name="WHATEVER2IMDI"/>
            </xsl:when>
            -->
            <!-- Not a known profile! Apply identity -->
            <xsl:otherwise>
                <xsl:call-template name="cmdi2general"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="/">
        <xsl:choose>
            <xsl:when test="$htmlEnvelope">
                <xsl:call-template name="htmlEnvelope" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="content" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="htmlEnvelope">
            <html>
                <head>
                    <xsl:if test="$styleLocation">
                        <link rel="stylesheet" type="text/css">
                            <xsl:attribute name="href" select="$styleLocation" />
                        </link>
                    </xsl:if>
                </head>
                <body>
                    <div id="nodePanel">
                        <div id="nodePresentationContainer">
                            <div id="nodePresentation">
                                <xsl:call-template name="content" />
                            </div>
                        </div>
                    </div>
                </body>
            </html>
        
    </xsl:template>
    
</xsl:stylesheet>
