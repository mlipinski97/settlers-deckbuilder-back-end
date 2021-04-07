insert into users (email, password, role, is_active)
values ('user@email.com', '$2y$10$mWAlAqaruPx6xT8ZQuRd2uWSxm.ettcNzlZlIptZ73xQt/tW.Cglq', 0, true),
       ('admin@email.com', '$2y$10$15CBlmf6qkh1spbawbuCaOE/h69MFJaS4pjwDknZuk5gfuNVSlysm', 1, true);

insert into card (name, faction, type, cost_foundation, cost_first_resource_quantity, cost_first_resource_type,
cost_second_resource_quantity, cost_second_resource_type, raise_first_resource_quantity, raise_first_resource_type,
raise_second_resource_quantity, raise_second_resource_type, deal_effect, color, quantity, effect, building_bonus, expansion)
values ('ninjas', 'japanese', 'action', 1, 1, 'stone', null, null, 1, 'victory point', null, null, 'sword', 'black', 2,
'action: spend 1 worker to choose one enemy Deal. Discard it and immediately gain the Goods the Deal provides.',
'2 workers to immediately use as samurai.', 'base game');
