// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.app2.app2t.domain.security;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

privileged aspect AppRole_Custom_Jpa_ActiveRecord {

    @Transactional
    public static List<AppRole> AppRole.findNameApprole() {
        EntityManager ent = AppRole.entityManager();
        Criteria criteria = ((Session) ent.getDelegate()).createCriteria(AppRole.class);

        try {
            List<AppRole> appRoles = criteria.list();
            AppRole appRole = appRoles.get(0);

        } catch (IndexOutOfBoundsException e) {

            return criteria.list();
        }
        return criteria.list();
    }

    public static AppRole AppRole.insertAppRole(String roleCode, String roleName) {
        AppRole appRole = new AppRole();
        appRole.setRoleCode(roleCode);
        appRole.setRoleName(roleName);
        appRole.setVersion(0);
        appRole.persist();

        return appRole;
    }

    public static AppRole AppRole.updateRole(Long roleId, String roleCode, String roleName) {
        try {
            AppRole appRole = AppRole.findAppRole(roleId);
            appRole.setRoleCode(roleCode);
            appRole.setRoleName(roleName);
            appRole.merge();
            return appRole;
        } catch (Exception ex) {

        }

        return null;
    }

    public static List<AppRole> AppRole.findByRoleCode(String roleCode, Long roleId) {
        EntityManager ent = AppRole.entityManager();
        Criteria criteria = ((Session) ent.getDelegate()).createCriteria(AppRole.class);
        criteria.add(Restrictions.eq("roleCode", roleCode));
        if(roleId != null)
            criteria.add(Restrictions.ne("id", roleId));
        return criteria.list();
    }

    public static boolean AppRole.deleteRole(Long roleId) {
        try {
            AppRole appRole = AppRole.findAppRole(roleId);
            appRole.remove();
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

}