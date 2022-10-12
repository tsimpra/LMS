package gr.apt.lms.config.vaadinservlet

import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

/*
    Custom Filter for all requests from vaadin servlet
 */
@WebFilter("/")
class LmsFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        chain.doFilter(LmsHttpServletRequest(request as HttpServletRequest), response)
    }

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
    }

    override fun destroy() {}
}