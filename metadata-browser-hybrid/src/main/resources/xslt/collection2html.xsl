<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0"
    xpath-default-namespace="http://www.clarin.eu/cmd/">
        
    <!-- Map for display labels based on element name -->
    <xsl:variable name="map">
        <entry key="Name">Name</entry>
        <entry key="iso-639-3-code">Language code</entry>
        <entry key="DistributionType">Distribution type</entry>
    </xsl:variable>
    
    <xsl:template name="CMDI_COLLECTION_2_HTML">
        <article>
            <div class="IMDI_group" style="margin-left: 0px">
                <div class="IMDI_group_header_static">Collection</div>
                <div class="IMDI_group_static">
                    <!-- Basic info (root fields) -->
                    <xsl:apply-templates select="//CollectionInfo/Name" mode="CMDICOLLECTIONFIELD" />                    
                    <xsl:apply-templates select="//CollectionInfo/Title" mode="CMDICOLLECTIONFIELD" />                    
                    <xsl:apply-templates select="//CollectionInfo/Description/Description" mode="CMDICOLLECTIONFIELD" />                    
                    <xsl:apply-templates select="//CollectionInfo/ISO639/iso-639-3-code" mode="CMDICOLLECTIONFIELD" />                    
                    
                    <!-- Subgroups for contact, license info, ... -->
                    <xsl:apply-templates select="//Contact" mode="CMDICOLLECTIONGROUP" />                    
                    <xsl:apply-templates select="//License" mode="CMDICOLLECTIONGROUP" />                    
                </div>
            </div>
        </article>
    </xsl:template>
    
    <xsl:template mode="CMDICOLLECTIONGROUP" match="Contact">
        <div class="IMDI_group" style="margin-left: 0px">
            <div class="IMDI_group_header_static">Contact</div>
            <div class="IMDI_group_static">                   
                <xsl:apply-templates select="Person" mode="CMDICOLLECTIONFIELD" />                    
                <xsl:apply-templates select="Address" mode="CMDICOLLECTIONFIELD" />                    
                <xsl:apply-templates select="Email" mode="CMDICOLLECTIONFIELD" />                    
                <xsl:apply-templates select="Organisation" mode="CMDICOLLECTIONFIELD" />                    
                <xsl:apply-templates select="Telephone" mode="CMDICOLLECTIONFIELD" />                    
                <xsl:apply-templates select="Website" mode="CMDICOLLECTIONFIELD" />                    
            </div>
        </div>
    </xsl:template>
    
    <xsl:template mode="CMDICOLLECTIONGROUP" match="License">
        <div class="IMDI_group" style="margin-left: 0px">
            <div class="IMDI_group_header_static">License</div>
            <div class="IMDI_group_static">                   
                <xsl:apply-templates select="DistributionType" mode="CMDICOLLECTIONFIELD" />                    
            </div>
        </div>
    </xsl:template>
    
    <!-- Matching a single field element -->
    <xsl:template mode="CMDICOLLECTIONFIELD" match="*">
        <div class="IMDI_name_value">
            
            <!-- Determine label -->
            <xsl:variable name="label">
                <xsl:variable name="name" select="name()" />
                <xsl:choose >
                    <!-- If there is a map entry, use its value... -->
                    <xsl:when test="$map/*[@key=$name]">
                        <xsl:value-of select="$map/*[@key=$name]" />
                    </xsl:when>
                    <!-- ...otherwise just use the element name itself -->
                    <xsl:otherwise>
                        <xsl:value-of select="$name" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>            
            
            <span class="IMDI_label"><xsl:value-of select="$label" /></span>
            <span class="IMDI_value"><xsl:value-of select="." /></span>
        </div>
        <br />
        
    </xsl:template>
</xsl:stylesheet>
