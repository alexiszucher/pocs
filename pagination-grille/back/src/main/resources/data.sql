-- Clients
INSERT INTO client_entity (id, name) VALUES ('c1', 'Bastian');
INSERT INTO client_entity (id, name) VALUES ('c2', 'Robin');
INSERT INTO client_entity (id, name) VALUES ('c3', 'Joe');
INSERT INTO client_entity (id, name) VALUES ('c4', 'Nixon');
INSERT INTO client_entity (id, name) VALUES ('c5', 'Gérald');

-- Projets (liés aux clients)
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p1', 'Moma', 'c1', 'ARCHIVED');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p2', 'Solvertools', 'c2', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p3', 'ACR', 'c3', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p4', 'PF', 'c4', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p5', 'Datarep', 'c5', 'ACTIVE');

INSERT INTO project_entity (id, name, client_id, status) VALUES ('p6', 'Alpine', 'c1', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p7', 'Boreal', 'c1', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p8', 'Cobalt', 'c2', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p9', 'DeltaWorks', 'c2', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p10', 'Epsilon', 'c1', 'ACTIVE');
INSERT INTO project_entity (id, name, client_id, status) VALUES ('p11', 'Moma', 'c2', 'ACTIVE');
