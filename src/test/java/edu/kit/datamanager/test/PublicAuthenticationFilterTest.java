/*
 * Copyright 2019 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.datamanager.test;

import com.nimbusds.jose.util.StandardCharset;
import edu.kit.datamanager.entities.RepoServiceRole;
import edu.kit.datamanager.security.filter.JwtAuthenticationToken;
import edu.kit.datamanager.security.filter.JwtUserToken;
import edu.kit.datamanager.security.filter.PublicAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import java.util.Date;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doAnswer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author jejkal
 */
@RunWith(MockitoJUnitRunner.class)
public class PublicAuthenticationFilterTest {

    private final String key = "vkfvoswsohwrxgjaxipuiyyjgubggzdaqrcuupbugxtnalhiegkppdgjgwxsmvdb";

    @Test
    public void test() throws Exception {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        doAnswer((Answer) new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Authentication answer = (Authentication) invocation.getArguments()[0];
                Assert.assertTrue(answer instanceof JwtAuthenticationToken);
                Assert.assertEquals("PUBLIC", ((JwtAuthenticationToken) answer).getGroups().get(0));
                Assert.assertEquals(JwtAuthenticationToken.TOKEN_TYPE.USER, ((JwtUserToken) answer).getTokenType());
                Assert.assertEquals(PublicAuthenticationFilter.PUBLIC_USER, ((JwtUserToken) answer).getPrincipal());
                for (GrantedAuthority item: ((JwtUserToken) answer).getAuthorities()) {
                  System.out.println("item: " + item.getAuthority());
                }
                Assert.assertFalse(((JwtUserToken) answer).getAuthorities().contains(new SimpleGrantedAuthority(RepoServiceRole.SERVICE_WRITE.getValue())));
                Assert.assertFalse(((JwtUserToken) answer).getAuthorities().contains(new SimpleGrantedAuthority(RepoServiceRole.SERVICE_READ.getValue())));
                Assert.assertTrue(((JwtUserToken) answer).getAuthorities().contains(new SimpleGrantedAuthority(PublicAuthenticationFilter.ROLE_PUBLIC_READ)));

                Key secretKey = new SecretKeySpec(key.getBytes(StandardCharset.UTF_8), "HmacSHA256");
                Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(((JwtUserToken) answer).getToken());
                DefaultClaims claims = (DefaultClaims) jws.getBody();

                Assert.assertTrue(claims.containsKey("groups"));
                Assert.assertTrue(claims.containsKey("tokenType"));
                Assert.assertTrue(claims.containsKey("username"));
                Assert.assertTrue(claims.containsKey("roles"));
                Assert.assertTrue(claims.containsKey("exp"));
                Assert.assertTrue(claims.get("exp", Date.class).before(DateUtils.addHours(new Date(), 1)));

                return null;
            }
        }).when(securityContext).setAuthentication(any(Authentication.class));

        //Not needed? Re-used from elsewhere?
        // Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        FilterChain filterChain = Mockito.mock(FilterChain.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        PublicAuthenticationFilter filter = new PublicAuthenticationFilter(key);

        filter.doFilter(request, response, filterChain);
    }
}
