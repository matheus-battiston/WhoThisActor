# Etapa de construção
FROM node:16 AS build

WORKDIR /app

# Definindo argumentos que serão passados pelo docker-compose
ARG REACT_APP_API_URL
ARG APP_ADDRESS

# Passando os argumentos como variáveis de ambiente dentro do container
ENV REACT_APP_API_URL=${REACT_APP_API_URL}
ENV APP_ADDRESS=${APP_ADDRESS}

COPY package*.json ./
RUN npm install

COPY . .

# Instalação e execução do servidor em modo de desenvolvimento
CMD ["npm", "start"]