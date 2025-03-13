package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import com.example.demo.controller.AdminUserController;
import com.example.demo.dto.AdminPanelUserDto;
import com.example.demo.dto.UserRoleDto;
import com.example.demo.dto.UserStatusDto;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        AdminPanelUserDto user1 = new AdminPanelUserDto();
        AdminPanelUserDto user2 = new AdminPanelUserDto();
        List<AdminPanelUserDto> users = Arrays.asList(user1, user2);

        when(userService.getAdminPanelUsers(nullable(Long.class))).thenReturn(users);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(get("/admin/users")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService, times(1)).getAdminPanelUsers(nullable(Long.class)); // Используем nullable()
    }

    @Test
    public void testChangeUserStatus() throws Exception {
        Long userId = 1L;
        UserStatusDto statusDto = new UserStatusDto();

        doNothing().when(userService).updateUserStatus(eq(userId), any(UserStatusDto.class));

        mockMvc.perform(post("/admin/users/status/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDto)))
                .andDo(print()) // Логирование запроса и ответа
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserStatus(eq(userId), any(UserStatusDto.class));
    }

    @Test
    public void testChangeUserRoles() throws Exception {
        Long userId = 1L;
        UserRoleDto roleDto = new UserRoleDto();

        doNothing().when(userService).updateUserRoles(eq(userId), any(UserRoleDto.class));

        mockMvc.perform(post("/admin/users/roles/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDto)))
                .andDo(print()) // Логирование запроса и ответа
                .andExpect(status().isOk());

        verify(userService, times(1)).updateUserRoles(eq(userId), any(UserRoleDto.class));
    }
}