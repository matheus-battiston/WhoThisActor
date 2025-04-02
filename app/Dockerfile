# Etapa de construção
FROM node:16

WORKDIR /app

ARG REACT_APP_API_URL
ARG APP_ADDRESS

ENV REACT_APP_API_URL=${REACT_APP_API_URL}
ENV APP_ADDRESS=${APP_ADDRESS}

COPY package.json package-lock.json ./
RUN npm install

COPY . .
EXPOSE 3000

CMD ["npm", "start"]