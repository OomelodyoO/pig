package win.zhangzhixing.pig.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import win.zhangzhixing.pig.constant.DataConstant;
import win.zhangzhixing.pig.entity.User;
import win.zhangzhixing.pig.repository.UserRepository;
import win.zhangzhixing.pig.service.AuthService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    @Value("${pig.token.timeout}")
    private Long tokenTimeout;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserRepository userRepository;

    @Override
    public JSONObject login(JSONObject jsonObject) {
        User user = userRepository.findByMobile(jsonObject.getString("username")).orElse(null);
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("user not exits!");
        }
        if (!passwordEncoder.matches(jsonObject.getString("password"), user.getPassword())) {
            throw new RuntimeException("password error!");
        }
        JSONObject response = new JSONObject();
        response.put("token", UUID.randomUUID().toString());
        redisTemplate.opsForValue().set(
                String.format(DataConstant.AUTH_TOKEN, response.getString("token")),
                user,
                tokenTimeout,
                TimeUnit.SECONDS
        );
        return response;
    }
}
