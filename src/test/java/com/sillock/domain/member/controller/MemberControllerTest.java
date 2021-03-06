package com.sillock.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sillock.annotation.SillogUser;
import com.sillock.common.AbstractControllerTest;
import com.sillock.common.DtoFactory;
import com.sillock.common.EntityFactory;
import com.sillock.core.auth.jwt.model.SocialProfile;
import com.sillock.domain.member.model.component.MemberMapper;
import com.sillock.domain.member.model.dto.MemberProfile;
import com.sillock.domain.member.model.dto.MemberSignUp;
import com.sillock.domain.member.model.entity.Member;
import com.sillock.domain.member.service.MemberAuthService;
import com.sillock.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.sillock.common.message.ResponseMessage.*;
import static com.sillock.config.ApiDocumentUtils.getDocumentRequest;
import static com.sillock.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class MemberControllerTest extends AbstractControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberAuthService memberAuthService;

    @Autowired
    private ObjectMapper objectMapper;

    @SillogUser
    @Test
    public void ?????????_????????????() throws Exception {
        mockMvc.perform(get("/api/v1/members/me")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("test@gmail.com"))
                .andDo(print())
                .andDo(document("api/v1/members/me",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("status").description("?????? ???"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data.id").description("????????? id"),
                                fieldWithPath("data.email").description("????????? ?????????"),
                                fieldWithPath("data.nickname").description("????????? ?????????"),
                                fieldWithPath("data.profileImage").description("????????? ????????? ????????? URL"),
                                fieldWithPath("data.identifier").description("????????? identifier"),
                                fieldWithPath("timestamp").description("???????????????")
                        )
                ));
    }

    @Test
    public void ?????????_?????????_?????????() throws Exception {
        SocialProfile socialProfile = new SocialProfile("id", "test@gmail.com");
        String content = objectMapper.writeValueAsString(socialProfile);

        given(memberAuthService.login(any(String.class))).willReturn(EntityFactory.basicMemberEntity());

        mockMvc.perform(post("/api/v1/members/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(LOGIN_SUCCESS))
                .andDo(print())
                .andDo(document("api/v1/members/login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("id").description("?????? ????????? id"),
                                fieldWithPath("email").description("?????? ????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ???"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data.accessToken").description("????????? ????????? ??????"),
                                fieldWithPath("data.refreshToken").description("????????? ???????????? ??????"),
                                fieldWithPath("timestamp").description("???????????????")
                        )
                ));
    }

    @Test
    public void ?????????_????????????_?????????() throws Exception {
        MemberSignUp memberSignUp = DtoFactory.memberSignUpDto();
        String content = objectMapper.writeValueAsString(memberSignUp);

        given(memberAuthService.signup(any(Member.class))).willReturn(EntityFactory.basicMemberEntity());

        mockMvc.perform(post("/api/v1/members/signup")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SIGN_UP_SUCCESS))
                .andDo(print())
                .andDo(document("api/v1/members/signup",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("????????? ?????????"),
                                fieldWithPath("nickname").description("????????? ?????????"),
                                fieldWithPath("birth").description("????????? ??????"),
                                fieldWithPath("password").description("????????? ????????????"),
                                fieldWithPath("profileImage").description("????????? ????????? ????????? ??????(default : null)"),
                                fieldWithPath("gender").description("????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ???"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data.accessToken").description("????????? ????????? ??????"),
                                fieldWithPath("data.refreshToken").description("????????? ???????????? ??????"),
                                fieldWithPath("timestamp").description("???????????????")
                        )
                ));
    }

    @SillogUser
    @Test
    public void ?????????_?????????_??????_?????????() throws Exception {
        MemberProfile profile = new MemberProfile();
        profile.setNickname("changed nickname");

        String content = objectMapper.writeValueAsString(profile);

        mockMvc.perform(patch("/api/v1/members/me")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(UPDATE_MEMBER_PROFILE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value(profile.getNickname()))
                .andDo(print())
                .andDo(document("api/v1/members/me/patch",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("id").description("????????? id - ??????"),
                                fieldWithPath("email").description("????????? ????????? - ??????"),
                                fieldWithPath("nickname").description("????????? ????????? - ??????"),
                                fieldWithPath("profileImage").description("????????? ????????? ????????? URL - ??????"),
                                fieldWithPath("identifier").description("????????? identifier - ??????")
                        ),
                        responseFields(
                                fieldWithPath("status").description("?????? ???"),
                                fieldWithPath("message").description("?????? ?????????"),
                                fieldWithPath("data.id").description("????????? id"),
                                fieldWithPath("data.email").description("????????? ?????????"),
                                fieldWithPath("data.nickname").description("????????? ?????????"),
                                fieldWithPath("data.profileImage").description("????????? ????????? ????????? URL"),
                                fieldWithPath("data.identifier").description("????????? identifier"),
                                fieldWithPath("timestamp").description("???????????????")
                        )
                ));
    }

//
//    @Test
//    public void ?????????_??????_??????_?????????() throws Exception{
//        mockMvc.perform(get("/api/members/{memberId}/sillogs", 1)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].author").value("sillog"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("??????"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].sequence").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].qnaData[0].question").value("????????? ???????????????."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].qnaData[0].answer").value("????????? ???????????????."))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].qnaData[0].tags[0]").value("tag1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].image").value("/src/image"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].qualification").value("/src/qualification"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].regDate").value("2021-07-07"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].startDate").value("2021-07-07"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].endDate").value("2021-07-08"))
//                .andDo(print())
//                .andDo(document("api/sillogList",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("status").description("HttpStatus"),
//                                fieldWithPath("message").description("?????????"),
//                                fieldWithPath("data.[].memberId").description("???????????????"),
//                                fieldWithPath("data.[].author").description("?????????"),
//                                fieldWithPath("data.[].title").description("??????"),
//                                fieldWithPath("data.[].sequence").description("?????? ??? ?????? 1, ????????? ?????? ?????????"),
//                                fieldWithPath("data.[].qnaData.[].question").description("QnA ??????"),
//                                fieldWithPath("data.[].qnaData.[].answer").description("QnA ??????"),
//                                fieldWithPath("data.[].qnaData.[].tags.[]").description("QnA ??????"),
//                                fieldWithPath("data.[].image.[]").description("?????????"),
//                                fieldWithPath("data.[].qualification.[]").description("?????????"),
//                                fieldWithPath("data.[].regDate").description("?????????"),
//                                fieldWithPath("data.[].startDate").description("?????????"),
//                                fieldWithPath("data.[].endDate").description("?????????"),
//                                fieldWithPath("timestamp").description("??????????????? ????????? ????????? ??????(???????????????????????? ??????)")
//                        )
//                ));
//    }
//
//    @Test
//    public void ?????????_??????_????????????_????????????_?????????() throws Exception {
//        mockMvc.perform(get("/api/members/{memberId}/sillogs",1,"??????")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].author").value("sillog"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("??????"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].sequence").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].image").value("/src/image"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].qualification").value("/src/qualification"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].author").value("sillog"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("??????"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].sequence").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].image").value("/src/image"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].qualification").value("/src/qualification"))
//                .andDo(print())
//                .andDo(document("api/sillogListByTitle",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("status").description("HttpStatus"),
//                                fieldWithPath("message").description("?????????"),
//                                fieldWithPath("data.[].memberId").description("???????????????"),
//                                fieldWithPath("data.[].author").description("?????????"),
//                                fieldWithPath("data.[].title").description("????????? ?????? ??????"),
//                                fieldWithPath("data.[].sequence").description("?????? ??? ?????? 1, ????????? ?????? ?????????"),
//                                fieldWithPath("data.[].qnaData.[].question").description("QnA ??????"),
//                                fieldWithPath("data.[].qnaData.[].answer").description("QnA ??????"),
//                                fieldWithPath("data.[].qnaData.[].tags.[]").description("QnA ??????"),
//                                fieldWithPath("data.[].image.[]").description("?????????"),
//                                fieldWithPath("data.[].qualification.[]").description("?????????"),
//                                fieldWithPath("data.[].regDate").description("?????????"),
//                                fieldWithPath("data.[].startDate").description("?????????"),
//                                fieldWithPath("data.[].endDate").description("?????????"),
//                                fieldWithPath("timestamp").description("??????????????? ????????? ????????? ??????(???????????????????????? ??????)")
//                        )
//                ));
//    }
}