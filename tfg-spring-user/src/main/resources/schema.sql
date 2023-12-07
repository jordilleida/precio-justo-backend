INSERT INTO users (name, surname, email, password, disabled)
VALUES  ('Jordi', 'Prueba', 'jordi@uoc.edu', '123', false),
        ('Pepe', 'Prueba', 'pepe@uoc.edu', '123', false),
        ('Juan', 'Prueba', 'jordimimbrera@gmail.com', '123', false),
        ('Pablo', 'Prueba', 'pablo@uoc.edu', '123', false);

INSERT INTO roles (name) VALUES ('BUYER'), ('SELLER') , ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES  (1, 3), -- Jordi es ADMIN
        (2, 1), -- Pepe es BUYER
        (3, 1), -- Juan es BUYER
     --   (3, 2), -- Juan tambien es SELLER
        (4, 1), -- Pablo es BUYER
        (4, 2); -- Pablo tambien es SELLER
