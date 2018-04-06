package itcast.research.service.other.impl;

import itcast.research.entity.other.Log;
import itcast.research.entity.user.User;
import itcast.research.repository.other.ILogRepository;
import itcast.research.service.other.ILogService;
import itcast.research.util.VEAStringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/30 14:27
 * Description:
 */
@Service
@Transactional
public class LogServiceImpl implements ILogService {
    @Resource
    private ILogRepository logRepository;

    @Override
    public void save(Log log) throws Exception{
        logRepository.save(log);
    }

    @Override
    public Page<Log> findAllByPage(int page, int pageSize, Long userId, String keyword, String sort) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, pageSize, VEAStringUtil.getSort(sort, Sort.Direction.DESC));
        Page<Log> logPage = logRepository.findAll(new Specification<Log>() {
            @Override
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (userId != null && userId > 0) {
                    Join<Log, User> logUserJoin = root.join(root.getModel().getSingularAttribute("user", User.class), JoinType.LEFT);
                    Predicate userPath = criteriaBuilder.equal(logUserJoin.get("id").as(Long.class), userId);
                    predicates.add(userPath);
                }
                if (VEAStringUtil.isNotBlank(keyword)) {
                    Path<String> keywordPath = root.get("url");
                    Predicate keywordLike = criteriaBuilder.like(keywordPath, "%" + keyword + "%");
                    predicates.add(keywordLike);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return logPage;
    }
}
