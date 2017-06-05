drop view if EXISTS v_family_user;
create view v_family_user as
select fum.*,u.phone,u.nickname,f.name as family_name from family_user_map fum, user u, family f  where u.id = fum.user_id and f.id = fum.family_id;