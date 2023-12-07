-- Insert Countries
INSERT INTO countries (name) VALUES ('España'), ('Estados Unidos');

-- Insert Regions
INSERT INTO regions (name, country_id) VALUES ('Cataluña', 1), ('California', 2);

-- Insert Cities
INSERT INTO cities (name, region_id) VALUES ('Barcelona', 1), ('San Francisco', 2);

-- Insert Postal Codes
INSERT INTO postal_codes (code, city_id) VALUES ('08001', 1), ('94105', 2);

-- Propiedades para el vendedor con ID 3
INSERT INTO properties (type, description, rooms, baths, surface, status, registry_document_url, catastral_reference, user_id, contact, address, latitude, longitude, postal_code_id, created_at)
VALUES ('VIVIENDA', 'Piso en Barcelona', 3, 2, 120.5, 'PENDING_VALIDATION', 'https://example.com/registry1.pdf', '67890BCN', 3, 'jordimimbrera@gmail.com', 'Calle Falsa 124, Barcelona', 41.38879, 2.15899, 1, NOW());

INSERT INTO property_images (image_url, property_id) VALUES ('https://static.inmofactory.com/images/inmofactory/documents/1/108152/35250690/631947342.jpg?rule=web_1200x0', 1),
                                                            ('https://static.inmofactory.com/images/inmofactory/documents/1/108152/35250690/631947343.jpg?rule=web_1200x0', 1);

INSERT INTO owner_history (property_id, user_id, start_date) VALUES (1, 3, NOW());

-- Propiedad para el vendedor con ID 4
INSERT INTO properties (type, description, rooms, baths, surface, status, registry_document_url, catastral_reference, user_id, contact, address, latitude, longitude, postal_code_id, created_at)
VALUES ('VIVIENDA', 'Casa en Barcelona', 3, 2, 120.5, 'VALIDATED', 'https://example.com/registry1.pdf', '12345BCN', 4, 'jordimimbrera@gmail.com', 'Calle Falsa 123, Barcelona', 41.38879, 2.15899, 1, NOW()),
       ('VIVIENDA', 'Apartamento en San Francisco', 2, 1, 85.0, 'IN_AUCTION', 'https://example.com/registry2.pdf', '67890SF', 4, 'jmimbrera@uoc.edu', 'Market St, San Francisco', 37.77493, -122.41942, 2, NOW());

INSERT INTO property_images (image_url, property_id) VALUES ('https://static.inmofactory.com/images/inmofactory/documents/1/137917/35016910/625465709.jpg?rule=web_1200x0', 2),
                                                            ('https://static.inmofactory.com/images/inmofactory/documents/1/137917/35016910/624603524.jpg?rule=web_1200x0', 2);

INSERT INTO owner_history (property_id, user_id, start_date) VALUES (3, 4, NOW());
