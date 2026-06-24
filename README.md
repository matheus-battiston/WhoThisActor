# Who Is This Actor

Aplicação web para identificar atores a partir de uma imagem e explorar suas participações em filmes e séries.

O projeto combina pesquisa de produções, dados de elenco, classificação facial e favoritos para facilitar a descoberta de quem aparece em uma cena ou imagem.

🔗 **[Leia o estudo de caso completo](https://mbattiston.com/#/estudos-de-caso/whothis)**

## Funcionalidades

- Identificação de atores por upload de imagem.
- Busca de atores, filmes e séries.
- Visualização de detalhes de produções e elenco.
- Consulta da filmografia de um ator.
- Favoritos de atores, filmes e séries.
- Filtro de classificação baseado nos favoritos do usuário.
- Cache de consultas e dados de elenco para reduzir chamadas externas.

## Arquitetura

```text
React (app)
    │
    ▼
Spring Boot API (api) ─────► TMDB
    │         │
    │         ├────────────► Redis
    │         └────────────► PostgreSQL
    │
    ▼
FastAPI Classifier (classify)
```

| Componente | Responsabilidade | Tecnologias principais |
| --- | --- | --- |
| `app` | Interface web e interação com o usuário | React, Material UI, Redux Toolkit, React Query |
| `api` | API REST, regras de negócio, autenticação e persistência | Java 17, Spring Boot, JPA, Flyway, Redis |
| `classify` | Processamento de imagem e classificação facial | Python, FastAPI, OpenCV, ONNX Runtime, FAISS |
| PostgreSQL | Dados persistentes da aplicação | PostgreSQL 16 |
| Redis | Cache de consultas e dados temporários | Redis 7 |

## Estrutura do repositório

```text
app/        # Frontend React
api/        # API REST e regras de negócio
classify/   # Serviço de classificação facial
docker-compose.yml
```

## Decisões técnicas

- **Separação de responsabilidades:** a API concentra regras de negócio e persistência; o serviço Python é dedicado ao processamento de imagens e reconhecimento facial.
- **Cache:** Redis reduz a repetição de consultas e permite reutilizar informações de elenco e provedores.
- **Migrações versionadas:** o schema do banco é controlado com Flyway.
- **Integração de catálogo:** filmes, séries, elenco e imagens são obtidos por integração com a API do TMDB.
- **Índices de busca facial:** o classificador utiliza FAISS para comparar embeddings faciais com eficiência.

## Disponibilidade e execução

Os arquivos `.env.example`, na raiz e em cada serviço, documentam as variáveis de ambiente e indicam brevemente a finalidade de cada grupo de configuração.

O projeto, porém, não está preparado para execução local no formato tradicional: a configuração Docker descreve a arquitetura original, mas não constitui um ambiente público e reproduzível.

Para executá-lo integralmente, são necessários:

- o projeto de autenticação [AuthMatheus](https://github.com/matheus-battiston/AuthMatheus), responsável pela emissão e validação dos tokens;
- um token de API do TMDB, usado para consultar filmes, séries e elenco;
- um bucket no Oracle Object Storage, com as credenciais e artefatos do classificador facial;
- PostgreSQL e Redis configurados para a aplicação.

Segredos e artefatos privados não são distribuídos neste repositório.

## Testes

A API possui testes unitários para serviços e regras de negócio. Eles estão em:

```text
api/src/test/java
```

Sua execução também pode exigir dependências privadas do ambiente original.

## Créditos

Este produto utiliza a API do TMDB para informações de filmes, séries e elenco.

> This product uses the TMDB API but is not endorsed or certified by TMDB.

## Licença

Licença ainda não definida.
