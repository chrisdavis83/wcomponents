<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ui="https://github.com/openborders/wcomponents/namespace/ui/v1.0" xmlns:html="http://www.w3.org/1999/xhtml" version="1.0">
	<xsl:import href="wc.debug.common.contentCategory.xsl"/>
	<xsl:import href="wc.common.separator.n.separatorOrientation.xsl"/>
	<!--
		Common template used to build WMenuItemGroup (wc.ui.menuGroup.xsl) and
		separators in menus (wc.ui.separator.xsl).
	-->
	<xsl:template name="separator">
		<xsl:element name="hr">
			<xsl:attribute name="role">
				<xsl:text>separator</xsl:text>
			</xsl:attribute>
			<xsl:call-template name="separatorOrientation"/>
			<xsl:if test="$isDebug=1">
				<xsl:call-template name="debugAttributes"/>
				<xsl:call-template name="thisIsNotAllowedHere-debug">
					<xsl:with-param name="testForPhraseOnly" select="1"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>