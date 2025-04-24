--Create_produto(Pai/Base)
CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL

);

--Create_doce_(Filha)
CREATE TABLE doce (
    id INT PRIMARY KEY,
    categoria VARCHAR(255) NOT NULL,
    sabor VARCHAR(255) NOT NULL,
    CONSTRAINT fk_produto_doce
        FOREIGN KEY (id)
        REFERENCES produto(id)
);

--Create_plano_(Filha)
CREATE TABLE plano (
    id INT PRIMARY KEY,
    duracao_meses INT NOT NULL,