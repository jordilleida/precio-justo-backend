INSERT INTO auctions (property_id, user_id, start_date, end_date, initial_price, status, winning_bid)
VALUES (3, 4, '2023-12-01T00:00:00',CURRENT_TIMESTAMP + INTERVAL '5 minutes', 100000.00, 'ACTIVE', NULL);
