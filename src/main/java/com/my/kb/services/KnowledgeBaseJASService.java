package com.my.kb.services;

import com.my.kb.entity.KnowledgeBaseJASEntity;
import com.my.kb.entity.UserJASEntity;
import com.my.kb.util.DateUtil;
import com.my.kb.util.MarkdownEnum;
import com.my.kb.util.PasswordUtil;
import com.my.kb.vo.KnowledgeBaseVO;
import com.my.kb.vo.LoginVO;
import com.my.kb.vo.UserVO;
import com.oberasoftware.jasdb.api.entitymapper.EntityManager;
import lombok.SneakyThrows;
import nl.renarj.jasdb.LocalDBSession;
import nl.renarj.jasdb.api.DBSession;
import nl.renarj.jasdb.api.query.QueryBuilder;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class KnowledgeBaseJASService {
    private static final Logger LOGGER = LoggerFactory.getLogger(KnowledgeBaseJASService.class);

    private final EntityManager entityManager;

    public KnowledgeBaseJASService() throws JasDBStorageException {
        DBSession session = new LocalDBSession();
        entityManager = session.getEntityManager();
    }

    public KnowledgeBaseVO create(KnowledgeBaseVO kbVO) {
        KnowledgeBaseJASEntity knowledgeBase = new KnowledgeBaseJASEntity();
        knowledgeBase.setTitle(kbVO.getTitle());
        //knowledgeBase.setType(kbVO.getType());
        String description = kbVO.getDescription();
        if (description.contains(MarkdownEnum.BOLD_2.getSyntax())) {
            description = description.replaceFirst(MarkdownEnum.BOLD_2.getSyntax(), "<b>");
            description = description.replace(MarkdownEnum.BOLD_2.getSyntax(), "</b>");
        }
        if (description.contains(MarkdownEnum.ITALIC_2.getSyntax())) {
            description = description.replaceFirst(MarkdownEnum.ITALIC_2.getSyntax(), "<I>");
            description = description.replace(MarkdownEnum.ITALIC_2.getSyntax(), "</I>");
        }
        knowledgeBase.setDescription(description);
        knowledgeBase.setSplunk_query(kbVO.getSplunkQuery());
        knowledgeBase.setTag(kbVO.getTag());
        //knowledgeBase.setReference(kbVO.getReference());
        knowledgeBase.setCreateDate(DateUtil.getDate());
        //knowledgeBase.setApplication(kbVO.getApplication());
        try {
            String id = entityManager.persist(knowledgeBase).getInternalId();
            kbVO.setKnowledgeId(id);
            kbVO.setCreateOn(knowledgeBase.getCreateDate());
            return kbVO;
        } catch (Exception e) {
            LOGGER.error("Not able to create Knowledge ", e);
            return null;
        }
    }

    /**
     * Search can be by ID or by any string value.
     * So check the DB first by ID and then check by given search string if the ID search return empty.
     *
     * @param knowledgeBase - An object binding with request. It contains 'search' string and a boolean filter to include
     *                      or not include duplicate.
     * @return List<KnowledgeBaseVO> - List of Value Object
     */
    public List<KnowledgeBaseVO> search(KnowledgeBaseVO knowledgeBase) {
        List<KnowledgeBaseVO> results = new ArrayList<>();

        String search = knowledgeBase.getSearch();
        Boolean isDuplicate = knowledgeBase.getFilter();

        List<KnowledgeBaseJASEntity> repoResult = new ArrayList<>();
        KnowledgeBaseJASEntity entity = null;
        try {
            entity = entityManager.findEntity(KnowledgeBaseJASEntity.class, search);// Search with ID
        } catch (JasDBStorageException | IllegalArgumentException e) {
            LOGGER.error("Error in search knowledge " + e.getMessage());
        }
        if (entity != null) {//
            repoResult.add(entity);
        } else {
            List<KnowledgeBaseJASEntity> allEntries = null;
            try {
                allEntries = entityManager.findEntities(KnowledgeBaseJASEntity.class, QueryBuilder.createBuilder());
            } catch (JasDBStorageException e) {
                LOGGER.error("Error in search knowledge " + e.getMessage());
            }
            assert allEntries != null;
            allEntries.forEach(e -> { // all result including duplicates
                if (e.getTitle().contains(search) || e.getDescription().contains(search) || e.getTag().contains(search)) {
                    repoResult.add(e);
                }
            });

            if (!isDuplicate) {// No duplicates - Default behavior
                Set resultSet = repoResult.stream().collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(KnowledgeBaseJASEntity::getTitle))));
                repoResult.clear();
                repoResult.addAll(resultSet);
            }

        }

        if (!repoResult.isEmpty()) {
            for (KnowledgeBaseJASEntity kb : repoResult) {
                KnowledgeBaseVO vo = new KnowledgeBaseVO();
                vo.setTitle(kb.getTitle());
                vo.setDescription(kb.getDescription());
                //vo.setType(kb.getType());
                vo.setTag(kb.getTag());
                vo.setSplunkQuery(kb.getSplunk_query());
                //vo.setReference(kb.getReference());
                //vo.setApplication(kb.getApplication());
                vo.setKnowledgeId(kb.getId());
                vo.setCreateOn(kb.getCreateDate());
                vo.setSearch(search);
                vo.setKnowledgeId(kb.getId());

                results.add(vo);

                // update the mostSearched result....
                long mostSearched = kb.getMostSearched() + 1;
                kb.setMostSearched(mostSearched);
                try {
                    entityManager.persist(kb);
                } catch (JasDBStorageException e) {
                    LOGGER.error("Error in search knowledge " + e.getMessage(), e);
                }
            }


        }

        return results;
    }

    @SneakyThrows
    public List<KnowledgeBaseVO> getKBForHome() {
        List<KnowledgeBaseVO> results = new ArrayList<>();

        QueryBuilder query = QueryBuilder.createBuilder().field("mostSearched").greaterThan(5);
        List<KnowledgeBaseJASEntity> repoResult = entityManager.findEntities(KnowledgeBaseJASEntity.class, query);
        if (repoResult != null && !repoResult.isEmpty()) {

            for (KnowledgeBaseJASEntity kb : repoResult) {
                KnowledgeBaseVO vo = new KnowledgeBaseVO();
                vo.setTitle(kb.getTitle());
                vo.setDescription(kb.getDescription());
                vo.setTag(kb.getTag());
                vo.setSplunkQuery(kb.getSplunk_query());
                vo.setKnowledgeId(kb.getId());
                vo.setMostSearched(kb.getMostSearched());
                results.add(vo);
            }

        }
        return results;
    }

    @SneakyThrows
    public KnowledgeBaseVO getKbByID(String id) {
        KnowledgeBaseJASEntity entity = entityManager.findEntity(KnowledgeBaseJASEntity.class, id);
        if (entity != null) {
            KnowledgeBaseVO vo = new KnowledgeBaseVO();
            vo.setTitle(entity.getTitle());
            vo.setDescription(entity.getDescription());
            //vo.setType(entity.getType());
            vo.setTag(entity.getTag());
            vo.setSplunkQuery(entity.getSplunk_query());
            //vo.setReference(entity.getReference());
            //vo.setApplication(entity.getApplication());
            vo.setCreateOn(entity.getCreateDate());
            vo.setKnowledgeId(id);
            return vo;
        }
        return null;
    }


    public KnowledgeBaseVO update(KnowledgeBaseVO requestVO) {
        try {
            KnowledgeBaseJASEntity entity = entityManager.findEntity(KnowledgeBaseJASEntity.class, requestVO.getKnowledgeId());
            if (entity != null) {
                entity.setSplunk_query(requestVO.getSplunkQuery());
                entity.setDescription(requestVO.getDescription());
                if (requestVO.getCreateOn() == null)
                    entity.setCreateDate(DateUtil.getDate());
                else
                    entity.setCreateDate(requestVO.getCreateOn());

                String id = entityManager.persist(entity).getInternalId();

                KnowledgeBaseVO vo = new KnowledgeBaseVO();
                vo.setTitle(entity.getTitle());
                vo.setDescription(entity.getDescription());
                //vo.setType(entity.getType());
                vo.setCreateOn(entity.getCreateDate());
                vo.setSplunkQuery(entity.getSplunk_query());
                //vo.setReference(entity.getReference());
                //vo.setApplication(entity.getApplication());
                vo.setKnowledgeId(id);

                return vo;
            }
        } catch (JasDBStorageException | RuntimeException e) {
            LOGGER.error("Error updating the Record " + e.getMessage());
        }

        return null;
    }

    @SneakyThrows
    public Integer getKBCount() {
        List<KnowledgeBaseJASEntity> allEntry = entityManager.findEntities(KnowledgeBaseJASEntity.class, QueryBuilder.createBuilder());
        return !allEntry.isEmpty() ? allEntry.size() : 0;
    }

    @SneakyThrows
    public Boolean validateUser(LoginVO user) {
        QueryBuilder query = QueryBuilder.createBuilder().field("email").value(user.getEmail());
        List<UserJASEntity> list = entityManager.findEntities(UserJASEntity.class, query, 1);
        for (UserJASEntity userJASEntity : list) {
            Boolean hashedPwd = PasswordUtil.checkPassword(user.getPassword(), userJASEntity.getPassword());
            if (hashedPwd) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void saveUser(UserVO user) throws JasDBStorageException {
        UserJASEntity userJASEntity = new UserJASEntity();

        userJASEntity.setEmail(user.getEmail());
        userJASEntity.setProjectName(user.getProjectName());
        userJASEntity.setPassword(PasswordUtil.hashPassword(user.getPassword()));

        entityManager.persist(userJASEntity);
    }

    public Boolean isUserExist(String email) throws JasDBStorageException {
        QueryBuilder query = QueryBuilder.createBuilder().field("email").value(email);
        List<UserJASEntity> user = entityManager.findEntities(UserJASEntity.class, query, 1);

        return (null != user && !user.isEmpty());
    }
}
