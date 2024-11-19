-- Inserindo produtos
INSERT INTO produto (id, nome, tipo, preco) VALUES ('1', 'Vinho Tinto', 'Tinto', 75.50);
INSERT INTO produto (id, nome, tipo, preco) VALUES ('2', 'Vinho Branco', 'Branco', 60.00);
INSERT INTO produto (id, nome, tipo, preco) VALUES ('3', 'Espumante', 'Espumante', 120.00);

-- Inserindo clientes
INSERT INTO cliente (cpf, nome) VALUES ('11111111111', 'João Silva');
INSERT INTO cliente (cpf, nome) VALUES ('22222222222', 'Maria Oliveira');
INSERT INTO cliente (cpf, nome) VALUES ('33333333333', 'Pedro Souza');

-- Inserindo compras
INSERT INTO compra (cliente_id, produto_id, quantidade, valor_total) VALUES (1, 1, 2, 151.00);
INSERT INTO compra (cliente_id, produto_id, quantidade, valor_total) VALUES (2, 2, 1, 60.00);
INSERT INTO compra (cliente_id, produto_id, quantidade, valor_total) VALUES (3, 3, 3, 360.00);
