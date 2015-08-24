package com.github.openborders.container;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.openborders.RenderContext;
import com.github.openborders.Request;
import com.github.openborders.Response;
import com.github.openborders.WebUtilities;
import com.github.openborders.XmlStringBuilder;
import com.github.openborders.container.ResponseCacheInterceptor.CacheType;
import com.github.openborders.servlet.WServlet;
import com.github.openborders.util.Factory;
import com.github.openborders.util.LookupTable;
import com.github.openborders.util.XMLUtil;

/**
 * This interceptor is used to render data lists back to the client.
 * 
 * @author Yiannis Paschalidis
 * @since 1.0.0
 */
public class DataListInterceptor extends InterceptorComponent
{
    /** The logger instance for this class. */
    private static final Log log = LogFactory.getLog(DataListInterceptor.class);

    /** The Application-wide lookup-table to use. */
    private static final LookupTable LOOKUP_TABLE = Factory.newInstance(LookupTable.class);

    /** The data list key is stored for use during the paint phase. */
    private String key;

    /** {@inheritDoc} */
    @Override
    public void serviceRequest(final Request request)
    {
        key = request.getParameter(WServlet.DATA_LIST_PARAM_NAME);
        if (key == null)
        {
            super.serviceRequest(request);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final RenderContext renderContext)
    {
        if (key == null)
        {
            super.paint(renderContext);
            return;
        }

        try
        {

            Response response = getResponse();

            Object table = null;
            List<?> data = null;

            table = LOOKUP_TABLE.getTableForCacheKey(key);
            data = LOOKUP_TABLE.getTable(table);
            response.setContentType(WebUtilities.CONTENT_TYPE_XML);
            response.setHeader("Cache-Control", CacheType.DATALIST_CACHE.getSettings());

            XmlStringBuilder xml = new XmlStringBuilder(response.getWriter());
            xml.write(XMLUtil.XML_DECLERATION);

            if (data != null)
            {
                xml.appendTagOpen("ui:datalist");
                xml.append(XMLUtil.UI_NAMESPACE);
                xml.appendAttribute("id", key);
                xml.appendClose();

                if (data != null)
                {
                    for (Object item : data)
                    {
                        // Check for null option (ie null or empty). Match isEmpty() logic.
                        boolean isNull = item == null ? true : (item.toString().length() == 0);

                        xml.appendTagOpen("ui:option");
                        xml.appendAttribute("value", LOOKUP_TABLE.getCode(table, item));
                        xml.appendOptionalAttribute("isNull", isNull, "true");
                        xml.appendClose();
                        xml.append(WebUtilities.encode(LOOKUP_TABLE.getDescription(table, item)));
                        xml.appendEndTag("ui:option");
                    }
                }

                xml.appendEndTag("ui:datalist");
            }
        }
        catch (IOException e)
        {
            log.error("Failed to write data list", e);
        }
    }
}
