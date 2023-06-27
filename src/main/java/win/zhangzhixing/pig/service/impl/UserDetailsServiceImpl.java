package win.zhangzhixing.pig.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import win.zhangzhixing.pig.entity.User;
import win.zhangzhixing.pig.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User mobile = userRepository.findByMobile(username).orElse(null);
        if (ObjectUtils.isEmpty(mobile)) {
            throw new RuntimeException("user not exits");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                mobile.getUsername(), mobile.getPassword(), authorities
        );
        return user;
    }
}
