INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (1, 'John Doe', 10, '2021-01-01 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (2, 'Jane Doe', 100, '2021-01-02 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (3, 'Alice', 110, '2021-01-03 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (4, 'Bob', 120, '2021-01-04 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (5, 'Charlie', 500, '2021-01-05 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (6, 'David', 550, '2021-01-06 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (7, 'Eve', 560, '2021-01-07 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (8, 'Frank', 1000, '2021-01-08 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (9, 'Grace', 10000, '2021-01-09 00:00:00', 0);
INSERT INTO freelancer (id, name, detail_view_count, created_at, point) VALUES (10, 'Hank', 100000, '2021-01-10 00:00:00', 0);

-- 쿠폰
INSERT INTO coupon (id, name, code, discount_rate, created_at, updated_at) VALUES (1, '20% 할인 쿠폰', 'dis20', 20, '2024-09-15 13:00:00', '2024-09-15 13:00:00');

-- 10프로 추가 적립 이벤트 코드
INSERT INTO event (id, name, event_code, point_rate, created_at, updated_at, start_date, end_date) VALUES (1, '10% 추가 적립 이벤트', 'event10', 10, '2024-09-15 13:00:00', '2024-09-15 13:00:00', '2023-09-15 13:00:00', '2026-09-15 13:00:00');
