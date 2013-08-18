package org.apache.struts2.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.rest.handler.ContentTypeHandler;

/**
 * 添加基于Request Header的Accept信息设置类型处理器
 */
public class NegotiationContentTypeHandlerManager extends DefaultContentTypeHandlerManager {

    @Override
    public ContentTypeHandler getHandlerForRequest(HttpServletRequest request) {
        ContentTypeHandler httpHeaderContentTypeHandler = getHandlerByHttpHeader(request);
        return httpHeaderContentTypeHandler != null ? httpHeaderContentTypeHandler : super
                .getHandlerForRequest(request);
    }

    @Override
    public ContentTypeHandler getHandlerForResponse(HttpServletRequest request, HttpServletResponse response) {
        ContentTypeHandler httpHeaderContentTypeHandler = getHandlerByHttpHeader(request);
        return httpHeaderContentTypeHandler != null ? httpHeaderContentTypeHandler : super.getHandlerForResponse(
                request, response);
    }

    private ContentTypeHandler getHandlerByHttpHeader(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (StringUtils.isNotBlank(acceptHeader)) {
            if (acceptHeader.indexOf("json") > -1) {
                return handlersByExtension.get("json");
            }
        }
        return null;
    }
}
