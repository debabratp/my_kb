package com.my.kb.services;

import org.springframework.stereotype.Service;

@Service
public class KnowledgeBaseService {

   /* @Autowired
    private KnowledgeBaseRepository knowledgeBaseRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoOperations mongoOperations;

    public KnowledgeBaseVO create(KnowledgeBaseVO kbVO) {
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setTitle(kbVO.getTitle());
        knowledgeBase.setType(kbVO.getType());
        knowledgeBase.setDescription(kbVO.getDescription());
        knowledgeBase.setSplunk_query(kbVO.getSplunkQuery());
        knowledgeBase.setTag(kbVO.getTag());
        knowledgeBase.setReference(kbVO.getReference());
        LocalDateTime parseDate = LocalDateTime.parse(DateUtil.getDate());
        knowledgeBase.setCreate_date(parseDate);

        knowledgeBase.setApplication(kbVO.getApplication());
        try {
            knowledgeBase = knowledgeBaseRepository.save(knowledgeBase);
            kbVO.setKnowledgeId(knowledgeBase.getId());
            return kbVO;
        } catch (Exception e) {
            return null;
        }
    }

    *//**
     * Search can be by ID or by any string value.
     * So check the DB first by ID and then check by given search string if the ID search return empty.
     *
     * @param search
     * @return
     *//*
    public List<KnowledgeBaseVO> search(String search) {
        List<KnowledgeBaseVO> results = new ArrayList<>();

        List<KnowledgeBase> repoResult;

        Optional<KnowledgeBase> optional = knowledgeBaseRepository.findById(search);

        if (optional.isPresent()) {
            repoResult = new ArrayList<>();

            KnowledgeBase kb = optional.get();
            repoResult.add(kb);

        } else {
            repoResult = knowledgeBaseRepository.findBySearchString(search);
        }

        if (repoResult != null && !repoResult.isEmpty()) {
            for (KnowledgeBase kb : repoResult) {
                KnowledgeBaseVO vo = new KnowledgeBaseVO();
                vo.setTitle(kb.getTitle());
                vo.setDescription(kb.getDescription());
                vo.setType(kb.getType());
                vo.setTag(kb.getTag());
                vo.setSplunkQuery(kb.getSplunk_query());
                vo.setReference(kb.getReference());
                vo.setApplication(kb.getApplication());
                vo.setKnowledgeId(kb.getId());
                vo.setCreateOn(kb.getCreate_date().toString());
                vo.setSearch(search);
                vo.setKnowledgeId(kb.getId());

                results.add(vo);

                // update the mostSearched result....
                long mostSearched = kb.getMost_searched() + 1;
                kb.setMost_searched(mostSearched);
                knowledgeBaseRepository.save(kb);
            }


        }

        return results;
    }

    public List<KnowledgeBaseVO> getKBForHome() {
        List<KnowledgeBaseVO> results = new ArrayList<>();

        Query query = new Query();
        query.addCriteria(Criteria.where("most_searched").gt(5)).limit(10);

        List<KnowledgeBase> repoResult = mongoOperations.find(query, KnowledgeBase.class);
        //List<KnowledgeBase> repoResult = knowledgeBaseRepository.findByMostSearched();
        if (repoResult != null && !repoResult.isEmpty()) {

            for (KnowledgeBase kb : repoResult) {
                KnowledgeBaseVO vo = new KnowledgeBaseVO();
                vo.setTitle(kb.getTitle());
                vo.setDescription(kb.getDescription());
                vo.setType(kb.getType());
                vo.setTag(kb.getTag());
                vo.setSplunkQuery(kb.getSplunk_query());
                vo.setReference(kb.getReference());
                vo.setApplication(kb.getApplication());
                vo.setKnowledgeId(kb.getId());
                vo.setMostSearched(kb.getMost_searched());

                results.add(vo);
            }

        }
        return results;
    }

    public KnowledgeBaseVO getKbByID(String id) {
        Optional<KnowledgeBase> optional = knowledgeBaseRepository.findById(id);
        if (optional.isPresent()) {
            KnowledgeBase base = optional.get();
            KnowledgeBaseVO vo = new KnowledgeBaseVO();
            vo.setTitle(base.getTitle());
            vo.setDescription(base.getDescription());
            vo.setType(base.getType());
            vo.setTag(base.getTag());
            vo.setSplunkQuery(base.getSplunk_query());
            vo.setReference(base.getReference());
            vo.setApplication(base.getApplication());
            vo.setKnowledgeId(id);
            return vo;
        }
        return null;
    }

    public KnowledgeBaseVO update(KnowledgeBaseVO requestVO) {
        Optional<KnowledgeBase> optional = knowledgeBaseRepository.findById(requestVO.getKnowledgeId());
        if (optional.isPresent()) {
            KnowledgeBase kb = optional.get();
            kb.setApplication(requestVO.getApplication());
            kb.setSplunk_query(requestVO.getSplunkQuery());
            kb.setDescription(requestVO.getDescription());
            kb.setReference(requestVO.getReference());
            if (requestVO.getCreateOn() != null) {
                LocalDateTime parseDate = LocalDateTime.parse(requestVO.getCreateOn());
                kb.setCreate_date(parseDate);
            }

            kb = knowledgeBaseRepository.save(kb);

            KnowledgeBaseVO vo = new KnowledgeBaseVO();
            vo.setTitle(kb.getTitle());
            vo.setDescription(kb.getDescription());
            vo.setType(kb.getType());
            vo.setCreateOn(kb.getCreate_date().toString());
            vo.setSplunkQuery(kb.getSplunk_query());
            vo.setReference(kb.getReference());
            vo.setApplication(kb.getApplication());

            return vo;
        }
        return null;
    }
*/
    /*public List<KnowledgeBaseVO> findAll() {
        List<KnowledgeBaseVO> results = new ArrayList<>();
        List<KnowledgeBase> repoResults = knowledgeBaseRepository.findAll();
        for (KnowledgeBase kb : repoResults) {
            KnowledgeBaseVO vo = new KnowledgeBaseVO();
            vo.setTitle(kb.getTitle());
            vo.setDescription(kb.getDescription());
            vo.setType(kb.getType());
            vo.setTag(kb.getTag());
            vo.setSplunkQuery(kb.getSplunk_query());
            vo.setReference(kb.getReference());
            vo.setApplication(kb.getApplication());
            vo.setKnowledgeId(kb.getId());
            vo.setMostSearched(kb.getMost_searched());

            results.add(vo);
        }

        return results;
    }*/
}
