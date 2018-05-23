package itcast.research.service.permission.impl;

import itcast.research.entity.permission.*;
import itcast.research.exception.CommonException;
import itcast.research.repository.permission.*;
import itcast.research.service.permission.IPermissionService;
import itcast.research.util.permission.PermissionConstants;
import itcast.research.util.VEAStringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created with IDEA
 * Author:xzengsf
 * Date:2018/3/22 11:51
 * Description: 权限服务接口实现类
 */
@Service
@Transactional
public class PermissionServiceImpl implements IPermissionService {
    @Resource
    private IPermissionRepository permissionRepository;
    @Resource
    private IPermissionMenuExtendRepository permissionMenuExtendRepository;
    @Resource
    private IPermissionPointExtendRepository permissionPointExtendRepository;
    @Resource
    private IPermissionApiExtendRepository permissionApiExtendRepository;
    @Resource
    private IPermissionGroupRepository permissionGroupRepository;

    @Override
    public void save(Permission permission) throws Exception {
        //处理父权限数据
        if (permission.getParentPermission() != null) {
            Permission parentPermission = permissionRepository.getOne(permission.getParentPermission().getId());
            if (parentPermission == null) {
                throw new CommonException("父权限不存在！");
            }
            permission.setParentPermission(parentPermission);
        }
        Date now = new Date();
        permission.setCreateTime(now);
        permissionRepository.save(permission);
        switch (permission.getType()) {
            case PermissionConstants.PERMISSION_TYPE_MENU:
                //权限类型为菜单权限
                PermissionMenuExtend menuExtend = permission.getPermissionMenuExtend();
                if (menuExtend == null) {
                    throw new CommonException("权限信息不完整！");
                }
                menuExtend.setPermission(permission);
                permissionMenuExtendRepository.save(menuExtend);
                break;
            case PermissionConstants.PERMISSION_TYPE_POINT:
                //权限类型为点权限
                PermissionPointExtend pointExtend = permission.getPermissionPointExtend();
                if (pointExtend == null) {
                    throw new CommonException("权限信息不完整！");
                }
                pointExtend.setPermission(permission);
                permissionPointExtendRepository.save(pointExtend);
                break;
            default:
                throw new CommonException("暂不支持该类型！");
        }
    }

    @Override
    public List<Permission> findAll(String sort) throws Exception {
        //查询
        List<Permission> permissionList = permissionRepository.findAll(new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //查询第一层级权限
                Predicate predicate = criteriaBuilder.isNull(root.get("parentPermission"));
                predicates.add(predicate);
                //权限类型不能为API权限
                Predicate typePredicate = criteriaBuilder.notEqual(root.get("type"), PermissionConstants.PERMISSION_TYPE_API);
                predicates.add(typePredicate);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, VEAStringUtil.getSort(sort, Sort.Direction.ASC));
        return permissionList;
    }

