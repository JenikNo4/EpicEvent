package cz.simek.epicevent;

import cz.simek.epicevent.init.AuthenticationProviderFromDB;
import org.apache.log4j.Logger;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SecureWebSession extends AuthenticatedWebSession {

    private static Logger logger = Logger.getLogger(SecureWebSession.class);

    private HttpSession httpSession;

//	@SpringBean(name = "authenticationManager")
//	private AuthenticationManager authenticationManager;

    @SpringBean
    private AuthenticationProviderFromDB authenticationProviderFromDB;

    public SecureWebSession(Request request) {
        super(request);
        this.httpSession = ((HttpServletRequest) request.getContainerRequest()).getSession(true);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationProviderFromDB.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            //Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authentication.isAuthenticated()) {
                // the authentication object has to be stored in the SecurityContextHolder and in the HttpSession manually, so that the
                // security context will be accessible in the next request
                SecurityContextHolder.getContext().setAuthentication(authentication);
                httpSession.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());
                return true;
            } else {
                return false;
            }
        } catch (AuthenticationException e) {
            logger.warn("Failed login attempt due to exception!", e);
            return false;
        }
    }

    @Override
    public Roles getRoles() {
        Roles roles = new Roles();
        if (isSignedIn()) {
            SecurityContext securityContext = (SecurityContext) this.httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
            addRolesFromAuthentication(roles, securityContext.getAuthentication());
        }
        return roles;
    }

    private void addRolesFromAuthentication(Roles roles, Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
    }

    public boolean hasRole(Role role) {
        return getRoles().hasRole(role.getSpringSecurityRoleName());
    }

}
