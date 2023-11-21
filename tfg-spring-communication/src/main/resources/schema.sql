INSERT INTO notifications (user_id, content, send_date, status)
VALUES
    (1, 'Bienvenido a nuestra plataforma', '2023-11-17 10:00:00', 'SENT'),

    (2, 'Tu suscripción ha sido activada', '2023-11-17 11:00:00', 'SENT'),

    (3, 'Recordatorio: tu factura está pendiente de pago', '2023-11-17 12:00:00', 'RECEIVED');

INSERT INTO messages (sender_id, receiver_id, content, send_date, reply_to_message_id, status) VALUES
   (1, 2, 'Hola, ¿cómo estás?', '2023-11-01 10:00:00', NULL, 'SENT'),
   (2, 1, 'Bien, gracias. ¿Y tú?', '2023-11-01 10:05:00', 1, 'SENT'),
   (1, 2, 'Todo bien por aquí también.', '2023-11-01 10:10:00', 2, 'SENT'),
   (1, 4, 'Hola, ¿vas a la reunión mañana?', '2023-11-01 11:00:00', NULL, 'SENT'),
   (4, 1, 'Sí, nos vemos allí.', '2023-11-01 11:15:00', 3, 'SENT'),
   (2, 4, 'Recuerda traer los documentos.', '2023-11-01 12:00:00', NULL, 'SENT');