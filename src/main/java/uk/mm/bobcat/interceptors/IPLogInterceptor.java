package uk.mm.bobcat.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import uk.mm.bobcat.controller.LMT;

public class IPLogInterceptor extends HandlerInterceptorAdapter {
	private final Logger logger = LoggerFactory.getLogger(IPLogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("{} [{}] {} {}", LMT.IP_LOG, request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
		return super.preHandle(request, response, handler);
	}
}