    @Override
    public List<Permission> findAllByCheckApi() {
        List<Permission> permissionList = permissionRepository.findAll(new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //权限类型为API权限
                Predicate typePredicate = criteriaBuilder.equal(root.get("type"), PermissionConstants.PERMISSION_TYPE_API);
                predicates.add(typePredicate);
                //需校验API权限
                Predicate checkPredicate = criteriaBuilder.equal(root.get("permissionApiExtend").get("apiLevel"), PermissionConstants.API_LEVEL_CHECK);
                predicates.add(checkPredicate);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        return permissionList;
    }

    @Override
    public List<Permission> findAllByApi() throws Exception {
        List<Permission> permissionList = permissionRepository.findAll(new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //权限类型为API权限
                Predicate typePredicate = criteriaBuilder.equal(root.get("type"), PermissionConstants.PERMISSION_TYPE_API);
                predicates.add(typePredicate);
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        return permissionList;
    }

    @Override
    public Permission update(Permission permission) throws Exception {
        Permission originPermission = permissionRepository.getOne(permission.getId());
        //更新父ID
        if (permission.getParentPermission() != null) {
            Permission parentPermission = permissionRepository.getOne(permission.getParentPermission().getId());
            if (parentPermission != null) {
                originPermission.setParentPermission(parentPermission);
            }
        }
        //更新名称
        if (VEAStringUtil.isNotBlank(permission.getName())) {
            originPermission.setName(permission.getName());
        }
        if (originPermission.getType().equals(PermissionConstants.PERMISSION_TYPE_MENU)) {
            //更新菜单权限扩展信息
            if (permission.getPermissionMenuExtend() != null) {
                PermissionMenuExtend menuExtend = originPermission.getPermissionMenuExtend();
                if (permission.getPermissionMenuExtend().getCode() != null) {
                    menuExtend.setCode(permission.getPermissionMenuExtend().getCode());
                }
            }
        } else if (originPermission.getType().equals(PermissionConstants.PERMISSION_TYPE_POINT)) {
            //更新权限点扩展信息
            if (permission.getPermissionPointExtend() != null) {
                PermissionPointExtend pointExtend = originPermission.getPermissionPointExtend();
                if (permission.getPermissionPointExtend().getCode() != null) {
                    pointExtend.setCode(permission.getPermissionPointExtend().getCode());
                }
            }
        } else {
            throw new CommonException("暂不支持该类型！");
        }
        return originPermission;
    }

    @Override
    public Permission findOne(Long id) throws Exception {
        return permissionRepository.getOne(id);
    }

    @Override
    public void del(Long id) throws Exception {
        Permission permission = permissionRepository.getOne(id);
        if (permission != null) {
            //处理与权限组关联关系
            if (permission.getPermissionGroups() != null && permission.getPermissionGroups().size() > 0) {
                for (PermissionGroup group : permission.getPermissionGroups()) {
                    PermissionGroup originGroup = permissionGroupRepository.getOne(group.getId());
                    if (originGroup != null && originGroup.getPermissions() != null && originGroup.getPermissions().size() > 0) {
                        Set<Permission> permissionSet = originGroup.getPermissions();
                        Set<Permission> inPermissionSet = new HashSet<>();
                        for (Permission originPermission : permissionSet) {
                            if (originPermission.getId() != permission.getId()) {
                                inPermissionSet.add(originPermission);
                            }
                        }
                        originGroup.setPermissions(inPermissionSet);
                    }
                }
            }
            //处理与子权限的关系
            LinkedList<Permission> subPermissionList = new LinkedList<>();
            getNode(new ArrayList<>(permission.getChildrenPermission()), subPermissionList);
            while (subPermissionList.size() != 0) {
                Permission subPermission = subPermissionList.pollLast();
                permissionRepository.deleteById(subPermission.getId());
            }
            permissionRepository.deleteById(id);
        }
    }

    private void getNode(List<Permission> permissionList, List<Permission> subPermissionList) {
        for (int i = 0; i < permissionList.size(); i++) {
            Permission permission = permissionList.get(i);
            //处理与权限组关联关系
            if (permission.getPermissionGroups() != null && permission.getPermissionGroups().size() > 0) {
                for (PermissionGroup group : permission.getPermissionGroups()) {
                    PermissionGroup originGroup = permissionGroupRepository.getOne(group.getId());
                    if (originGroup != null && originGroup.getPermissions() != null && originGroup.getPermissions().size() > 0) {
                        Set<Permission> permissionSet = originGroup.getPermissions();
                        Set<Permission> inPermissionSet = new HashSet<>();
                        for (Permission originPermission : permissionSet) {
                            if (originPermission.getId() != permission.getId()) {
                                inPermissionSet.add(originPermission);
                            }
                        }
                        originGroup.setPermissions(inPermissionSet);
                    }
                }
            }
            if (permission.getChildrenPermission() != null && permission.getChildrenPermission().size() > 0) {
                Set<Permission> permissionSet = permission.getChildrenPermission();
                List<Permission> subMenuList = new ArrayList<>();
                for (Permission per : permissionSet) {
                    if (per.getType().equals(PermissionConstants.PERMISSION_TYPE_MENU)) {
                        subMenuList.add(per);
                        subPermissionList.add(per);
                    } else {
                        permissionRepository.deleteById(per.getId());
                    }
                }
                if (subMenuList.size() > 0) {
                    getNode(subMenuList, subPermissionList);
                }
            }
        }
    }

    @Override
    public void saveApiPermissions(List<Permission> permissionList) throws Exception {
        Date now = new Date();
        for (Permission permission : permissionList) {
            PermissionApiExtend apiExtend = permission.getPermissionApiExtend();
            //检测API权限是否存在
            Optional<Permission> permissionOptional = permissionRepository.findOne(new Specification<Permission>() {
                @Override
                public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<Predicate>();
                    Predicate typePredicate = criteriaBuilder.equal(root.get("type"), PermissionConstants.PERMISSION_TYPE_API);
                    predicates.add(typePredicate);
                    Predicate urlPredicate = criteriaBuilder.equal(root.get("permissionApiExtend").get("apiUrl"), apiExtend.getApiUrl());
                    predicates.add(urlPredicate);
                    Predicate methodPredicate = criteriaBuilder.equal(root.get("permissionApiExtend").get("apiMethod"), apiExtend.getApiMethod());
                    predicates.add(methodPredicate);
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            });
            if (!permissionOptional.isPresent() || permissionOptional.get() == null) {
                //不存在则添加
                permission.setCreateTime(now);
                permissionRepository.save(permission);
                apiExtend.setPermission(permission);
                permissionApiExtendRepository.save(apiExtend);
            } else {
                //存在则更新
                permissionOptional.get().getPermissionApiExtend().setApiLevel(apiExtend.getApiLevel());
            }
        }
    }
}
