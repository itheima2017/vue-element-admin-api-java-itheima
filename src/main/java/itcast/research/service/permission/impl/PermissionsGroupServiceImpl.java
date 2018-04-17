package itcast.research.service.permission.impl;

import itcast.research.entity.permission.Permission;
import itcast.research.entity.permission.PermissionGroup;
import itcast.research.exception.CommonException;
import itcast.research.repository.permission.IPermissionGroupRepository;
import itcast.research.repository.permission.IPermissionRepository;
import itcast.research.service.permission.IPermissionsGroupService;
import itcast.research.util.permission.PermissionConstants;
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
import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/23 10:35
 * Description: 权限组服务接口实现类
 */
@Service
@Transactional
public class PermissionsGroupServiceImpl implements IPermissionsGroupService {
    @Resource
    private IPermissionGroupRepository permissionGroupRepository;
    @Resource
    private IPermissionRepository permissionRepository;

    @Override
    public Page<PermissionGroup> findPermissionGroupByPage(int page, int pageSize, String title, String sort) throws Exception {
        Pageable pageable = PageRequest.of(page - 1, pageSize, VEAStringUtil.getSort(sort, Sort.Direction.ASC));
        Page<PermissionGroup> permissionGroupPage = permissionGroupRepository.findAll(new Specification<PermissionGroup>() {
            @Override
            public Predicate toPredicate(Root<PermissionGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (VEAStringUtil.isNotBlank(title)) {
                    Path<String> namePath = root.get("name");
                    Predicate nameLike = criteriaBuilder.like(namePath, "%" + title + "%");
                    predicates.add(nameLike);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);
        return permissionGroupPage;
    }

    @Override
    public List<PermissionGroup> findAll() throws Exception {
        return permissionGroupRepository.findAll();
    }

    @Override
    public void save(PermissionGroup permissionGroup) throws Exception {
        Date now = new Date();
        permissionGroup.setCreateTime(now);
        permissionGroup.setUpdateTime(now);
        //处理权限绑定
        if (permissionGroup.getPermissions() != null && permissionGroup.getPermissions().size() > 0) {
            Set<Permission> bindPermissionSet = new HashSet<>();
            for (Permission permission : permissionGroup.getPermissions()) {
                Permission bindPermission = permissionRepository.getOne(permission.getId());
                if (bindPermission != null) {
                    bindPermissionSet.add(bindPermission);
                }
            }
            permissionGroup.setPermissions(bindPermissionSet);
        }
        permissionGroupRepository.save(permissionGroup);
    }

    @Override
    public PermissionGroup update(PermissionGroup permissionGroup) throws Exception {
        PermissionGroup originPermissionGroup = permissionGroupRepository.getOne(permissionGroup.getId());
        if (originPermissionGroup == null) {
            throw new CommonException("权限组不存在！");
        }
        //处理权限组名称
        if (VEAStringUtil.isNotBlank(permissionGroup.getName())) {
            originPermissionGroup.setName(permissionGroup.getName());
        }
        //从权限列表中提取API权限
        Set<Permission> originPermissionSet = originPermissionGroup.getPermissions();
        Set<Permission> apiPermissionSet = new HashSet<>();
        for (Permission permission : originPermissionSet) {
            if (permission.getType() == PermissionConstants.PERMISSION_TYPE_API) {
                apiPermissionSet.add(permission);
            }
        }
        //处理权限绑定
        Set<Permission> bindPermissionSet = new HashSet<>();
        if (permissionGroup.getPermissions() != null && permissionGroup.getPermissions().size() > 0) {
            for (Permission permission : permissionGroup.getPermissions()) {
                Permission bindPermission = permissionRepository.getOne(permission.getId());
                if (bindPermission != null) {
                    bindPermissionSet.add(bindPermission);
                }
            }
        }
        //添加API权限
        bindPermissionSet.addAll(apiPermissionSet);
        originPermissionGroup.setPermissions(bindPermissionSet);
        return originPermissionGroup;
    }

    @Override
    public PermissionGroup updateApi(PermissionGroup permissionGroup) throws Exception {
        PermissionGroup originPermissionGroup = permissionGroupRepository.getOne(permissionGroup.getId());
        if (originPermissionGroup == null) {
            throw new CommonException("权限组不存在！");
        }
        //处理权限组名称
        if (VEAStringUtil.isNotBlank(permissionGroup.getName())) {
            originPermissionGroup.setName(permissionGroup.getName());
        }
        //从权限列表中提取菜单权限
        Set<Permission> originPermissionSet = originPermissionGroup.getPermissions();
        Set<Permission> menuPermissionSet = new HashSet<>();
        for (Permission permission : originPermissionSet) {
            if (permission.getType() != PermissionConstants.PERMISSION_TYPE_API) {
                menuPermissionSet.add(permission);
            }
        }
        //处理权限绑定
        Set<Permission> bindPermissionSet = new HashSet<>();
        if (permissionGroup.getPermissions() != null && permissionGroup.getPermissions().size() > 0) {
            for (Permission permission : permissionGroup.getPermissions()) {
                Permission bindPermission = permissionRepository.getOne(permission.getId());
                if (bindPermission != null) {
                    bindPermissionSet.add(bindPermission);
                }
            }
        }
        //添加菜单权限
        bindPermissionSet.addAll(menuPermissionSet);
        originPermissionGroup.setPermissions(bindPermissionSet);
        return originPermissionGroup;
    }

    @Override
    public void del(Long id) throws Exception {
        PermissionGroup permissionGroup = permissionGroupRepository.getOne(id);
        if (permissionGroup != null) {
            permissionGroup.setPermissions(null);
            permissionGroupRepository.deleteById(id);
        }
    }

    @Override
    public PermissionGroup findOne(Long id) throws Exception {
        return permissionGroupRepository.getOne(id);
    }

    @Override
    public PermissionGroup findOneByUid(Long uid) throws Exception {
        Optional<PermissionGroup> permissionGroupOptional = permissionGroupRepository.findOne(new Specification<PermissionGroup>() {
            @Override
            public Predicate toPredicate(Root<PermissionGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                SetJoin setJoin = root.join(root.getModel().getSet("users"), JoinType.LEFT);
                Predicate p = criteriaBuilder.equal(setJoin.get("id").as(Long.class), uid);
                predicates.add(p);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        return permissionGroupOptional.get();
    }
}
