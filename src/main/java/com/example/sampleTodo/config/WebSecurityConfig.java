package com.example.sampleTodo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // パスワードの暗号化用に、bcrypt（ビー・クリプト）を使用します
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/images/", "/css/",
                "/javascript/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // AUTHORIZE
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                // LOGIN
                .formLogin()
                .defaultSuccessUrl("/task");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // メモリ内認証を設定
                .inMemoryAuthentication()
                    // "user"を追加
                .withUser("user")
                // "password"をBCryptで暗号化
                .password(passwordEncoder().encode("password"))
                // 権限（ロール）を設定
                .authorities("ROLE_USER");
    }

}
