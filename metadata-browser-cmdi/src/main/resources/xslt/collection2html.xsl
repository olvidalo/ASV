<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xpath-default-namespace="http://www.clarin.eu/cmd/">

    <xsl:template name="CMDI_COLLECTION_2_HTML">
        <article>
            <div class="IMDI_group" style="margin-left: 0px">
                <div class="IMDI_group_header_static">Collection</div>
                <div class="IMDI_group_static">
                    <xsl:apply-templates select="//CollectionInfo" mode="CMDICOLLECTION" />                    
                    <xsl:apply-templates select="//License" mode="CMDICOLLECTION" />                    
                </div>
            </div>
        </article>
    </xsl:template>
    
    <xsl:template mode="CMDICOLLECTION" match="CollectionInfo/Name">
        <xsl:call-template name="imdiNameValue">
            <xsl:with-param name="name" select="'Name'" />
            <xsl:with-param name="value" select="." />
        </xsl:call-template>
    </xsl:template>    
    
    <xsl:template mode="CMDICOLLECTION" match="CollectionInfo/Title">
        <xsl:call-template name="imdiNameValue">
            <xsl:with-param name="name" select="'Title'" />
            <xsl:with-param name="value" select="." />
        </xsl:call-template>
    </xsl:template>    
    
    <xsl:template mode="CMDICOLLECTION" match="CollectionInfo/Description/Description">
        <xsl:call-template name="imdiNameValue">
            <xsl:with-param name="name" select="'Description'" />
            <xsl:with-param name="value" select="." />
        </xsl:call-template>
    </xsl:template>    
    
    <xsl:template mode="CMDICOLLECTION" match="CollectionInfo/ISO639/iso-639-3-code">
        <xsl:call-template name="imdiNameValue">
            <xsl:with-param name="name" select="'Language code'" />
            <xsl:with-param name="value" select="." />
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template mode="CMDICOLLECTION" match="License/DistributionType">
        <xsl:call-template name="imdiNameValue">
            <xsl:with-param name="name" select="'Distribution type'" />
            <xsl:with-param name="value" select="." />
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="imdiNameValue">
        <xsl:param name="name" />
        <xsl:param name="value" />
        <div class="IMDI_name_value">
            <span class="IMDI_label"><xsl:value-of select="$name" /></span>
            <span class="IMDI_value"><xsl:value-of select="$value" /></span>
        </div>
        <br />
    </xsl:template>

</xsl:stylesheet>
