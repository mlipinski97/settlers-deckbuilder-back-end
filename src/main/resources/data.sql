insert into users (email, password, role, is_active)
values ('user@email.com', '$2y$10$mWAlAqaruPx6xT8ZQuRd2uWSxm.ettcNzlZlIptZ73xQt/tW.Cglq', 0, true),
       ('admin@email.com', '$2y$10$15CBlmf6qkh1spbawbuCaOE/h69MFJaS4pjwDknZuk5gfuNVSlysm', 1, true);

insert into card (name, color, number_of_copies, affiliation, type, effect, building_bonus, cost, expansion)
values ('first', 'blue', 3, 'roman', 'production', '1 gold', null, '2 Stone', 'base'),
('second card', 'red', 2, 'roman', 'action', '1 gold for 2 banans :)', null, '2 Stone 1 wood', 'base');
