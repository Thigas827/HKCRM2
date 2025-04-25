--Create_produto(Pai/Base)
CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL

);

--Create_doce_(Filha)
CREATE TABLE doce (
    id INT PRIMARY KEY,
    sabor VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    data_compra DATE NOT NULL CURRENT_DATE,
    CONSTRAINT fk_produto_doce
        FOREIGN KEY (id)
        REFERENCES produto(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

--Create_plano_(Filha)
CREATE TABLE plano (
    id INT PRIMARY KEY,
    tipo_plano INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    data_vencimento DATE NOT NULL DEFAULT CURRENT_DATE + INTERVAL '30 days',
    valor_mensal DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_produto_plano
        FOREIGN KEY (id)
        REFERENCES produto(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);