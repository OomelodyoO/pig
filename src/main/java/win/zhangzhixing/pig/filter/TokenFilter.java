package win.zhangzhixing.pig.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import win.zhangzhixing.pig.constant.DataConstant;
import win.zhangzhixing.pig.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        if (!ObjectUtils.isEmpty(authorization)) {
            String token = authorization.substring(7);
            if (redisTemplate.hasKey(String.format(DataConstant.AUTH_TOKEN, token))) {
                User user = (User) redisTemplate.opsForValue().get(String.format(DataConstant.AUTH_TOKEN, token));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                List<GrantedAuthority> authorities = new ArrayList<>();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword(), authorities
                );
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            } else {
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
