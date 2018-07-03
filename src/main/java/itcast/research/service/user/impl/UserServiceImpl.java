package itcast.research.service.user.impl;

import itcast.research.entity.permission.PermissionGroup;
import itcast.research.entity.user.User;
import itcast.research.exception.CommonException;
import itcast.research.repository.permission.IPermissionGroupRepository;
import itcast.research.repository.user.IUserRepository;
import itcast.research.service.user.IUserService;
import itcast.research.util.CodecUtil;
import itcast.research.util.EUserStatus;
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
import java.util.Date;
import java.util.List;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 14:02
 * Description: 用户服务接口实现类
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserRepository userRepository;
    @Resource
    private IPermissionGroupRepository permissionGroupRepository;

    @Override
    public Page<User> findAllByPage(int page, int pageSize, Long id, String username, String sort) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, pageSize, VEAStringUtil.getSort(sort, Sort.Direction.ASC));
        Page<User> userPage = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (id != null) {
                    Path<Long> idPath = root.get("id");
                    Predicate id_predicate = criteriaBuilder.equal(idPath.as(Long.class), id);
                    predicates.add(id_predicate);
                }
                if (username != null) {
                    Path<String> usernamePath = root.get("username");
                    Predicate usernameLike = criteriaBuilder.like(usernamePath, "%" + username + "%");
                    predicates.add(usernameLike);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return userPage;
    }

    @Override
    public List<User> findAllBySimple(String username, String sort) throws Exception {
        return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //用户状态为启用状态
                Path<Integer> statusPath = root.get("status");
                Predicate status_predicate = criteriaBuilder.equal(statusPath.as(Integer.class), EUserStatus.NORMAL.getCode());
                predicates.add(status_predicate);
                //根据传入用户名关键字模糊查询
                if (username != null) {
                    Path<String> usernamePath = root.get("username");
                    Predicate usernameLike = criteriaBuilder.like(usernamePath, "%" + username + "%");
                    predicates.add(usernameLike);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, VEAStringUtil.getSort(sort, Sort.Direction.ASC));
    }

    @Override
    public void save(User user) throws Exception {
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastUpdateTime(now);
        user.setStatus(EUserStatus.NORMAL.getCode());
        //对密码进行加密
        user.setPassword(CodecUtil.getSHA256(user.getPassword()));
        if (user.getPermissionGroup() != null) {
            PermissionGroup permissionGroup = permissionGroupRepository.getOne(user.getPermissionGroup().getId());
            if (permissionGroup != null) {
                user.setPermissionGroup(permissionGroup);
            } else {
                throw new CommonException("所选权限组不存在！");
            }
        }
        userRepository.save(user);
    }

    @Override
    public User update(User user) throws Exception {
        User originUser = userRepository.getOne(user.getId());
        if (originUser == null) {
            throw new CommonException("用户不存在！");
        }
        originUser.setLastUpdateTime(new Date());
        //处理用户邮箱信息
        if (VEAStringUtil.isNotBlank(user.getEmail())) {
            originUser.setEmail(user.getEmail());
        }
        //处理密码信息
        if (VEAStringUtil.isNotBlank(user.getPassword())) {
            originUser.setPassword(CodecUtil.getSHA256(user.getPassword()));
        }
        //处理电话信息
        if (VEAStringUtil.isNotBlank(user.getPhone())) {
            originUser.setPhone(user.getPhone());
        }
        //处理用户名信息
        if (VEAStringUtil.isNotBlank(user.getUsername())) {
            originUser.setUsername(user.getUsername());
        }
        //处理头像信息
        if (VEAStringUtil.isNotBlank(user.getAvatar())) {
            originUser.setAvatar(user.getAvatar());
        }
        //处理用户说明
        if (user.getIntroduction() != null) {
            originUser.setIntroduction(user.getIntroduction());
        }
        //处理权限组ID
        if (user.getPermissionGroup() != null) {
            PermissionGroup group = permissionGroupRepository.getOne(user.getPermissionGroup().getId());
            if (group != null) {
                originUser.setPermissionGroup(group);
            }
        }
        return originUser;
    }

    @Override
    public void del(Long id) throws Exception {
        User user = userRepository.getOne(id);
        if (user != null) {
            user.setStatus(EUserStatus.DISABLE.getCode());
        }
    }

    @Override
    public User findById(Long id) throws Exception {
        return userRepository.getOne(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
