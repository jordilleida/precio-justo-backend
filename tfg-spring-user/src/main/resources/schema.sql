INSERT INTO users (id, name, surname, email, password, disabled)
VALUES  (0, 'Jordi', 'Prueba', 'jordi@uoc.edu', '123', false),
        (1, 'Pepe', 'Prueba', 'pepe@uoc.edu', '123', false),
        (2, 'Juan', 'Prueba', 'juan@uoc.edu', '123', false);

INSERT INTO roles (name) VALUES ('BUYER'), ('SELLER') , ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES  (1, 3), -- Jordi es ADMIN
        (2, 1), -- Pepe es SELLER
        (0, 1), -- Empresario es BUYER
        (0, 2); -- Empresario también es SELLER
