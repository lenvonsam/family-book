drop view if EXISTS v_family_user;
create view v_family_user as
select fum.id,fum.family_id,fum.user_id,fum.default_choosed,u.phone,u.nickname,u.open_id,f.name as family_name,f.is_show,f.creator_id from family_user_map fum, user u, family f  where u.id = fum.user_id and f.id = fum.family_id;

drop view if EXISTS v_family;
create view v_family as
select f.*,u.nickname from family f, user u where u.id = f.creator_id;

drop view if EXISTS v_message;
create view v_message as
select m.*,(select u.nickName from user u where u.id = m.sender) as senderName,(select u.nickName from user u where u.id = m.receiver) as receiverName,(case when m.family_id =-1 then '' else (select f.name from family f where f.id = m.family_id) end) as familyName from message m;