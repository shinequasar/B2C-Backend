package com.sillock.domain.sillog.service;

import com.sillock.common.DtoFactory;
import com.sillock.common.EntityFactory;
import com.sillock.domain.member.model.entity.Member;
import com.sillock.domain.member.repository.MemberRepository;
import com.sillock.domain.sillog.model.component.SillogMapper;
import com.sillock.domain.sillog.model.dto.SillogPostDto;
import com.sillock.domain.sillog.model.entity.Sillog;
import com.sillock.domain.sillog.model.entity.SillogTitle;
import com.sillock.domain.sillog.repository.SillogRepository;
import com.sillock.domain.sillog.repository.TagRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class SillogServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SillogRepository sillogRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private SillogService sillogService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SillogMapper sillogMapper;

    @Test
    @Disabled
    public void 환경_세팅() {
        mongoTemplate.getDb().drop();

        Member member = EntityFactory.basicMemberEntity();
        memberRepository.save(member);

        Sillog sillog = EntityFactory.basicSillogMemoEntity();
        sillog.setMemberId(member.getId());

        tagRepository.saveAll(sillog.getTagList());

        Sillog sillog2 = EntityFactory.basicSillogMemoEntity();
        sillog2.setMemberId(member.getId());

        tagRepository.saveAll(sillog2.getTagList());
        sillogRepository.saveAll(Arrays.asList(sillog, sillog2));
    }

    @Test
    @Disabled
    public void 등록_테스트() {
        SillogPostDto sillogPostDto = DtoFactory.sillogPostDto();
        sillogPostDto.setMemo(EntityFactory.basicMemoEntity());

        Member member = EntityFactory.basicMemberEntity();

        sillogService.register(
                sillogMapper.toEntityFromPostDto(sillogPostDto, member)
        );
    }

    @Test
    @Disabled
    public void DB_삭제() {
        mongoTemplate.getDb().drop();
    }

    @Test
    @Disabled
    public void getMemberSillogTitleList() {
        List<SillogTitle> sillogTitleList = sillogService.getMemberSillogTitleList(new ObjectId(EntityFactory.basicObjectId()));
        assertEquals(sillogTitleList.size(), 1);

    }
}