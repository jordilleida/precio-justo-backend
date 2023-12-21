INSERT INTO notifications (user_id, content, send_date, status)
VALUES
    (1, 'Bienvenido a nuestra plataforma', '2023-11-17 10:00:00', 'SENT'),

    (2, 'Tu suscripción ha sido activada', '2023-11-17 11:00:00', 'SENT'),

    (3, 'Recordatorio: tu factura está pendiente de pago', '2023-11-17 12:00:00', 'RECEIVED');

INSERT INTO messages (sender_id, receiver_id, content, send_date, reply_to_message_id, status) VALUES
   (2, 3, 'Hola, cómo es la  terraza?', '2023-11-01 10:00:00', NULL, 'SENT'),
   (3, 2, 'Buenas, muy grande, 70 metros', '2023-11-01 10:05:00', 1, 'SENT'),
   (2, 3, 'Muchas gracias, pujaré entonces porque es lo que busco', '2023-11-01 10:10:00', 2, 'SENT'),
   (1, 4, 'El inmueble tiene vistas al mar?', '2023-11-01 11:00:00', NULL, 'SENT'),
   (4, 1, 'Sí, correcto.', '2023-11-01 11:15:00', 4, 'SENT'),
   (4, 3, 'Buenas, Las habitaciones son grandes ?', '2023-11-01 12:00:00', NULL, 'SENT');