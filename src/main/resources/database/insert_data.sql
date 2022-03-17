insert into person (first_name, last_name, email, password, role_id, image_name) values ('Nani', 'Proger', 'nproger0@is.gd', 'UK9T26T2fA', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Odette', 'Balwin', 'obalwin1@com.com', '2XrO6K2a1', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Hayes', 'Moisey', 'hmoisey2@sourceforge.net', '1RkBZPgkv7MJ', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Bancroft', 'Dayce', 'bdayce3@google.nl', '8QCXBi4', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Heida', 'Gadson', 'hgadson4@qq.com', 'WBr6Bw2G', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Frasier', 'Date', 'fdate6@merriam-webster.com', '7YXCOyDLIb', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Spenser', 'Elvish', 'selvish8@simplemachines.org', 'NQIFewLqF0o', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Mab', 'Joe', 'mjoea@tumblr.com', 'VaDdnr1syH', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Patten', 'Beggin', 'pbegginb@tinyurl.com', 'pX36BF0U4n', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Ami', 'Aicheson', 'student@mail.com', '$2a$10$nF46av2BqtbjKmZbVaGAfuPIF3shcjQY3mLE4ZsKFMSXDmOrSteCm', 3, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Gayler', 'Jarrel', 'admin@mail.com', '$2a$10$nF46av2BqtbjKmZbVaGAfuPIF3shcjQY3mLE4ZsKFMSXDmOrSteCm', 1, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Kalila', 'Espinay', 'kespinay7@discovery.com', 'RpjFCa', 2, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Sallyanne', 'Cranny', 'scranny5@freewebs.com', 'yV2Wqgi', 2, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Uriel', 'Brickham', 'ubrickham9@surveymonkey.com', 'QNFnrwRHCbJS', 2, 'default.jpeg');
insert into person (first_name, last_name, email, password, role_id, image_name) values ('Kalil', 'Odby', 'teacher@mail.com', '$2a$10$nF46av2BqtbjKmZbVaGAfuPIF3shcjQY3mLE4ZsKFMSXDmOrSteCm', 2, 'default.jpeg');

insert into subject(id, name) values(default, 'Business analysis');
insert into subject(id, name) values(default, 'Philology');
insert into subject(id, name) values(default, 'Cybersecurity');
insert into subject(id, name) values(default, 'Programming & Development');
insert into subject(id, name) values(default, 'Artificial Intelligence');
insert into subject(id, name) values(default, 'Cloud Computing');

insert into subject_translation(subject_id, language_id, name_translation) values(1, 1, 'Business analysis');
insert into subject_translation(subject_id, language_id, name_translation) values(1, 2, 'Бізнес-аналіз');
insert into subject_translation(subject_id, language_id, name_translation) values(2, 1, 'Philology');
insert into subject_translation(subject_id, language_id, name_translation) values(3, 1, 'Cybersecurity');
insert into subject_translation(subject_id, language_id, name_translation) values(3, 2, 'Кібербезка');
insert into subject_translation(subject_id, language_id, name_translation) values(4, 1, 'Programming & Development');
insert into subject_translation(subject_id, language_id, name_translation) values(4, 2, 'Програмування');
insert into subject_translation(subject_id, language_id, name_translation) values(5, 1, 'Artificial Intelligence');
insert into subject_translation(subject_id, language_id, name_translation) values(5, 2, 'Штучний інтелект');
insert into subject_translation(subject_id, language_id, name_translation) values(6, 1, 'Cloud Computing');
insert into subject_translation(subject_id, language_id, name_translation) values(6, 2, 'Хмарні обчислення');


insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (12, 1, 1, 'Business Development', 'Etiam faucibus cursus urna.', 37, '2022-12-18 12:14:09', '2023-09-18 12:28:56', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 2, 2, 'Responsive', 'Vivamus metus arcu, adipiscing molestie, hendrerit at, vulputate vitae, nisl.', 94, '2022-04-07 18:20:58', '2022-12-24 21:41:33', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 1, 3, 'Asynchronous', 'Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci.', 62, '2022-05-06 22:54:14', '2022-09-13 03:27:40', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 1, 4, 'Engineering', 'Nulla mollis molestie lorem. Quisque ut erat.', 98, '2022-09-28 09:07:42', '2022-11-01 18:39:26', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 1, 5, 'Exuding', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam.', 15, '2022-05-13 19:48:10', '2022-07-26 07:07:23', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 2, 6, 'Cohesive', 'Morbi vestibulum, velit id pretium iaculis, diam erat fermentum justo, nec condimentum neque sapien placerat ante. Nulla justo.', 45, '2021-12-11 21:18:01', '2022-03-07 12:32:32', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (14, 2, 1, 'Sharable', 'Integer ac leo. Pellentesque ultrices mattis odio.', 27, '2022-04-30 08:24:26', '2022-11-05 17:43:48', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 1, 2, 'Empowering', 'Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.', 19, '2022-03-17 03:31:13', '2022-03-025 16:57:45', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 2, 3, 'Multimedia', 'Morbi vel lectus in quam fringilla rhoncus. Mauris enim leo, rhoncus sed, vestibulum sit amet, cursus id, turpis.', 93, '2022-07-31 23:14:20', '2022-11-01 10:39:20', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (12, 2, 4, 'Support', 'Aliquam quis turpis eget elit sodales scelerisque.', 56, '2022-02-19 23:01:00', '2022-04-02 19:21:39', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (12, 2, 5, 'Stand-alone', 'Vivamus vel nulla eget eros elementum pellentesque.', 18, '2022-07-19 21:14:01', '2022-09-16 18:32:12', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (14, 1, 6, 'Services', 'Vivamus in felis eu sapien cursus vestibulum. Proin eu mi.', 89, '2022-05-07 14:29:25', '2022-07-20 21:04:49', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 1, 1, 'Legal', 'Vivamus vel nulla eget eros elementum pellentesque.', 87, '2022-03-15 11:49:47', '2022-03-15 18:34:39', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 1, 2, 'Japanese', 'Praesent lectus.', 80, '2021-12-24 16:21:52', '2022-03-10 20:01:39', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (14, 1, 3, 'Reverse-engineered', 'Integer tincidunt ante vel ipsum. Praesent blandit lacinia erat.', 74, '2022-01-15 16:33:58', '2022-06-24 05:43:33', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 2, 4, 'Cross-platform', 'In congue.', 81, '2022-08-01 15:00:30', '2022-08-28 06:42:58', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (12, 2, 5, 'Superstructure', 'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est.', 99, '2022-07-20 14:05:05', '2022-10-13 14:50:35', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (15, 2, 6, 'Concepts', 'Aenean sit amet justo. Morbi ut odio.', 50, '2022-02-08 15:37:43', '2022-10-21 09:20:19', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (12, 1, 1, 'Sales', 'In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.', 33, '2022-03-19 12:17:31', '2022-10-18 18:38:40', 'default.jpeg');
insert into course (teacher_id, language_id, subject_id, title, description, max_score, start_date, end_date, image_name) values (13, 1, 2, 'French. Pronunciation', 'Donec posuere metus vitae ipsum.', 95, '2022-12-08 17:57:54', '2023-01-23 15:18:12', 'default.jpeg');

insert into student_course(student_id, course_id, score) values(10, 14, 75);
insert into student_course(student_id, course_id) values(10, 18);