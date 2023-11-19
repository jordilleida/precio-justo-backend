INSERT INTO users (name, surname, email, password, disabled)
VALUES  ('Jordi', 'Prueba', 'jordi@uoc.edu', '123', false),
        ('Pepe', 'Prueba', 'pepe@uoc.edu', '123', false),
        ('Juan', 'Prueba', 'juan@uoc.edu', '123', false);

INSERT INTO roles (name) VALUES ('BUYER'), ('SELLER') , ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES  (1, 3), -- Jordi es ADMIN
        (2, 1), -- Pepe es SELLER
        (3, 1), -- Juan es BUYER
        (3, 2); -- Juan también es SELLER
