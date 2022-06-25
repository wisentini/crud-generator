# crudgenerator

Projeto em Java que lê metadados de um banco de dados e gera três tipos de classes (entidade, [DAO][dao-wikipedia] e exemplo) para cada tabela do esquema, além de uma classe responsável pela utilização das outras classes geradas.

## Pré-requisitos

Para rodar o projeto, você precisará das seguintes tecnologias à sua disposição:

- [Docker][docker]
- [Java][java]
- [Maven][maven]

## Banco de dados

- Os arquivos `config/database.properties` e `src/main/resources/META-INF/persistence.xml` contém informações críticas para a correta execução do programa, portanto, certifique-se de que eles refletem suas intenções.

- O banco de dados de teste é gerenciado de forma automática pelo Hibernate por meio de scripts SQL predefinidos em `src/main/resources/sql`. Caso você opte por utilizar outro banco de dados ou um esquema diferente, não se esqueça de alterar esses scripts. Além disso, verifique se os arquivos mencionados no item anterior dessa lista ainda fazem sentido após quaisquer mudanças.

### Esquema

- Toda tabela do esquema deve possuir uma chave primária simples do tipo inteiro com auto incremento feito pelo próprio banco de dados.

- Pelo fato de ter sido desenvolvido em um contexto acadêmico, o programa não irá funcionar para esquemas de banco de dados mais elaborados. Por isso, evite o uso de chaves estrangeiras, chaves primárias compostas, tipos de dados de colunas fora do comum, etc.

- Segue abaixo o esquema do banco de dados de teste `crudgenerator`:

#### `Regiao`

| name   | type        | constraints                              |
|--------|-------------|------------------------------------------|
| codigo | INT         | PRIMARY KEY GENERATED ALWAYS AS IDENTITY |
| nome   | VARCHAR(31) | NOT NULL                                 |

#### `Estado`

| name         | type        | constraints                              |
|--------------|-------------|------------------------------------------|
| codigo       | INT         | PRIMARY KEY GENERATED ALWAYS AS IDENTITY |
| nome         | VARCHAR(63) | NOT NULL                                 |
| uf           | CHAR(2)     | NOT NULL                                 |
| codigoRegiao | INT         | NOT NULL                                 |

#### `Municipio`

| name         | type         | constraints                              |
|--------------|--------------|------------------------------------------|
| codigo       | INT          | PRIMARY KEY GENERATED ALWAYS AS IDENTITY |
| nome         | VARCHAR(255) | NOT NULL                                 |
| uf           | CHAR(2)      | NOT NULL                                 |

- Para o esquema acima, serão geradas as seguintes classes:

| entidade    | DAO            | exemplo            |
|-------------|----------------|--------------------|
| `Regiao`    | `RegiaoDAO`    | `RegiaoExample`    |
| `Estado`    | `EstadoDAO`    | `EstadoExample`    |
| `Municipio` | `MunicipioDAO` | `MunicipioExample` |

## Execução

### Docker

- Para utilizar as tecnologias voltadas ao banco de dados através do Docker, rode os seguintes comandos dentro do diretório `docker`:
  - `docker compose up -d`: inicia os containers
  - `docker compose down`: para e remove os containers em execução

- A instância do `pgAdmin 4` estará rodando em `localhost:5050`, podendo ser acessada através do email `pgadmin@pgadmin.org` e da senha `admin`.

### Projeto

- Antes de tudo, você precisa criar o banco de dados `crudgenerator` manualmente, já que o Hibernate só consegue criar as tabelas para um esquema já existente.

- Para rodar o projeto, execute os comandos abaixo na ordem em que eles são descritos:

  1. `mvn clean`
  2. `mvn compile`
  3. `mvn -q exec:java@main`
  4. `mvn compile`
  5. `mvn -q exec:java@application`

  - O comando 3 gera as classes DAO, de exemplo, de entidade e da aplicação final. O comando 5 consome as classes geradas pelo comando 3.

## Scripts

| script     | task                                                    |
|------------|---------------------------------------------------------|
| `run.sh`   | Roda os comandos necessários para a execução do projeto |
| `purge.sh` | Remove os arquivos resultantes da execução do projeto   |

## Exemplo

Clique [aqui][video] para ver um vídeo do projeto sendo executado.

<!-- Links -->
[dao-wikipedia]: https://en.wikipedia.org/wiki/Data_access_object
[docker]: https://www.docker.com/ "Docker"
[java]: https://www.java.com/ "Java"
[maven]: https://maven.apache.org/ "Maven"
[video]: https://www.youtube.com/watch?v=Hh76WF---Dk
