-- Заполнение ролей
INSERT INTO roles (name, description) VALUES
                                          ('ASTRONOMER', 'Астроном - основной пользователь системы'),
                                          ('ENGINEER', 'Инженер - обслуживание оборудования'),
                                          ('ADMIN', 'Администратор - полный доступ'),
                                          ('STUDENT', 'Студент - ограниченный доступ'),
                                          ('GUEST', 'Гость - минимальный доступ');

insert into users (login, password_hash, full_name, email, phone, role_id, is_active)
values ('ADMIN', '$2a$10$rx4VJi8eRqv5bkmW8dioi.qsYgU4Skcjfav/HcMC/GhR0n.dZ3eLm', 'ADMIN', 'admin@gmail.com', '89996667744', 3, true);


-- Заполнение телескопов
INSERT INTO telescopes (name, type, aperture, focal_length, location, status, max_resolution) VALUES
                                                                                                  ('БТА-1', 'OPTICAL', 6.00, 24000, '44.725°N, 41.025°E', 'AVAILABLE', '0.02 угл. сек.'),
                                                                                                  ('РТ-64', 'RADIO', 64.00, NULL, '55.930°N, 48.745°E', 'AVAILABLE', '1 угл. мин.'),
                                                                                                  ('ИК-Тел', 'INFRARED', 2.50, 15000, '43.646°N, 41.432°E', 'MAINTENANCE', '0.5 угл. сек.');

-- Заполнение астрономических объектов
INSERT INTO astronomical_objects (name, type, constellation, coordinates, magnitude, distance, description) VALUES
                                                                                                                ('Сириус', 'STAR', 'Большой Пес', '06h 45m 08.9s, -16° 42′ 58″', -1.46, 8.6, 'Самая яркая звезда ночного неба'),
                                                                                                                ('Андромеда', 'GALAXY', 'Андромеда', '00h 42m 44.3s, +41° 16′ 9″', 3.44, 2537000, 'Галактика Андромеды, спиральная галактика'),
                                                                                                                ('Орион', 'NEBULA', 'Орион', '05h 35m 17.3s, -05° 23′ 28″', 4.0, 1344, 'Туманность Ориона, область звездообразования');