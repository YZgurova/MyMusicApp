FROM node:18
WORKDIR /app/
COPY ./public/ ./
COPY ./public/index.html ./
COPY ./src/ ./
COPY ./package*.json ./
RUN ["npm", "i"]
ENTRYPOINT ["npm", "start"]
