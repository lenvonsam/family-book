drop view if EXISTS v_family_user;
create view v_family_user as
select fum.id,fum.family_id,fum.user_id,fum.default_choosed,u.phone,u.nickname,u.open_id,f.name as family_name,f.is_show from family_user_map fum, user u, family f  where u.id = fum.user_id and f.id = fum.family_id;