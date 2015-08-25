<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ui="https://github.com/openborders/wcomponents/namespace/ui/v1.0" xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.constants.xsl"/>
	<xsl:import href="wc.ui.root.n.getXslPath.xsl"/>
	<!--
		README

		These are 'global' variables but are applicable only in the wc.ui.root.* templates.
		You probably do not want to over-ride these. In the cases where over-rides
		may be necessary or desirable you will find ANT properties which can be
		more easily manipulated on a theme-by-theme basis.
	-->

	<!--
		Used to calculate the path to the libs based on the stylesheet processing instruction stripped is used in the 
		config object. Param so it can be overridden if necessary.
	-->
	<xsl:param name="xslPath">
		<xsl:call-template name="getXslPath"/>
	</xsl:param>

	<xsl:variable name="scriptDir">
		<xsl:choose>
			<xsl:when test="$isDebug=1">
				<xsl:text>${script.debug.target.dir.name}</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>${script.target.dir.name}</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!--
		this is the absolute or server relative path to the resources used to build the site calculated from the XSLT 
		processing instruction.
	-->
	<xsl:variable name="resourceRoot">
		<xsl:value-of select="substring-before($xslPath, '${xslt.target.dir.name}')"/>
	</xsl:variable>

	<!--
		This string is used to build a query string on all resources requested as part of a page. It is a replacement
		for the partial solution of adding build=build.number.
	-->
	<xsl:variable name="cacheBuster">
		<xsl:value-of select="substring-after($xslPath, '?')"/>
	</xsl:variable>
</xsl:stylesheet>
