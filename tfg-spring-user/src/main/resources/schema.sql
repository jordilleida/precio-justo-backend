INSERT INTO users (name, surname, email, password, disabled)
VALUES  ('Jordi', 'Prueba', 'jordi@uoc.edu', '123', false),
        ('Pepe', 'Prueba', 'pepe@uoc.edu', '123', false),
        ('Juan', 'Prueba', 'juan@uoc.edu', '123', false);

INSERT INTO roles (name) VALUES ('BUYER'), ('SELLER') , ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES  (2, 3), -- Jordi es ADMIN
        (3, 1), -- Pepe es SELLER
        (1, 1), -- Empresario es BUYER
        (1, 2); -- Empresario también es SELLER
