package com.example.todo.oauth.domain.client;

import com.example.todo.oauth.OauthMember;
import com.example.todo.oauth.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String code);
}